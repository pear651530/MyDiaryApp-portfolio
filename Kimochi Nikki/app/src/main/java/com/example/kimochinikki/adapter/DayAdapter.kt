package com.example.kimochinikki.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.kimochinikki.R
import com.example.kimochinikki.bean.DayBean
import android.util.Log
import java.util.ArrayList
import java.util.HashMap

class DayAdapter(val list: List<DayBean>, val all_emo_array : ArrayList<HashMap<String, String>>, val context: Context) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }
    companion object {
        private const val TAG = "!!!!!!!!!!!!!!!!!!!"
    }
    private var onDateItemClickListener: OnDateItemClickListener? = null
    fun setOnDateItemClickListener(listener: OnDateItemClickListener) {
        onDateItemClickListener = listener
    }
    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        var itemlayout: LinearLayout? = null

        if (view == null) {
            itemlayout = inflater.inflate(R.layout.grid_item, null) as? LinearLayout
        } else {
            itemlayout = view as? LinearLayout
        }
        val bean = getItem(position) as DayBean

        val textView: TextView = itemlayout?.findViewById(R.id.dayTextView)!!
        textView.text = bean.day.toString()
        textView.gravity = Gravity.CENTER
        textView.setTextColor(Color.BLACK)
        textView.setTypeface(Typeface.DEFAULT_BOLD)
       // Log.e("bean",bean.toString())
        //Log.e("all_emo_array",all_emo_array.toString())

        if (bean.currentDay) {
            textView.setBackgroundColor(Color.parseColor("#fd5f00"))
            textView.setTextColor(Color.WHITE)
        } else if (bean.currentMonth) {
            textView.setBackgroundColor(Color.WHITE)
            textView.setTextColor(Color.BLACK)
        } else {
            // 通过 parseColor 方法得到的颜色不可以简写，必须写满六位
            textView.setBackgroundColor(Color.parseColor("#aaaaaa"))
            textView.setTextColor(Color.BLACK)
        }

        val imageView: ImageView = itemlayout?.findViewById(R.id.iconImageView)!!
        Glide.with(context).load(R.drawable.ic_menu_home).into(imageView)

        itemlayout.setOnClickListener {
            onDateItemClickListener?.onDateItemClick(bean)
        }
        return itemlayout
    }

    interface OnDateItemClickListener {
        fun onDateItemClick(date: DayBean)
    }
}
