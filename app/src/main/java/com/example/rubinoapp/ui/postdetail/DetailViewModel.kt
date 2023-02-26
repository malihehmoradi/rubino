package com.example.rubinoapp.ui.postdetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rubinoapp.data.database.Post
import com.example.rubinoapp.di.DBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val dbRepository: DBRepository) : ViewModel() {

    private val _selectedPost = MutableStateFlow<Post?>(null)
    val selectedPost = _selectedPost.asStateFlow()


    private val _allComments = MutableStateFlow(listOf<Post.Comment>())
    private val _comments = MutableStateFlow(listOf<Post.Comment>())
    val comments = _comments.asStateFlow()


    fun getSelectedPost(id: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val post = dbRepository.getPost(id)
                _selectedPost.emit(post)
                _allComments.emit(post.comments)
            }
        }
    }


    fun likePost(id: Int) {
        dbRepository.like(id)
//        _items.getAndUpdate { list ->
//            list.map { post ->
//                if (post.id == id) post.copy(isLiked = true) else post
//            }
//        }
    }

    fun disLikePost(id: Int) {
        dbRepository.disLike(id)
//        _items.getAndUpdate { list ->
//            list.map { post ->
//                if (post.id == id) post.copy(isLiked = false) else post
//            }
//        }
    }

    fun getTenMoreComment() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val list = mutableListOf<Post.Comment>()
                list.addAll(comments.value)
                repeat(10) { index ->
                    try {
                        if (comments.value.size < _allComments.value.size) {
                            list.add(_allComments.value[comments.value.size + index])
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                _comments.emit(list)
                Log.d("TAG", "getTenMoreComment: $_comments")
            }
        }
    }


}