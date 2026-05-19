package com.zsolt.valisko.assignment_mobile_programming_ii

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    // Task 4: LiveData to hold the text content
    private val _text = MutableLiveData<String>("")
    val text: LiveData<String> = _text

    // Task 4: Method to update text immediately from the UI
    fun onTextChanged(newText: String) {
        _text.value = newText
    }

    // Task 2: Method to set the final state text
    fun setFinalText() {
        _text.value = "Process ended."
    }
}
