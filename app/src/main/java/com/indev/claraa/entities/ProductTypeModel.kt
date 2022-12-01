package com.indev.claraa.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_type")
data class ProductTypeModel(@PrimaryKey(autoGenerate = true)
                              val local_id: Int,
                              val id: Int,
                              val type_id: String,
                              val type_name: String,
                              var active: String)
