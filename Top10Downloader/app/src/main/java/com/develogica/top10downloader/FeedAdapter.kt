package com.develogica.top10downloader

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ViewHolder(v: View) {
    val feedTitle: TextView = v.findViewById(R.id.feedTitle)
    val feedDescription: TextView = v.findViewById(R.id.feedDescription)
    val feedDate: TextView = v.findViewById(R.id.feedDate)
}

private const val TAG = "FeedAdapter"

class FeedAdapter(context:Context, private val resource : Int, private var feedList : List<FeedEntry>)
    : ArrayAdapter<FeedEntry>(context, resource) {

    private val inflater = LayoutInflater.from(context);

    fun setFeedList(feedList: List<FeedEntry>) {
        this.feedList = feedList
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
//        Log.d(TAG, "getCount() called")
        return feedList.size
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        Log.d(TAG, "getView() called")

        val view:View
        val viewHolder:ViewHolder

        if (convertView == null) {
//            Log.d(TAG, "getView() called with null convertView")
            view = inflater.inflate(resource, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
//            Log.d(TAG, "getView() called, provided convertView")
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

//        val feedTitle:TextView = view.findViewById(R.id.feedTitle)
//        val feedDate:TextView = view.findViewById(R.id.feedDate)
//        val feedDescription:TextView = view.findViewById(R.id.feedDescription)

        val currentItem = feedList[position]

        viewHolder.feedTitle.text = currentItem.title
        viewHolder.feedDate.text = currentItem.publicationDate
        viewHolder.feedDescription.text = currentItem.description

        return view
    }
}