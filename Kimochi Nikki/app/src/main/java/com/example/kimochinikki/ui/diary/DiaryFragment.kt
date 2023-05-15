package com.example.kimochinikki.ui.diary

import android.R
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kimochinikki.databinding.FragmentDiaryBinding
import android.widget.ListView
import com.example.kimochinikki.bean.DiaryBean
import com.example.kimochinikki.adapter.DiaryArrayAdapter

class DiaryFragment : Fragment() {
    private lateinit var listView: ListView
    private var _binding: FragmentDiaryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiaryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val diarylist = ArrayList<DiaryBean>()
        diarylist.add(DiaryBean("5/15", "aaaa"))

        listView = binding.lvDiary

        listView.adapter = DiaryArrayAdapter(requireContext(), diarylist)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}