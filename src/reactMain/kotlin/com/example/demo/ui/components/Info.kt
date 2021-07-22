package com.example.demo.ui.components

import com.example.demo.utils.translate
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.a
import react.dom.footer
import react.dom.p

@OptIn(ExperimentalJsExport::class)
@JsExport
class Info : RComponent<RProps, RState>() {

  override fun RBuilder.render() {
    footer("info") {
      p { +"Double-click to edit a todo".translate() }
      p {
        +"Created by".translate()
        +" "
        a("https://venturus.org.br/") { +"venturus.org.br" }
      }
      p {
        +"Part of"
        +" "
        a("http://todomvc.com") { +"TodoMVC" }
      }
    }
  }
}

fun RBuilder.info() = child(Info::class) {}
