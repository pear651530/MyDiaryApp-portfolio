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
    val emo_resourceMap = hashMapOf(
        "smiling" to R.drawable.smiling,
        "angry" to R.drawable.angry,
        "sad" to R.drawable.sad,
        "heart" to R.drawable.heart,
        "confusion" to R.drawable.confusion,
    )
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
        val max_emo=(item?.get("max_emo")).toString()
        Log.e("emo", max_emo)
// val emo_array = arrayOf("smiling", "angry","sad","heart","confusion")
        // diary_iconImageView.setImageResource(emo_resourceMap.get("max_emo"))

        emo_resourceMap[max_emo]?.let { resourceId ->
            diary_iconImageView.setImageResource(resourceId)
        }

        itemlayout.setOnClickListener {
            item?.let { nonNullItem ->
                onDiaryItemClickListener?.onDiaryItemClick(nonNullItem)
            }
        }

        return itemlayout
    }
}
