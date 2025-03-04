package com.example.demo.service

import com.example.demo.enums.EventType.*
import com.example.demo.model.Todo
import com.example.demo.model.TodoEvent
import com.example.demo.repository.TodoRepository
import com.example.demo.service.TodoService.CheckResult.*

class TodoService(private val todoRepository: TodoRepository) {
  fun handleEvent(todoEvent: TodoEvent) {
    when (todoEvent.type) {
      ADD -> todoRepository.save(todoEvent.todo)
      UPDATE -> todoRepository.update(todoEvent.todo)
      REMOVE -> todoRepository.remove(todoEvent.todo)
      UPSERT -> when (check(todoEvent.todo)) {
        NEW -> todoRepository.save(todoEvent.todo)
        CHANGED -> todoRepository.update(todoEvent.todo)
        REMOVED -> todoRepository.remove(todoEvent.todo)
        SAME -> {
          /* Do Nothing */
        }
      }
    }
  }

  fun listTodos(): List<Todo> = todoRepository.all()

  private fun check(todo: Todo): CheckResult {
    return todoRepository.get(todo.id)
        ?.let { existingTodo ->
          when {
            existingTodo == todo -> SAME
            existingTodo.isDeleted() -> REMOVED
            todo.isDeleted() -> REMOVED
            else -> CHANGED
          }
        }
        ?: if (todo.isDeleted()) REMOVED else NEW
  }

  enum class CheckResult {
    SAME, CHANGED, REMOVED, NEW
  }
}
