package com.example.countdownapplication.util

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import timber.log.Timber
import java.text.SimpleDateFormat
import androidx.lifecycle.*
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.countdownapplication.WorkModel.NotifyWork
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class  MyApplication : Application(), Application.ActivityLifecycleCallbacks, LifecycleObserver {
    private val TAG : String= "MyApplication"
    private var dateFormat: SimpleDateFormat? = null

    var handler: Handler = Handler()
    val mainHandler = Handler(Looper.getMainLooper())
    var handlerThread : HandlerThread?= null

    var runnable = Runnable { afficher() }
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        //App in background

        Log.e(TAG, "************* backgrounded")
        Log.e(TAG, "************* ${isActivityVisible()}")
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(
            NotifyWork::class.java
        ).build()

        WorkManager.getInstance().enqueue(oneTimeWorkRequest)
    }
//
//    @OnLifecycleEvent(Lifecycle.Event.ON_START)
//    fun onAppForegrounded() {
//
//        Log.e(TAG, "************* foregrounded")
//        Log.e(TAG, "************* ${isActivityVisible()}")
//        // App in foreground
//    }

    companion object{
        lateinit var appContext: Context
        lateinit var appInstance: MyApplication
        var Counter : Int  =0
        const val ONE_SECOND = 1000
        val scope = MainScope() // could also use an other scope such as viewModelScope if available
        var job: Job?= null

    }

    init {

        appContext=this
        appInstance=this
    }
    fun isActivityVisible(): String {
        return ProcessLifecycleOwner.get().lifecycle.currentState.name
    }
    override fun onCreate() {
        super.onCreate()
        this.registerActivityLifecycleCallbacks(this)
        Timber.plant(Timber.DebugTree())

//        AndroidNetworking.initialize(applicationContext)

        appInstance = this;
//        startKoin {
//            androidLogger()
//            androidContext(this@MyApplication)
//            modules(appModules)
//        }
        dateFormat =  SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        appContext = applicationContext
        SharedPreferenceManager.init(appContext)

        Log.e(TAG, "************* onCreate")
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)


    }

    fun afficher() {
//        Toast.makeText(baseContext, dateFormat!!.format(Date()), Toast.LENGTH_LONG).show()
        Log.e(TAG, "************* afficher +${Counter.toString()}")

        // handler.postDelayed(runnable, 1000)
    }



    fun startTimer() {
        handlerThread = HandlerThread("HandlerThread")
        // Update the elapsed time every second.

        job = scope.launch {
            while(true) {
                Counter = Counter + 1
                Log.e(TAG, "************* afficher +${Counter.toString()}")

                delay(1000)
            }
        }

//        runnable.run()
//        handlerThread!!.start()
//         val backgroundHandler = Handler(handlerThread!!.looper, Handler.Callback {
//             // Your code logic goes here.
//
//             // Update UI on the main thread.
//             mainHandler.post {
//                 Counter = Counter + 1
//                 afficher()
//             }
//             true
//         })

    }

    fun stopTimer() {
        //  handler.removeCallbacks(runnable)
        // handlerThread!!.quitSafely()
        job?.cancel()

    }
    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        Log.e(TAG, "************* onActivityCreated ${p0.localClassName}")
    }

    override fun onActivityStarted(p0: Activity) {
        Log.e(TAG, "************* onActivityStarted ${p0.localClassName}")    }

    override fun onActivityResumed(p0: Activity) {
        Log.e(TAG, "************* onActivityResumed ${p0.localClassName}")    }

    override fun onActivityPaused(p0: Activity) {
        Log.e(TAG, "************* onActivityPaused ${p0.localClassName}")    }

    override fun onActivityStopped(p0: Activity) {
        Log.e(TAG, "************* onActivityStopped ${p0.localClassName}")    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
        Log.e(TAG, "************* onActivitySaveInstanceState ${p0.localClassName}")    }

    override fun onActivityDestroyed(p0: Activity) {
        Log.e(TAG, "************* onActivityDestroyed ${p0.localClassName}")

    }
}