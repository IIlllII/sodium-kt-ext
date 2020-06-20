package com.bitbreeds.p2p.wrapper

import com.bitbreeds.crypto.wrapper.Sodium
import com.bitbreeds.crypto.wrapper.SodiumHandler
import com.bitbreeds.crypto.wrapper.SodiumWindow
import com.bitbreeds.crypto.wrapper.Recipient
import com.bitbreeds.crypto.wrapper.createMultiBox
import com.bitbreeds.crypto.wrapper.openMultiBox

/*
 * Copyright (c) Jonas Waage 17/06/2020
 */
fun examplePrivateBox() {

    SodiumWindow.sodium = object :
        SodiumHandler {
        override var onload: (Sodium) -> dynamic = { sodium ->

            val keyPair = sodium.crypto_box_keypair()
            val data = sodium.createMultiBox("Wat Wat Wat!", listOf(
                Recipient(
                    keyPair.publicKey
                )
            ))
            console.log("Encrypted: $data")

            val out = sodium.openMultiBox(data,keyPair.privateKey)
            console.log("Decrypted: $out")

        }
    }
}