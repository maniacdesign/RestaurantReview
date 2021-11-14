package com.submission.restaurantreview.service

import PostReviewResponse
import RestaurantResponse
import retrofit2.Call
import retrofit2.http.*

/*
API Service merupakan interface yang berisi kumpulan endpoint yang digunakan pada sebuah aplikasi, pada aplikasi ini terdapat
2 endpoint yaitu:
1. fungsi getRestaurant dengan anotasi @GET untuk mengambil data
    kita dapat mengganti variabel {id} pada endpoint dengan menggunakkan @Path sehingga bisa mengakses detail
    suatu restoran dengan id nya
2. fungsi postReview dengan anotasi @POST untuk mengirim data
    kita juga dapat menambahkan @Header untuk menyematkan token jika API tersebut membutuhkan otorisasi
    kemudian kita juga harus menggunakan anotasi @FormUrlEncoded untuk mengirimkan data menggunakan @Field
    pastikan key yang dimasukkan pada @Field harus sama dengan field yang ada pada API
 */
interface ApiService {
    @GET("detail/{id}")
    fun getRestaurant(
        @Path("id") id:String
    ): Call<RestaurantResponse>

    @FormUrlEncoded
    @Headers("Athorization: token 12345")
    @POST("review")
    fun postReview(
        @Field("id") id: String,
        @Field("name") name: String,
        @Field("review") review: String
    ): Call<PostReviewResponse>
}