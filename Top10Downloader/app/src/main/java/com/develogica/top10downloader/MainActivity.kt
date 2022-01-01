package com.develogica.top10downloader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import kotlinx.android.synthetic.main.activity_main.*

class FeedEntry {
    var title: String = ""
    var description: String = ""
    var link: String = ""
    var guid: String = ""
    var publicationDate: String = ""

}

private const val TAG = "MainActivity"

private const val STATE_URL = "feedUrl"
private const val STATE_LIMIT = "feedLimit"


class MainActivity : AppCompatActivity() {

    private val feedViewModel:FeedViewModel by lazy {
        ViewModelProvider(this)[FeedViewModel::class.java]
    }

    private var feedUrl:String = "http://feeds.bbci.co.uk/news/rss.xml#"
    private var feedLimit = 10


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "On create called")

        val feedAdapter = FeedAdapter(this, R.layout.list_record, EMPTY_FEED_LIST)
        xmlListView.adapter = feedAdapter

        if (savedInstanceState != null) {
            feedUrl = savedInstanceState.getString(STATE_URL).toString()
            feedLimit = savedInstanceState.getInt(STATE_LIMIT)
        }


//        feedViewModel.feedEntries.observe(this,
//            Observer<List<FeedEntry>> { feedEntries -> feedEntries.setFeedList(feedEntries!!) })
        feedViewModel.feedEntries.observe(this,
            Observer<List<FeedEntry>> { feedEntries -> feedAdapter.setFeedList(feedEntries ?: EMPTY_FEED_LIST ) } )

//        val listView : ListView = findViewById(R.id.xmlListView)
        feedViewModel.downloadUrl(feedUrl)


        Log.d(TAG, "OnCreate done")
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.feeds_menu, menu)

        if (feedLimit == 10) {
            menu?.findItem(R.id.mnuTop10)?.isChecked = true
        } else {
            menu?.findItem(R.id.mnuTop25)?.isChecked = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mnuFree -> feedUrl = "http://feeds.bbci.co.uk/news/science_and_environment/rss.xml"
            R.id.mnuPaid -> feedUrl = "http://feeds.bbci.co.uk/news/entertainment_and_arts/rss.xml"
            R.id.mnuSongs -> feedUrl = "http://feeds.bbci.co.uk/news/technology/rss.xml"
            R.id.mnuTop10, R.id.mnuTop25 -> {
                if (!item.isChecked) {
                    item.isChecked = true
                    feedLimit = 35 - feedLimit
                    Log.d(TAG, "onOptionItemSelected: ${item.title} setting feedLimit to $feedLimit")
                } else {
                    Log.d(TAG, "onOptionItemSelected: ${item.title} feedLimit unchanged")
                }
            }
            R.id.mnuRefresh -> feedViewModel.invalidate()
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }

        feedViewModel.downloadUrl(feedUrl)

        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_URL, feedUrl)
        outState.putInt(STATE_LIMIT, feedLimit)
    }


}