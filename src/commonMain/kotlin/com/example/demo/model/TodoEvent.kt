package com.example.demo.model

import com.example.demo.enums.EventType
import kotlinx.serialization.Serializable

@Serializable
data class TodoEvent(val type: EventType, val todo: Todo)
