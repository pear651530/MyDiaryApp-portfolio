package com.example.kimochinikki

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class SignActivity : AppCompatActivity() {
    private lateinit var select_img: ImageView
    private lateinit var select_btn: Button
    private lateinit var btn_sign: Button
    private  var storageRef= Firebase.storage
    private lateinit var uri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)
        select_img=findViewById(R.id.select_img)
        btn_sign=findViewById(R.id.btn_sign)
        select_btn=findViewById(R.id.select_btn)


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
            Log.e("PhotoPicker", "dsdssdfffsdfdsfd")
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

            //pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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
}