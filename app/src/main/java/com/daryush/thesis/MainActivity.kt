package com.daryush.thesis

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.turingcomplete.kotlinonetimepassword.HmacAlgorithm
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordConfig
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private var btn_scan:Button?= null
    private var btn_show:Button?= null
    private var progressBar:ProgressBar?= null
    private var newToTp: ToTpDataBase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //init
        newToTp = ToTpDataBase.getAppDatabase(this)
        btn_show = findViewById<Button>(R.id.btn_Show)
        btn_scan = findViewById<Button>(R.id.btn_Scan)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)


        //button
        btn_scan?.setOnClickListener {
            val intent = Intent(this,ScanPage::class.java)
            startActivity(intent)
        }
        //button
//        btn_show?.setOnClickListener {
//            getData()
//        }

        //refresh data
        startAutoRefresh()
    }

    //show database
    private fun getData() {
        // Launch a coroutine in the Main (UI) scope
        lifecycleScope.launch {
            // Do the background work in the IO context
            val data = withContext(Dispatchers.IO) {
                newToTp!!.toTpDao().getAll() // Fetch the data from the database
            }

            val rec = findViewById<RecyclerView>(R.id.rec)
            // Update the UI (RecyclerView) after the data is fetched
            rec.layoutManager = LinearLayoutManager(this@MainActivity)
            val newData = MutableList(data.size) { i -> data[i] }
            val adapter = RecyclerAdapter(newData, this@MainActivity, newToTp!!, this@MainActivity)

            rec.adapter = adapter
        }
    }


    //refresh data
    private fun startAutoRefresh() {
        lifecycleScope.launch {
            while (isActive) {
                getData()
                for (i in 300 downTo 0) {
                    progressBar!!.progress = i

                    when (i) {
                        in 100..300 -> progressBar!!.setProgressDrawableTiled(resources.getDrawable(R.drawable.progress_bar_normal))
                        in 50..99 -> progressBar!!.setProgressDrawableTiled(resources.getDrawable(R.drawable.progress_bar_last_10_sec))
                        in 0..49 -> progressBar!!.setProgressDrawableTiled(resources.getDrawable(R.drawable.progress_bar_last_5_sec))
                    }
                    delay(100) // 0.1 second
                }
            }
        }
    }


}