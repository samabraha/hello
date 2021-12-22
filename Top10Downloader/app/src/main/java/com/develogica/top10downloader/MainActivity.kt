package com.develogica.top10downloader

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*

import java.net.URL
import kotlin.properties.Delegates

class FeedEntry {
    var title: String = ""
    var description: String = ""
    var link: String = ""
    var guid: String = ""
    var publicationDate: String = ""

    override fun toString(): String {
        return """
            [ $title ]
            ${"_".repeat(title.length)}
            description = $description
            link = $link
            guid = $guid
            publicationDate = $publicationDate
        """.trimIndent()
    }
}

class MainActivity : AppCompatActivity() {
    private val TAG = "TopTenApp"

    private var downloadData: DownloadData? = null

    private var feedUrl:String = "http://feeds.bbci.co.uk/news/rss.xml#"
    private var feedLimit = 10

    private var feedCachedUrl = "INVALIDATED"
    private val STATE_URL = "feedUrl"
    private val STATE_LIMIT = "feedLimit"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "On create called")

        if (savedInstanceState != null) {
            feedUrl = savedInstanceState.getString(STATE_URL).toString()
            feedLimit = savedInstanceState.getInt(STATE_LIMIT)
        }

//        val listView : ListView = findViewById(R.id.xmlListView)
        downloadUrl(feedUrl)


        Log.d(TAG, "OnCreate done")
    }


    private fun downloadUrl(feedUrl : String) {
        if (feedUrl == feedCachedUrl) {
            Log.d(TAG, "downloadUrl Url not changed")
            return
        }
        downloadData = DownloadData(this, xmlListView)
        Log.d(TAG, "downloadUrl called")
        downloadData?.execute(feedUrl)
        feedCachedUrl = feedUrl
        Log.d(TAG, "downloadUrl done")
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
            R.id.mnuRefresh -> feedCachedUrl = "INVALIDATED"
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }

        downloadUrl(feedUrl)

        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_URL, feedUrl)
        outState.putInt(STATE_LIMIT, feedLimit)
    }

    override fun onDestroy() {
        super.onDestroy()
        downloadData?.cancel(true)
    }

    companion object {
        private class DownloadData(context: Context, listView: ListView): AsyncTask<String, Void, String>() {
            private val TAG = "DownloadData"

            var propContext : Context by Delegates.notNull()
            var propListView : ListView by Delegates.notNull()

            init {
                propContext = context
                propListView = listView
            }

            override fun onPostExecute(result: String) {
                super.onPostExecute(result)
//                Log.d(TAG, "onPostExecute: parameter is $result")
                val parseApplications = ParseApplications()
                parseApplications.parse(result)

                val feedAdapter = FeedAdapter(propContext, R.layout.list_record, parseApplications.applications)
                propListView.adapter = feedAdapter
            }

            override fun doInBackground(vararg url: String?): String {
                Log.d(TAG, "doInBackground: starts with ${url[0]}")
                val rssFeed = downloadXML(url[0])
                if (rssFeed.isEmpty()) {
                    Log.e(TAG, "doInBackground: Error downloading")
                }
                return rssFeed
            }

            private fun downloadXML(urlPath: String?): String {
                return URL(urlPath).readText()
            }
/*
            private fun downloadXML(urlPath : String?): String {
                val xmlResult = StringBuilder()
                try {
                    val url = URL(urlPath)
                    val connection : HttpURLConnection = url.openConnection() as HttpURLConnection
                    val response = connection.responseCode
                    Log.d(TAG, "downloadXML: The response code was $response")

//                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
//                    val inputBuffer = CharArray(500)
//                    var charsRead = 0
//                    while (charsRead >= 0) {
//                        charsRead = reader.read(inputBuffer)
//                        if (charsRead > 0) {
//                            xmlResult.append(String(inputBuffer, 0, charsRead))
//                        }
//                    }
//                    reader.close()

                    connection.inputStream.buffered().reader().use {
                        xmlResult.append(it.readText())
                    }

                    Log.d(TAG, "downloadXML: Received ${xmlResult.length} bytes")

                    return xmlResult.toString()

//                }  catch (exception : MalformedURLException) {
//                    Log.e(TAG, "downloadXML: Invalid url ${exception.message}")
//                } catch (exception : IOException) {
//                    Log.e(TAG, "downloadXML: IO error reading data ${exception.message}")
//                } catch (exception : SecurityException) {
//                    Log.e(TAG, "downloadXML: Security exception. Needs permission? ${exception.message}")
//                } catch (exception : Exception) {
//                    Log.e(TAG, "downloadXML: Unknown error ${exception.message}")
//                }
                } catch (exception : Exception) {
                    val errorMessage : String  = when (exception) {
                        is MalformedURLException -> "downloadXML: Invalid URL ${exception.message}"
                        is IOException -> "downloadXML: IO exception reading data: ${exception.message}"
                        is SecurityException -> { exception.printStackTrace()
                            "downloadXML: Security error. Needs permission? ${exception.message}"
                        }
                        else -> "downloadXML: Unknown error ${exception.message}"
                    }

                    Log.e(TAG, errorMessage)
                }

                return "" // Problem! return empty string
            }

            */
        }
    }
}