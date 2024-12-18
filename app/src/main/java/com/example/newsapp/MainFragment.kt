package com.example.newsapp

import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.OnItemViewSelectedListener
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Timer

/**
 * Loads a grid of cards with movies to browse.
 */
class MainFragment : BrowseSupportFragment() {


    private lateinit var mBackgroundManager: BackgroundManager
    private var mDefaultBackground: Drawable? = null
    private lateinit var mMetrics: DisplayMetrics
    private var mBackgroundTimer: Timer? = null
    private var mBackgroundUri: String? = null

    private lateinit var  newsViewModel: ViewModelN




    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        super.onActivityCreated(savedInstanceState)
        WebServiceClient.initialize(requireContext())
        val viewModelFactory = ViewModelFactory(requireContext())
        newsViewModel = ViewModelProvider(this, viewModelFactory)[ViewModelN::class.java]

        prepareBackgroundManager()

        setupUIElements()

        loadNews()



        setupEventListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: " + mBackgroundTimer?.toString())
        mBackgroundTimer?.cancel()
    }

    private fun prepareBackgroundManager() {

        mBackgroundManager = BackgroundManager.getInstance(activity)
        mBackgroundManager.attach(activity!!.window)
        mDefaultBackground = ContextCompat.getDrawable(activity!!, R.drawable.default_background)
        mMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(mMetrics)
    }

    private fun setupUIElements() {
        title = getString(R.string.browse_title)
        // over title
        headersState = BrowseSupportFragment.HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true

        // set fastLane (or headers) background color
        brandColor = ContextCompat.getColor(activity!!, R.color.fastlane_background)
        // set search icon color
        searchAffordanceColor = ContextCompat.getColor(activity!!, R.color.search_opaque)
    }
     fun loadNews() {
        CoroutineScope(Dispatchers.Main).launch {

            try {
                newsViewModel.topHeadlines.observe(viewLifecycleOwner, Observer { topHeadlines ->


                    val article = topHeadlines.articles?: emptyList()

                    // Update UI on the main thread
                    setupRowsWithArticles(article)
                    Log.d(TAG, "loadNews: ${article}")
                })

                newsViewModel.error.observe(viewLifecycleOwner, Observer { error ->
                    println("Error: $error")
                })

                // Trigger API call
                newsViewModel.fetchTopHeadlines(country = "us")



            } catch (e: Exception) {
                Log.e("NewsError", "Error fetching news", e)
            }
        }
    }

    private fun setupRowsWithArticles(articles: List<Article>) {

        if (articles.isEmpty()) {
            Toast.makeText(context, "No articles found", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "No articles found")
            return
        }

        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val cardPresenter = CardPresenter()
        val listRowAdapter = ArrayObjectAdapter(cardPresenter)

        for (article in articles) {
            listRowAdapter.add(article)

        }
        val header = HeaderItem(articles.size.toLong(), "Top Headlines")
        rowsAdapter.add(ListRow(header, listRowAdapter))



        adapter = rowsAdapter
    }


    private val receiver = NetworkReceiver { isOnline ->
        if (isOnline) {
            // Handle online state

        } else {
            // Handle offline state
            Toast.makeText(requireContext(),"You are offline",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        // Register the receiver to listen for connectivity changes
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        requireContext().registerReceiver(receiver, filter)
    }

    override fun onStop() {
        super.onStop()
        // Unregister the receiver when its no longer needed
        requireContext().unregisterReceiver(receiver)
    }




    private fun setupEventListeners() {
        setOnSearchClickedListener {
            Toast.makeText(activity!!, "Implement your own in-app search", Toast.LENGTH_LONG)
                .show()
        }

        onItemViewClickedListener = ItemViewClickedListener()
//        onItemViewSelectedListener = ItemViewSelectedListener()
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder,
            item: Any,
            rowViewHolder: RowPresenter.ViewHolder,
            row: Row
        ) {

            if (item is Article) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
                startActivity(intent)
            }


        }
    }


//
//    private inner class ItemViewSelectedListener : OnItemViewSelectedListener {
//        override fun onItemSelected(
//            itemViewHolder: Presenter.ViewHolder?, item: Any?,
//            rowViewHolder: RowPresenter.ViewHolder, row: Row
//        ) {
//            if (item is Article) {
//                mBackgroundUri = item.urlToImage
////                startBackgroundTimer()
//            }
//        }
//    }

//    private fun updateBackground(uri: String?) {
//        val width = mMetrics.widthPixels
//        val height = mMetrics.heightPixels
//        Glide.with(activity!!)
//            .load(uri)
//            .centerCrop()
//            .error(mDefaultBackground)
//            .into<SimpleTarget<Drawable>>(
//                object : SimpleTarget<Drawable>(width, height) {
//                    override fun onResourceReady(
//                        drawable: Drawable,
//                        transition: Transition<in Drawable>?
//                    ) {
//                        mBackgroundManager.drawable = drawable
//                    }
//                })
//        mBackgroundTimer?.cancel()
//    }

//    private fun startBackgroundTimer() {
//        mBackgroundTimer?.cancel()
//        mBackgroundTimer = Timer()
//        mBackgroundTimer?.schedule(UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY.toLong())
//    }

//    private inner class UpdateBackgroundTask : TimerTask() {
//
//        override fun run() {
//            mHandler.post { updateBackground(mBackgroundUri) }
//        }
//    }



    companion object {
        private val TAG = "MainFragment"
    }
}