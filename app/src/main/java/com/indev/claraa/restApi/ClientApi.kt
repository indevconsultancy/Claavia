package com.indev.claraa.restApi

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ClientApi {

    companion object{
        private const val BASE_URL = "https://claraa.indevconsultancy.in/api/"
        val BASE_IMAGE_URL = "https://claraa.indevconsultancy.in/api/product_image/"

        private var retrofit: Retrofit? = null
        fun getClient(): Retrofit? {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.NONE
            val client = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(logging).build()
            val gson = GsonBuilder()
                .setLenient()
                .create()
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
            return retrofit
        }
    }
}