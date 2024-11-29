package com.zaqly.storyapp.network.api

import com.zaqly.storyapp.network.response.FileUploadResponse
import com.zaqly.storyapp.network.response.LoginResponse
import com.zaqly.storyapp.network.response.RegisterResponse
import com.zaqly.storyapp.network.response.StoryDetailResponse
import com.zaqly.storyapp.network.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path


interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getAllStories(): StoryResponse

    @GET("stories/{id}")
    suspend fun getStoryDetail(
        @Path("id") id: String
    ): StoryDetailResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): FileUploadResponse



}