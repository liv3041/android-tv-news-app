package com.example.newsapp

import com.google.gson.annotations.SerializedName

data class Source (
//    The serialized name annotations is used in serializing and desearialising the JSON data into Java objects.
     @SerializedName("id")
      val id:String = "",
     @SerializedName("name")
     val name:String = "",
    @SerializedName("description")
    val description:String = "",
     @SerializedName("url")
    val url:String = "",
     @SerializedName("category")
    val category:String = "",
     @SerializedName("language")
    val language:String = "",
     @SerializedName("country")
    val country:String = ""

 )