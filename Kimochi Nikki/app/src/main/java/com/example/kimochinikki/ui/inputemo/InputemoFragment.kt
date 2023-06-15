package com.example.kimochinikki.ui.inputemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kimochinikki.databinding.FragmentInputemoBinding
import com.example.kimochinikki.R
import com.example.kimochinikki.SignActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Integer.max

class InputemoFragment : Fragment() {

    private var _binding: FragmentInputemoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val db = Firebase.firestore
  //  private lateinit var firebaseAuth: FirebaseAuth
    val firebaseUser = Firebase.auth.currentUser
   // val email=firebaseUser!!.email
    val uid=firebaseUser!!.uid

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {




        _binding = FragmentInputemoBinding.inflate(inflater, container, false)
        val root: View = binding.root


        init()
        val imageButton_Simle = binding.ImageButtonSimle
        imageButton_Simle.setOnClickListener { addtime("Simle") }
        val imageButton_Sad = binding.ImageButtonSad
        imageButton_Sad.setOnClickListener { addtime("Sad") }
        val imageButton_Angry = binding.ImageButtonAngry
        imageButton_Angry.setOnClickListener { addtime("Angry") }
        val imageButton_Heart = binding.ImageButtonHeart
        imageButton_Heart.setOnClickListener { addtime("Heart") }

        binding.btnAdddiary.setOnClickListener{
            add_new_diary()
        }


        culavg()
        return root
    }
    private fun add_new_diary(){
        val datePicker: DatePicker = binding.datepickerDiary
        val year = datePicker.year
        val month = datePicker.month
        val day = datePicker.dayOfMonth
        val dateString = "$year-${month + 1}-$day" // 月份需要加1，因為月份從0開始

        var num_simle = binding.TextViewSimletime.text.toString()

        var num_sadtime =binding.TextViewSadtime.text.toString()

        var num_angry = binding.TextViewAngrytime.text.toString()

        var num_heart =binding.TextViewHearttime.text.toString()
        var content=binding.edittextDiary.text.toString()

        Log.e("why","can????")

        //check weather it is existed
        val docRef = db.collection("users").document(uid)
         .collection("user_diary").document(dateString)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                   // Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    var dataBase_simle=Integer.parseInt(document.getString("smile"))?:0
                    var dataBase_sadtime=Integer.parseInt(document.getString("sad"))?:0
                    var dataBase_angry=Integer.parseInt(document.getString("angry"))?:0
                    var dataBase_heart=Integer.parseInt(document.getString("heart"))?:0
                    var dataBase_content=document.getString("content")?:""

                    dataBase_simle+=Integer.parseInt(num_simle)
                    dataBase_sadtime+=Integer.parseInt(num_sadtime)
                    dataBase_angry+=Integer.parseInt(num_angry)
                    dataBase_heart+=Integer.parseInt(num_heart)
                    dataBase_content=dataBase_content+"\n"+content
                    val now_diary = hashMapOf(
                        "date" to dateString,
                        "smile" to dataBase_simle.toString() ,
                        "sad" to dataBase_sadtime.toString(),
                        "angry" to dataBase_angry.toString(),
                        "heart" to dataBase_heart.toString(),
                        "content" to dataBase_content,
                    )

                    db.collection("users").document(uid.toString()).collection("user_diary")
                        .document(dateString)
                        .set(now_diary)
                        .addOnSuccessListener { Log.e("add succe", "DocumentSnapshot successfully written!") }
                        .addOnFailureListener { e -> Log.e("add fail", "Error writing document", e) }
                    Toast.makeText(requireContext(),"更新日記成功", Toast.LENGTH_SHORT).show()


                } else {
                    Log.e("why","adadsdsadf")

                    val now_diary = hashMapOf(
                        "date" to dateString,
                        "smile" to num_simle ,
                        "sad" to num_sadtime,
                        "angry" to num_angry,
                        "heart" to num_heart,
                        "content" to content,
                    )
Log.e("why",now_diary.toString())
                    db.collection("users").document(uid.toString()).collection("user_diary")
                        .document(dateString)
                        .set(now_diary)
                        .addOnSuccessListener { Log.e("add succe", "DocumentSnapshot successfully written!") }
                        .addOnFailureListener { e -> Log.e("add fail", "Error writing document", e) }
                    Toast.makeText(requireContext(),"新增日記成功", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.d("total failed", "get failed with ", exception)
            }



//芷柔


    }

    fun init() {
        binding.TextViewSimletime.text = "0"
        binding.TextViewSadtime.text = "0"
        binding.TextViewAngrytime.text = "0"
        binding.TextViewHearttime.text = "0"
    }

    fun addtime(whice: String) {
        if (whice == "Simle") {
            var num = Integer.parseInt(binding.TextViewSimletime.text.toString())
            num++
            binding.TextViewSimletime.text = num.toString()
        } else if (whice == "Sad") {
            var num = Integer.parseInt(binding.TextViewSadtime.text.toString())
            num++
            binding.TextViewSadtime.text = num.toString()
        } else if (whice == "Angry") {
            var num = Integer.parseInt(binding.TextViewAngrytime.text.toString())
            num++
            binding.TextViewAngrytime.text = num.toString()
        } else {
            var num = Integer.parseInt(binding.TextViewHearttime.text.toString())
            num++
            binding.TextViewHearttime.text = num.toString()
        }
    }

    public fun culavg() {
        max(
            max(
                Integer.parseInt(binding.TextViewSimletime.text.toString()),
                Integer.parseInt(binding.TextViewSadtime.text.toString())
            ),
            max(
                Integer.parseInt(binding.TextViewAngrytime.text.toString()),
                Integer.parseInt(binding.TextViewHearttime.text.toString())
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}