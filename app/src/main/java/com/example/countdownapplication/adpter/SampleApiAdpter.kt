package com.example.countdownapplication.adpter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.countdownapplication.R
import com.example.countdownapplication.database.Comments
import com.example.countdownapplication.databinding.RowSampleDataBinding

class SampleApiAdpter (private val clickListener: (Comments) -> Unit) :
    RecyclerView.Adapter<MyAccountHolder>() {
    private val commentList = ArrayList<Comments>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAccountHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: RowSampleDataBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.row_sample_data, parent, false)
        return MyAccountHolder(binding)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onBindViewHolder(holder: MyAccountHolder, position: Int) {
        holder.bind(commentList[position], clickListener)
    }

    fun setList(subscribers: List<Comments>) {
        commentList.clear()
        commentList.addAll(subscribers)

    }

}

class MyAccountHolder(val binding: RowSampleDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(comments:  Comments, clickListener: (Comments) -> Unit) {

        binding.txtRowPostId.text=comments.postId.toString()
        binding.txtRowEmail.text=comments.email.toString()
        binding.txtRowBody.text=comments.body.toString()
        binding.txtRowName.text=comments.name.toString()
    }
}