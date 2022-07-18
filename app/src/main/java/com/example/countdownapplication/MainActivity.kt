package com.example.countdownapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.countdownapplication.databinding.ActivityMainBinding
import com.example.countdownapplication.model.CountDownTimerModel
import com.example.countdownapplication.model.CountDownTimerModelFactory
import com.example.countdownapplication.retrofit.Response.CommentResponseItem
import com.example.countdownapplication.retrofit.RetrofitAPI
import com.example.countdownapplication.util.AppConstants
import com.example.countdownapplication.util.MyApplication
import com.example.countdownapplication.util.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var countDownTimerModel: CountDownTimerModel
    private val TAG : String= "MyApplication"
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
//        startService(Intent(baseContext, OnClearFromRecentService::class.java))

        val factory = CountDownTimerModelFactory(this.lifecycle)
        countDownTimerModel= ViewModelProvider(this,factory).get(CountDownTimerModel::class.java)
        binding.countViewModel = countDownTimerModel

        binding.lifecycleOwner=this
        Log.e(TAG,"Current Value : ${MyApplication.Counter.toString()}")
        val appOpenCount = SharedPreferenceManager.getInt(AppConstants.appCount,0)

        val textChange = countDownTimerModel.getCurrentText()
        binding.btnTextTimer.text=textChange

        binding.btnTextTimer.setOnClickListener {
            val getChangeButton = countDownTimerModel.changeButtonText()
            if(getChangeButton.equals(AppConstants.startTime)){

                MyApplication.appInstance.startTimer()
            }
            else{
                MyApplication.appInstance.stopTimer()
            }
            binding.btnTextTimer.text=getChangeButton

        }

        getAllCourses()
    }

    private fun getAllCourses() {
        // on below line we are creating a retrofit
        // builder and passing our base url
        val retrofit = Retrofit.Builder()
            .baseUrl("http://jsonplaceholder.typicode.com/") // on below line we are calling add
            // Converter factory as Gson converter factory.
            .addConverterFactory(GsonConverterFactory.create()) // at last we are building our retrofit builder.
            .build()
        // below line is to create an instance for our retrofit api class.
        val retrofitAPI  = retrofit.create(RetrofitAPI::class.java)

        // on below line we are calling a method to get all the courses from API.
        val call: Call<ArrayList<CommentResponseItem>> = retrofitAPI.getCommentResponse()

        // on below line we are calling method to enqueue and calling
        // all the data from array list.
        call.enqueue(object : Callback<ArrayList<CommentResponseItem>> {
            override fun onResponse(
                call: Call<ArrayList<CommentResponseItem>>,
                response: Response<ArrayList<CommentResponseItem>>
            ) {
                if (response.isSuccessful()) {

                    // on successful we are hiding our progressbar.

                    // below line is to add our data from api to our array list.
//                        recyclerDataArrayList = response.body()

                    Log.e("Api Data : ","Data is : ${response.body().toString()}")

                    // below line we are running a loop to add data to our adapter class.
//                        for (i in 0 until recyclerDataArrayList.size()) {
//                            recyclerViewAdapter =
//                                RecyclerViewAdapter(recyclerDataArrayList, this@MainActivity)
//
//                            // below line is to set layout manager for our recycler view.
//                            val manager = LinearLayoutManager(this@MainActivity)
//
//                            // setting layout manager for our recycler view.
//                            courseRV.setLayoutManager(manager)
//
//                            // below line is to set adapter to our recycler view.
//                            courseRV.setAdapter(recyclerViewAdapter)
//                        }
                }                }

            override fun onFailure(call: Call<ArrayList<CommentResponseItem>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Fail to get data", Toast.LENGTH_SHORT).show()
            }
        })


    }

}