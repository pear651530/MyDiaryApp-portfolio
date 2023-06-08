package com.example.kimochinikki.ui.inputemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kimochinikki.databinding.FragmentInputemoBinding
import com.example.kimochinikki.R
import java.lang.Integer.max

class InputemoFragment : Fragment() {

    private var _binding: FragmentInputemoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentInputemoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        init()
        val imageButton_Simle = binding.ImageButtonSimle
        imageButton_Simle.setOnClickListener { addtime("Simle") }
        val imageButton_Sad = binding.ImageButtonSad
        imageButton_Sad.setOnClickListener { addtime("Sad") }
        val imageButton_Angry = binding.ImageButtonAngry
        imageButton_Angry.setOnClickListener { addtime("Angry") }
        val imageButton_Heart = binding.ImageButtonHeart
        imageButton_Heart.setOnClickListener { addtime("Heart") }

        culavg()
        return root
    }

    fun init() {
        binding.TextViewSimletime.text = "0"
        binding.TextViewSadtime.text = "0"
        binding.TextViewAngrytime.text = "0"
        binding.TextViewHearttime.text = "0"
    }

    fun addtime(whice: String) {
        if (whice == "Simle") {
            var num = Integer.parseInt(binding.TextViewSimletime.text.toString())
            num++
            binding.TextViewSimletime.text = num.toString()
        } else if (whice == "Sad") {
            var num = Integer.parseInt(binding.TextViewSadtime.text.toString())
            num++
            binding.TextViewSadtime.text = num.toString()
        } else if (whice == "Angry") {
            var num = Integer.parseInt(binding.TextViewAngrytime.text.toString())
            num++
            binding.TextViewAngrytime.text = num.toString()
        } else {
            var num = Integer.parseInt(binding.TextViewHearttime.text.toString())
            num++
            binding.TextViewHearttime.text = num.toString()
        }
    }

    public fun culavg() {
        max(
            max(
                Integer.parseInt(binding.TextViewSimletime.text.toString()),
                Integer.parseInt(binding.TextViewSadtime.text.toString())
            ),
            max(
                Integer.parseInt(binding.TextViewAngrytime.text.toString()),
                Integer.parseInt(binding.TextViewHearttime.text.toString())
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}