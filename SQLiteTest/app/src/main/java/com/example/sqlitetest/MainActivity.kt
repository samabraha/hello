package com.example.sqlitetest

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.example.sqlitetest.databinding.ActivityMainBinding

private const val TAG = "ActivityMain"

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val database = baseContext.openOrCreateDatabase("sqlite-test-1.db", MODE_PRIVATE, null)
        database.execSQL("DROP TABLE IF EXISTS contacts")

        var sql = "CREATE TABLE IF NOT EXISTS contacts(_id INTEGER PRIMARY KEY NOT NULL, name TEXT, phone INTEGER, email TEXT)"

        Log.d(TAG, "onCreate: sql is $sql")

        database.execSQL(sql)

        sql = "INSERT INTO contacts(name, phone, email) VALUES('Sam Abraha', 564164, 'samabr@coInfo.com')"
        database.execSQL(sql)

        val values = ContentValues().apply {
            put("name", "Miletus")
            put("phone", 12345)
            put("email", "fred@freddApp.com")
        }

        val generatedId = database.insert("contacts", null, values)

        val query = database.rawQuery("SELECT * FROM contacts", null)
        query.use {
            while (it.moveToNext()) {
                with(query) {
                    val id = getLong(0)
                    val name = getString(1)
                    val phone = getInt(2)
                    val email = getString(3)

                    val result = "id = $id, name = $name, phone = $phone, email = $email"

                    Log.d(TAG, "onCreate: [$result]")
                }
            }
        }

        database.close()

        Log.d(TAG, "onCreate: record added with id $generatedId")

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}