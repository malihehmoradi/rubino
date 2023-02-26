package com.example.rubinoapp.ui.postdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rubinoapp.data.database.Post
import com.example.rubinoapp.databinding.ItemCommentBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageAdapter @Inject constructor(
    var list: List<Post.Comment>
) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: Post.Comment?) {
            binding.comment = comment
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCommentBinding =
            ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = list[position]
        holder.bind(comment)
    }
}