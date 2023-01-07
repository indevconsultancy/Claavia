package com.indev.claraa.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "slider")
data class SliderModel(@PrimaryKey(autoGenerate = false)
                       val id: String,
                       val image_url: String,
                       val status: String)
