package com.reephub.praeter.utils

import android.text.TextUtils


fun String.isValidEmail(): Boolean =
    !TextUtils.isEmpty(this)
            && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

