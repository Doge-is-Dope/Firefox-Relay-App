package com.dogeisdope.firefoxrelay.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateAddressData(
    @SerialName("description")
    val description: String = "",
    @SerialName("enabled")
    val enabled: Boolean = true,
    @SerialName("generated_for")
    val generatedFor: String = "",
    @SerialName("used_on")
    val usedOn: String = ""
)