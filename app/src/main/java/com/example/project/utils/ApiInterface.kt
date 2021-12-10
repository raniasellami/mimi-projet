package com.example.project.utils

import com.example.project.models.User
//import com.google.firebase.appdistribution.models.UploadResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiInterface {
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("login")
    fun seConnecter(@Field("username") username: String,
                    @Field("password") password: String): Call<User>
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("register")
    fun register(@Field("firstname") firstname: String,
                 @Field("lastname") lastname: String,
                 @Field("email") email: String,
                 @Field("password") password: String
                 ,@Field("avatar") avatar: String): Call<User>

 /*   @Multipart
    @POST("Accept: application/json")
    fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("desc") desc: RequestBody
    ): Call<UploadResponse>*/

    companion object {

        var BASE_URL = "http://172.17.8.95:3000/api/"

        fun create() : ApiInterface {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(ApiInterface::class.java)
        }
    }
}