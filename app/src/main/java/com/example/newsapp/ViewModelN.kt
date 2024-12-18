package com.example.newsapp

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ViewModelN (context: Context): ViewModel() {
    private val repository = Repository(WebServiceClient.getClient(context).create(WebService::class.java))

    private val _topHeadlines = MutableLiveData<TopHeadline>()
    val topHeadlines: LiveData<TopHeadline> get() = _topHeadlines

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchTopHeadlines(country: String) {
        viewModelScope.launch {
            try {
                repository.fetchTopHeadlines(
                    country = country,
                    onResult = { result ->
                        _topHeadlines.postValue(result)
                        Log.d("ViewModel", "Fetched articles: ${topHeadlines.value}")
                    },
                    onError = { errorMessage ->
                        Log.e("ViewModel", "API Error: ${errorMessage}")
                        _error.postValue(errorMessage)
                    }
                )

            }catch (e:Exception){
                Log.e("ViewModel", "Exception in fetchTopHeadlines", e)
                _error.postValue("Exception: ${e.message}")
            }


        }

    }
}
