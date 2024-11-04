package com.example.marsphotos.data

import com.example.marsphotos.network.MarsApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

/**
 * An object that contains (all) the dependencies that the app requires.
 * Can be called dependency container(?)
 */
interface AppContainer {
    val marsPhotoRepository: MarsPhotoRepository
}

class DefaultAppContainer : AppContainer {
    // The repository is a dependency (used by MarsViewModel) so it is put inside the container
    override val marsPhotoRepository: MarsPhotoRepository by lazy {
        NetworkMarsPhotoRepository(retrofitService)
    }

    private val baseUrl = "https://android-kotlin-fun-mars-server.appspot.com"

    /**
     * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
     */
    private val retrofit = Retrofit.Builder()
        // Factory to convert JSON into primitive type (String in this case)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    // Using lazy init to create the network object only when actually needed instead of from
    // the beginning, because this is an expensive task
    private val retrofitService: MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java)
    }
}