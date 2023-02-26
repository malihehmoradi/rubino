package com.example.rubinoapp.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConvertor {

    @TypeConverter
    fun convertCommentsToJSON(comments: List<Post.Comment>): String {
        return Gson().toJson(comments)
    }

    @TypeConverter
    fun convertJSONToComments(json: String): List<Post.Comment> {
        return Gson().fromJson(json, object : TypeToken<List<Post.Comment>>() {}.type)
    }
}