package com.daryush.thesis

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.binary.Base32
import dev.turingcomplete.kotlinonetimepassword.GoogleAuthenticator
import dev.turingcomplete.kotlinonetimepassword.HmacAlgorithm
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordConfig
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordGenerator
import java.util.concurrent.TimeUnit

class RecyclerAdapter(val items: List<ToTp>, val context: Context) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    // Inflate the layout and return a ViewHolder object
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.totpshow, parent, false)
        return ViewHolder(view)
    }

    // Return the total number of items in the list
    override fun getItemCount(): Int {
        return items.size
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val totp = items[position]
        holder.bind(totp)
    }

    // Inner ViewHolder class to hold item views
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val secretTextView: TextView = itemView.findViewById(R.id.secret)
        private val issuerTextView: TextView = itemView.findViewById(R.id.issuer)
        private val labelTextView: TextView = itemView.findViewById(R.id.label)
        private val rowLayout: ConstraintLayout = itemView.findViewById(R.id.row_layout)

        // Bind the data to the views
        fun bind(totp: ToTp) {
            secretTextView.text = totpGenerator(totp.secret)
            issuerTextView.text = totp.IssuerName
            labelTextView.text = totp.label

        }


        private fun totpGenerator(secret: String): String {
            //google authentication
//            val config = TimeBasedOneTimePasswordConfig(codeDigits = 6,
//                hmacAlgorithm = HmacAlgorithm.SHA1,
//                timeStep = 30,
//                timeStepUnit = TimeUnit.SECONDS)
//            val timeBasedOneTimePasswordGenerator = TimeBasedOneTimePasswordGenerator(secret.toByteArray(), config)
//            return timeBasedOneTimePasswordGenerator.generate()


            //////////////////////////////////////////////////////////////////////////////////////////////////////

            // Warning: the length of the plain text may be limited, see next chapter
            //val plainTextSecret = secret.toByteArray(Charsets.UTF_8)

            // This is the encoded one to use in most of the generators (Base32 is from the Apache commons codec library)
            //val base32EncodedSecret = Base32().encodeToString(plainTextSecret)
            val newSecret = secret.toByteArray()
            val googleAuthenticator = GoogleAuthenticator(newSecret)
            var code = googleAuthenticator.generate() // Will use System.currentTimeMillis()
            return code.toString()

        }
    }
}


