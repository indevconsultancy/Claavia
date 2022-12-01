package com.indev.claraa.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class Cart(@PrimaryKey(autoGenerate = true)
                val id: Int,
                val packets: String,
                val ranges: String)
