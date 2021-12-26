package com.example.android.helloapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.example.android.helloapp.astro.AstroRequest
import com.example.android.helloapp.databinding.ActivityWelcomeBinding
import com.google.android.material.snackbar.Snackbar
import org.jetbrains.anko.appcompat.v7.actionBarContextView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
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

        insertNameAndUpdateView(name)
    }

    private fun insertNameAndUpdateView(name: String) {
        doAsync {
            val dao: = NamesDAO(applicationContext)

            if (!dao.exists(name)) {
                dao.insertName(name)
            }
            val names = dao.getAllNames()

            uiThread {
                binding.namesList.adapter =
                    ArrayAdapter<String>(this, R.layout.simple_list_item_1, names)

            }
        }
    }

    private fun deleteAllNames() {
        doAsync {
            NamesDAO(applicationContext).deleteAllNames()

            uiThread {
                binding.namesList.adapter =
                    ArrayAdapter<String>(this, R.layout.simple_list_item_1,
                    arrayListOf())
                binding.welcomeText.text = "Hello World!"
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.update_astronauts -> {
                updateAstronauts()
                true
            }
            R.id.clear_database -> {
                deleteAllNames()
                true
            }
            R.id.about -> {
//                Snackbar.make(this.actionBarContextView(), "DEVELOGICA", Snackbar.LENGTH_LONG).show()
                toast("Hello from develogica")
                true
            } else -> super.onOptionsItemSelected(item)
        }

    private fun updateAstronauts() {
        doAsync {
            val result = AstroRequest().execute()
            val astronauts = result.people.map { "${it.name} on the ${it.craft}" }
            uiThread {
                numPeopleText.text = getString(R.string.number_of_people, result.number)

                binding.astronautNamesList.adapter = ArrayAdapter<String>(
                    this, R.layout.simple_list_item_1, astronauts
                )

            }
        }
    }
}