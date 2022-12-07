package com.indev.claraa.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "state_master")
data class StateModel(@PrimaryKey(autoGenerate = true)
                      val local_id: Int,
                      val state_id: Int,
                      val state_name: String,
                      val active: Int)
