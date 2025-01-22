package com.dogeisdope.firefoxrelay.utils

import org.junit.Test

class CryptoTest {

    @ExperimentalUnsignedTypes
    fun ByteArray.toHex(): String =
        asUByteArray().joinToString("") { it.toString(radix = 16).padStart(2, '0') }

    @Test
    fun getCredentials() {
        val kwe = kwe(name = "quickStretch", email = "gn03312936@yahoo.com.tw")
        println(kwe.toHex())

        val quickStretch = pbkdf2(
            password = "Fo150505".toCharArray(),
            salt = kwe,
            iterationCount = 1000,
            keyLength = 32
        ).also { println(it.toHex()) }

        val kw = kw(name = "authPW")

        val authPW = deriveKey(
            ikm = quickStretch,
            salt = "".toByteArray(),
            info = kw,
            length = 32
        ).also { println(it.toHex()) }
    }
}