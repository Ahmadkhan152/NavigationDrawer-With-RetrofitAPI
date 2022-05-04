package com.example.navigation_drawer.Classes

import com.google.gson.annotations.SerializedName

data class Listofusers(@SerializedName("page") var page:Int,
                       @SerializedName("per_page")var per_page:Int,
                       @SerializedName("totalrecord")var totalrecord:Int,
                       @SerializedName("total_pages")var total_pages:Int,
                       @SerializedName("data")var data:ArrayList<Userinformation>)