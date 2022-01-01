package com.develogica.top10downloader


import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import kotlin.math.log

private const val TAG = "DownloadData"

class DownloadData(private val callBack: DownloaderCallback) : AsyncTask<String, Void, String>() {

    interface DownloaderCallback {
        fun onDataAvailable(data: List<FeedEntry>)
    }

    override fun onPostExecute(result: String) {
        val parseApplications = ParseApplications()
        if (result.isNotEmpty()) {
            parseApplications.parse(result)
        }
        callBack.onDataAvailable(parseApplications.applications)
    }

    override fun doInBackground(vararg url: String): String {
        Log.d(TAG, "doInBackground: starts with ${url[0]}")
        val rssFeed = downloadXML(url[0])
        if (rssFeed.isEmpty()) {
            Log.e(TAG, "doInBackground: Error downloading")
        }
        return rssFeed
    }

    private fun downloadXML(urlPath: String): String {
        try {
            return URL(urlPath).readText()
        } catch (muException: MalformedURLException) {
            Log.d(TAG, "downloadXML: Invalid URL ${muException.message}")
        } catch (ioException :IOException) {
            Log.d(TAG, "downloadXML: IO Exception reading data ${ioException.message}")
        } catch (securityException : SecurityException) {
            Log.d(TAG, "downloadXML: Security exception. Needs permission? ${securityException.message}")
            securityException.printStackTrace()
        }

        return ""
    }
}