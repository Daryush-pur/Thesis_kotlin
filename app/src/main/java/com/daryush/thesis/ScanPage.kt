package com.daryush.thesis



import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.zxing.integration.android.IntentIntegrator

@Suppress("DEPRECATION")
class ScanPage : AppCompatActivity() {

    private var btn_Start:Button?= null
    private var txt_Result: TextView?= null



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
                startQRCodeScan() // Start scanning when permission is granted
                Toast.makeText(this," done",Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "Permission not granted", Toast.LENGTH_SHORT).show()
            }
        }
        btn_Start = findViewById(R.id.btn_Start)
        txt_Result = findViewById(R.id.txt_Result)

        btn_Start?.setOnClickListener {
            requestCamera.launch(android.Manifest.permission.CAMERA)
        }

    }

    private fun startQRCodeScan() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE) // Scan only QR codes
        integrator.setPrompt("Scan a QR Code") // Set a prompt message
        integrator.initiateScan() // Start the scan
    }

    // Handle the result of the scan
    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                txt_Result?.text = result.contents // Display the scanned QR code content
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}
