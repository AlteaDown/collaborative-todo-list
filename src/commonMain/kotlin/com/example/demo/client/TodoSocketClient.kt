package com.example.demo.client

import com.example.demo.model.Todo
import com.example.demo.model.TodoEvent

interface TodoSocketClient {
  fun handleTodos(handler: (TodoEvent) -> Unit)
  fun exchange(todo: List<Todo>)
  fun addTodo(todo: Todo)
  fun updateTodo(todo: Todo)
  fun removeTodo(todo: Todo)
}
