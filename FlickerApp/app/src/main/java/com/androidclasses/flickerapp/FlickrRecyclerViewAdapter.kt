package com.androidclasses.flickerapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidclasses.flickerapp.databinding.BrowseBinding
import com.squareup.picasso.Picasso

class FlickrImageViewHolder(view : View) :RecyclerView.ViewHolder(view) {
    var thumbnail : ImageView = BrowseBinding.bind(view).thumbnail
    var title : TextView = BrowseBinding.bind(view).photoTitle
}

class FlickrRecyclerViewAdapter(private var photoList: List<Photo>)
    : RecyclerView.Adapter<FlickrImageViewHolder>() {
    private val TAG = "FlickrRecyclerViewAdapt"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlickrImageViewHolder {
        Log.d(TAG, ".onCreateViewHolder new view requested")
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.browse, parent, false)
        return FlickrImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlickrImageViewHolder, position: Int) {
        val photoItem = photoList[position]
        Log.d(TAG, ".onBindViewHolder ${photoItem.title} --> $position")

        if (photoList.isEmpty()) {
            holder.thumbnail.setImageResource(R.drawable.placeholder_image)
            holder.title.setText(R.string.empty_photo)

        } else {
            val picasso =  Picasso.Builder(holder.thumbnail.context).build()
            picasso.load(photoItem.image)
                .error(R.drawable.placeholder_image)
                .placeholder(R.drawable.placeholder_image)
                .into(holder.thumbnail)
        }

        holder.title.text = photoItem.title
    }

    override fun getItemCount(): Int {
        Log.d(TAG, ".getItemCount called")
        return if (photoList.isNotEmpty()) photoList.size else 0
    }

    fun loadNewData(newPhotos : List<Photo>) {
        photoList = newPhotos
        notifyDataSetChanged()
    }

    fun getPhoto(position: Int) : Photo? {
        return if (photoList.isNotEmpty()) photoList[position] else null
    }
}