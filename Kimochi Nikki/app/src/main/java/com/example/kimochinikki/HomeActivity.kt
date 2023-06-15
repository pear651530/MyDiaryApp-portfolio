package com.example.kimochinikki

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kimochinikki.databinding.ActivityMain2Binding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.kimochinikki.ui.setting.SettingFragment
import com.yalantis.ucrop.UCrop


class HomeActivity : AppCompatActivity() {


    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMain2Binding
    private lateinit var rongtry: TextView
    //////// pear try
    private lateinit var now_img: ImageView
    private lateinit var TextView_name: TextView
    private lateinit var TextView_email: TextView

    private lateinit  var user_password:String
    private lateinit  var user_img_url:String
    private lateinit  var user_name:String
    private lateinit  var user_email:String
    private lateinit  var user_key:String

    val db = Firebase.firestore
    val firebaseUser = Firebase.auth.currentUser
    val email=firebaseUser!!.email
    val uid=firebaseUser!!.uid
    val docRef = db.collection("users").document(uid.toString())
    /////////////
    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.example.MY_CUSTOM_ACTION") {
                val message = intent.getStringExtra("message")
                // 在這裡處理接收到的訊息
                Log.d("ParentActivity", "Received message: $message")
                //val tt=
                TextView_name.setText(message)

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_diary, R.id.nav_inputemo, R.id.nav_touch, R.id.nav_setting
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
 ///// rong try

        val user = Firebase.auth.currentUser
        Log.e("now err",user.toString())


 /////////////////////

        ///// pear try
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    user_password=document.getString("password")?:""
                    user_img_url= document.getString("img_url")?:""
                    user_name  = document.getString("name")?:""
                    user_email = document.getString("email")?:""
                    user_key = document.getString("key")?:""
                    //Log.e("url",user_name)
                    val navHeaderView = navView.getHeaderView(0)
                    TextView_name = navHeaderView.findViewById(R.id.TextView_name)
                    TextView_email = navHeaderView.findViewById(R.id.TextView_email)
                    TextView_name.setText(user_name)

                    //var try_name = GlobalVariable.getName()
                    //TextView_name.setText(try_name)

                    TextView_email.setText(user_email)


                    if(user_img_url!="")
                    {
//get user image
                       Log.e("!!url",user_img_url)
                        val storage = Firebase.storage
                        val storageRef = storage.reference.child(user_img_url.toString())
                        storageRef.downloadUrl.addOnSuccessListener { uri ->
                            val imageURL = uri.toString()
                            // 在這裡使用 imageURL，例如顯示圖片或進行其他操作
                            Log.e("temp ", imageURL)
                            now_img= findViewById(R.id.imageView)
                            //用url顯示圖片
                            Glide.with(this)
                                .load(imageURL)
                                .apply(RequestOptions.circleCropTransform())
                                .into(now_img)

                        }.addOnFailureListener { exception ->
                            // 發生錯誤時的處理
                        }
//////////////////////
                    }else{
                        now_img= findViewById(R.id.imageView)
                        Glide.with(this)
                            .load(R.drawable.head_preimg)
                            .apply(RequestOptions.circleCropTransform())
                            .into(now_img)
                    }

                    Log.d("db message", "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d("db message", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("db message", "get failed with ", exception)
            }
        val filter = IntentFilter("com.example.MY_CUSTOM_ACTION")
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter)
        // 或者使用 BroadcastManager.registerReceiver(broadcastReceiver, filter)

        /////////////////////
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        // 或者使用 BroadcastManager.unregisterReceiver(broadcastReceiver)
    }


}