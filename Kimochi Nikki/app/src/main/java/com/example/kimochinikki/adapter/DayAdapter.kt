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
    fun getItem_emo(position: Int): HashMap<String, String> {
        return all_emo_array[position]
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
    var cnt=0
    val array = arrayOf("smiling", "angry","sad","heart","confusion")

    val emo_resourceMap = hashMapOf(
        "smiling" to R.drawable.smiling,
        "angry" to R.drawable.angry,
        "sad" to R.drawable.sad,
        "heart" to R.drawable.heart,
        "confusion" to R.drawable.confusion,
    )

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        Log.e("nono",all_emo_array.toString())
        val inflater = LayoutInflater.from(context)
        var itemlayout: LinearLayout? = null

        if (view == null) {
            itemlayout = inflater.inflate(R.layout.grid_item, null) as? LinearLayout
        } else {
            itemlayout = view as? LinearLayout
        }
        val bean = getItem(position) as DayBean
        //



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
        //Glide.with(context).load(R.drawable.ic_menu_home).into(imageView)
        imageView.setImageResource(0)
        if (bean.currentMonth&&all_emo_array.isNotEmpty()) {
            Log.e("err??",all_emo_array.toString())
           var item=getItem_emo(bean.day-1)
            Log.e("item err??",item.toString())

            //var item=getItem_emo(2)
            if(item?.get("content")=="have"){
                val max_emo=item?.get("max_emo")
                emo_resourceMap[max_emo]?.let { resourceId ->
                    imageView.setImageResource(resourceId)
                }
            }
            else{
                imageView.setImageResource(0)
            }
        }


        itemlayout.setOnClickListener {
            //Log.e("aaaaaaaaa", all_emo_array.toString())
            if(bean.currentMonth) {
                onDateItemClickListener?.onDateItemClick(bean, all_emo_array[bean.day - 1])
            }
        }
        return itemlayout
    }

    interface OnDateItemClickListener {
        fun onDateItemClick(date: DayBean, date_diary: HashMap<String, String>)
    }
}
