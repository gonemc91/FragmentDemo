package com.example.fragmentdemo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Options(
    val boxCount: Int,
    val isTimerEnabled: Boolean
) : Parcelable{
    companion object{
        @JvmStatic val Default = Options(boxCount = 3, isTimerEnabled = false)
    }
}
