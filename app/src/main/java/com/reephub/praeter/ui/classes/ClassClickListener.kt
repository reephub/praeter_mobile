package com.reephub.praeter.ui.classes

import android.view.View
import com.reephub.praeter.data.remote.dto.ClassesDto

interface ClassClickListener {
    fun onClassClicked(view: View, selectedClass: ClassesDto)
}