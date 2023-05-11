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
import android.graphics.Color
import android.net.wifi.hotspot2.pps.HomeSp
import androidx.core.content.ContextCompat
import android.view.MotionEvent
class MainActivity : AppCompatActivity() {

    lateinit var imageView: ImageView
    lateinit var LoginButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.book)
        Glide.with(this).load(R.drawable.book).into(imageView)

        LoginButton = findViewById<Button>(R.id.loginbtn)
        //LoginButton.setOnClickListener{gohome()}
        // 设置按钮的触摸监听器
        LoginButton.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 按下按钮时将背景色更改为深蓝色
                    LoginButton.setBackgroundResource(R.drawable.rounded_button_click)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // 松开按钮时将背景色恢复为半透明黑色
                    LoginButton.setBackgroundResource(R.drawable.rounded_button)
                    if (event.action == MotionEvent.ACTION_UP) {
                        // 执行按钮被点击时的操作
                        gohome()
                    }
                }
            }
            true
        }
    }

    private fun gohome(){
        val intent = Intent()
        intent.setClass(this@MainActivity, HomeActivity::class.java)
        startActivity(intent)
    }
}