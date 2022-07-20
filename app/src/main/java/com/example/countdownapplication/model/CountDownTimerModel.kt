package com.example.countdownapplication.model

import android.util.Log
import androidx.lifecycle.*
import com.example.countdownapplication.database.CommentRepository
import com.example.countdownapplication.database.Comments
import com.example.countdownapplication.util.AppConstants
import com.example.countdownapplication.util.MyApplication
import com.example.countdownapplication.util.SharedPreferenceManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CountDownTimerModel(private  val repository: CommentRepository, val lifecycle: Lifecycle) : ViewModel() {

      var btnTitle = MutableLiveData<String>()
      var strCurrentTimer = MutableLiveData<String>()
    val scope = MainScope() // could also use an other scope such as viewModelScope if available
    var job: Job?= null
    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        strCurrentTimer.value = MyApplication.Counter.toString()
    }


    fun changesCounterValue ()  {
        var getCounterValue: Int =0
        job = scope.launch {
            while(true) {
                getCounterValue= MyApplication.Counter
                Log.e("CounterValue","Count Value : ${getCounterValue} : ${MyApplication.Counter.toString()}")
                strCurrentTimer.value = getCounterValue.toString()
                delay(1000)
            }
        }

    }

    fun changeButtonText() : String{
        val appOpenCount = SharedPreferenceManager.getInt(AppConstants.appCount,0)
        if(appOpenCount.equals(0)){
            btnTitle.value=AppConstants.startTime
            SharedPreferenceManager.putString(AppConstants.btnTitle,AppConstants.startTime)
        }
        else{
            if(SharedPreferenceManager.getString(AppConstants.btnTitle,"").equals(AppConstants.startTime)){
               SharedPreferenceManager.putBoolean(AppConstants.isClickStartTime,true)

                btnTitle.value= AppConstants.stopTime
            }
            else{
                SharedPreferenceManager.putBoolean(AppConstants.isClickStartTime,false)

                btnTitle.value= AppConstants.startTime
            }
        }
        SharedPreferenceManager.putString(AppConstants.btnTitle,  btnTitle.value.toString())

        return btnTitle.value!!
    }

    fun getCurrentText(): String {

        val appOpenCount = SharedPreferenceManager.getInt(AppConstants.appCount,0)

        if(appOpenCount.equals(0)){
            btnTitle.value=AppConstants.startTime
        }
        else {
            if(SharedPreferenceManager.getString(AppConstants.btnTitle,"").equals(AppConstants.startTime)){
                btnTitle.value=AppConstants.startTime
                changesCounterValue()
            }
            else{
                changesCounterValue()
                btnTitle.value=AppConstants.stopTime
            }

        }
        SharedPreferenceManager.putString(AppConstants.btnTitle,btnTitle.value.toString())

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