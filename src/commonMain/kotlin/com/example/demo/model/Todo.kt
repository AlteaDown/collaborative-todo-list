package com.example.demo.model

import com.benasher44.uuid.uuid4
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantIso8601Serializer
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class Todo(
  val id: String = uuid4().toString(),
  val title: String,
  val isCompleted: Boolean = false,
  @Serializable(InstantIso8601Serializer::class) val createdAt: Instant = Clock.System.now(),
  @Serializable(InstantIso8601Serializer::class) val updatedAt: Instant = Clock.System.now(),
  @Serializable(InstantIso8601Serializer::class) val deletedAt: Instant? = null
) {
  fun isDeleted() = deletedAt != null
}

