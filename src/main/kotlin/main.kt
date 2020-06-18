package com.bitbreeds.p2p.crypto

import com.bitbreeds.p2p.crypto.sodium.Recipient
import com.bitbreeds.p2p.crypto.sodium.createMultiBox
import com.bitbreeds.p2p.crypto.sodium.openMultiBox
import sodium.Sodium
import sodium.SodiumHandler
import sodium.SodiumWindow

/*
 * Copyright (c) Jonas Waage 17/06/2020
 */
fun main() {

    console.log("Start")

    SodiumWindow.sodium = object : SodiumHandler {
        override var onload: (Sodium) -> dynamic = { sodium ->
            console.log("Sodium load")
            val a = sodium.crypto_box_keypair()
            console.log(a.keyType)

            val key = sodium.from_hex("724b092810ec86d7e35c9d067702b31ef90bc43a7b598626749914d6a3e033ed")
            console.log(key)
            val nonce = sodium.randombytes_buf(sodium.crypto_secretbox_NONCEBYTES);
            console.log(nonce)

            val cipherTxt = sodium.crypto_secretbox_easy(sodium.from_string("Hei hei"),nonce,key)
            console.log(cipherTxt)

            val resp = sodium.crypto_secretbox_open_easy(cipherTxt,nonce,key)
            console.log(sodium.to_string(resp!!))

            val someOtherKeyPair = sodium.crypto_box_keypair()
            val keyPair = sodium.crypto_box_keypair()
            val data = sodium.createMultiBox("Wat Wat Wat", listOf(Recipient(keyPair.publicKey)))
            console.log("Encrypted $data")

            val out = sodium.openMultiBox(data,keyPair.privateKey)
            console.log("Decrypted $out")

            val out2 = sodium.openMultiBox(data,someOtherKeyPair.privateKey)
            console.log("Decrypted $out2")

        }
    }

    console.log("end")

}