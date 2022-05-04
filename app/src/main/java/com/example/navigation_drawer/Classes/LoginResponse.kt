package com.example.navigation_drawer.Classes

data class LoginResponse (val code:Int,val message:String,val data:DataLoginResponse)
data class DataLoginResponse(val Id:Int,val Name:String,val Email:String,val Token:String)