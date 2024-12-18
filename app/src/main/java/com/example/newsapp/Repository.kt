package com.example.newsapp


import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository(private val webService: WebService) {

    private val TAG = "Repository"






    fun fetchTopHeadlines(
        country: String,
        onResult: (TopHeadline?) -> Unit,
        onError: (String) -> Unit
    ) {
        val call = webService.getTopHeadlines(
            country = country,
            apiKey = APIConstants.API_KEY
        )

        call.enqueue(object : Callback<TopHeadline> {
            override fun onResponse(
                call: Call<TopHeadline>,
                response: Response<TopHeadline>
            ) {
                if (response.isSuccessful) {
                    onResult(response.body())
                    Log.d(TAG, "onResponse:${response.body()}")
                } else {
                    onError("Response failed: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<TopHeadline>, t: Throwable) {
                onError("Error: ${t.message}")
            }
        })
    }
}
