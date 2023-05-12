package com.example.kimochinikki

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, LoadingActivity::class.java)
        startActivity(intent)

        val gif_book = findViewById<ImageView>(R.id.gif_book)
        Glide.with(this).load(R.drawable.book).into(gif_book)

        val btn_login = findViewById<Button>(R.id.btn_login)
        //LoginButton.setOnClickListener{gohome()}
        // 设置按钮的触摸监听器
        btn_login.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 按下按钮时将背景色更改为深蓝色
                    btn_login.setBackgroundResource(R.drawable.rounded_button_click)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // 松开按钮时将背景色恢复为半透明黑色
                    btn_login.setBackgroundResource(R.drawable.rounded_button)
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