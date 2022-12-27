package com.indev.claraa.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_master")
data class OrderMasterModel(@PrimaryKey(autoGenerate = true)
                              val local_id: Int,
                              val order_id: String,
                              val user_id: String,
                              val order_date: String,
                              val order_amount: String,
                              var error_code: String,
                              var active: String,
                              var invoice_no : String,
                              var latitude : String,
                              var longitude: String,
                              val payment_status: String)

