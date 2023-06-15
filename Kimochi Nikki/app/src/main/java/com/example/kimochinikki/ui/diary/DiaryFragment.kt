package com.example.kimochinikki.ui.diary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kimochinikki.databinding.FragmentDiaryBinding
import android.widget.ListView
import com.example.kimochinikki.adapter.DayAdapter
import com.example.kimochinikki.adapter.DiaryArrayAdapter
import com.example.kimochinikki.bean.DayBean
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.random.Random

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
    val uid = firebaseUser!!.uid

    //  var all_diary_array: Array<HashMap<String, String>> = arrayOf()
    val all_diary_array = ArrayList<HashMap<String, String>>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiaryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*懸浮的加號按鈕
            binding.fabAddnewdiary.setOnClickListener { view ->
            val intent = Intent()

            intent.setClass(requireContext(), AddNewDiaryActivity::class.java)

            startActivity(intent)
        }*/
//need recover
        //listView = binding.lvDiary

        //listView.adapter = DiaryArrayAdapter(requireContext(), diarylist)


        //get data

        /*  db.collection("users").document(uid)
              .collection("user_diary").get()//.await()
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
  */
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val documents = readFirebaseData()
                // 在這裡處理資料
                for (document in documents) {
                    val data = document.data
                    val date: String? = data?.get("date") as? String
                    val smile: String? = data?.get("smile") as? String
                    val sad: String? = data?.get("sad") as? String
                    val angry: String? = data?.get("angry") as? String
                    val heart: String? = data?.get("heart") as? String
                    val content: String? = data?.get("content") as? String

                    all_diary_array.add(
                        hashMapOf(
                            "date" to date.orEmpty(),
                            "smile" to smile.orEmpty(),
                            "sad" to sad.orEmpty(),
                            "angry" to angry.orEmpty(),
                            "heart" to heart.orEmpty(),
                            "content" to content.orEmpty()
                        )
                    )
                    Log.e("show data", data.toString())
                    Log.e("show list[0]", all_diary_array[0].toString())
                    Log.e("show list", all_diary_array.toString())
                    // 處理資料的邏輯
                }
                Log.e("ddoutside show list", all_diary_array.toString())
//芷柔
                listView = binding.lvDiary

                listView.adapter = DiaryArrayAdapter(requireContext(), all_diary_array)
                val diaryAdapter = listView.adapter as? DiaryArrayAdapter
                diaryAdapter?.setOnDiaryItemClickListener(object : DiaryArrayAdapter.OnDiaryItemClickListener {
                    override fun onDiaryItemClick(date_diary: HashMap<String, String>) {
                        binding.diaryTextViewSimletime.text = date_diary["smile"]
                        binding.diaryTextViewSadtime.text = date_diary["sad"]
                        binding.diaryTextViewAngrytime.text = date_diary["angry"]
                        binding.diaryTextViewHearttime.text = date_diary["heart"]
                        binding.diaryTextViewContext.text = date_diary["content"]
                        binding.jumpDiary.visibility = View.VISIBLE
                        binding.deleteBtn.setOnClickListener {
                            binding.jumpDiary.visibility = View.GONE
                        }
                    }
                })
            } catch (e: Exception) {
                Log.e("err", "eqwewr")

                // 處理例外狀況
            }
        }
        Log.e("outside show list", all_diary_array.toString())
        //
        return root
    }

    suspend fun readFirebaseData(): List<DocumentSnapshot> {
        val querySnapshot = db.collection("users").document(uid)
            .collection("user_diary").get().await()
        return querySnapshot.documents
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}