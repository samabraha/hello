package com.androidclasses.youtubeplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView


//const val YOUTUBE_VIDEO_ID = "Q_1M2JaijjQ"
const val YOUTUBE_VIDEO_ID = "Ec-9w4wWZt0"

const val YOUTUBE_PLAYLIST = "PLFFB16EE3B2041866"

class YoutubeActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {
    private val TAG = "YoutubeActivity"
    private val DIALOG_REQUEST_CODE = 1

    val playerView by lazy { YouTubePlayerView(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_youtube)
//        val layout = findViewById<ConstraintLayout>(R.id.activity_youtube)

        val layout = layoutInflater.inflate(R.layout.activity_youtube, null) as ConstraintLayout
        setContentView(layout)

//        val button1 = Button(this)
//        button1.layoutParams = ConstraintLayout.LayoutParams(600, 180)
//        button1.text = "Button added"
//        layout.addView(button1)

        playerView.layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        layout.addView(playerView)

        playerView.initialize(getString(R.string.GOOGLE_API_KEY), this)
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider?, player: YouTubePlayer?, wasRestored: Boolean
    ) {
        Log.d(TAG, "onInitializationSuccess: provider is ${provider?.javaClass}")
        Log.d(TAG, "onInitializationSuccess: provider is ${player?.javaClass}")
        Toast.makeText(this, "Initialized YoutubePlayer successfully", Toast.LENGTH_SHORT).show()

        player?.setPlayerStateChangeListener(playerStateChangeListener)
        player?.setPlaybackEventListener(playbackEventListener)
        if (!wasRestored) {
            player?.loadVideo(YOUTUBE_VIDEO_ID)
        } else {
            player?.play()
        }
    }

    override fun onInitializationFailure(
        provider: YouTubePlayer.Provider?, initializationResult: YouTubeInitializationResult?
    ) {
        if (initializationResult?.isUserRecoverableError == true) {
            initializationResult.getErrorDialog(this, DIALOG_REQUEST_CODE).show()
        } else {
            val errorMessage = "There was an error initializing the YoutubePlayer ${initializationResult}"
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    private val playbackEventListener = object : YouTubePlayer.PlaybackEventListener {
        override fun onPlaying() {
            Toast.makeText(this@YoutubeActivity, "Good, video is playing ok!", Toast.LENGTH_SHORT).show()
        }

        override fun onPaused() {
            Toast.makeText(this@YoutubeActivity, "Haha, you paused the video.", Toast.LENGTH_SHORT).show()
        }

        override fun onStopped() {

        }

        override fun onBuffering(p0: Boolean) {

        }

        override fun onSeekTo(p0: Int) {

        }
    }

    private val playerStateChangeListener = object : YouTubePlayer.PlayerStateChangeListener {
        override fun onLoading() {

        }

        override fun onLoaded(p0: String?) {

        }

        override fun onAdStarted() {
            Toast.makeText(this@YoutubeActivity, "Click ad now. Make the video creator rich!", Toast.LENGTH_SHORT).show()
        }

        override fun onVideoStarted() {
            Toast.makeText(this@YoutubeActivity, "Well done for choosing our video!", Toast.LENGTH_SHORT).show()
        }

        override fun onVideoEnded() {
            Toast.makeText(this@YoutubeActivity, "Congratulations! You've completed one video.", Toast.LENGTH_SHORT).show()
        }

        override fun onError(p0: YouTubePlayer.ErrorReason?) {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult called with response code ${resultCode} for request ${requestCode}")

        if (requestCode == DIALOG_REQUEST_CODE) {
            Log.d(TAG, intent.toString())
            Log.d(TAG, intent.extras.toString())
            playerView.initialize(getString(R.string.GOOGLE_API_KEY), this)
        }
    }
}   