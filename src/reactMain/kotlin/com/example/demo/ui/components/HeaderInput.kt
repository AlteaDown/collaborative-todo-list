package com.example.demo.ui.components

import com.example.demo.model.Todo
import com.example.demo.utils.Keys
import com.example.demo.utils.translate
import com.example.demo.utils.value
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onKeyDownFunction
import react.*
import react.dom.attrs
import react.dom.h1
import react.dom.header
import react.dom.input

@OptIn(ExperimentalJsExport::class)
@JsExport
class HeaderInput : RComponent<HeaderInput.Props, HeaderInput.State>() {

  override fun componentWillMount() {
    setState {
      title = ""
    }
  }

  override fun RBuilder.render() {
    header(classes = "header") {
      h1 {
        +"todos".translate()
      }
      input(classes = "new-todo", type = InputType.text) {
        attrs {
          autoFocus = true
          placeholder = "What needs to be done?".translate()
          value = state.title

          onChangeFunction = { event ->
            val newValue = event.value

            setState {
              title = newValue
            }
          }

          onKeyDownFunction = { keyEvent ->
            val key = Keys.fromString(keyEvent.asDynamic().key as String)

            if (key == Keys.Enter) {
              if (state.title.isNotBlank()) {
                props.create(Todo(title = state.title.trim()))
              }

              setState {
                title = ""
              }
            }
          }
        }
      }
    }
  }

  class Props(var create: (Todo) -> Unit) : RProps
  class State(var title: String) : RState
}

fun RBuilder.headerInput(create: (Todo) -> Unit) = child(HeaderInput::class) {
  attrs.create = create
}
