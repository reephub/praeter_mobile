package com.reephub.praeter.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView
import com.reephub.praeter.PraeterApplication
import jp.wasabeef.glide.transformations.BlurTransformation

@SuppressLint("CheckResult")
@BindingAdapter("imageResource", "error", requireAll = false)
public fun loadImage(
    targetImageView: ShapeableImageView,
    iconResDrawable: Drawable,
    error: Drawable,
) {
    //Load the background  thumb image
    Glide.with(PraeterApplication.getInstance().getContext())
        .load(iconResDrawable)
        .error(error)
        .apply {
            dontTransform()
        }
        .into(targetImageView)
}


@SuppressLint("CheckResult")
class LabGlideUtils {

    companion object {
        private var INSTANCE: LabGlideUtils? = null

        fun getInstance(): LabGlideUtils {
            if (null == INSTANCE)
                INSTANCE = LabGlideUtils()

            return INSTANCE as LabGlideUtils
        }
    }


    /*@JvmStatic
    @BindingAdapter("android:src", "error", requireAll = false)*/
    public fun loadImage(
        targetImageView: ShapeableImageView,
        iconIntRes: Int,
        error: Drawable,
    ) {
        //Load the background  thumb image
        Glide.with(PraeterApplication.getInstance().getContext())
            .load(iconIntRes)
            .error(error)
            .apply {
                dontTransform()
            }
            .into(targetImageView)
    }

    // @BindingAdapter("app:srcCompat", "error", requireAll = false)
    public fun loadImage(
        targetImageView: ShapeableImageView,
        iconIntVectorRes: Int,
        error: Drawable,
        context: Context
    ) {
        //Load the background  thumb image
        Glide.with(PraeterApplication.getInstance().getContext())
            .load(iconIntVectorRes)
            .error(error)
            .apply {
                dontTransform()
            }
            .into(targetImageView)
    }

    /*@JvmStatic
    @BindingAdapter("imageUrl", "error", requireAll = false)*/
    public fun loadImage(
        targetImageView: ShapeableImageView,
        iconResDrawable: Drawable,
        error: Drawable,
    ) {
        //Load the background  thumb image
        Glide.with(PraeterApplication.getInstance().getContext())
            .load(iconResDrawable)
            .error(error)
            .apply {
                dontTransform()
            }
            .into(targetImageView)
    }

    fun loadImage(
        context: Context,
        iconResDrawable: Any,
        targetImageView: ShapeableImageView
    ) {
        //Load the background  thumb image
        Glide.with(context)
            .load(iconResDrawable)
            .apply {
                dontTransform()
            }
            .into(targetImageView)
    }

    fun loadImage(
        context: Context,
        iconResDrawable: Any,
        targetImageView: ShapeableImageView,
        listener: RequestListener<Drawable>
    ) {
        //Load the background  thumb image
        Glide.with(context)
            .load(iconResDrawable)
            .listener(listener)
            .apply {
                dontTransform()
            }
            .into(targetImageView)
    }

    /**
     * Load Image in view and apply a blurry effect
     */
    fun loadImageBlurred(
        context: Context,
        imageURL: Any,
        targetImageView: ShapeableImageView
    ) {
        //Load the background  thumb image
        Glide.with(context)
            .load(imageURL)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(5, 5)))
            .into(targetImageView)
    }
}