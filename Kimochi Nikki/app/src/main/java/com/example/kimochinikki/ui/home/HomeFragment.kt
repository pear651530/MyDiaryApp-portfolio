package com.example.kimochinikki.ui.home

import android.R
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kimochinikki.databinding.FragmentHomeBinding
import com.example.kimochinikki.adapter.DayAdapter;
import com.example.kimochinikki.bean.DayBean
import android.widget.GridView
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import kotlin.math.log
import kotlin.random.Random
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var tvCurrentDate: TextView
    private lateinit var tvPreMonth: TextView
    private lateinit var tvNextMonth: TextView
    private lateinit var gv: GridView
    private lateinit var rong_try: TextView
    val temp_all_diary_array = ArrayList<HashMap<String, String>>()

    val emo_resourceMap = hashMapOf(
        "smiling" to com.example.kimochinikki.R.drawable.smiling,
        "angry" to com.example.kimochinikki.R.drawable.angry,
        "sad" to com.example.kimochinikki.R.drawable.sad,
        "heart" to com.example.kimochinikki.R.drawable.heart,
        "confusion" to com.example.kimochinikki.R.drawable.confusion,
    )

    private lateinit var year: String
    private lateinit var month: String

    private val db = Firebase.firestore
    val firebaseUser = Firebase.auth.currentUser
    val uid=firebaseUser!!.uid

    val QuotationsSimleId = intArrayOf(
        com.example.kimochinikki.R.string.quotations_simle1,
        com.example.kimochinikki.R.string.quotations_simle2,
        com.example.kimochinikki.R.string.quotations_simle3,
        com.example.kimochinikki.R.string.quotations_simle4,
        com.example.kimochinikki.R.string.quotations_simle5,
        com.example.kimochinikki.R.string.quotations_simle6,
        com.example.kimochinikki.R.string.quotations_simle7,
        com.example.kimochinikki.R.string.quotations_simle8,
        com.example.kimochinikki.R.string.quotations_simle9,
        com.example.kimochinikki.R.string.quotations_simle10
    )

    val QuotationsSadId = intArrayOf(
        com.example.kimochinikki.R.string.quotations_sad1,
        com.example.kimochinikki.R.string.quotations_sad2,
        com.example.kimochinikki.R.string.quotations_sad3,
        com.example.kimochinikki.R.string.quotations_sad4,
        com.example.kimochinikki.R.string.quotations_sad5,
        com.example.kimochinikki.R.string.quotations_sad6,
        com.example.kimochinikki.R.string.quotations_sad7,
        com.example.kimochinikki.R.string.quotations_sad8,
        com.example.kimochinikki.R.string.quotations_sad9,
        com.example.kimochinikki.R.string.quotations_sad10
    )

    val QuotationsAngryId = intArrayOf(
        com.example.kimochinikki.R.string.quotations_angry1,
        com.example.kimochinikki.R.string.quotations_angry2,
        com.example.kimochinikki.R.string.quotations_angry3,
        com.example.kimochinikki.R.string.quotations_angry4,
        com.example.kimochinikki.R.string.quotations_angry5,
        com.example.kimochinikki.R.string.quotations_angry6,
        com.example.kimochinikki.R.string.quotations_angry7,
        com.example.kimochinikki.R.string.quotations_angry8,
        com.example.kimochinikki.R.string.quotations_angry9,
        com.example.kimochinikki.R.string.quotations_angry10
    )

    val QuotationsHeartId = intArrayOf(
        com.example.kimochinikki.R.string.quotations_heart1,
        com.example.kimochinikki.R.string.quotations_heart2,
        com.example.kimochinikki.R.string.quotations_heart3,
        com.example.kimochinikki.R.string.quotations_heart4,
        com.example.kimochinikki.R.string.quotations_heart5,
        com.example.kimochinikki.R.string.quotations_heart6,
        com.example.kimochinikki.R.string.quotations_heart7,
        com.example.kimochinikki.R.string.quotations_heart8,
        com.example.kimochinikki.R.string.quotations_heart9,
        com.example.kimochinikki.R.string.quotations_heart10
    )

    val SuggestionsSimleId = intArrayOf(
        com.example.kimochinikki.R.string.suggestions_simle1,
        com.example.kimochinikki.R.string.suggestions_simle2,
        com.example.kimochinikki.R.string.suggestions_simle3,
        com.example.kimochinikki.R.string.suggestions_simle4,
        com.example.kimochinikki.R.string.suggestions_simle5,
        com.example.kimochinikki.R.string.suggestions_simle6,
        com.example.kimochinikki.R.string.suggestions_simle7,
        com.example.kimochinikki.R.string.suggestions_simle8,
        com.example.kimochinikki.R.string.suggestions_simle9,
        com.example.kimochinikki.R.string.suggestions_simle10
    )

    val SuggestionsSadId = intArrayOf(
        com.example.kimochinikki.R.string.suggestions_sad1,
        com.example.kimochinikki.R.string.suggestions_sad2,
        com.example.kimochinikki.R.string.suggestions_sad3,
        com.example.kimochinikki.R.string.suggestions_sad4,
        com.example.kimochinikki.R.string.suggestions_sad5,
        com.example.kimochinikki.R.string.suggestions_sad6,
        com.example.kimochinikki.R.string.suggestions_sad7,
        com.example.kimochinikki.R.string.suggestions_sad8,
        com.example.kimochinikki.R.string.suggestions_sad9,
        com.example.kimochinikki.R.string.suggestions_sad10
    )

    val SuggestionsAngryId = intArrayOf(
        com.example.kimochinikki.R.string.suggestions_angry1,
        com.example.kimochinikki.R.string.suggestions_angry2,
        com.example.kimochinikki.R.string.suggestions_angry3,
        com.example.kimochinikki.R.string.suggestions_angry4,
        com.example.kimochinikki.R.string.suggestions_angry5,
        com.example.kimochinikki.R.string.suggestions_angry6,
        com.example.kimochinikki.R.string.suggestions_angry7,
        com.example.kimochinikki.R.string.suggestions_angry8,
        com.example.kimochinikki.R.string.suggestions_angry9,
        com.example.kimochinikki.R.string.suggestions_angry10
    )

    val SuggestionsHeartId = intArrayOf(
        com.example.kimochinikki.R.string.suggestions_heart1,
        com.example.kimochinikki.R.string.suggestions_heart2,
        com.example.kimochinikki.R.string.suggestions_heart3,
        com.example.kimochinikki.R.string.suggestions_heart4,
        com.example.kimochinikki.R.string.suggestions_heart5,
        com.example.kimochinikki.R.string.suggestions_heart6,
        com.example.kimochinikki.R.string.suggestions_heart7,
        com.example.kimochinikki.R.string.suggestions_heart8,
        com.example.kimochinikki.R.string.suggestions_heart9,
        com.example.kimochinikki.R.string.suggestions_heart10
    )

    val SuggestionsConfuseId = intArrayOf(
        com.example.kimochinikki.R.string.suggestions_confuse1,
        com.example.kimochinikki.R.string.suggestions_confuse2,
        com.example.kimochinikki.R.string.suggestions_confuse3,
        com.example.kimochinikki.R.string.suggestions_confuse4,
        com.example.kimochinikki.R.string.suggestions_confuse5,
        com.example.kimochinikki.R.string.suggestions_confuse6,
        com.example.kimochinikki.R.string.suggestions_confuse7,
        com.example.kimochinikki.R.string.suggestions_confuse8,
        com.example.kimochinikki.R.string.suggestions_confuse9,
        com.example.kimochinikki.R.string.suggestions_confuse10
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initView()
        /*val calendarView: CalendarView = binding.calendarView

        val calendar = Calendar.getInstance()
        val minDate = calendar.timeInMillis // 設置最小日期為當前日期
        calendar.add(Calendar.MONTH, 6) // 將日期增加 6 個月
        val maxDate = calendar.timeInMillis // 設置最大日期為當前日期加 6 個月

        calendarView.minDate = minDate
        calendarView.maxDate = maxDate

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // 獲取選中日期對應的格子位置
            val position = getPosition(year, month, dayOfMonth)

            // 獲取當前日期的格子 View
            val grid = (view as ViewGroup).getChildAt(0) as GridView
            val cellView = grid.getChildAt(position) as RelativeLayout

            // 添加圖示到格子中
            val imageView = ImageView(requireContext())
            imageView.setImageDrawable(context?.let { ContextCompat.getDrawable(it, R.drawable.ic_menu_home) })

            val layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
            cellView.addView(imageView, layoutParams)
        }*/

        //val textView = findViewById<TextView>(R.id.rong_try)
        ///R.id.imageView.setImageResource(R.drawable.image);

        //binding.rongTry.setText()

        return root
    }

    private fun initView() {
        Glide.with(this).load(com.example.kimochinikki.R.drawable.load_cln).into(binding.jumpLoadGif)
        tvCurrentDate = binding.tvCurrentDate
        tvPreMonth = binding.tvPreMonth
        tvNextMonth = binding.tvNextMonth
        gv = binding.gv
        initAdapter()
    }
    private fun initAdapter() {
        val dataList = ArrayList<DayBean>()
        val all_emo_array = ArrayList<HashMap<String, String>>()
        all_emo_array.add(hashMapOf(
            "ini" to "???",))
        //rong
        val adapter = DayAdapter(dataList,all_emo_array, requireContext())
        adapter.setOnDateItemClickListener(object : DayAdapter.OnDateItemClickListener { //改文字!!!
            override fun onDateItemClick(date: DayBean, item: HashMap<String, String>) {
                Log.e("item", item.toString())
                binding.textviewPushDate.text=date.year.toString()+"-"+date.month.toString()+"-"+date.day.toString()
                if(item["content"]=="nothing"){
                    binding.ImageViewMax.setImageResource(0)
                    binding.textviewQuotations.text=""
                    binding.textviewSuggestions.text=""
                }
                else{
                    var randomValue = Random.nextInt(10)
                    val max_emo=item?.get("max_emo")
                    emo_resourceMap[max_emo]?.let { resourceId ->
                        binding.ImageViewMax.setImageResource(resourceId)
                    }

                    if(max_emo== "confusion") {
                        //<a href="https://www.flaticon.com/free-icons/emojis" title="emojis icons">Emojis icons created by zafdesign - Flaticon</a>
                        binding.textviewQuotations.text="沒有特別的心情趨勢歐~"
                        binding.textviewSuggestions.text=getString(SuggestionsConfuseId[randomValue])
                    }
                    else if(max_emo== "smiling") {
                        binding.textviewQuotations.text=getString(QuotationsSimleId[randomValue])
                        randomValue = Random.nextInt(10)
                        binding.textviewSuggestions.text=getString(SuggestionsSimleId[randomValue])
                    }
                    else if(max_emo== "angry") {
                        binding.textviewQuotations.text=getString(QuotationsAngryId[randomValue])
                        randomValue = Random.nextInt(10)
                        binding.textviewSuggestions.text=getString(SuggestionsAngryId[randomValue])
                    }
                    else if(max_emo== "sad") {
                        binding.textviewQuotations.text=getString(QuotationsSadId[randomValue])
                        randomValue = Random.nextInt(10)
                        binding.textviewSuggestions.text=getString(SuggestionsSadId[randomValue])
                    }
                    else if(max_emo== "heart") {
                        binding.textviewQuotations.text=getString(QuotationsHeartId[randomValue])
                        randomValue = Random.nextInt(10)
                        binding.textviewSuggestions.text=getString(SuggestionsHeartId[randomValue])
                    }
                }
            }
        })
        gv.adapter = adapter
        val calendar = Calendar.getInstance()
        setCurrentData(calendar)
      //  Log.e("initAdapter QQQQQQ",calendar.toString())
        updateAdapter(calendar, dataList,all_emo_array, adapter)
        tvPreMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, -1)
            updateAdapter(calendar, dataList,all_emo_array, adapter)
          //  GetDataFromFirebse()
        }
        tvNextMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, 1)
            updateAdapter(calendar, dataList,all_emo_array, adapter)
        }
    }

    private fun updateAdapter(calendar: Calendar, dataList: ArrayList<DayBean>, all_emo_array :ArrayList<HashMap<String, String>>,adapter: DayAdapter) {
        dataList.clear()
        all_emo_array.clear()
        setCurrentData(calendar)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val weekIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1
        calendar.add(Calendar.MONTH, -1)
        val preMonthDays = getMonth(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR))
        for (i in 0 until weekIndex) {
            val bean = DayBean()
            bean.year = calendar.get(Calendar.YEAR)
            bean.month = calendar.get(Calendar.MONTH) + 1
            bean.day = preMonthDays - weekIndex + i + 1
            bean.currentDay = false
            bean.currentMonth = false
            dataList.add(bean)
        }
        calendar.add(Calendar.MONTH, 1)
        val currentDays = getMonth(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR))
        val rong_use_year= (calendar.get(Calendar.YEAR))
        val rong_use_month= (calendar.get(Calendar.MONTH) + 1)
        Log.e("QQQQQQ",currentDays.toString())
        Log.e("QQQQQQ year", (calendar.get(Calendar.YEAR)).toString())
        //rong get list from firebase
