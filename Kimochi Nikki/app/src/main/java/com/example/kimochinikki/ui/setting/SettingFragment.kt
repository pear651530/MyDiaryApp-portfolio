package com.example.kimochinikki.ui.setting

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.text.method.NumberKeyListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kimochinikki.MainActivity
import com.example.kimochinikki.R
import com.example.kimochinikki.databinding.FragmentSettingBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.yalantis.ucrop.UCrop
import java.io.File
import java.io.IOException
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var now_img: ImageView
    private lateinit var btn_changeimg: Button
    private lateinit var btn_changepassword: Button
    private lateinit var btn_changeusername: Button
    private lateinit var btn_changeuserkey: Button
    private lateinit var wait_sign_btn: Uri
    private var choose_img = 0
   //////// 芷柔
    private lateinit  var user_password:String
    private lateinit  var user_img_url:String
    private lateinit  var user_name:String
    private lateinit  var user_email:String
    private lateinit  var user_key:String
    /////////////
    val db = Firebase.firestore
    val firebaseUser = Firebase.auth.currentUser
    val email=firebaseUser!!.email
    val uid=firebaseUser!!.uid
    val docRef = db.collection("users").document(uid.toString())
    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
///////download user data from database
        //read
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    user_password=document.getString("password")?:""
                    user_img_url= document.getString("img_url")?:""
                    user_name  = document.getString("name")?:""
                    user_email = document.getString("email")?:""
                    user_key = document.getString("key")?:""
                    binding.nowUserid.setText(email)
                    //binding.nowPassword.setText(user_password)
                    binding.nowUsername.setText(user_name)
                    //binding.nowUserkey.setText(user_key)
                    //Log.e("url",img_url.toString())
                    if(user_img_url=="")
                    {
                        Log.e("url","is ull")
                    }else
                    {
//get user image
                        Log.e("url",user_img_url)
                        val storage = Firebase.storage
                          val storageRef = storage.reference.child(user_img_url.toString())
                          storageRef.downloadUrl.addOnSuccessListener { uri ->
                              val imageURL = uri.toString()
                              // 在這裡使用 imageURL，例如顯示圖片或進行其他操作
                            Log.e("temp ", imageURL)
                            now_img= binding.nowImg
                              //用url顯示圖片
                              Glide.with(this)
                                  .load(imageURL)
                                  .apply(RequestOptions.circleCropTransform())
                                  .into(now_img)

                          }.addOnFailureListener { exception ->
                              // 發生錯誤時的處理
                          }
//////////////////////
                    }

                    Log.d("db message", "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d("db message", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("db message", "get failed with ", exception)
            }
        // 獲取數值

        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val btn_sign_out: Button = binding.btnSignOut
        btn_sign_out.setOnClickListener {
            signOut()
        }
        //binding.nowUsername.text = user_email
        now_img = binding.nowImg
        Glide.with(this)
            .load(R.drawable.head_preimg)
            .apply(RequestOptions.circleCropTransform())
            .into(now_img)

        btn_changeimg = binding.btnChangeimg
        btn_changeimg.setOnClickListener {

            GlobalScope.launch(Dispatchers.Main) {
                val openGalleryDeferred = async { openGallery() }
                openGalleryDeferred.await()

                var  getimgpath:String? = null
                Log.e("choose_img",choose_img.toString())

                if (choose_img==1) {
                    getimgpath = UploadImage(wait_sign_btn)
                    Log.e("getimgpath",wait_sign_btn.toString())
                }
                docRef
                    .update("img_url", getimgpath)
                    .addOnSuccessListener { Log.d("update key", "DocumentSnapshot successfully updated!") }
                    .addOnFailureListener { e -> Log.w("update key", "Error updating document", e) }
            }
        }

        btn_changepassword = binding.btnChangepassword
        btn_changepassword.setOnClickListener {
            changepassword()
        }

        btn_changeusername = binding.btnChangeusername
        btn_changeusername.setOnClickListener {
            changeusername()
        }

        btn_changeuserkey = binding.btnChangeuserkey
        btn_changeuserkey.setOnClickListener {
            changeuserkey()
        }

        return root
    }
    private fun changepassword() {
        val allowedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()-=_+[]{}|;':\",./<>?"
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("欲修改密碼")

        val layoutAlert = LinearLayout(requireContext())
        layoutAlert.orientation = LinearLayout.VERTICAL

        val textView1 = TextView(requireContext())
        textView1.text = "請輸入舊密碼"
        layoutAlert.addView(textView1)
        val input1 = EditText(requireContext())
        val keyListener = object : NumberKeyListener() {
            override fun getInputType(): Int {
                return InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }
            override fun getAcceptedChars(): CharArray {
                return allowedChars.toCharArray()
            }
        }
        input1.keyListener = keyListener
        //builder.setView(input1)
        layoutAlert.addView(input1)

        val textView2 = TextView(requireContext())
        textView2.text = "請輸入新密碼"
        layoutAlert.addView(textView2)
        val input2 = EditText(requireContext())
        input2.keyListener = keyListener
        //builder.setView(input2)
        layoutAlert.addView(input2)

        builder.setView(layoutAlert)

        // 添加确定按钮
        builder.setPositiveButton("确定") { dialog, which ->
            val userInput = input2.text.toString()
            if (input1.text.toString() == user_password) {
                user_password=userInput
    //更新密碼 芷柔
                docRef
                    .update("password", userInput)
                    .addOnSuccessListener { Log.d("update password", "DocumentSnapshot successfully updated!") }
                    .addOnFailureListener { e -> Log.w("update password", "Error updating document", e) } 
     //////////////////////////
                Toast.makeText(requireContext(), "修改成功!", Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(requireContext(), "舊密碼錯誤! 請重試一次", Toast.LENGTH_SHORT).show()
            }
        }

        // 添加取消按钮
        builder.setNegativeButton(
            "取消"
        ) { dialog, which -> dialog.cancel() }

        // 创建并显示 AlertDialog
        val dialog = builder.create()
        dialog.show()
    }

    private fun changeusername() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("欲修改的名稱")

        // 创建一个 EditText 作为输入字段
        val input = EditText(requireContext())
        builder.setView(input)

        // 添加确定按钮
        builder.setPositiveButton("确定") { dialog, which ->
            val userInput = input.text.toString()
            binding.nowUsername.text = userInput
            GlobalVariable.setName(userInput)
            docRef
                .update("name", userInput)
                .addOnSuccessListener { Log.d("update name", "DocumentSnapshot successfully updated!") }
                .addOnFailureListener { e -> Log.w("update name", "Error updating document", e) }
            Toast.makeText(requireContext(), "修改成功!", Toast.LENGTH_SHORT).show()
            //left bar 儣播改名字
            val intent = Intent("com.example.MY_CUSTOM_ACTION")
            intent.putExtra("message", userInput)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
// 或者使用 BroadcastManager.sendBroadcast(intent)

        }

        // 添加取消按钮
        builder.setNegativeButton(
            "取消"
        ) { dialog, which -> dialog.cancel() }

        // 创建并显示 AlertDialog
        val dialog = builder.create()
        dialog.show()
    }

    private fun changeuserkey() {
        val allowedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()-=_+[]{}|;':\",./<>?"
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("欲修改日記鎖")

        val layoutAlert = LinearLayout(requireContext())
        layoutAlert.orientation = LinearLayout.VERTICAL

        val textView1 = TextView(requireContext())
        textView1.text = "請輸入舊日記鎖"
        layoutAlert.addView(textView1)
        val input1 = EditText(requireContext())
        val keyListener = object : NumberKeyListener() {
            override fun getInputType(): Int {
                return InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }
            override fun getAcceptedChars(): CharArray {
                return allowedChars.toCharArray()
            }
        }
        input1.keyListener = keyListener
        //builder.setView(input1)
        layoutAlert.addView(input1)

        val textView2 = TextView(requireContext())
        textView2.text = "請輸入新日記鎖"
        layoutAlert.addView(textView2)
        val input2 = EditText(requireContext())
        input2.keyListener = keyListener
        //builder.setView(input2)
        layoutAlert.addView(input2)

        builder.setView(layoutAlert)

        // 添加确定按钮
        builder.setPositiveButton("确定") { dialog, which ->
            val userInput = input2.text.toString()
            if (input1.text.toString() == user_key) {
                user_key=userInput
                //binding.nowUserkey.text = userInput
                docRef
                    .update("key", userInput)
                    .addOnSuccessListener { Log.d("update key", "DocumentSnapshot successfully updated!") }
                    .addOnFailureListener { e -> Log.w("update key", "Error updating document", e) }
                Toast.makeText(requireContext(), "修改成功!", Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(requireContext(), "舊密碼錯誤! 請重試一次", Toast.LENGTH_SHORT).show()
            }
        }

        // 添加取消按钮
        builder.setNegativeButton(
            "取消"
        ) { dialog, which -> dialog.cancel() }

        // 创建并显示 AlertDialog
        val dialog = builder.create()
        dialog.show()
    }

    private fun signOut() {
        AlertDialog.Builder(requireContext())
            .setTitle("即將登出")
            .setMessage(" ")
            .setPositiveButton("確認") { _, _ ->
                Firebase.auth.signOut()
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish() // 如果需要结束当前Activity，请添加此行
            }
            .setNegativeButton("取消", null)
            .create()
            .show()
    }
    private fun changeimage(){
        var  getimgpath:String? = null
        if (choose_img==1)
            getimgpath=UploadImage(wait_sign_btn)
        docRef
            .update("img_url", getimgpath)
            .addOnSuccessListener { Log.d("update key", "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w("update key", "Error updating document", e) }


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
            val formatter= SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
            val now= Date()
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
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("runrun", "runrurnun")

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            selectedImageUri?.let { uri ->
                cropImage(uri)
                Log.e("pic", uri.toString())
                choose_img = 1
                wait_sign_btn = uri
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == AppCompatActivity.RESULT_OK) {
            val croppedImageUri = UCrop.getOutput(data!!)
            displayCroppedImage(croppedImageUri)
        }
    }

    private fun cropImage(sourceUri: Uri) {
        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, "cropped"))
        val options = UCrop.Options().apply {
            setCircleDimmedLayer(true)
        }

        UCrop.of(sourceUri, destinationUri)
            .withOptions(options)
            .start(requireContext(), this)
    }

    private fun displayCroppedImage(croppedImageUri: Uri?) {
        croppedImageUri?.let { uri ->
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                Glide.with(this)
                    .load(bitmap)
                    .apply(RequestOptions.circleCropTransform())
                    .into(now_img)

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}