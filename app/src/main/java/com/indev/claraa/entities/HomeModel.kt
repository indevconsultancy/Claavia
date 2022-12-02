package com.indev.claraa.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list")
data class HomeModel(@PrimaryKey(autoGenerate = true)
                     val id: Int,
                     val monthly_btn: String,
                     val ranges: String)
