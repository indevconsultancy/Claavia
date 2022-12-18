package com.indev.claraa.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_details")
data class OrderDetailsModel(@PrimaryKey(autoGenerate = true)
                            val local_id: Int,
                            val id: Int,
                            val order_id: String,
                            val order_detail_id: String,
                            val product_id: String,
                            val amount: String,
                            var latitude : String,
                            var longitude: String,
                            var user_id: Int,
                            var order_date: String,
                            var active: String)

