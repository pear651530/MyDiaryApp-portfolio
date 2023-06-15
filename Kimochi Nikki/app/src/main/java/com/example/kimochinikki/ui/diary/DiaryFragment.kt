package com.example.kimochinikki.ui.diary

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.ScrollView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.kimochinikki.adapter.DiaryArrayAdapter
import com.example.kimochinikki.databinding.FragmentDiaryBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream

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
                    val max_emo: String? = data?.get("max_emo") as? String

                    all_diary_array.add(
                        hashMapOf(
                            "date" to date.orEmpty(),
                            "smile" to smile.orEmpty(),
                            "sad" to sad.orEmpty(),
                            "angry" to angry.orEmpty(),
                            "heart" to heart.orEmpty(),
                            "content" to content.orEmpty(),
                            "max_emo" to max_emo.orEmpty(),
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
        //獲取當前Activity的根視圖
        var rootView = binding.jumpDiary
        binding.shareBtn.setOnClickListener {
            var bm: Bitmap =getScreenShot(rootView)//將當前畫面轉成bitmap型態
            do_share(bm)
        }
        return root
    }

    fun getScreenShot( screenView: ScrollView): Bitmap {
        //val screenView = view.rootView
        var height = 0
        // 正确获取 ScrollView 的高度
        for (i in 0 until screenView.childCount) {
            height += screenView.getChildAt(i).height
        }
        // 将屏幕截图保存为 Bitmap
        val bitmap = Bitmap.createBitmap(screenView.width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        screenView.draw(canvas)
        return bitmap
    }
    fun do_share(bm: Bitmap) {
        try {
            // 儲存圖片到暫存檔案中
            val cachePath = File(requireContext().cacheDir, "images")
            cachePath.mkdirs()
            val stream = FileOutputStream("$cachePath/image.png")
            //將 Bitmap 壓縮為 PNG 格式的圖片並寫入到 image.png 檔案中
            bm.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()
            // 取得暫存檔案的Uri
            val imagePath = File(requireContext().cacheDir, "images")
            val newFile = File(imagePath, "image.png")
            val contentUri = FileProvider.getUriForFile(requireContext(), "com.example.kimochinikki.fileprovider", newFile)
            // 使用分享Intent分享圖片
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/png"
            //使用 putExtra() 把要分享的資料加到Intent中
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
            //startActivity() 執行Intent，同時create 一個 chooser來顯示"選擇清單"讓使用者選擇要用什麼軟體
            startActivity(Intent.createChooser(shareIntent, "分享圖片"))
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("wawa", e.toString());
        }
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