package com.example.rubinoapp.di

import com.example.rubinoapp.data.database.Post
import com.example.rubinoapp.data.database.PostDao
import com.example.rubinoapp.data.database.PostPagingSource
import javax.inject.Inject

class DBRepository @Inject constructor(private val postDao: PostDao) {

    fun postPagingSource() = PostPagingSource(postDao)
    fun insertPost(post: Post) = postDao.insetPost(post)
    fun getAllPosts() = postDao.getAll()
    fun getPostCount()=postDao.getCount()
    fun like(id:Int)=postDao.like(id)
    fun disLike(id: Int)=postDao.disLike(id)
    fun getPost(id:Int)=postDao.getPost(id)
}