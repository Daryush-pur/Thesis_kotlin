package com.daryush.thesis


import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class ScanPage : AppCompatActivity() {

    private var btn_Start:Button?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_scan_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val requestCamera = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                Toast.makeText(applicationContext, "permitione granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "permitione not granted", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        btn_Start = findViewById(R.id.btn_Start)
        btn_Start?.setOnClickListener(View.OnClickListener {
            requestCamera.launch(android.Manifest.permission.CAMERA )
        })
    }


}
