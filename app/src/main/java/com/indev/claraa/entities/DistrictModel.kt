package com.indev.claraa.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "district_master")
data class DistrictModel(@PrimaryKey(autoGenerate = true)
                         val local_id: Int,
                         val district_id: Int,
                         val district_name: String,
                         val active: Int,
                         val state_id: Int)
