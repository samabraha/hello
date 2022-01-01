package com.develogica.top10downloader

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

private const val TAG = "FeedViewModel"

val EMPTY_FEED_LIST: List<FeedEntry> = Collections.emptyList()

class FeedViewModel : ViewModel(), DownloadData.DownloaderCallback {
    private lateinit var downloadData: DownloadData
    private var feedCachedUrl = "INVALIDATED"

    private val feed = MutableLiveData<List<FeedEntry>>()
    val feedEntries: LiveData<List<FeedEntry>>
        get() = feed

    init {
        feed.postValue(EMPTY_FEED_LIST)
    }


    fun downloadUrl(feedUrl : String) {
        Log.d(TAG, "downloadUrl: called with url $feedUrl")
        if (feedUrl == feedCachedUrl) {
            Log.d(TAG, "downloadUrl Url not changed")
            return
        }

        downloadData = DownloadData(this)
        Log.d(TAG, "downloadUrl called")
        downloadData.execute(feedUrl)
        feedCachedUrl = feedUrl
        Log.d(TAG, "downloadUrl done")
    }

    fun invalidate() {
        feedCachedUrl = "INVALIDATE"
    }

    override fun onDataAvailable(data: List<FeedEntry>) {
        Log.d(TAG, "onDataAvailable: starts")
        feed.value = data
        Log.d(TAG, "onDataAvailable: ends")
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared: cancelling pending downloads")
        downloadData.cancel(true)
    }
}