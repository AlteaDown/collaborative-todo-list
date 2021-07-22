package com.example.demo.ui.components

import com.example.demo.enums.TodoFilter
import com.example.demo.utils.pluralize
import kotlinx.html.LI
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*

@OptIn(ExperimentalJsExport::class)
@JsExport
class TodoBar : RComponent<Props, RState>() {

  override fun RBuilder.render() {
    footer("footer") {
      span("todo-count") {
        strong { +props.pendingCount.toString() }
        +" "
        +"item left".pluralize(props.pendingCount)
      }

      ul(classes = "filters") {
        li {
          filterItem(TodoFilter.ANY, "All")
        }
        span {}

        li {
          filterItem(TodoFilter.PENDING, "Active")
        }
        span {}
        li {
          filterItem(TodoFilter.COMPLETED, "Completed")
        }
        span {}
      }

      if (props.anyCompleted) {
        button(classes = "clear-completed") {
          +"Clear completed"
          attrs.onClickFunction = { props.clearCompleted() }
        }
      }
    }
  }

  private fun RDOMBuilder<LI>.filterItem(filter: TodoFilter, text: String) {
    val classes = if (props.currentFilter == filter) "selected" else ""

    a(classes = classes) {
      +text
      attrs.onClickFunction = { props.updateFilter(filter) }
    }
  }


}

@OptIn(ExperimentalJsExport::class)
@JsExport
class Props(
  var pendingCount: Int,
  var anyCompleted: Boolean,
  var clearCompleted: () -> Unit,
  var updateFilter: (TodoFilter) -> Unit,
  var currentFilter: TodoFilter
) : RProps

fun RBuilder.todoBar(
  pendingCount: Int,
  anyCompleted: Boolean,
  clearCompleted: () -> Unit,
  currentFilter: TodoFilter,
  updateFilter: (TodoFilter) -> Unit
) = child(TodoBar::class) {
  attrs.pendingCount = pendingCount
  attrs.clearCompleted = clearCompleted
  attrs.anyCompleted = anyCompleted
  attrs.updateFilter = updateFilter
  attrs.currentFilter = currentFilter
}
