package com.example.navigation_drawer.Retrofit_Interfaces

import com.example.navigation_drawer.Classes.Listofusers
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterfaceUserList {

    @GET("users")
    fun listRepos(@Query("page")page:String):Call<Listofusers?>
    val post:Call<Listofusers?>

    companion object {
        const val baseUrl="http://restapi.adequateshop.com/api/"
    }
}