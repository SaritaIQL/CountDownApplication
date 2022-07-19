package com.example.countdownapplication.util

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.core.content.ContextCompat

class ReusedMethod {
    companion object{
        fun isNetworkConnected(context: Context): Boolean {
            if (context != null) {
                val connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                return if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    true
                } else {

                    Toast.makeText(context,"Please Check Your internet connection",Toast.LENGTH_LONG).show()
                    false
                }
            }
            return false
        }
    }
}