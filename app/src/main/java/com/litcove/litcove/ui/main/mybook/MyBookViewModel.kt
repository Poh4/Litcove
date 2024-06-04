package com.litcove.litcove.ui.main.mybook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyBookViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is my book Fragment"
    }
    val text: LiveData<String> = _text
}