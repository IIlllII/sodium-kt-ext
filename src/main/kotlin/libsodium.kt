package com.bitbreeds.crypto.wrapper

import org.khronos.webgl.Uint8Array
import org.w3c.dom.Window

/*
 * Copyright (c) Jonas Waage 09/06/2020
 *
 * Kotlin definitions for libsodium
 *
 */

@JsName("window")
abstract external class SodiumWindow : Window {
    companion object {
        var sodium: SodiumHandler = definedExternally
    }
}

interface SodiumHandler {
    var onload : (Sodium) -> dynamic
}

external interface Sodium {
    fun crypto_box_keypair() : Curve25519
    fun from_hex(hexString : String) : Uint8Array
    fun to_hex(uint8Array: Uint8Array) : String
    fun from_string(data : String) : Uint8Array
    fun to_string(data : Uint8Array) : String
    fun randombytes_buf(hmm : dynamic) : Uint8Array
    val crypto_secretbox_NONCEBYTES : dynamic
    fun crypto_secretbox_easy(msg : dynamic ,nonce : Uint8Array, key : Uint8Array) : Uint8Array
    fun crypto_secretbox_open_easy(cipherText : Uint8Array,nonce :Uint8Array, key: Uint8Array) : Uint8Array?
    fun crypto_scalarmult(keyA : Uint8Array,keyB : Uint8Array) : Uint8Array
}

data class Curve25519(
    val keyType : String,
    val privateKey : Uint8Array,
    val publicKey : Uint8Array
)

