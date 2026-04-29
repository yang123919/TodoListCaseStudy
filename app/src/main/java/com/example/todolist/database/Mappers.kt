package com.example.todolist.database

import com.example.todolist.model.TodoDetails

fun TodoEntity.toTodoDetails(): TodoDetails = TodoDetails(
    id       = this.id.toDouble(),
    title    = this.title,
    meaning  = this.meaning,
    synonyms = this.synonyms,
    details  = this.details,
    status   = this.status,
    date     = this.date
)

fun TodoDetails.toTodoEntity(): TodoEntity = TodoEntity(
    id       = this.id.toInt(),
    title    = this.title,
    meaning  = this.meaning,
    synonyms = this.synonyms,
    details  = this.details,
    status   = this.status,
    date     = this.date
)