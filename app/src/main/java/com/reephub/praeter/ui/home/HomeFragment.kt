package com.reephub.praeter.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.IntentSender
import android.location.Criteria
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceLikelihood
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.maps.android.PolyUtil
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.reephub.praeter.R
import com.reephub.praeter.core.utils.PraeterAddressesUtils
import com.reephub.praeter.core.utils.PraeterLocationManager
import com.reephub.praeter.core.utils.PraeterLocationUtils
import com.reephub.praeter.core.utils.UIManager
import com.reephub.praeter.data.local.bean.MapsEnum
import com.reephub.praeter.data.remote.dto.directions.Steps
import com.reephub.praeter.databinding.FragmentHomeBinding
import com.reephub.praeter.ui.base.BaseFragment
import com.reephub.praeter.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.IOException
import java.text.DateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class HomeFragment : BaseFragment(),
    CoroutineScope,
    android.location.LocationListener,
    GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMarkerClickListener,
    PlaceSelectionListener {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private var _viewBinding: FragmentHomeBinding? = null
    private val binding get() = _viewBinding!!

    private val mLocationViewModel: HomeLocationViewModel by viewModels()

    private lateinit var mapFragment: SupportMapFragment
    private var geocoder: Geocoder? = null
    private lateinit var mMap: GoogleMap
    private var mLocation: Location? = null
    private var mLocationManager: LocationManager? = null
    private var mCriteria: Criteria? = null
    private var mProvider: String? = null

    // bunch of location related apis
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mSettingsClient: SettingsClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationSettingsRequest: LocationSettingsRequest? = null
    private var mLocationCallback: LocationCallback? = null
    private var mCurrentLocation: Location? = null

    // boolean flag to toggle the ui
    private var mRequestingLocationUpdates: Boolean = false

    // location last updated time
    private var mLastUpdateTime: String? = null

    private var CURRENT_LOCATION_TO_STRING: String = ""
    private var TARGET_LOCATION_TO_STRING: String = ""

    private var placesClient: PlacesClient? = null
    private var currentPlace: Place? = null
    private var request: FindCurrentPlaceRequest? = null

    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Dexter.withContext(requireActivity())
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        Timber.i("All permissions are granted")
                        initAutoCompleteView()
                        mRequestingLocationUpdates = true
                        initGoogleMap()
                        initLocationSettings()
                    } else {
                        Timber.e("All permissions are not granted")
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    list: List<PermissionRequest>,
                    permissionToken: PermissionToken
                ) {
                    permissionToken.continuePermissionRequest()
                }
            })
            .withErrorListener { dexterError: DexterError -> Timber.e(dexterError.toString()) }
            .onSameThread()
            .check()

        initViewModelsObservers()
    }

    override fun onPause() {
        Timber.e("onPause()")
        if (mRequestingLocationUpdates) {
            // pausing location updates
            stopLocationUpdates()
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        // Resuming location updates depending on button state and
        // allowed permissions
        if (mRequestingLocationUpdates) {
            startLocationUpdates()
        }

        updateLocationUI()
    }

    override fun onConnected() {
        // Ignored
    }

    override fun onDisconnected() {
        // Ignored
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.e("onActivityResult()")
        // Check for the integer request code originally supplied to startResolutionForResult().
        if (requestCode == Constants.REQUEST_CHECK_SETTINGS) {
            when (resultCode) {
                Activity.RESULT_OK -> Timber.e("User agreed to make required location settings changes.")
                Activity.RESULT_CANCELED -> {
                    Timber.e("User chose not to make required location settings changes.")
                    mRequestingLocationUpdates = false
                }
                else -> {
                    Timber.e("else branch")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////
    private fun initViewModelsObservers() {
        Timber.i("initViewModelsObservers()")
        mLocationViewModel.getGoogleDirection()
            .observe(
                requireActivity(),
                { response ->
                    Timber.d("getGoogleDirection().observe")

                    try {
                        Timber.d("Overview : ${response.routes[0].overviewPolyline.points}")
                        val overview = response.routes[0].overviewPolyline.points

                        val stepsNumber = response.routes[0].legs[0].steps.size

                        val polylines: Array<String?> = arrayOfNulls(stepsNumber)

                        for ((i, step: Steps) in response.routes[0].legs[0].steps.withIndex()) {

                            val polygone: String = step.polyline.points

                            polylines[i] = polygone
                        }

                        Timber.e("Polylines : $polylines")
                        val polylinesCount = polylines.size

                        for (i in 0..polylinesCount) {

                            val option = PolylineOptions()
                            option.color(
                                ContextCompat.getColor(
                                    requireActivity(),
                                    R.color.black
                                )
                            )
                            option.addAll(PolyUtil.decode(overview))

                            mMap.addPolyline(option)
                        }

                        val southWestBounds = TARGET_LOCATION_TO_STRING.split(",")
                        val northEastBounds = CURRENT_LOCATION_TO_STRING.split(",")


                        /*val australiaBounds = LatLngBounds(
                            LatLng(
                                southWestBounds[0].toDouble(),
                                southWestBounds[1].toDouble()
                            ),  // SW bounds
                            LatLng(
                                northEastBounds[0].toDouble(),
                                northEastBounds[1].toDouble()
                            ) // NE bounds
                        )
                        mMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                australiaBounds.center,
                                10f
                            )
                        )*/

                    } catch (exception: Exception) {
                        exception.printStackTrace()
                    }
                })
    }

    @SuppressLint("MissingPermission")
    private fun initAutoCompleteView() {
        Timber.i("initAutoCompleteView()")

        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment =
            this.childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        // ref : https://stackoverflow.com/questions/36398061/how-to-change-text-size-in-places-autocompletefragment-in-android
        // placeAutocompleteFragment - is my PlaceAutocompleteFragment instance
        (autocompleteFragment.view
            ?.findViewById(R.id.places_autocomplete_search_input) as EditText)
            .setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))

        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if (!Places.isInitialized()) {
            Places.initialize(
                requireContext(),
                getString(R.string.google_maps_key),
                Locale.FRANCE
            )
        }

        placesClient = Places.createClient(requireContext())

        // Use the builder to create a FindCurrentPlaceRequest.
        request = FindCurrentPlaceRequest.newInstance(Constants.CURRENT_PLACE_FIELDS)

        placesClient
            ?.findCurrentPlace(request!!)
            ?.addOnSuccessListener { task ->
                Timber.d("onSuccessListener")

                var previousPercentage = 0.0

                for (placeLikelihood: PlaceLikelihood in task.placeLikelihoods) {
                    Timber.e(
                        "Place '${placeLikelihood.place.name}' has likelihood: ${placeLikelihood.likelihood}"
                    )

                    if (previousPercentage < placeLikelihood.likelihood) {
                        previousPercentage = placeLikelihood.likelihood
                        currentPlace = placeLikelihood.place
                    }
                }

                Timber.d("final place : $currentPlace")
            }
            ?.addOnFailureListener { exception ->
                Timber.e("onFailureListener")

                if (exception is ApiException) {
                    Timber.e("Place not found: ${exception.statusCode}")
                }
            }
            ?.addOnCompleteListener { _ ->
                Timber.d("onCompleteListener")
            }

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Constants.PLACES_FIELDS)

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(this)
    }

    private fun showBottomItineraryFragment(currentPlace: Place, targetPlace: Place) {
        Timber.d("showBottomItineraryFragment()")

        BottomSheetItineraryFragment
            .newInstance(currentPlace, targetPlace)
            .show(
                this.childFragmentManager,
                BottomSheetItineraryFragment.TAG
            )
    }

    private fun buildItinerary(currentPlace: Place, targetPlace: Place) {
        Timber.d("buildItinerary()")

        val currentLocation = Location("")
        currentLocation.latitude = currentPlace.latLng?.latitude!!
        currentLocation.longitude = currentPlace.latLng?.longitude!!

        val targetLocation = Location("")
        targetLocation.latitude = targetPlace.latLng?.latitude!!
        targetLocation.longitude = targetPlace.latLng?.longitude!!

        val mPLM =
            PraeterLocationManager(requireActivity(), requireContext())

        CURRENT_LOCATION_TO_STRING =
            mPLM.convertLatLngLocationToString(currentLocation)
        TARGET_LOCATION_TO_STRING =
            mPLM.convertLatLngLocationToString(targetLocation)

        mLocationViewModel.getItinerary(CURRENT_LOCATION_TO_STRING, TARGET_LOCATION_TO_STRING)
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     *
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    private fun initGoogleMap() {
        Timber.i("setupMap()")
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = this.childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        // Setup Google Map using coroutines
        lifecycle.coroutineScope.launchWhenCreated {
            Timber.d("Setup Google Map using coroutines")
            mMap = mapFragment.awaitMap()

            setupMap()

            // Set a preference for minimum and maximum zoom.
            mMap.setMinZoomPreference(MapsEnum.WORLD.distance)
            mMap.setMaxZoomPreference(MapsEnum.DEFAULT_MAX_ZOOM.distance)
            //mMap.moveCamera(CameraUpdateFactory.zoomTo(MapsEnum.WORLD.distance))

            CoroutineScope(coroutineContext).launch {
                delay(3000)
                setLocationSettings()
                hideLoading()
            }

        }
    }

    private fun initLocationSettings() {
        Timber.i("initLocationSettings()")

        // Construct a FusedLocationProviderClient.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mSettingsClient = LocationServices.getSettingsClient(requireActivity())
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                // location is received
                mCurrentLocation = locationResult.lastLocation
                mLastUpdateTime = DateFormat.getTimeInstance().format(Date())
                updateLocationUI()
            }
        }
        mRequestingLocationUpdates = false
        @Suppress("DEPRECATION")
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()

        builder.addLocationRequest(mLocationRequest!!)
        mLocationSettingsRequest = builder.build()
        geocoder = Geocoder(requireContext(), Locale.getDefault())
    }

    @SuppressLint("MissingPermission")
    private fun setupMap() {
        Timber.i("setupMap()")
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        // mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment


        // TODO: Before enabling the My Location layer, you must request
        // location permission from the user. This sample does not include
        // a request for location permission.
        // Used for finding current location with button
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true

        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)

        /*
            source :  https://stackoverflow.com/questions/36785542/how-to-change-the-position-of-my-location-button-in-google-maps-using-android-st
         */
        if (mapFragment.view != null) {
            Timber.d("Get the button view")
            // Get the button view
            val locationButton =
                (mapFragment.requireView()
                    .findViewById<View>("1".toInt()).parent as View)
                    .findViewById<View>("2".toInt())
            Timber.d("and next place it, on bottom right (as Google Maps app)")
            // and next place it, on bottom right (as Google Maps app)
            val layoutParams = locationButton.layoutParams as RelativeLayout.LayoutParams
            Timber.d("position on right bottom")
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            layoutParams.setMargins(0, 0, 30, 260)
        }

        // Set a preference for minimum and maximum zoom.
        mMap.setMinZoomPreference(MapsEnum.WORLD.distance)
        mMap.setMaxZoomPreference(MapsEnum.DEFAULT_MAX_ZOOM.distance)
        mMap.moveCamera(CameraUpdateFactory.zoomTo(MapsEnum.WORLD.distance))
        mMap.uiSettings.setAllGesturesEnabled(true)

        setLocationSettings()
        hideLoading()

    }

    private fun setLocationSettings() {
        Timber.i("setLocationSettings()")
        mLocationManager = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
        mCriteria = Criteria()
        mProvider = mLocationManager?.getBestProvider(mCriteria!!, true)!!
        if (null == mProvider) {
            Timber.e("Cannot get location please enable position")
            val locationManager = PraeterLocationManager(requireActivity(), requireActivity())
            locationManager.showSettingsAlert()
        } else {
            try {
                mLocation = mLocationManager?.getLastKnownLocation(mProvider!!)
            } catch (e: SecurityException) {
                e.printStackTrace()
            }

            if (null != mLocation) {
                onLocationChanged(mLocation!!)
                try {
                    mLocationManager?.requestLocationUpdates(
                        mProvider!!,
                        20000,
                        0f,
                        this
                    )
                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
            }

        }
    }

    private fun hideLoading() {
        if (binding.rlMapsLoading.visibility == View.VISIBLE)
            startAnimation(binding.rlMapsLoading)

    }

    private fun startAnimation(view: View) {
        Timber.d("startAnimation()")
        val animFadeOut =
            AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)
        animFadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                Timber.d("onAnimationStart()")
            }

            override fun onAnimationEnd(animation: Animation) {
                Timber.d("onAnimationEnd()")
                view.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {
                Timber.d("onAnimationRepeat()")
            }
        })
        val mAnimationSet = AnimationSet(true)
        mAnimationSet.interpolator = AccelerateInterpolator()
        mAnimationSet.addAnimation(animFadeOut)
        view.startAnimation(mAnimationSet)
    }

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        mSettingsClient
            ?.checkLocationSettings(mLocationSettingsRequest!!)
            ?.addOnSuccessListener(requireActivity()) { _: LocationSettingsResponse? ->

                Timber.i("All location settings are satisfied.")
                UIManager.showActionInToast(requireActivity(), "Started location updates!")

                mFusedLocationClient!!.requestLocationUpdates(
                    mLocationRequest!!,
                    mLocationCallback!!,
                    Looper.myLooper()!!
                )
                updateLocationUI()
            }
            ?.addOnFailureListener(requireActivity()) { e: Exception ->
                val statusCode = (e as ApiException).statusCode
                if (statusCode == CommonStatusCodes.RESOLUTION_REQUIRED) {
                    Timber.i("Location settings are not satisfied. Attempting to upgrade location settings ")
                    try {
                        // Show the dialog by calling startResolutionForResult(), and check the
                        // result in onActivityResult().
                        val rae = e as ResolvableApiException
                        rae.startResolutionForResult(
                            requireActivity(),
                            REQUEST_CHECK_SETTINGS
                        )
                    } catch (sie: IntentSender.SendIntentException) {
                        Timber.i("PendingIntent unable to execute request.")
                    }
                } else if (statusCode == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE) {
                    val errorMessage =
                        "Location settings are inadequate, and cannot be " +
                                "fixed here. Fix in Settings."
                    UIManager.showActionInToast(requireActivity(), errorMessage)
                    Timber.e(errorMessage)
                }
                updateLocationUI()
            }
    }

    private fun stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
            ?.removeLocationUpdates(mLocationCallback!!)
            ?.addOnCompleteListener(requireActivity()) {
                UIManager.showActionInToast(requireActivity(), "Location updates stopped!")
            }
    }

    /**
     * Update the UI displaying the location data
     * and toggling the buttons
     */
    private fun updateLocationUI() {
        Timber.d("updateLocationUI()")
        if (mCurrentLocation != null) {
            Timber.e("Lat: ${mCurrentLocation!!.latitude} , ${mCurrentLocation!!.longitude}")
            geocoder = Geocoder(requireActivity(), Locale.getDefault())

            try {
                val address = PraeterAddressesUtils.getDeviceAddress(geocoder!!, mCurrentLocation!!)
                Timber.e("Address : %S", address?.countryName.orEmpty())

            } catch (ioException: IOException) {
                // Catch network or other I/O problems.
//            errorMessage = getString(R.string.service_not_available);
                Timber.e(ioException, "errorMessage")
            } catch (illegalArgumentException: IllegalArgumentException) {
                // Catch invalid latitude or longitude values.
//            errorMessage = getString(R.string.invalid_lat_long_used);
                Timber.e(
                    illegalArgumentException,
                    "errorMessage : Latitude = ${mCurrentLocation!!.latitude}, Longitude : ${mCurrentLocation!!.longitude}"
                )
            }
        }
    }


    /////////////////////////////////////
    //
    // IMPLEMENTS
    //
    /////////////////////////////////////
    /*override fun onConnected(isConnected: Boolean) {
        // Ignored
    }*/

    @SuppressLint("MissingPermission", "SetTextI18n")
    override fun onLocationChanged(location: Location) {
        Timber.i("onLocationChanged()")

        // Remove markers
        mMap.clear()

        val latitude = location.latitude
        val longitude = location.longitude
        Timber.e("Lat : %s - Lon : %s", latitude, longitude)
        val latLng = LatLng(latitude, longitude)
        mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(
                    geocoder?.let {
                        PraeterLocationUtils.getDeviceLocationToString(
                            it,
                            location
                        )
                    }
                )
        )
        mMap.isMyLocationEnabled = true
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(750f), 2000, null)
        mMap.uiSettings.isScrollGesturesEnabled = true
        Timber.e("Latitude:$latitude\nLongitude:$longitude")
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Timber.d("onStatusChanged()")
    }

    override fun onProviderEnabled(provider: String) {
        Timber.d("onProviderEnabled()")
    }

    override fun onProviderDisabled(provider: String) {
        Timber.d("onProviderDisabled()")
    }

    override fun onMyLocationClick(myLocation: Location) {
        Timber.e("onMyLocationClick() - $myLocation")

        BottomSheetLocationFragment
            .newInstance(myLocation)
            .show(
                this.childFragmentManager,
                BottomSheetLocationFragment.TAG
            )
    }

    override fun onMyLocationButtonClick(): Boolean {
        Timber.d("onMyLocationButtonClick()")

        Toast.makeText(requireActivity(), "MyLocation button clicked", Toast.LENGTH_SHORT).show()
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        Timber.d("onMarkerClick()")
        return true
    }

    override fun onError(status: Status) {
        // TODO: Handle the error.
        Timber.e("An error occurred: $status")
    }

    @SuppressLint("MissingPermission")
    override fun onPlaceSelected(place: Place) {
        // TODO: Get info about the selected place.
        Timber.i("onPlaceSelected() - Place: ${place.name}, ${place.id}, ${place.latLng}")

        placesClient
            ?.findCurrentPlace(request!!)
            ?.addOnSuccessListener { task ->
                Timber.d("onSuccessListener")

                var previousPercentage = 0.0

                for (placeLikelihood: PlaceLikelihood in task.placeLikelihoods) {
                    Timber.e(
                        "Place '${placeLikelihood.place.name}' has likelihood: ${placeLikelihood.likelihood}"
                    )

                    if (previousPercentage < placeLikelihood.likelihood) {
                        previousPercentage = placeLikelihood.likelihood
                        currentPlace = placeLikelihood.place
                    }
                }

                Timber.d("final place : $currentPlace")

                place.latLng?.let {
                    mMap.addMarker {
                        position(it)
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 10f))
                }

                if (null != currentPlace) {
                    showBottomItineraryFragment(currentPlace!!, place)
                    buildItinerary(currentPlace!!, place)
                } else {
                    Timber.e("currentPlace is NULL")
                }
            }
            ?.addOnFailureListener { exception ->
                Timber.e("onFailureListener")

                if (exception is ApiException) {
                    Timber.e("Place not found: ${exception.statusCode}")
                }
            }
            ?.addOnCompleteListener { _ ->
                Timber.d("onCompleteListener")
            }

    }

    companion object {
        val TAG: String = HomeFragment::class.java.simpleName

        // location updates interval - 10sec
        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000
//        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5 * 60 * 1000

        // fastest updates interval - 5 sec
        // location updates will be received if another app is requesting the locations
        // than your app can handle
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5000

        //        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 2 * 60 * 5000
        private const val REQUEST_CHECK_SETTINGS = 100

        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}