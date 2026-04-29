package com.example.todolist.model

import java.io.Serializable
import java.util.Date

data class TodoDetails(
    val id: Double,
    val title:String,
    val meaning:String? = null,
    val synonyms:String? = null,
    val details: String,
    val status:ToDoStatus,
    val date: Date? = null
) : Serializable

enum class ToDoStatus {
    NEW,
    DONE
}