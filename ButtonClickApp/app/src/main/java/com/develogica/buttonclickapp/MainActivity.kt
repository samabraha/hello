package com.develogica.buttonclickapp

import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener

private const val TAG = "MyActivity"
private const val TEXT_CONTENTS = "TextContent"

class MainActivity : AppCompatActivity() {

    private var textView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: called")
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val userInput: EditText = findViewById(R.id.editText)
        val button: Button = findViewById(R.id.button)
        textView = findViewById(R.id.textView)
        textView?.text = ""
        textView?.movementMethod = ScrollingMovementMethod()
        userInput.text.clear()

        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Log.d(TAG, "onClick: called")

                if (!userInput.text.isEmpty()) {
                    textView?.append("<> ")
                    textView?.append(userInput.text)
                    textView?.append("\n")
                    userInput.text.clear()
                }
            }
        })

        userInput.addTextChangedListener {
            button.isEnabled = userInput.text.length > 0
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: called")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(TAG, "onRestoreInstanceState: called")

//        val savedString = savedInstanceState?.getString(TEXT_CONTENTS, "")
//        textView?.text = savedString
        textView?.text = savedInstanceState?.getString(TEXT_CONTENTS, "")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: called")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState: called")

        outState?.putString(TEXT_CONTENTS, textView?.text.toString())
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: called")

    }
}