package com.example.kimochinikki.ui.setting

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kimochinikki.HomeActivity
import com.example.kimochinikki.MainActivity
import com.example.kimochinikki.R
import com.example.kimochinikki.databinding.FragmentSettingBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private lateinit var btn_sign_out: Button
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        btn_sign_out=root.findViewById(R.id.btn_sign_out)
        btn_sign_out.setOnClickListener{
            sign_out()
        }
        return root
    }


    private fun sign_out() {
        //this都換成requireContext()
        AlertDialog.Builder(requireContext())
            //AlertDialog.Builder (context: Context)
            //參數放要傳入的 MainActivity Context
            .setTitle("即將登出")
            .setMessage(" ")  //訊息內容
            .setPositiveButton("確認") {_,_ ->
                Firebase.auth.signOut()
                startActivity(Intent(requireContext(), MainActivity::class.java))
            }
            .setNegativeButton("取消",null)
            .create()
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}