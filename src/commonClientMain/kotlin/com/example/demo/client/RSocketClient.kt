package com.example.demo.client

import com.example.demo.model.Todo
import com.example.demo.model.TodoEvent
import io.ktor.utils.io.core.*
import io.rsocket.kotlin.ExperimentalMetadataApi
import io.rsocket.kotlin.RSocket
import io.rsocket.kotlin.metadata.CompositeMetadata
import io.rsocket.kotlin.metadata.RoutingMetadata
import io.rsocket.kotlin.metadata.metadata
import io.rsocket.kotlin.payload.buildPayload
import io.rsocket.kotlin.payload.data
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMetadataApi::class)
class RSocketClient(val rsocket: RSocket) : Client {

  override fun handleTodos(handler: (TodoEvent) -> Unit) {
    rsocket
        .requestStream(buildPayload {
          data(ByteReadPacket.Empty)
          metadata(CompositeMetadata(RoutingMetadata("todos")))
        })
        .map { Json.decodeFromString<TodoEvent>(it.data.readText()) }
        .onEach { handler(it) }
        .launchIn(MainScope())
  }

  override fun exchange(todo: List<Todo>) {
    MainScope().launch {
      todo.forEach {
        rsocket
            .fireAndForget(buildPayload {
              data(Json.encodeToString(it))
              metadata(CompositeMetadata(RoutingMetadata("todos.upsert")))
            })
      }
    }
  }

  override fun addTodo(todo: Todo) {
    MainScope().launch {
      rsocket
          .fireAndForget(buildPayload {
            data(Json.encodeToString(todo))
            metadata(CompositeMetadata(RoutingMetadata("todos.add")))
          })
    }
  }

  override fun updateTodo(todo: Todo) {
    MainScope().launch {
      rsocket
          .fireAndForget(buildPayload {
            data(Json.encodeToString(todo))
            metadata(CompositeMetadata(RoutingMetadata("todos.update")))
          })
    }
  }

  override fun removeTodo(todo: Todo) {
    MainScope().launch {
      rsocket.fireAndForget(buildPayload {
        data(Json.encodeToString(todo))
        metadata(CompositeMetadata(RoutingMetadata("todos.remove")))
      })
    }
  }

  companion object
}

expect suspend fun RSocketClient.Companion.create(): Client
