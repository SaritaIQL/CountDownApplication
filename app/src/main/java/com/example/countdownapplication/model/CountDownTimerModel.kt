package com.example.countdownapplication.model

import androidx.lifecycle.*
import com.example.countdownapplication.util.AppConstants
import com.example.countdownapplication.util.MyApplication
import com.example.countdownapplication.util.SharedPreferenceManager

class CountDownTimerModel(val lifecycle: Lifecycle) : ViewModel() {

      var btnTitle = MutableLiveData<String>()
      var strCurrentTimer = MutableLiveData<String>()

    init {
        strCurrentTimer.value = MyApplication.Counter.toString()
    }

    fun changesCounterValue () : String {
        val getCounterValue = MyApplication.Counter
        strCurrentTimer.value = getCounterValue.toString()
        return strCurrentTimer.value!!
    }
    fun changeButtonText() : String{
        if(SharedPreferenceManager.getString(AppConstants.btnTitle,"").equals(AppConstants.startTime)){
            SharedPreferenceManager.putInt(AppConstants.appCount,1)
            btnTitle.value= AppConstants.stopTime
            SharedPreferenceManager.putString(AppConstants.btnTitle, AppConstants.stopTime)
        }
        else{
            btnTitle.value= AppConstants.startTime
            SharedPreferenceManager.putString(AppConstants.btnTitle, AppConstants.startTime)
        }
        return btnTitle.value!!
    }

    fun getCurrentText(): String {

        val appOpenCount = SharedPreferenceManager.getInt(AppConstants.appCount,0)

        if(appOpenCount.equals(0)){
            btnTitle.value=AppConstants.startTime
            SharedPreferenceManager.putString(AppConstants.btnTitle,AppConstants.startTime)
        }
        else {
            if(SharedPreferenceManager.getString(AppConstants.btnTitle,"").equals(AppConstants.startTime)){
                btnTitle.value=AppConstants.startTime
                SharedPreferenceManager.putString(AppConstants.btnTitle,AppConstants.startTime)
            }
            else{
                btnTitle.value=AppConstants.stopTime
                SharedPreferenceManager.putString(AppConstants.btnTitle,AppConstants.stopTime)
            }

        }
        return btnTitle.value!!
    }
}