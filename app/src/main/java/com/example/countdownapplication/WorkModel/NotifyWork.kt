package com.example.countdownapplication.WorkModel

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.countdownapplication.Extention.vectorToBitmap
import com.example.countdownapplication.MainActivity
import com.example.countdownapplication.R
import com.example.countdownapplication.util.AppConstants
import com.example.countdownapplication.util.MyApplication
import com.example.countdownapplication.util.SharedPreferenceManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NotifyWork(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        val id = inputData.getLong(NOTIFICATION_ID, 0).toInt()
        startTimer()
        return Result.success()
    }

    fun startTimer() {
        // Update the elapsed time every second.
        val getIsTimerClick = SharedPreferenceManager.getBoolean(AppConstants.isClickStartTime,false)
        val appBackState = SharedPreferenceManager.getBoolean(AppConstants.isAppBackground,false)

        if(getIsTimerClick && appBackState){
            MyApplication.job = MyApplication.scope.launch {
                while(true) {
                    MyApplication.Counter = MyApplication.Counter + 1
//                Log.e(TAG, "************* afficher +${MyApplication.Counter.toString()}")
                    sendNotification(MyApplication.Counter)

                    delay(1000)
                }
            }
        }
    }
    private fun sendNotification(id: Int) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(NOTIFICATION_ID, id)

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val bitmap = applicationContext.vectorToBitmap(R.drawable.ic_schedule_black_24dp)
        val titleNotification = applicationContext.getString(R.string.notification_title)
        val subtitleNotification = applicationContext.getString(R.string.notification_subtitle) +" :"+id.toString()
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)
            .setLargeIcon(bitmap).setSmallIcon(R.drawable.ic_schedule_white)
            .setContentTitle(titleNotification).setContentText(subtitleNotification)
            .setDefaults(NotificationCompat.DEFAULT_ALL).setContentIntent(pendingIntent).setAutoCancel(true)

        notification.priority = NotificationCompat.PRIORITY_MAX

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(NOTIFICATION_CHANNEL)

            val ringtoneManager = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

            val channel =
                NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_HIGH)

            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            channel.setSound(ringtoneManager, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(id, notification.build())
    }

    companion object {
        const val NOTIFICATION_ID = "appName_notification_id"
        const val NOTIFICATION_NAME = "appName"
        const val NOTIFICATION_CHANNEL = "appName_channel_01"
        const val NOTIFICATION_WORK = "appName_notification_work"
        val scope = MainScope() // could also use an other scope such as viewModelScope if available
        lateinit var job: Job
    }
}