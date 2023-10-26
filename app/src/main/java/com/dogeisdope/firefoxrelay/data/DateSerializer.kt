package com.dogeisdope.firefoxrelay.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.ZonedDateTime

object DateSerializer : KSerializer< ZonedDateTime> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("DateSerializer", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ZonedDateTime) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder):  ZonedDateTime {
        val string = decoder.decodeString()
        return ZonedDateTime.parse(string)
    }
}