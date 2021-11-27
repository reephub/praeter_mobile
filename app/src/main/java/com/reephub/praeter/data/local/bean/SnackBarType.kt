package com.reephub.praeter.data.local.bean

import com.reephub.praeter.R

enum class SnackBarType(val backgroundColor: Int, val textColor: Int) {
    NORMAL(R.color.success, R.color.white),
    WARNING(R.color.warning, R.color.white),
    ALERT(R.color.error, R.color.white);

}