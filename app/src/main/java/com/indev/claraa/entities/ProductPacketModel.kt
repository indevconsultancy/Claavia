package com.indev.claraa.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_packet")
data class ProductPacketModel(@PrimaryKey(autoGenerate = false)
                            val packet_id: String,
                            val packet_size: String,
                            var active: String)

