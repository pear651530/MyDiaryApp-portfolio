package com.example.kimochinikki

import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.ActionBar
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kimochinikki.databinding.ActivitySignBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.yalantis.ucrop.UCrop
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class SignActivity : AppCompatActivity() {
    val TAG="wrong"

    private lateinit var select_img: ImageView
    private lateinit var select_btn: Button
    private lateinit var btn_sign: Button
    private  var storageRef= Firebase.storage
    private lateinit var uri: Uri
    private lateinit var wait_sign_btn: Uri
    private val PICK_IMAGE_REQUEST = 1
    
   // private lateinit var binding: ActivitySignBinding
   //
   // private lateinit var  actionBar: ActionBar
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth
    private var email=""
    private var password=""
    private lateinit var sign_userid: EditText
    private lateinit var sign_password: EditText
    private lateinit var edittext_username: EditText

    ///firebase
    val db = Firebase.firestore
    var choose_img=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     //   binding=ActivitySignBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_sign)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //返回鍵啟用

        select_img=findViewById(R.id.select_img)
        btn_sign=findViewById(R.id.btn_sign)
        select_btn=findViewById(R.id.select_btn)

       // actionBar=supportActionBar!!D
     //   actionBar.title="Sign Up"
        //enable actionbar ,enable back button
    //    actionBar.setDisplayHomeAsUpEnabled(true)
      //  actionBar.setDisplayShowHomeEnabled(true)

        //configure progress dialog
        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Create account In...")
        progressDialog.setCanceledOnTouchOutside(false)
        //init firebase auth
        firebaseAuth=FirebaseAuth.getInstance()

        Glide.with(this)
            .load(R.drawable.head_preimg)
            .apply(RequestOptions.circleCropTransform()) //裁成圓形
            .into(select_img)

        storageRef= FirebaseStorage.getInstance()


        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

        select_btn.setOnClickListener{
            openGallery()
        }

       // select_btn.setOnClickListener{galleryImage.launch("image/*")}

       btn_sign.setOnClickListener{
           /* storageRef.getReference("images").child(System.currentTimeMillis().toString())
                .putFile(uri)
                .addOnSuccessListener{task->task.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener {
                    }
                }*/
           validdateData()

        }
    }
    private fun validdateData(){
        //get data
    //Log.e("      ewwew","eqwe      ")
        sign_userid = findViewById<EditText>(R.id.sign_userid)
        sign_password = findViewById<EditText>(R.id.sign_password)
        email=sign_userid.text.toString().trim()
        password=sign_password.text.toString().trim()
        Log.e("1111111 ",email)
        Log.e("32   ",password)
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            sign_userid.error="Invaild email format"
        }else if(TextUtils.isEmpty(password)){
            sign_password.error="Please enter password"
        }else if(password.length<6)
        {// too short
            sign_password.error="Password must at least 6 chracters long"
        }else{
            firebaseSignUp()
        }
    }
    private fun firebaseSignUp(){
        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener{
                progressDialog.dismiss()
                val firebaseUser=firebaseAuth.currentUser
                val email=firebaseUser!!.email

                edittext_username = findViewById<EditText>(R.id.edittext_username)
                val username=edittext_username.text.toString().trim()
//upload img
                var  getimgpath:String? = null
                if (choose_img==1)
                    getimgpath=UploadImage(wait_sign_btn)
                // Create a new user with a first and last name
                val user = hashMapOf(
                    "email" to email.toString(),
                    "name" to username,
                    "password" to password,
                    "img_url" to getimgpath,

                )

// Add a new document with a generated ID
                db.collection("users")
                    .add(user)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }


                Toast.makeText(this,"Account create with $email",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,HomeActivity::class.java))
            }
            .addOnFailureListener {e->
                progressDialog.dismiss()
                Toast.makeText(this,"ssign failed due to ${e.message}",Toast.LENGTH_SHORT).show()
            }

    }
    /*override fun onSuportNavigateUp():Boolean{
        onBackPressed()//go back to previous activity
        return super.onSupportNavigateUp()
    }*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    // 打开手机媒体库
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // 处理选择的图片
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val selectedImageUri: Uri = data.data ?: return
          //  Log.e("pic",uri.toString())
            cropImage(selectedImageUri)
            Log.e("pic",selectedImageUri.toString())
            if(selectedImageUri!=null) {
                choose_img=1
                wait_sign_btn=selectedImageUri
            }

        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            val croppedImageUri = UCrop.getOutput(data!!)
            displayCroppedImage(croppedImageUri)
        }
    }

    // 裁剪图像
    private fun cropImage(sourceUri: Uri) {
        val destinationUri = Uri.fromFile(File(cacheDir, "cropped"))
        val options = UCrop.Options().apply {
            setCircleDimmedLayer(true)
        }

        UCrop.of(sourceUri, destinationUri)
            .withOptions(options)
            .start(this)
    }

    // 显示裁剪后的图像
    private fun displayCroppedImage(croppedImageUri: Uri?) {
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, croppedImageUri)
            Glide.with(this)
                .load(bitmap)
                .apply(RequestOptions.circleCropTransform())
                .into(select_img)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    fun UploadImage(uri: Uri?):String {
        var path="failed"
        if(uri!=null) {
            //var pd= ProgressDialog(this)
           // pd.setTitle("uploadimg")
          //  pd.show()
            var succ=0
            val storageRef = FirebaseStorage.getInstance().reference
           // Log.d("URI", uri.toString())
            val formatter=SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
            val now=Date()
            val file_name=formatter.format(now)
            path="images/$file_name"
            val task = storageRef.child("images/$file_name").putFile(uri)
            task.addOnSuccessListener {

                Log.d("UploadImage", "Task Is Successful")
            }.addOnFailureListener {
                Log.d("UploadImageFail", "Image Upload Failed ${it.printStackTrace()}")
            }
            /////////////////
////////////////////
        }
        return path
    }
}