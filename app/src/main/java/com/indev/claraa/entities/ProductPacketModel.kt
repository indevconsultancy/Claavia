package com.indev.claraa.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_packet")
data class ProductPacketModel(@PrimaryKey(autoGenerate = true)
                            val local_id: Int,
                            val id: Int,
                            val packet_id: String,
                            val packet_name: String,
                            var active: String)

