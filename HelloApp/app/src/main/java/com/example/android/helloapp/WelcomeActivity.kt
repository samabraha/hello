package com.example.android.helloapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.android.helloapp.databinding.ActivityWelcomeBinding
import org.jetbrains.anko.view

private lateinit var binding: ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_welcome)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val name = intent.extras?.get("user") as String

        binding.welcomeText.text = getString(R.string.greeting, name)

    }
}