package com.indev.claraa.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_master")
data class UserRegistrationModel(@PrimaryKey(autoGenerate = true)
                                 var user_id: Int,
                                 var shop_name: String,
                                 var owner_name: String,
                                 var user_name: String,
                                 var password: String,
                                 var email: String,
                                 var mobile_number: String,
                                 var address: String,
                                 var state_id: String,
                                 var district_id: String,
                                 var active: String,
                                 var register_date: String,
                                 var gender: String,
                                 var latitude: String,
                                 var longitude: String,
                                 var credit: String,
                                 var approvel_id: String,
                                 var market_id: String,
                                 var pinCode: String)

