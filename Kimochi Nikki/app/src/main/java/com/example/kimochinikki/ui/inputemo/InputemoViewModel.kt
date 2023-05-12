package com.example.kimochinikki.ui.inputemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InputemoViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is inputemo Fragment"
    }
    val text: LiveData<String> = _text
}