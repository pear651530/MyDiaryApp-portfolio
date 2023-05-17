package com.example.kimochinikki

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    val db = Firebase.firestore
    val TAG="wrong"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, LoadingActivity::class.java)
        startActivity(intent)

        val gif_book = findViewById<ImageView>(R.id.gif_book)
        Glide.with(this).load(R.drawable.book).into(gif_book)

        val btn_login = findViewById<Button>(R.id.btn_login)
        val btn_sign = findViewById<Button>(R.id.btn_sign)
        //LoginButton.setOnClickListener{gohome()}
        // 设置按钮的触摸监听器
        btn_login.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 按下按钮时将背景色更改为深蓝色
                    btn_login.setBackgroundResource(R.drawable.rounded_button_click)
                    test()
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

        btn_sign.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 按下按钮时将背景色更改为深蓝色
                    btn_sign.setBackgroundResource(R.drawable.rounded_button_click)
                    test()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // 松开按钮时将背景色恢复为半透明黑色
                    btn_sign.setBackgroundResource(R.drawable.rounded_button)
                    if (event.action == MotionEvent.ACTION_UP) {
                        // 执行按钮被点击时的操作
                        gosign()
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

    private fun gosign(){
        val intent = Intent()

        intent.setClass(this@MainActivity, SignActivity::class.java)

        startActivity(intent)
    }



    private fun test(){
     // Create a new user with a first and last name
val user = hashMapOf(
        "first" to "Ada",
        "last" to "Lovelace",
        "born" to 1815
)
        Log.e(TAG, "QQQQQ")
// Add a new document with a generated ID
db.collection("users")
    .add(user)
    .addOnSuccessListener { documentReference ->
        Log.e(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
    }
    .addOnFailureListener { e ->
        Log.e(TAG, "Error adding document", e)
    }
        Log.e(TAG, "eeeeeeeeeeeeeee")


    }
}