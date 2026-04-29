package com.example.todolist.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todolist.model.ToDoStatus
import java.util.Date

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val meaning: String? = null,
    val synonyms: String? = null,
    val details: String,
    val status: ToDoStatus = ToDoStatus.NEW,
    val date: Date? = null
)