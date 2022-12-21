package com.indev.claraa

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.*

class CommonClass {

    companion object {
        var currentDateandTime = ""

        @SuppressLint("NewApi")
        @RequiresApi(Build.VERSION_CODES.N)
        fun getUniqueId(): String? {
            val sdf = SimpleDateFormat("mmddMMYYYY", Locale.getDefault())
            currentDateandTime = sdf.format(Date())
            return currentDateandTime
        }
    }
}