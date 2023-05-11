package com.example.kimochinikki

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import android.view.View


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, LoadingActivity::class.java)
        startActivity(intent)

        val book_gif = findViewById<ImageView>(R.id.book_gif)
        Glide.with(this).load(R.drawable.book).into(book_gif)

        val login_btn = findViewById<Button>(R.id.login_btn)
        //LoginButton.setOnClickListener{gohome()}
        // 设置按钮的触摸监听器
        login_btn.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 按下按钮时将背景色更改为深蓝色
                    login_btn.setBackgroundResource(R.drawable.rounded_button_click)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // 松开按钮时将背景色恢复为半透明黑色
                    login_btn.setBackgroundResource(R.drawable.rounded_button)
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