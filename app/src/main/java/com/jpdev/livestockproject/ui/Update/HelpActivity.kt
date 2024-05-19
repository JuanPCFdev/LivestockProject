package com.jpdev.livestockproject.ui.Update

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.databinding.ActivityHelpBinding
import com.jpdev.livestockproject.ui.Diagnostic.ConsultDiagnosticActivity
import com.jpdev.livestockproject.ui.Farm.deleteEdit.FarmEditDeleteActivity
import com.jpdev.livestockproject.ui.Home.HomePageActivity

class HelpActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHelpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val key = intent.extras?.getString("userKey")
        val farmKey = intent.extras?.getString("farmKey")
        initListeners(key, farmKey)
    }

    private fun initListeners(key:String?, farmKey: String?) {
        binding.viewToolBar.back.setOnClickListener {
            finish()
        }
        binding.btnCowVideo.setOnClickListener {
            val youtubeVideoCow = "https://www.youtube.com/watch?v=ZN1POCHP-rY&ab_channel=mr.J"
            openVideo(youtubeVideoCow)
        }
        binding.btnVaccineVideo.setOnClickListener {
            val youtubeVideoVaccine = "https://www.youtube.com/watch?v=1CUAuxWFus4&ab_channel=mr.J"
            openVideo(youtubeVideoVaccine)
        }
        binding.btnFinanceVideo.setOnClickListener {
            val youtubeVideoFinance = "https://www.youtube.com/watch?v=LkWkbKM2MYw&ab_channel=mr.J"
            openVideo(youtubeVideoFinance)
        }
        binding.btnOtherVideo.setOnClickListener {
            val youtubeVideoOther = "https://www.youtube.com/watch?v=1js-0gcnqQM&ab_channel=mr.J"
            openVideo(youtubeVideoOther)
        }
        binding.btnDiagnostic.setOnClickListener {
            val intent = Intent(this, ConsultDiagnosticActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
        }
    }
    private fun openVideo(url: String) {
        try {
            val videoUri = url
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUri))
            if (videoUri!= null) {
                startActivity(intent)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error, asegurese de tener youtube", Toast.LENGTH_SHORT).show()
        }
    }

}