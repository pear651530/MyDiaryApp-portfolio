package com.example.kimochinikki.ui.touch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TouchViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is touch Fragment"
    }
    val text: LiveData<String> = _text
}