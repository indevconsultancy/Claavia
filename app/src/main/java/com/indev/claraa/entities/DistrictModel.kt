package com.indev.claraa.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "district_master")
data class DistrictModel(@PrimaryKey(autoGenerate = false)
                         var district_id: String,
                         var district_name: String,
                         val active: String,
                         val state_id: String)
