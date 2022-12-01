package com.indev.claraa.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_master")
data class UserRegistrationModel(@PrimaryKey(autoGenerate = true)
                                 val local_id: Int,
                                 var user_id: Int,
                                 var shop_name: String,
                                 var user_name: String,
                                 var email: String,
                                 var mobile_number: String,
                                 var address: String,
                                 var city: String,
                                 var state: String,
                                 var district: String,
                                 var active: String,
                                 var register_date: String,
                                 var pinCode: String)

