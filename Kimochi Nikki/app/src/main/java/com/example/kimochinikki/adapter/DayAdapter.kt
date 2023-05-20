package com.example.kimochinikki.adapter

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import android.content.Context
import android.widget.TextView
import android.graphics.Color
import android.graphics.Typeface
import android.view.*
import com.example.kimochinikki.bean.DayBean
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.kimochinikki.R

class DayAdapter(val list: List<DayBean>, val context: Context) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
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

        return itemlayout
    }
}
