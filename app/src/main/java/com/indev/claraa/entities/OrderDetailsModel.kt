package com.indev.claraa.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_details")
data class OrderDetailsModel(@PrimaryKey(autoGenerate = true)
                            val local_id: Int,
                            val id: String,
                            val order_id: String,
                            val cart_id: String,
                            val product_id: String,
                            val price: Int,
                            val quantity: Int,
                            val payment_status: String,
                            val amount: Int,
                            var user_id: Int,
                            var active: String)

