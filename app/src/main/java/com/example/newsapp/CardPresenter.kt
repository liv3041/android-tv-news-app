package com.example.newsapp

import android.graphics.drawable.Drawable
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import androidx.core.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView


import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.newsapp.databinding.CardItemBinding
import kotlin.properties.Delegates

/**
 * A CardPresenter is used to generate Views and bind Objects to them on demand.
 * It contains an ImageCardView.
 */
class CardPresenter : Presenter() {
    private var mDefaultCardImage: Drawable? = null
    private var sSelectedBackgroundColor: Int by Delegates.notNull()
    private var sDefaultBackgroundColor: Int by Delegates.notNull()

    override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
        Log.d(TAG, "onCreateViewHolder")

        sDefaultBackgroundColor = ContextCompat.getColor(parent.context, R.color.default_background)
        sSelectedBackgroundColor =
            ContextCompat.getColor(parent.context, R.color.selected_background)
//        mDefaultCardImage = ContextCompat.getDrawable(parent.context, R.drawable.movie)

        val inflater = LayoutInflater.from(parent.context)

        val binding= CardItemBinding.inflate(inflater, parent, false)
//        binding.root.tag = "card_item_tag"

        val cardView = binding.root
        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
//        updateCardBackgroundColor(, false)
        return Presenter.ViewHolder(cardView)
    }

//    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
//        val movie = item as Movie
//        val cardView = viewHolder.view as ImageCardView
//
//        Log.d(TAG, "onBindViewHolder")
//        if (movie.cardImageUrl != null) {
//            cardView.titleText = movie.title
//            cardView.contentText = movie.studio
//            cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT)
//            Glide.with(viewHolder.view.context)
//                .load(movie.cardImageUrl)
//                .centerCrop()
//                .error(mDefaultCardImage)
//                .into(cardView.mainImageView)
//        }
//    }
override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
    val article = item as Article
    val binding = CardItemBinding.bind(viewHolder.view)



    binding.cardTitle.tag = "titleTextView"
    binding.cardImage.tag = "HeadlineImageView"
    binding.cardDescription.tag = "ContentTextView"
    binding.cardLayout.tag = "LinearLayout"
    binding.cardView.tag = "CardViewTag"
    binding.cardTitle.text = article.title
    binding.cardDescription.text = article.description




    Glide.with(viewHolder.view.context)
        .load(article.urlToImage)
        .centerCrop()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .error(R.drawable.default_background)
        .into(binding.cardImage)
}


    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        Log.d(TAG, "onUnbindViewHolder")
        val binding = CardItemBinding.bind(viewHolder.view)
        // Remove references to images so that the garbage collector can free up memory
      binding.cardImage.setImageDrawable(null)
    }

    private fun updateCardBackgroundColor(view: CardView, selected: Boolean) {
        val color = if (selected) sSelectedBackgroundColor else sDefaultBackgroundColor
        // Both background colors should be set because the view"s background is temporarily visible
        // during animations.
        view.setBackgroundColor(color)
//        view.setInfoAreaBackgroundColor(color)
    }

    companion object {
        private val TAG = "CardPresenter"

        private val CARD_WIDTH = 313
        private val CARD_HEIGHT = 176
    }
}