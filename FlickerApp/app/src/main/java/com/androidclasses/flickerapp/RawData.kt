package com.androidclasses.flickerapp

import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

enum class DownloadStatus {
    OK, IDLE, NOT_INITIALIZED, FAILED_OR_EMPTY, PERMISSIONS_ERROR, ERROR
}

class RawData(private val listener: OnDownloadComplete) : AsyncTask<String, Void, String>() {
    private val TAG = "GetRawData"
    private var downloadStatus = DownloadStatus.IDLE

    interface OnDownloadComplete {
        fun onDownloadComplete(data:String, status:DownloadStatus)
    }

    override fun doInBackground(vararg params: String?): String {
        if (params[0] == null) {
            downloadStatus = DownloadStatus.NOT_INITIALIZED
            return "No url specified"
        }

        try {
            downloadStatus = DownloadStatus.OK
            return URL(params[0]).readText()
        } catch (exception : Exception) {
            val errorMessage = when (exception) {
                is MalformedURLException -> {
                    downloadStatus = DownloadStatus.NOT_INITIALIZED
                    "doInBackground: Invalid URL ${exception.message}"
                }
                is IOException -> {
                    downloadStatus = DownloadStatus.FAILED_OR_EMPTY
                    "doInBackground: IO Exception reading data ${exception.message}"
                }
                is SecurityException -> {
                    downloadStatus = DownloadStatus.PERMISSIONS_ERROR
                    "doInBackground: Security exception: Needs permission? ${exception.message}"
                }
                else -> {
                    downloadStatus = DownloadStatus.ERROR
                    "Unknown error ${exception.message}"
                }
            }

            Log.e(TAG, errorMessage)

            return errorMessage
        }
    }

    override fun onPostExecute(result: String) {
        Log.d(TAG, "onPostExecute() called, parameter is ${result}")
        listener.onDownloadComplete(result, downloadStatus)
    }
}
