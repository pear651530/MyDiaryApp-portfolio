package com.example.kimochinikki

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.yalantis.ucrop.UCrop
import java.io.File
import java.io.IOException


class SignActivity : AppCompatActivity() {
    private lateinit var select_img: ImageView
    private lateinit var select_btn: Button
    private lateinit var btn_sign: Button
    private  var storageRef= Firebase.storage
    private lateinit var uri: Uri
    private val PICK_IMAGE_REQUEST = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        supportActionBar?.setDisplayHomeAsUpEnabled(true) //返回鍵啟用

        select_img=findViewById(R.id.select_img)
        btn_sign=findViewById(R.id.btn_sign)
        select_btn=findViewById(R.id.select_btn)

        Glide.with(this)
            .load(R.drawable.head_preimg)
            .apply(RequestOptions.circleCropTransform()) //裁成圓形
            .into(select_img)

        storageRef= FirebaseStorage.getInstance()
        /*private val pickImage = 100
        var galleryImage=registerForActivityResult(
          ActivityResultContracts.GetContent(),
            ActivityResultCallback{
                select_img.setImageURI(it)
                Log.e("image ",it.toString())
               // uri=it
            })
*/
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
/*
       btn_sign.setOnClickListener{
            storageRef.getReference("images").child(System.currentTimeMillis().toString())
                .putFile(uri)
                .addOnSuccessListener{task->task.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener {
                    }
                }

        }*/
    }
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
            cropImage(selectedImageUri)
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
}