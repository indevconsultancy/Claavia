package com.indev.claraa.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_master")
data class ProductMasterModel(@PrimaryKey(autoGenerate = false)
                              val product_id: String,
                              val product_name: String,
                              val product_img1: String,
                              val product_img2: String,
                              var price: String,
                              var type_id: String,
                              var packet_id: String,
                              var power_range: String,
                              var currency: String,
                              var active: String)

