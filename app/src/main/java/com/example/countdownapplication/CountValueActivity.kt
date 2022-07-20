package com.example.countdownapplication

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.countdownapplication.adpter.SampleApiAdpter
import com.example.countdownapplication.database.CommentDatabase
import com.example.countdownapplication.database.CommentRepository
import com.example.countdownapplication.database.Comments
import com.example.countdownapplication.databinding.ActivityCountValueBinding
import com.example.countdownapplication.model.CountDownTimerModel
import com.example.countdownapplication.model.CountDownTimerModelFactory
import com.example.countdownapplication.retrofit.Response.CommentResponseItem
import com.example.countdownapplication.retrofit.RetrofitAPI
import com.example.countdownapplication.util.AppConstants
import com.example.countdownapplication.util.MyApplication
import com.example.countdownapplication.util.ReusedMethod
import com.example.countdownapplication.util.SharedPreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CountValueActivity : AppCompatActivity() {

    private lateinit var countDownTimerModel: CountDownTimerModel
    private val TAG : String= "MyApplication"
    private lateinit var binding: ActivityCountValueBinding
    private lateinit var adapter: SampleApiAdpter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_count_value)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_count_value)
        val dao = CommentDatabase.getInstance(application).commentsDAO
        val repository = CommentRepository(dao)

        val factory = CountDownTimerModelFactory(repository,this.lifecycle)
        countDownTimerModel= ViewModelProvider(this,factory).get(CountDownTimerModel::class.java)
        binding.countDownTimerModel = countDownTimerModel
        binding.lifecycleOwner=this
        Log.e(TAG,"Current Value : ${MyApplication.Counter.toString()}")

        val textChange = countDownTimerModel.getCurrentText()
        Log.e("btnValue","Get the init value  btn value : ${textChange.toString()}")

        binding.btnTextTimer.text=textChange
        SharedPreferenceManager.putInt(AppConstants.appCount,1)



        binding.btnTextTimer.setOnClickListener {
            var getChangeButton = countDownTimerModel.changeButtonText()
            binding.btnTextTimer.text=countDownTimerModel.btnTitle.value
            getChangeButton = binding.btnTextTimer.text.toString()
//            Log.e("btnValue","Get the current btn value : ${getChangeButton.toString()} : ${countDownTimerModel.btnTitle.value}")

            if(getChangeButton.equals(AppConstants.startTime)){

                MyApplication.appInstance.stopTimer()
            }
            else{
                MyApplication.appInstance.startTimer()
            }
        }

    }

    private fun getAllCourses() {

        if(ReusedMethod.isNetworkConnected(this)){
            val retrofit = Retrofit.Builder()
                .baseUrl("http://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val retrofitAPI  = retrofit.create(RetrofitAPI::class.java)

            val call: Call<ArrayList<CommentResponseItem>> = retrofitAPI.getCommentResponse()

            call.enqueue(object : Callback<ArrayList<CommentResponseItem>> {
                override fun onResponse(
                    call: Call<ArrayList<CommentResponseItem>>,
                    response: Response<ArrayList<CommentResponseItem>>
                ) {
                    if (response.isSuccessful()) {


                      //  Log.d("Api Data : ","Data is : ${response.body().toString()}")

                        val getData = response.body()
                        val arrayList : ArrayList<CommentResponseItem> = arrayListOf()

                        arrayList.addAll(getData!!)

                     //   Log.d("Api Data : ","Data is : ${arrayList.size.toString()}")

                        for( data in arrayList){
                            countDownTimerModel.insertAllData(Comments(0,data.postId,data.id,data.name,data.email,data.body))
                        }
                        initRecyclerView()

                    }
                }

                override fun onFailure(call: Call<ArrayList<CommentResponseItem>>, t: Throwable) {
                    Log.e("Api data","Fail to get data ${t.toString()}")
                    Toast.makeText(this@CountValueActivity, "Fail to get data ${t.toString()}", Toast.LENGTH_SHORT).show()
                }
            })
        }
        else{
            Toast.makeText(this,"Please Check Your internet connection",Toast.LENGTH_LONG).show()

        }


    }


    private fun initRecyclerView() {
        binding.recySampleDataApi.layoutManager = LinearLayoutManager(this)
        adapter = SampleApiAdpter  ({ selectedItem: Comments -> listItemClicked(selectedItem) })
        binding.recySampleDataApi.adapter = adapter
        displaySubscribersList()
    }

    private fun displaySubscribersList() {
        countDownTimerModel.getAllRecord().observe(this, Observer {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    fun checkingDataBaseReocrd() {
        countDownTimerModel.getAllRecord().observe(this, Observer {
          val recordSize =  it.size
            Log.d("Api Data : ","Data is recordSize : ${recordSize.toString()}")
            if(recordSize>0){
                Thread {
                    CommentDatabase.getInstance(this@CountValueActivity).clearAllTables()
                }.start()

                deleteDatabase(CommentDatabase::class.java.name)
            }

        })
    }

    override fun onResume() {
        super.onResume()
        getAllCourses()
    }
    private fun listItemClicked(subscriber: Comments) {
    }

    override fun onStop() {
        super.onStop()
        GlobalScope.launch(Dispatchers.IO) {
            CommentDatabase.getInstance(applicationContext).clearAllTables()
        }
    }
    override fun onDestroy() {
        super.onDestroy()

        //this table is kill because when application again open after call the api and new updated data add into the table and showing inside the database.
        //Because not all data store in the requirement. Always fresh open and api call and after storing data display.
        Thread {
            CommentDatabase.getInstance(this@CountValueActivity).clearAllTables()
        }.start() //clear all rows from database


    }

    override fun onPause() {
        super.onPause()
        GlobalScope.launch(Dispatchers.IO) {
            CommentDatabase.getInstance(applicationContext).clearAllTables()
        }
    }

}