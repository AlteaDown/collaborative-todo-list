package com.example.demo.enums

import kotlinx.serialization.Serializable

@Serializable
enum class EventType {
  ADD, UPDATE, UPSERT, REMOVE
}
