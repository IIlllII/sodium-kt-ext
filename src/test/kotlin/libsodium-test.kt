package com.bitbreeds.p2p.wrapper

import com.bitbreeds.p2p.crypto.*
import sodium.Sodium
import sodium.SodiumHandler
import sodium.SodiumWindow
import kotlin.browser.window
import kotlin.test.Test
import kotlin.test.assertEquals

/*
 * Copyright (c) Jonas Waage 09/06/2020
 */

class SodiumTest {

    @Test
    fun testApi() {

        SodiumWindow.sodium = object :SodiumHandler {
            override var onload: (Sodium) -> dynamic = { sodium ->
                val a = sodium.crypto_box_keypair()
                console.log(a.keyType)
            }
        }




    }


}