package com.example.todolist.database

import com.example.todolist.model.ToDoStatus

class TodoRepository(private val dao: TodoDao) {

    fun getAll(): List<TodoEntity> = dao.getAll()

    fun getByStatus(status: ToDoStatus): List<TodoEntity> = dao.getByStatus(status)

    fun insert(todo: TodoEntity) = dao.insert(todo)

    fun update(todo: TodoEntity) = dao.update(todo)

    fun updateStatus(id: Int, status: ToDoStatus) = dao.updateStatus(id, status)

    fun deleteById(id: Int) = dao.deleteById(id)
}