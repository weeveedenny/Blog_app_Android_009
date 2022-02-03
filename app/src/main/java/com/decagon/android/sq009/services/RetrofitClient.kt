package com.decagon.android.sq009.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com"
    private var firstInstance: Retrofit? = null
    val instance: Retrofit

    /* create a retrofit instance to call the end point and retrieve information
        set the default converter
        build the retrofit object  */

        get() {
            if (firstInstance == null)
                firstInstance = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            //Get a reusable retrofit object
            return firstInstance!!
        }

    val api: ApiEndPointInterface by lazy {
        instance.create(ApiEndPointInterface::class.java)
    }
}