////////////////// rong
        //val all_emo_array = ArrayList<HashMap<String, String>>()

        GlobalScope.launch(Dispatchers.Main) {
            try {
              //  val documentNames = listOf("document1", "document2", "document3")
                binding.jumpLoad.visibility = View.VISIBLE
                binding.jumpLoad.setOnClickListener{}// 点击事件被拦截，不执行任何操作

                val prefix = String.format("%04d-%02d-", rong_use_year, rong_use_month)
                var indx=0
                // val prefix = "abc" // 欲查詢的字串前綴
                val collectionRef = FirebaseFirestore.getInstance().collection("users").
                document(uid).collection("user_diary")

                val querySnapshot = collectionRef
                    .whereGreaterThanOrEqualTo("date", prefix)
                    .whereLessThan("date", prefix + "\uf8ff").get().await()
                val documentList: List<DocumentSnapshot> = querySnapshot.documents

                for (documentSnapshot in documentList) {
                    val data: Map<String, Any>? = documentSnapshot.data

                    val max_emo: String? = data?.get("max_emo") as? String
                    val date: String? = data?.get("date") as? String

                    val parts = date?.split("-") // 使用 "-" 進行切割
                    val day = parts?.getOrNull(2)
                    Log.e("want ay",day.toString())

                    val now_emo = hashMapOf(
                        "content" to "have",
                        "max_emo" to max_emo.orEmpty(),
                    )
                    val num_day=day!!.toInt()-1
                    while(indx<num_day) {
                        all_emo_array.add(hashMapOf("content" to "nothing"))
                        indx+=1
                    }
                    all_emo_array.add(now_emo)
                    indx+=1

                }
                while(indx<currentDays) {
                    all_emo_array.add(hashMapOf("content" to "nothing"))
                    indx+=1
                }
                Log.e("temp_all_diary_array",all_emo_array.toString())
                Log.e("temp_all_diary_array_size",all_emo_array.size.toString())

                binding.jumpLoad.visibility = View.GONE
              Log.e("wwww",all_emo_array.toString())
                //   listView = binding.lvDiary
                adapter.notifyDataSetChanged()

                //    listView.adapter = DiaryArrayAdapter(requireContext(), all_diary_array)
            } catch (e: Exception) {
                Log.e("err", "eqwewr" )

                // 處理例外狀況
            }
        }

        //all_diary_array.add(hashMapOf("emtion" to "null"))
