package com.example.useviewmodelincustomview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CustomViewModel : ViewModel() {
    val nameLiveData = MutableLiveData<String>()

    fun loadName() {
        viewModelScope.launch(Dispatchers.Main) {
            val name = withContext(Dispatchers.IO) {
                return@withContext "custom name"
            }
            nameLiveData.value = name
        }
    }
}