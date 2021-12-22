package com.androidclasses.flickerapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidclasses.flickerapp.databinding.ActivityMainBinding
import com.androidclasses.flickerapp.databinding.ContentMainBinding


class MainActivity : BaseActivity(), RawData.OnDownloadComplete,
    FlickrJsonData.OnDataAvailable, RecyclerItemClickListener.OnRecyclerClickListener {
    private val TAG = "ActivityMain"

    private val flickrRecyclerViewAdapter = FlickrRecyclerViewAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate() called")
        super.onCreate(savedInstanceState)

        val mainActivity = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        activateToolbar(false)

        val recyclerView = ContentMainBinding.inflate(layoutInflater).recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(this, recyclerView, this))
        recyclerView.adapter = flickrRecyclerViewAdapter

        Log.d(TAG, "onCreate: endes")
    }

    private fun createUri(baseUrl: String, searchCriteria:String, lang:String, matchAll: Boolean) : String {
        Log.d(TAG, "createUri() starts")
        return Uri.parse(baseUrl).buildUpon()
            .appendQueryParameter("tags", searchCriteria)
            .appendQueryParameter("tagmode", if (matchAll) "ALL" else "ANY")
            .appendQueryParameter("lang", lang)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .build()
            .toString()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d(TAG, "onCreateOptionsMenu() called")
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected() called")
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_controller_view_tag)
//        return navController.navigateUp(appBarConfiguration)
//                || super.onSupportNavigateUp()
//    }

    override fun onDownloadComplete(data : String, status : DownloadStatus) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete() called.")

            val getFlickrJsonData = FlickrJsonData(this)
            getFlickrJsonData.execute(data)
        } else {
            Log.d(TAG, "onDownloadComplete() failed with status ${status}. Error message is ${data}")
        }
    }

    override fun onDataAvailable(data: List<Photo>) {
        Log.d(TAG, ".onDataAvailable called")

        flickrRecyclerViewAdapter.loadNewData(data)

        Log.d(TAG, ".onDataAvailable ends")
    }

    override fun onError(exception: Exception) {
        Log.d(TAG, ".onError called with ${exception.message}")
    }

    override fun onItemClick(view: View, position: Int) {
//        Log.d(TAG, ".onItemClick sterted")
        Toast.makeText(this, "Normal tap at position $position", Toast.LENGTH_SHORT)
            .show()
    }

    override fun onItemLongClick(view: View, position: Int) {
        Log.d(TAG, ".onItemLongClick started")
//        Toast.makeText(this, "Long tap at position $position", Toast.LENGTH_SHORT)
//            .show()
        val photo = flickrRecyclerViewAdapter.getPhoto(position)
        if (photo != null) {
            val intent = Intent(this, PhotoDetailsActivity::class.java)
            intent.putExtra(PHOTO_TRANSFER, photo)
            startActivity(intent)

        }
    }



    override fun onResume() {
        Log.d(TAG, ".onResume: called")
        super.onResume()

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val queryResult = sharedPref.getString(FLICKR_QUERY, "")

        if (queryResult != null && queryResult.isNotEmpty()) {
            val url = createUri(
                "https://api.flickr.com/services/feeds/photos_public.gne",
                queryResult, "en-us", true
            )
            val getRawData = RawData(this)
            getRawData.execute(url)
        }

        Log.d(TAG, "onResume: ends")
    }
}