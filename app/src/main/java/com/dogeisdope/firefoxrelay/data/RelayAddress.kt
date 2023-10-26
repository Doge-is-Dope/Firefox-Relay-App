package com.dogeisdope.firefoxrelay.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
data class RelayAddress(
    @SerialName("id")
    val id: Long,
    @SerialName("address")
    val address: String,
    @SerialName("full_address")
    val fullAddress: String,
    @SerialName("description")
    val description: String,
    @SerialName("enabled")
    val enabled: Boolean,
    @SerialName("generated_for")
    val generatedFor: String,
    @SerialName("last_modified_at")
    @Serializable(with = DateSerializer::class)
    val lastModifiedAt: ZonedDateTime,
    @SerialName("created_at")
    @Serializable(with = DateSerializer::class)
    val createdAt:  ZonedDateTime
)