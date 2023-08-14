package com.example.weatherapp.ui

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.content.FileProvider
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityShareBinding
import java.io.File

class ShareActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShareBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val screenshotPath = intent.getStringExtra("screenshotPath")
        if (!screenshotPath.isNullOrEmpty()) {
            val screenshotBitmap = BitmapFactory.decodeFile(screenshotPath)
            binding.screenshotImageView.setImageBitmap(screenshotBitmap)
        }

        binding.shareToSocialMediaButton.setOnClickListener {
            shareToSocialMedia(screenshotPath)
        }
    }

    private fun shareToSocialMedia(screenshotPath: String?) {
        val screenshotFile = screenshotPath?.let {
            FileProvider.getUriForFile(this, "com.example.weatherapp.fileprovider", File(it))
        }

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, screenshotFile)
        startActivity(Intent.createChooser(intent, "Share image via..."))
    }
}