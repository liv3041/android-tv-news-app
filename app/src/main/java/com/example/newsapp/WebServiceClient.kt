package com.example.newsapp


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object  WebServiceClient {

    private lateinit var cache: Cache

    fun initialize(context: Context) {
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        cache = Cache(context.cacheDir, cacheSize.toLong())
    }


    fun getClient(context: Context): Retrofit {
        if (!::cache.isInitialized) {
            throw IllegalStateException("WebServiceClient is not initialized. Call initialize(context) first.")
        }

//        for logging details of api
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val client: OkHttpClient = OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor{chain ->
                var request = chain.request()
                request = if (hasNetwork(context)) {
                    // Use fresh data if network is available
                    request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
                } else {
                    // Use cache if no network
                    request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24).build()
                }
                chain.proceed(request)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(APIConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    }
    //    check network state on demand

    fun hasNetwork(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }
}





