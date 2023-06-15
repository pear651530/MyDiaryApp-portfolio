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
        0 to R.drawable.smiling,
        1 to R.drawable.angry,
        2 to R.drawable.sad,
        2 to R.drawable.heart,
    )

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        var itemlayout: LinearLayout? = null

        if (view == null) {
            itemlayout = inflater.inflate(R.layout.grid_item, null) as? LinearLayout
        } else {
            itemlayout = view as? LinearLayout
        }
        val bean = getItem(position) as DayBean
        //

        /*if(all_emo_array.isNotEmpty()&&bean.currentMonth){
        val date_try=getItem_emo(position) as HashMap<String, String>
        Log.e("maybe?",date_try.toString())
            Log.e("cnt",cnt.toString())
            cnt+=1
        //

        }*/


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

        if (bean.currentMonth) {
            if(all_emo_array.isNotEmpty())
            {
                val item=getItem_emo(cnt) as HashMap<String, String>
                //  Log.e("maybe?",cnt.toString()+" "+item.toString())
                cnt+=1
                if(item?.get("content")=="have"){
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
                    var cnt_ = 0
                    var mx_idx: Int = 0
                    if(mx_emo==Integer.parseInt(item?.get("smile"))){
                        cnt_++
                        mx_idx = 0
                    }
                    if(mx_emo==Integer.parseInt(item?.get("angry"))){
                        cnt_++
                        mx_idx = 1
                    }
                    if(mx_emo==Integer.parseInt(item?.get("sad"))){
                        cnt_++
                        mx_idx = 2
                    }
                    if(mx_emo==Integer.parseInt(item?.get("heart"))){
                        cnt_++
                        mx_idx = 3
                    }
                    if(cnt_>1) Glide.with(context).load(R.drawable.confusion).into(imageView) //<a href="https://www.flaticon.com/free-icons/emojis" title="emojis icons">Emojis icons created by zafdesign - Flaticon</a>
                    else emo_resourceMap[mx_idx]?.let { resourceId ->
                        imageView.setImageResource(resourceId)
                    }
                }
            }
            if(cnt==all_emo_array.size)
            {cnt=0
                Log.e("maybe  size?",all_emo_array.size.toString())
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
