package com.example.rubinoapp.data.database

import androidx.room.*

@Entity(tableName = "post")
data class Post(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "caption") val caption: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "isLiked") var isLiked: Boolean,
    @ColumnInfo(name = "commentsCount") val commentsCount: Int,
    @TypeConverters(TypeConvertor::class)
    @ColumnInfo(name = "comments") val comments: List<Comment>,
    @ColumnInfo(name = "likesCount") val likesCount: Int
) {
    data class Comment(
        val id: Int,
        val author: String,
        val message: String
    )
}
