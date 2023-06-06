package com.example.kimochinikki

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.MotionEvent
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.kimochinikki.databinding.ActivityMainBinding
import com.example.kimochinikki.ui.ProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.widget.Button
import com.google.firebase.auth.ktx.auth


class MainActivity : AppCompatActivity() {
    val TAG="wrong"
    private lateinit var auth: FirebaseAuth
    //for 後端
    val db = Firebase.firestore
    private lateinit var  binding : ActivityMainBinding
    private lateinit var  actionBar: ActionBar
    private lateinit var  firebaseAuth: FirebaseAuth


    private var email=""
    private var password=""
    private lateinit var edittext_userid: EditText
    private lateinit var edittextuser_password: EditText
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
// Initialize Firebase Auth
        auth = Firebase.auth

        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
//configure actionbar
         actionBar=supportActionBar!!
         actionBar.title="login"
//configure progress dialog
        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging In...")
        progressDialog.setCanceledOnTouchOutside(false)
//init firebaseAuth
        val gif_book = findViewById<ImageView>(R.id.gif_book)
        Glide.with(this).load(R.drawable.book).into(gif_book)

        val btn_login = findViewById<Button>(R.id.btn_login)
        val btn_sign = findViewById<Button>(R.id.btn_sign)
        //LoginButton.setOnClickListener{gohome()}
        // 设置按钮的触摸监听器
        firebaseAuth=FirebaseAuth.getInstance()
         checkUser()
         binding.noAccountTv.setOnClickListener{
            startActivity(Intent(this,SignActivity::class.java))
             finish()
         }


///check validate

//動畫要留!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 芷柔
   //    val intent = Intent(this, LoadingActivity::class.java)
        //startActivity(intent)
        btn_login.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 按下按钮时将背景色更改为深蓝色
                    btn_login.setBackgroundResource(R.drawable.rounded_button_click)
                    validateData()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // 松开按钮时将背景色恢复为半透明黑色
                    btn_login.setBackgroundResource(R.drawable.rounded_button)
                    if (event.action == MotionEvent.ACTION_UP) {
                        // 执行按钮被点击时的操作
                         //gohome()
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
                    startActivity(Intent(this,SignActivity::class.java))
                    //test()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // 松开按钮时将背景色恢复为半透明黑色
                    btn_sign.setBackgroundResource(R.drawable.rounded_button)
                    if (event.action == MotionEvent.ACTION_UP) {
                        // 执行按钮被点击时的操作
                        validateData()
                    }
                }
            }
            true
        }
    }



     private fun validateData(){
         edittext_userid = findViewById<EditText>(R.id.edittext_userid)
         edittextuser_password = findViewById<EditText>(R.id.edittextuser_password)
         email=edittext_userid.text.toString().trim()
         password=edittextuser_password.text.toString().trim()


         if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
         {
             //invaild email format
             edittext_userid.error="Invalid email format"
         }else if(TextUtils.isEmpty(password))
         {
             edittextuser_password.error="Please enter password"
         }else {
            firebase_login()
         }
     }
     private fun firebase_login(){
         progressDialog.show()
         firebaseAuth.signInWithEmailAndPassword(email,password)
             .addOnSuccessListener {
                 progressDialog.dismiss()
                 val firebaseUser=firebaseAuth.currentUser
                 val email=firebaseUser!!.email
                 Toast.makeText(this,"LoggedIn as $email",Toast.LENGTH_SHORT).show()
                 startActivity(Intent(this,HomeActivity::class.java))
                 finish()
             }.addOnFailureListener{e->
                 progressDialog.dismiss()
                 Toast.makeText(this,"logi failed due to ${e.message}",Toast.LENGTH_SHORT).show()
             }
     }

     private fun checkUser(){
       val firebaseUse=firebaseAuth.currentUser
         if(firebaseUse!=null)
         {
             startActivity(Intent(this, HomeActivity::class.java))
             finish()
         }
     }

     private fun gohome(){
         val intent = Intent()
         intent.setClass(this@MainActivity, HomeActivity::class.java)
         startActivity(intent)
     }

     private fun gosign(){
         val intent = Intent()
 //sigin!!!!!
        intent.setClass(this@MainActivity,SignActivity::class.java)

         startActivity(intent)
     }



    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            //already log in
            gohome()
        }
    }
}