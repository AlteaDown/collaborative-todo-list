package com.example.demo.enums

import com.example.demo.model.Todo

enum class TodoFilter {
  ANY, COMPLETED, PENDING;

  fun filter(todo: Todo) = when (this) {
    ANY -> true
    COMPLETED -> todo.isCompleted
    PENDING -> !todo.isCompleted
  }
}
