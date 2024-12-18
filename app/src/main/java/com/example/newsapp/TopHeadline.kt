package com.example.newsapp

import com.google.gson.annotations.SerializedName

data class TopHeadline(
    @SerializedName("status")
    val status:String = "",
    @SerializedName("totalResults")
     val totalResults:String = "",
    @SerializedName("articles")
   val articles: List<Article> = emptyList()
)
