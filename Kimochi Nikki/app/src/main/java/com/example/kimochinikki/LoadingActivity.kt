package com.example.kimochinikki

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlin.random.Random
import com.bumptech.glide.Glide
import android.view.View
import android.os.Handler
import android.util.Log

class LoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading)
        supportActionBar?.hide()

        val loading_gif = findViewById<ImageView>(R.id.loading_gif)
        Glide.with(this).load(R.drawable.load).into(loading_gif)

        Handler().postDelayed({
            finish()
        }, 5500)

    }
}