package com.example.newsapp

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.FragmentActivity
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * Loads [MainFragment].
 */
class MainActivity : FragmentActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_browse_fragment, MainFragment())
                .commitNow()
        }
    }

//    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
//        when(keyCode){
//            KeyEvent.KEYCODE_DPAD_DOWN -> {
//
//                val fragment = supportFragmentManager.findFragmentById(R.id.main_browse_fragment) as? MainFragment
//                fragment?.loadNews()
//                return true
//            }
//
//            }
//        return super.onKeyLongPress(keyCode, event)
//        }
//override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
//    when (keyCode) {
//        KeyEvent.KEYCODE_DPAD_DOWN -> {
//            Log.d(TAG, "onKeyLongPress: ")
//            // Show loader
//            val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
//            progressBar.visibility = View.VISIBLE
//
//            // Find the fragment and trigger the loadNews function
//
//            Handler(Looper.getMainLooper()).postDelayed({
//                // Once data is loaded, execute the callback
//                onDataLoaded()
//            }, 2000) // Simulate a 2-second delay for loading
//
//
//
//            return true
//        }
//    }
//    return super.onKeyLongPress(keyCode, event)
//}
//
//    private fun onDataLoaded() {
//        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
//        progressBar.visibility = View.GONE
//        val fragment = supportFragmentManager.findFragmentById(R.id.main_browse_fragment) as? MainFragment
//        fragment?.loadNews()
//    }
override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
    when (keyCode) {
        KeyEvent.KEYCODE_DPAD_DOWN -> {
            Log.d(TAG, "onKeyLongPress: ")
            val fragment = supportFragmentManager.findFragmentById(R.id.main_browse_fragment) as? MainFragment

            // Show loader
            val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
            progressBar.visibility = View.VISIBLE

            // Trigger loadNews after a 2-second delay to simulate a loading time
            Handler(Looper.getMainLooper()).postDelayed({
                // Once data is loaded, execute the callback

                fragment?.loadNews()
            }, 2000) // Simulate a 2-second delay for loading

            return true
        }
    }
    return super.onKeyLongPress(keyCode, event)
}



}
