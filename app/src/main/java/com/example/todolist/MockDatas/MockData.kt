package com.example.todolist.MockDatas

import com.example.todolist.model.ToDoStatus
import com.example.todolist.model.TodoDetails

class MockTodoData {

    companion object {
        private val data = mutableListOf(
            TodoDetails(1.0, "Task 1", details = "Details for task 1", status = ToDoStatus.DONE),
            TodoDetails(2.0, "Task 2", details = "Details for task 2", status = ToDoStatus.NEW),
            TodoDetails(3.0, "Task 3", details = "Details for task 3", status = ToDoStatus.DONE),
            TodoDetails(4.0, "Task 4", details = "Details for task 4", status = ToDoStatus.NEW),
            TodoDetails(5.0, "Task 5", details = "Details for task 5", status = ToDoStatus.DONE),
            TodoDetails(6.0, "Task 6", details = "Details for task 6", status = ToDoStatus.DONE),
            TodoDetails(7.0, "Task 7", details = "Details for task 7", status = ToDoStatus.NEW),
            TodoDetails(8.0, "Task 8", details = "Details for task 8", status = ToDoStatus.DONE),
            TodoDetails(9.0, "Task 9", details = "Details for task 9", status = ToDoStatus.NEW),
            TodoDetails(10.0, "Task 10", details = "Details for task 10", status = ToDoStatus.NEW)
        )

        fun populateData(): List<TodoDetails> = data

        fun updateStatus(id: Double, newStatus: ToDoStatus) {
            val index = data.indexOfFirst { it.id == id }
            if (index != -1) {
                data[index] = data[index].copy(status = newStatus)
            }
        }
        fun addTodo(todo: TodoDetails) {
            data.add(todo)
        }
    }
}