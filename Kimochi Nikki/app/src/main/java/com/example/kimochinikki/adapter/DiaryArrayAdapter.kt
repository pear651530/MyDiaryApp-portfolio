package com.example.kimochinikki.adapter

import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Button
import com.example.kimochinikki.bean.DiaryBean
import com.example.kimochinikki.R

class DiaryArrayAdapter(val c: Context, val items: ArrayList<DiaryBean>) :

    ArrayAdapter<DiaryBean>(c, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
val inflater = LayoutInflater.from(c)
        var itemlayout: LinearLayout? = null

        if (convertView == null) {
            itemlayout = inflater.inflate(R.layout.diary_item, null) as? LinearLayout
        } else {
            itemlayout = convertView as? LinearLayout
        }

        val item: DiaryBean? = getItem(position) as? DiaryBean

        val tv_day: TextView = itemlayout?.findViewById(R.id.diary_day)!!
        tv_day.text = item!!.day

        val tv_title: TextView = itemlayout?.findViewById(R.id.diary_title)!!
        tv_title.text = item!!.title

        return itemlayout
    }
}
