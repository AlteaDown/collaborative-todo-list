package com.example.demo.ui.app

import com.example.demo.client.TodoSocketClient
import com.example.demo.enums.EventType
import com.example.demo.enums.TodoFilter
import com.example.demo.enums.TodoFilter.*
import com.example.demo.model.Todo
import com.example.demo.model.TodoEvent
import com.example.demo.service.TodoService
import com.example.demo.ui.components.headerInput
import com.example.demo.ui.components.info
import com.example.demo.ui.components.todoBar
import com.example.demo.ui.components.todoList
import com.example.demo.utils.translate
import io.rsocket.kotlin.ExperimentalMetadataApi
import kotlinx.browser.document
import kotlinx.datetime.Clock
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.title
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.attrs
import react.dom.input
import react.dom.label
import react.dom.section

object AppOptions {
  var language = "no-language"
  var localStorageKey = "todos-koltin-react"
}

/**
 * The React Component State
 */
external interface AppState : RState {
  var todos: List<Todo>
}

/**
 * The React Component Props
 */
external interface AppProps : RProps {
  var route: String
  var todoSocketClient: TodoSocketClient
  var service: TodoService
}

@OptIn(ExperimentalJsExport::class)
@JsExport
@ExperimentalMetadataApi
class App(props: AppProps) : RComponent<AppProps, AppState>(props) {

  override fun AppState.init(props: AppProps) {
    console.log("componentWillReceiveProps $props")

    props.todoSocketClient.handleTodos {
      props.service.handleEvent(it)

      setState {
        todos = props.service.listTodos()
      }
    }
  }

  override fun componentWillMount() {
    console.log("component will mount com.example.demo.ui.app")

    val listTodos = props.service.listTodos()

    props.todoSocketClient.exchange(listTodos)

    setState {
      todos = listTodos
    }
  }

  /**
   * This is the render function in react.
   */
  override fun RBuilder.render() {

    val currentFilter = when (props.route) {
      "pending" -> PENDING
      "completed" -> COMPLETED
      else -> ANY
    }

    section("todoapp") {
      headerInput(::createTodo)

      if (state.todos.isNotEmpty()) {

        val allChecked = isAllCompleted()

        section("main") {
          input(InputType.checkBox, classes = "toggle-all") {
            attrs {
              id = "toggle-all"
              checked = allChecked

              onChangeFunction = { event ->
                val isChecked = (event.currentTarget as HTMLInputElement).checked

                setAllStatus(isChecked)
              }
            }
          }
          label {
            attrs["htmlFor"] = "toggle-all"
            attrs.title = "Mark all as complete".translate()
          }

          todoList(::removeTodo, ::updateTodo, state.todos, currentFilter)
        }

        todoBar(
          pendingCount = countPending(),
          anyCompleted = state.todos.any { todo -> todo.isCompleted },
          clearCompleted = ::clearCompleted,
          currentFilter = currentFilter,
          updateFilter = ::updateFilter
        )
      }

    }
    info()
  }

  private fun updateFilter(newFilter: TodoFilter) {
    document.location!!.href = "#?route=${newFilter.name.lowercase()}"
  }

  private fun countPending() = pendingTodos().size

  private fun removeTodo(todo: Todo) {
    console.log("removeTodo [${todo.id}] ${todo.title}")
    props.todoSocketClient.removeTodo(todo)

    props.service.handleEvent(TodoEvent(EventType.REMOVE, todo))

    setState {
      todos = props.service.listTodos()
    }
  }

  private fun createTodo(todo: Todo) {
    console.log("createTodo [${todo.id}] ${todo.title}")

    props.todoSocketClient.addTodo(todo)
    props.service.handleEvent(TodoEvent(EventType.ADD, todo))

    setState {
      todos = props.service.listTodos()
    }
  }

  private fun updateTodo(todo: Todo) {
    console.log("updateTodo [${todo.id}] ${todo.title}")

    props.todoSocketClient.updateTodo(todo)

    props.service.handleEvent(TodoEvent(EventType.UPDATE, todo))

    setState {
      todos = props.service.listTodos()
    }
  }

  private fun setAllStatus(newStatus: Boolean) {
    state.todos.forEach { todo -> updateTodo(todo.copy(isCompleted = newStatus)) }
  }

  private fun clearCompleted() {
    state.todos
        .filter { it.isCompleted }
        .forEach { todo -> removeTodo(todo.copy(deletedAt = Clock.System.now())) }
  }

  private fun isAllCompleted(): Boolean = state.todos.all { it.isCompleted }

  private fun pendingTodos() = state.todos.filter { todo -> !todo.isCompleted }
}

fun RBuilder.app(route: String, todoSocketClient: TodoSocketClient, service: TodoService) = child(App::class) {
  attrs.route = route
  attrs.todoSocketClient = todoSocketClient
  attrs.service = service
}
