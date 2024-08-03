package com.reephub.praeter.data.local.bean

enum class TimeOut(val value: Int) {
    TIME_OUT_READ(60),
    TIME_OUT_CONNECTION(60);

    companion object {
        fun getValue(value: TimeOut): Int {
            return entries.first { it.name == value.name }.value
        }
    }
}