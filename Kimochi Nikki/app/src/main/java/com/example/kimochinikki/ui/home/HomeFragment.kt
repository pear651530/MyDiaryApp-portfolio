package com.example.kimochinikki.ui.home

import android.R
import android.os.Bundle
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
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import androidx.core.content.ContextCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
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

        val user = Firebase.auth.currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val name = it.displayName
            val email = it.email
            val photoUrl = it.photoUrl

            // Check if user's email is verified
            val emailVerified = it.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            val uid = it.uid

        }
        //binding.rongTry.setText()


        return root
    }

    private fun initView() {
        tvCurrentDate = binding.tvCurrentDate
        tvPreMonth = binding.tvPreMonth
        tvNextMonth = binding.tvNextMonth
        gv = binding.gv
        initAdapter()
        getfirebase()
    }
    private fun getfirebase(){
        //Log.e("can see?",(tvCurrentDate.text).toString())
        val str=(tvCurrentDate.text).toString()
        val pattern = "(\\d{4})年(\\d{2})月".toRegex()
        val matchResult = pattern.find(str)
        val year = matchResult?.groupValues?.get(1) // 2023
        val month = matchResult?.groupValues?.get(2) // 06
        Log.e("can see?",year.toString())
        Log.e("can see?",month.toString())

    }
    private fun initAdapter() {
        val dataList = ArrayList<DayBean>()
        val adapter = DayAdapter(dataList, requireContext())
        adapter.setOnDateItemClickListener(object : DayAdapter.OnDateItemClickListener { //改文字!!!
            override fun onDateItemClick(date: DayBean) {
                binding.textviewAverage.text=date.day.toString()
                var randomValue = Random.nextInt(10)
                binding.textviewQuotations.text=getString(QuotationsSimleId[randomValue])
                randomValue = Random.nextInt(10)
                binding.textviewSuggestions.text=getString(SuggestionsSimleId[randomValue])
            }
        })
        gv.adapter = adapter
        val calendar = Calendar.getInstance()
        setCurrentData(calendar)
        updateAdapter(calendar, dataList, adapter)
        tvPreMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, -1)
            updateAdapter(calendar, dataList, adapter)
        }
        tvNextMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, 1)
            updateAdapter(calendar, dataList, adapter)
        }
    }

    private fun updateAdapter(calendar: Calendar, dataList: ArrayList<DayBean>, adapter: DayAdapter) {
        dataList.clear()
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
        adapter.notifyDataSetChanged()
        calendar.add(Calendar.MONTH, -1)
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