package com.indev.claraa.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "address")
data class AddressDetailsModel(@PrimaryKey(autoGenerate = true)
                               val local_id: Int,
                               var user_id: Int,
                               var shop_name: String,
                               var user_name: String,
                               var email: String,
                               var mobile_number: String,
                               var address1: String,
                               var city: String,
                               var state: String,
                               var district: String,
                               var pinCode: String,
                               var address2: String,
                               var landmark: String,
)
