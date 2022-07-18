package com.example.countdownapplication.model

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class CountDownTimerModelFactory ( private  val lifecycle: Lifecycle) : ViewModelProvider.Factory  {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CountDownTimerModel::class.java)){
            return  CountDownTimerModel(lifecycle) as T
        }
        throw IllegalArgumentException("Unkown view model")
    }
}