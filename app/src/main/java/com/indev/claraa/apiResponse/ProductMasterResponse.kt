package com.indev.claraa.apiResponse

data class ProductMasterResponse(
    val product_id: Int,
    val product_name: String,
    val product_img1: String,
    val product_img2: String,
    val price: String,
    val type_id: String,
    val packet_id: String,
    val power_range: String,
    val currency: String,
    val active: Int)
