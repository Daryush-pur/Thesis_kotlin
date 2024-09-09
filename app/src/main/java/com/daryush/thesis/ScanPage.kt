@file:Suppress("DEPRECATION")

package com.daryush.thesis

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScanPage : AppCompatActivity() {

    private var btnStart: Button? = null
    private var txtResult: TextView? = null
    private var newToTp: ToTpDataBase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_page)

        newToTp = ToTpDataBase.getAppDatabase(this)
        btnStart = findViewById(R.id.btn_Start)
        txtResult = findViewById(R.id.txt_Result)

        val requestCamera = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                startQRCodeScan()
            } else {
                Toast.makeText(applicationContext, "Permission not granted", Toast.LENGTH_SHORT).show()
            }
        }

        btnStart?.setOnClickListener {
            val hasPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
            if (hasPermission) {
                startQRCodeScan()
            } else {
                requestCamera.launch(android.Manifest.permission.CAMERA)
            }
        }
    }

    private fun startQRCodeScan() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan a QR Code")
        integrator.initiateScan()
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null && result.contents != null) {
            val resultString = result.contents
            val secret = resultString.substringAfter("secret=").substringBefore("&")
            val issuer = resultString.substringAfter("issuer=")
            val label = resultString.substringAfter("totp/").substringBefore("?secret")
            insertToTp(ToTp(secret = secret, IssuerName = issuer, label = label))
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun insertToTp(totp: ToTp) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                newToTp?.toTpDao()?.insert(totp)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ScanPage, "Insert successful", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ScanPage, "Insert failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
