package com.example.rubinoapp.ui.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.rubinoapp.data.database.Post
import com.example.rubinoapp.di.DBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val ITEMS_PER_PAGE = 10

@HiltViewModel
class PostViewModel @Inject constructor(private val dbRepository: DBRepository) : ViewModel() {


    private val _postsCount = MutableStateFlow(0)
    val postsCount = _postsCount.asStateFlow()


    private val _items = MutableStateFlow<PagingData<Post>>(PagingData.empty())
    val items = _items.asStateFlow()



    private fun getPostCount() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val count = dbRepository.getPostCount()
                _postsCount.emit(count)
            }
        }
    }

    init {
        getPostCount()
        getPosts()
    }

    private fun getPosts() {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(
                    pageSize = ITEMS_PER_PAGE,
                    enablePlaceholders = false,
                    initialLoadSize = 10
                ),
                pagingSourceFactory = { dbRepository.postPagingSource() }
            ).flow.cachedIn(viewModelScope).collectLatest { list ->
                _items.emit(list)
            }

        }
    }

    fun saveFakePosts(list: List<Post>) {
        list.forEach {
            dbRepository.insertPost(it)
        }
    }

    fun likePost(id: Int) {
        dbRepository.like(id)
        _items.getAndUpdate { list ->
            list.map { post ->
                if (post.id == id) post.copy(isLiked = true) else post
            }
        }
    }

    fun disLikePost(id: Int) {
        dbRepository.disLike(id)
        _items.getAndUpdate { list ->
            list.map { post ->
                if (post.id == id) post.copy(isLiked = false) else post
            }
        }
    }


}