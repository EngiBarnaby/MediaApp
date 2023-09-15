package com.example.myyandexproject.ui.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myyandexproject.R

class TestActivity : AppCompatActivity() {

    private val imageUrl = "https://img.freepik.com/free-vector/open-blue-book-white_1308-69339.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val image = findViewById<ImageView>(R.id.image)
        Glide.with(applicationContext)
            .load(imageUrl)
            .into(image)
    }
}