package com.example.countdownapplication.model

import android.util.Log
import androidx.lifecycle.*
import com.example.countdownapplication.database.CommentRepository
import com.example.countdownapplication.database.Comments
import com.example.countdownapplication.util.AppConstants
import com.example.countdownapplication.util.MyApplication
import com.example.countdownapplication.util.SharedPreferenceManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CountDownTimerModel(private  val repository: CommentRepository, val lifecycle: Lifecycle) : ViewModel() {

      var btnTitle = MutableLiveData<String>()
      var strCurrentTimer = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        strCurrentTimer.value = MyApplication.Counter.toString()
    }

    fun changesCounterValue ()  {
        val getCounterValue = MyApplication.Counter
        Log.e("CounterValue","Count Value : ${getCounterValue} : ${MyApplication.Counter.toString()}")
        strCurrentTimer.value = getCounterValue.toString()
    }

    fun changeButtonText() : String{
        val appOpenCount = SharedPreferenceManager.getInt(AppConstants.appCount,0)
        if(appOpenCount.equals(0)){
            btnTitle.value=AppConstants.startTime
            SharedPreferenceManager.putString(AppConstants.btnTitle,AppConstants.startTime)
        }
        else{
            if(SharedPreferenceManager.getString(AppConstants.btnTitle,"").equals(AppConstants.startTime)){
                btnTitle.value= AppConstants.stopTime
                SharedPreferenceManager.putString(AppConstants.btnTitle, AppConstants.stopTime)
            }
            else{
                btnTitle.value= AppConstants.startTime
                SharedPreferenceManager.putString(AppConstants.btnTitle, AppConstants.startTime)
            }
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
               // changesCounterValue()
            }
            else{
               // changesCounterValue()
                btnTitle.value=AppConstants.stopTime
                SharedPreferenceManager.putString(AppConstants.btnTitle,AppConstants.stopTime)
            }

        }
        return btnTitle.value!!
    }

    fun getAllRecord() = liveData {
        repository.comments.collect {
            emit(it)
        }
    }

    fun insertAllData(comments: Comments) = viewModelScope.launch {
        val rowId = repository.insert(comments)
        if(rowId> -1 ){
            Log.i("Insert","Insert data : ${rowId.toString()}")
        }
        else{
            Log.i("Insert","Error Occurre ${comments.toString()}")

        }
    }
}