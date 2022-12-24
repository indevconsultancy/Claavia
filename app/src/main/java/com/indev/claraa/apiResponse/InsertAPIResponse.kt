package com.indev.claraa.apiResponse

data class InsertAPIResponse(val status : Int,
                             val message : String,
                             var last_insert_id : Int
)
