package com.indev.claraa.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "district_master")
data class DistrictModel(@PrimaryKey(autoGenerate = false)
                         val district_id: String,
                         val district_name: String,
                         val active: String,
                         val state_id: String)
