package com.bitbreeds.p2p.wrapper

import com.bitbreeds.crypto.wrapper.Sodium
import com.bitbreeds.crypto.wrapper.SodiumHandler
import com.bitbreeds.crypto.wrapper.SodiumWindow

/*
 * Copyright (c) Jonas Waage 17/06/2020
 */
fun exampleBox() {

    SodiumWindow.sodium = object :
        SodiumHandler {
        override var onload: (Sodium) -> dynamic = { sodium ->

            val key = sodium.from_hex("724b092810ec86d7e35c9d067702b31ef90bc43a7b598626749914d6a3e033ed")
            val nonce = sodium.randombytes_buf(sodium.crypto_secretbox_NONCEBYTES);
            val cipherTxt = sodium.crypto_secretbox_easy(sodium.from_string("Hei hei!"),nonce,key)
            console.log("Encrypted: $cipherTxt")

            val resp = sodium.crypto_secretbox_open_easy(cipherTxt,nonce,key)
            console.log("Decrypted: $resp")

        }
    }
}