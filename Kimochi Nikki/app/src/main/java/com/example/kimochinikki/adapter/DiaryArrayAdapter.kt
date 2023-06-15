package com.example.kimochinikki.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.kimochinikki.R
import com.example.kimochinikki.bean.DayBean

class DiaryArrayAdapter(val c: Context, val items: ArrayList<HashMap<String, String>>) :

    ArrayAdapter<HashMap<String, String>>(c, 0, items) {

    private var onDiaryItemClickListener: OnDiaryItemClickListener? = null

    fun setOnDiaryItemClickListener(listener: OnDiaryItemClickListener) {
        onDiaryItemClickListener = listener
    }

    interface OnDiaryItemClickListener {
        fun onDiaryItemClick(date_diary: HashMap<String, String>)
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(c)
        var itemlayout: LinearLayout? = null

        if (convertView == null) {
            itemlayout = inflater.inflate(R.layout.diary_item, null) as? LinearLayout
        } else {
            itemlayout = convertView as? LinearLayout
        }

        val item: HashMap<String, String>? = getItem(position) as? HashMap<String, String>
        //Log.e("see?",item.toString() )
        val tv_day: TextView = itemlayout?.findViewById(R.id.diary_day)!!
        tv_day.text = item?.get("date")

        val diary_iconImageView: ImageView = itemlayout?.findViewById(R.id.diary_iconImageView)!!
        val mx_emo = Integer.max(
            Integer.max(
                Integer.parseInt(item?.get("smile")),
                Integer.parseInt(item?.get("angry"))
            ),
            Integer.max(
                Integer.parseInt(item?.get("sad")),
                Integer.parseInt(item?.get("heart"))
            )
        )
        var cnt = 0
        var mx_idx = 0
        if(mx_emo==Integer.parseInt(item?.get("smile"))){
            cnt++
            mx_idx = 0
        }
        if(mx_emo==Integer.parseInt(item?.get("angry"))){
            cnt++
            mx_idx = 1
        }
        if(mx_emo==Integer.parseInt(item?.get("sad"))){
            cnt++
            mx_idx = 2
        }
        if(mx_emo==Integer.parseInt(item?.get("heart"))){
            cnt++
            mx_idx = 3
        }
        if(cnt>1) diary_iconImageView.setImageResource(R.drawable.confusion) //<a href="https://www.flaticon.com/free-icons/emojis" title="emojis icons">Emojis icons created by zafdesign - Flaticon</a>
        else if(mx_idx==0) diary_iconImageView.setImageResource(R.drawable.smiling)
        else if(mx_idx==1) diary_iconImageView.setImageResource(R.drawable.angry)
        else if(mx_idx==2) diary_iconImageView.setImageResource(R.drawable.sad)
        else if(mx_idx==3) diary_iconImageView.setImageResource(R.drawable.heart)

        itemlayout.setOnClickListener {
            item?.let { nonNullItem ->
                onDiaryItemClickListener?.onDiaryItemClick(nonNullItem)
            }
        }

        return itemlayout
    }
}
