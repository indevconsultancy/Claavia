package com.indev.claraa.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartModel(@PrimaryKey(autoGenerate = true)
                     val local_id: Int,
                     val id: Int,
                     val packets: String,
                     val product_id: String,
                     val user_id: Int,
                     val product_name: String,
                     val product_img1: String,
                     val product_img2: String,
                     var price: String,
                     var amount: Int,
                     var quantity: String,
                     var type_id: String,
                     var packet_id: String,
                     var power_range: String,
                     var currency: String,
                     var cart_date: String,
                     var latitude : String,
                     var longitude: String,
                     var active: String
                     )
