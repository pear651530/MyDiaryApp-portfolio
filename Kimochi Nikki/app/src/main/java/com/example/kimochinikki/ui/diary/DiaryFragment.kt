package com.example.kimochinikki.ui.diary

import android.R
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kimochinikki.databinding.FragmentDiaryBinding
import android.widget.ListView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kimochinikki.SignActivity
import com.example.kimochinikki.bean.DiaryBean
import com.example.kimochinikki.adapter.DiaryArrayAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class DiaryFragment : Fragment() {
    private lateinit var listView: ListView
    private var _binding: FragmentDiaryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    //  private lateinit var firebaseAuth: FirebaseAuth
    val firebaseUser = Firebase.auth.currentUser
    // val email=firebaseUser!!.email
    val uid=firebaseUser!!.uid

  //  var all_diary_array: Array<HashMap<String, String>> = arrayOf()
    val all_diary_array = ArrayList<HashMap<String, String>>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiaryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val diarylist = ArrayList<DiaryBean>()
        diarylist.add(DiaryBean("5/15", "aaaa"))

        /*懸浮的加號按鈕
            binding.fabAddnewdiary.setOnClickListener { view ->
            val intent = Intent()

            intent.setClass(requireContext(), AddNewDiaryActivity::class.java)

            startActivity(intent)
        }*/

        listView = binding.lvDiary

        listView.adapter = DiaryArrayAdapter(requireContext(), diarylist)


        //get data

        db.collection("users").document(uid)
            .collection("user_diary").get()
            .addOnSuccessListener { querySnapshot ->
                Log.e("now data,",querySnapshot.toString())
                val documentList: List<DocumentSnapshot> = querySnapshot.documents

                for (documentSnapshot in documentList) {
                    val data: Map<String, Any>? = documentSnapshot.data

                    val date: String? = data?.get("date") as? String
                    val smile: String? = data?.get("smile") as? String
                    val sad: String? = data?.get("sad") as? String
                    val angry: String? = data?.get("angry") as? String
                    val heart: String? = data?.get("heart") as? String
                    val content: String? = data?.get("content") as? String

                  all_diary_array.add( hashMapOf(
                        "date" to date.orEmpty(),
                        "smile" to smile.orEmpty(),
                        "sad" to sad.orEmpty(),
                        "angry" to angry.orEmpty(),
                        "heart" to heart.orEmpty(),
                        "content" to content.orEmpty()
                      )
                    )
                    Log.e("show data",data.toString())
                    Log.e("show list[0]",all_diary_array[0].toString())
                    Log.e("show list",all_diary_array.toString())

                    // 可以在這裡處理文檔的數據
                    // 例如，使用 data[key] 獲取具體數據
                }

            }.addOnFailureListener { exception ->
                Log.e("now data fail,",exception.toString())

            }
            .addOnFailureListener { exception ->
                Log.d("db message", "get failed with ", exception)
            }

        //
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}