package com.example.todolist.database

import androidx.room.TypeConverter
import com.example.todolist.model.ToDoStatus
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time

    @TypeConverter
    fun fromStatus(value: String?): ToDoStatus? = value?.let { ToDoStatus.valueOf(it) }

    @TypeConverter
    fun statusToString(status: ToDoStatus?): String? = status?.name
}