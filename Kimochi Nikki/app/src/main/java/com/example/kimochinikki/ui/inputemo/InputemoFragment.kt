package com.example.kimochinikki.ui.inputemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kimochinikki.databinding.FragmentInputemoBinding
import com.example.kimochinikki.R

class InputemoFragment : Fragment() {

    private var _binding: FragmentInputemoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val QuotationsSimleId = intArrayOf(
        R.string.quotations_simle1,
        R.string.quotations_simle2,
        R.string.quotations_simle3,
        R.string.quotations_simle4,
        R.string.quotations_simle5,
        R.string.quotations_simle6,
        R.string.quotations_simle7,
        R.string.quotations_simle8,
        R.string.quotations_simle9,
        R.string.quotations_simle10
    )

    val QuotationsSadId = intArrayOf(
        R.string.quotations_sad1,
        R.string.quotations_sad2,
        R.string.quotations_sad3,
        R.string.quotations_sad4,
        R.string.quotations_sad5,
        R.string.quotations_sad6,
        R.string.quotations_sad7,
        R.string.quotations_sad8,
        R.string.quotations_sad9,
        R.string.quotations_sad10
    )

    val SuggestionsSimleId = intArrayOf(
        R.string.suggestions_simle1,
        R.string.suggestions_simle2,
        R.string.suggestions_simle3,
        R.string.suggestions_simle4,
        R.string.suggestions_simle5,
        R.string.suggestions_simle6,
        R.string.suggestions_simle7,
        R.string.suggestions_simle8,
        R.string.suggestions_simle9,
        R.string.suggestions_simle10
    )

    val SuggestionsSadId = intArrayOf(
        R.string.suggestions_sad1,
        R.string.suggestions_sad2,
        R.string.suggestions_sad3,
        R.string.suggestions_sad4,
        R.string.suggestions_sad5,
        R.string.suggestions_sad6,
        R.string.suggestions_sad7,
        R.string.suggestions_sad8,
        R.string.suggestions_sad9,
        R.string.suggestions_sad10
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentInputemoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}