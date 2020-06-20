package com.bitbreeds.crypto.wrapper

import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get

/*
 * Copyright (c) Jonas Waage 18/06/2020
 *
 * This is a port of https://github.com/auditdrivencrypto/private-box to kotlin
 * It uses libsodium directly instead of chloride.
 *
 * It currently has a hardcoded max recipients of 10.
 *
 */

/**
 * Publicly available keys of recipients of message
 */
data class Recipient(val key : Uint8Array)

/**
 * Current maximum of recipients
 */
const val maxRecipients = 10

/**
 *
 * @param msg The payload to encrypt and send
 * @param recipients recipients (public keys) that can read this message
 *
 * @return the payload to send.
 */
fun Sodium.createMultiBox(msg : String, recipients : List<Recipient>) : Uint8Array {
    if(recipients.size > maxRecipients) {
        throw IllegalArgumentException("Too many recipients ${recipients.size}")
    }
    val nonce = this.randombytes_buf(this.crypto_secretbox_NONCEBYTES);
    val key = this.randombytes_buf(32);
    val onetime = this.crypto_box_keypair()

    key.buffer.byteLength
    val lengthAndKey = Uint8Array(key.buffer.byteLength+1)
    lengthAndKey.set(Array(1) {recipients.size.toByte()},0)
    lengthAndKey.set(key,1)

    val boxed = recipients.map { receipient ->
        val mult = this.crypto_scalarmult(onetime.privateKey,receipient.key)
        this.crypto_secretbox_easy(lengthAndKey,nonce,mult)
    }

    val joined = concatBuffers(boxed)
    val msgBytes = this.from_string(msg)
    val boxMsg = this.crypto_secretbox_easy(msgBytes,nonce,key)
    return concatBuffers(
        listOf(nonce, onetime.publicKey, joined, boxMsg)
    )
}


/**
 * @param box payload of box
 * @param secretKey user key needed to find key to open box
 *
 * @return a buffer with [0]=amount of keys this box was sent to, [1..size-1] single use key.
 */
fun Sodium.openMultiboxKey(box : Uint8Array, secretKey : Uint8Array) : Uint8Array? {
    val nonce = box.subarray(0, 24)
    val key = box.subarray(24, 24+32)
    val myKey = this.crypto_scalarmult(secretKey, key)

    val start = 24+32
    val size = 32+1+16
    for (i in 0 until maxRecipients) {
        val end = start + i*size + size
        if(end > box.length ) {
            return null;
        }
        val s = start + i*size;
        val sub = box.subarray(s,end)
        val lengthAndKey = this.silentOpenCryptoBox(sub, nonce, myKey)
        if(lengthAndKey != null) {
            return lengthAndKey
        }
    }
    return null
}

/**
 * @param cipherText The encrypted payload
 * @param nonce The used nonce
 * @param key key to open box
 *
 * @return the decrypted box or null
 */
fun Sodium.silentOpenCryptoBox(cipherText:Uint8Array, nonce : Uint8Array, key : Uint8Array) : Uint8Array? {
    return try {
        this.crypto_secretbox_open_easy(cipherText, nonce, key)
    }catch (e : Throwable) {
        null
    }
}

/**
 * @param box the box to open
 * @param secretKey key to open box
 *
 * @return the decrypted box or throws an error
 */
fun Sodium.openMultiBox(box : Uint8Array, secretKey : Uint8Array) : String? {
    val lgtAndKey = this.openMultiboxKey(box,secretKey)
    return if(lgtAndKey != null) {
        val key = lgtAndKey.subarray(1,lgtAndKey.length)
        val length = lgtAndKey[0]
        val start = 24+32
        val size = 32+1+16
        val nonce = box.subarray(0, 24)
        val encText = box.subarray(start+length*size,box.length)

        val decrypted = this.silentOpenCryptoBox(encText,nonce,key)
        if(decrypted != null) {
            this.to_string(decrypted)
        }
        else {
            null
        }
    }
    else {
        //Message not for us
        null;
    }
}

/**
 * @param buffers arrays to join
 *
 * @return buffer containing data of all given buffers
 */
fun concatBuffers(buffers:List<Uint8Array>) : Uint8Array {
    val size = buffers.map { i->i.length }.sum()
    val nu = Uint8Array(size)
    var offset = 0
    for (b in buffers) {
        nu.set(b,offset=offset)
        offset+=b.length
    }
    return nu
}