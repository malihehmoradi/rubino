package com.example.rubinoapp.data.database

import androidx.room.*

@Dao
interface PostDao {

    @Query("SELECT * FROM post ORDER BY id ASC LIMIT :limit OFFSET :offset")
    fun getPagedList(limit: Int, offset: Int): List<Post>

    @Query("SELECT * FROM post")
    fun getAll(): List<Post>

    @Query("SELECT COUNT(id) FROM post")
    fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insetPost(post: Post)

    @Query("UPDATE post SET isLiked = :isLiked WHERE id = :id")
    fun like(id: Int, isLiked: Boolean = true)

    @Query("UPDATE post SET isLiked = :isLiked WHERE id = :id")
    fun disLike(id: Int, isLiked: Boolean = false)

    @Query("SELECT * FROM post WHERE id = :id")
    fun getPost(id: Int): Post
}