//////////////////
        for (i in 0 until currentDays) {
            val bean = DayBean()
            bean.year = calendar.get(Calendar.YEAR)
            bean.month = calendar.get(Calendar.MONTH) + 1
            bean.day = i + 1
            val nowDate = getFormatTime("yyyy-M-d", Calendar.getInstance().time)
            val selectDate = getFormatTime("yyyy-M-", calendar.time) + (i + 1)
            bean.currentDay = nowDate == selectDate
            bean.currentMonth = true
            dataList.add(bean)
        }
        calendar.add(Calendar.MONTH, 1)
        val nextWeekIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1
        for (i in 0 until 7 - nextWeekIndex) {
            val bean = DayBean()
            bean.year = calendar.get(Calendar.YEAR)
            bean.month = calendar.get(Calendar.MONTH) + 1
            bean.day = i + 1
            bean.currentDay = false
            bean.currentMonth = false
            dataList.add(bean)
        }
        Log.e("QAQ",all_emo_array.toString())
       // adapter.notifyDataSetChanged()
        calendar.add(Calendar.MONTH, -1)
 /////////////////////////////////////
        //val handler = Handler()
        //handler.postDelayed({
            //binding.jumpLoadGif.visibility = View.GONE
        //}, 7000L)

    }

    suspend fun readFirebaseData(target_doc:String): DocumentSnapshot {
        val querySnapshot = db.collection("users").document(uid)
            .collection("user_diary").document(target_doc).get().await()
        return querySnapshot
    }

    private fun setCurrentData(calendar: Calendar) {
        tvCurrentDate.text = getFormatTime("yyyy年MM月", calendar.time)
    }
    fun isRunYear(y: Int): Boolean {
        return y % 4 == 0 && y % 100 != 0 || y % 400 == 0
    }

    fun getFormatTime(p: String, t: Date): String {
        return SimpleDateFormat(p, Locale.CHINESE).format(t)
    }

    fun getMonth(m: Int, y: Int): Int {
        when (m) {
            2 -> return if (isRunYear(y)) 29 else 28
            4, 6, 9, 11 -> return 30
            else -> return 31
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}