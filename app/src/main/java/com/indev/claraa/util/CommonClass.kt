package com.indev.claraa.util

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.*

class CommonClass {

    companion object {
        var unique_id = ""

        @SuppressLint("NewApi")
        @RequiresApi(Build.VERSION_CODES.N)
        fun getUniqueId(): String? {
            val sdf = SimpleDateFormat("ssmmddMMYYYY", Locale.getDefault())
            unique_id = sdf.format(Date())
            return unique_id
        }

        fun currentDate(): String? {
            val date = Date()
            //Calendar calUUID = Calendar.getInstance();
            val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return df.format(date)
        }
    }
}