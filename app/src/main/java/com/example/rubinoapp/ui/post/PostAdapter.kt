package com.example.rubinoapp.ui.post

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.rubinoapp.R
import com.example.rubinoapp.data.database.Post
import com.example.rubinoapp.databinding.ItemPostBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostAdapter @Inject constructor(
    private val clickCallback: (post: Post) -> Unit,
    private val likeCallback: (post: Post) -> Unit
) : PagingDataAdapter<Post, PostAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(
        private val binding: ItemPostBinding,
        private val context: Context,
        val clickCallback: (post: Post) -> Unit,
        private val likeCallback: (post: Post) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private fun doLike(post: Post, likeCallback: (post: Post) -> Unit) {
            binding.btnLikes.setImageResource(R.drawable.round_favorite_24)
            likeCallback(post)
        }

        private fun doDislike(post: Post, likeCallback: (post: Post) -> Unit) {
            binding.btnLikes.setImageResource(R.drawable.round_favorite_border_24)
            likeCallback(post)
        }

        fun bind(post: Post?) {
            binding.post = post
            binding.imagePost.load(post?.image)
            if (post?.isLiked == true) binding.btnLikes.setImageResource(R.drawable.round_favorite_24) else binding.btnLikes.setImageResource(
                R.drawable.round_favorite_border_24
            )
            binding.imagePost.setOnClickListener { post?.let { it1 -> clickCallback(it1) } }
            binding.btnLikes.setOnClickListener {
                if (post?.isLiked == true) doDislike(
                    post,
                    likeCallback
                ) else post?.let { it1 -> doLike(it1, likeCallback) }
            }
            binding.btnComments.setOnClickListener { post?.let { it1 -> clickCallback(it1) } }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemPostBinding =
            ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.context, clickCallback, likeCallback)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem == newItem
        }
    }
}