package com.example.android.helloapp.astro

import android.provider.ContactsContract.CommonDataKinds.Website.URL
import com.google.gson.Gson
import java.net.URL

class AstroRequest {
    companion object {
        private const val ASTRO_URL = "http://api.open-notify.org/astros.json"
    }

    fun execute() : AstroResult {
        val responseString = URL(ASTRO_URL).readText()
        return Gson().fromJson(responseString, AstroResult::class.java)
    }
}