package com.bitbreeds.p2p.wrapper

import com.bitbreeds.crypto.wrapper.Sodium
import com.bitbreeds.crypto.wrapper.SodiumHandler
import com.bitbreeds.crypto.wrapper.SodiumWindow
import org.w3c.dom.HTMLDivElement
import kotlin.browser.document
import kotlin.browser.window
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.expect

/*
 * Copyright (c) Jonas Waage 09/06/2020
 */

class SodiumTest {

    @Test
    fun testApi() {

        SodiumWindow.sodium = object :
            SodiumHandler {
            override var onload: (Sodium) -> dynamic = { sodium ->
                val a = sodium.crypto_box_keypair()
                assertNotNull(a.keyType)
                assertEquals(a.keyType,"gjhgjk")
            }
        }


    }


}