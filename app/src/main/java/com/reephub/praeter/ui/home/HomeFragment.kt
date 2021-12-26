package com.reephub.praeter.ui.home

import android.Manifest
import android.annotation.SuppressLint
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
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.lifecycle.coroutineScope
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
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
import com.reephub.praeter.databinding.FragmentHomeBinding
import com.reephub.praeter.ui.base.BaseFragment
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.IOException
import java.text.DateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class HomeFragment : BaseFragment(),
    CoroutineScope,
    android.location.LocationListener,
    GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMarkerClickListener {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private var _viewBinding: FragmentHomeBinding? = null
    private val binding get() = _viewBinding!!

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
                        // initAutoCompleteView()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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
            AnimationUtils.loadAnimation(requireContext(), com.reephub.praeter.R.anim.fade_out)
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
    override fun onConnected(isConnected: Boolean) {
        // Ignored
    }

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

    companion object {
        val TAG = HomeFragment::class.java.simpleName

        // location updates interval - 10sec
        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000

        // fastest updates interval - 5 sec
        // location updates will be received if another app is requesting the locations
        // than your app can handle
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5000
        private const val REQUEST_CHECK_SETTINGS = 100

        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

}