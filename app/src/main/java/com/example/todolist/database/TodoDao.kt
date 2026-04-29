package com.example.todolist.database

import androidx.room.*
import com.example.todolist.model.ToDoStatus

@Dao
interface TodoDao {

    @Query("SELECT * FROM todos ORDER BY date DESC")
    fun getAll(): List<TodoEntity>

    @Query("SELECT * FROM todos WHERE status = :status ORDER BY date DESC")
    fun getByStatus(status: ToDoStatus): List<TodoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(todo: TodoEntity)

    @Update
    fun update(todo: TodoEntity)

    @Query("UPDATE todos SET status = :status WHERE id = :id")
    fun updateStatus(id: Int, status: ToDoStatus)

    @Query("DELETE FROM todos WHERE id = :id")
    fun deleteById(id: Int)
}