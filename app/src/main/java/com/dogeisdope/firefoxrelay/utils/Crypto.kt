package com.dogeisdope.firefoxrelay.utils

import java.nio.charset.Charset
import java.security.spec.KeySpec
import javax.crypto.Mac
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

val NAMESPACE = "identity.mozilla.com/picl/v1/"


fun pbkdf2(password: CharArray, salt: ByteArray, iterationCount: Int, keyLength: Int): ByteArray {
    val keySpec: KeySpec = PBEKeySpec(password, salt, iterationCount, keyLength * 8)
    val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
    return secretKeyFactory.generateSecret(keySpec).encoded
}

fun kw(name: String): ByteArray {
    val combinedString = "$NAMESPACE$name"
    return combinedString.toByteArray(Charset.forName("UTF-8"))
}

fun kwe(name: String, email: String): ByteArray {
    val combinedString = "$NAMESPACE$name:$email"
    return combinedString.toByteArray(Charset.forName("UTF-8"))
}

fun deriveKey(ikm: ByteArray, salt: ByteArray, info: ByteArray, length: Int): ByteArray {
    val hmacKey = if (salt.isEmpty()) {
        val key = ikm.copyOf(ikm.size)
        key
    } else {
        salt
    }

    val hmac = Mac.getInstance("HmacSHA256")
    val keySpec = SecretKeySpec(hmacKey, "HmacSHA256")
    hmac.init(keySpec)

    val hashLength = 32 // HMAC-SHA256 output length
    val numBlocks = (length + hashLength - 1) / hashLength
    var prev: ByteArray = byteArrayOf()
    var output = ByteArray(0)

    for (i in 0 until numBlocks) {
        val hmac2 = Mac.getInstance("HmacSHA256")
        hmac2.init(SecretKeySpec(hmac.doFinal(prev), "HmacSHA256"))

        val input = prev + info + byteArrayOf((i + 1).toByte())

        val tempOutput = hmac2.doFinal(input)
        val minLen = minOf(hashLength, length - output.size)
        output += tempOutput.copyOf(minLen)

        prev = tempOutput
    }

    return output
}

