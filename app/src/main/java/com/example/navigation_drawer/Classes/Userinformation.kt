package com.example.navigation_drawer.Classes

import com.google.gson.annotations.SerializedName

data class Userinformation(
    @SerializedName("id") var id:Int,
    @SerializedName("name") var name:String,
    @SerializedName("email") var email:String,
    @SerializedName("profilepicture") var profilepicture:String,
    @SerializedName("location") var location:String,
    @SerializedName("createdat") var createdat:String)