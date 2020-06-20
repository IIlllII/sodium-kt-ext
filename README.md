sodium-kt-ext
--------------------
Kotlin js definitions and extensions for [libsodium.js](https://github.com/jedisct1/libsodium.js)
Meant to be used with gradle based kotlin javascript projects.

### Use with Gradle
Add this to your dependencies:

```
dependencies {
    implementation("com.bitbreeds.crypto","kt-libsodium","1.0-SNAPSHOT")
}
```

Download the [sodium.js](https://github.com/jedisct1/libsodium.js/tree/master/dist/browsers)
file, and add it to your html like this.

```
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>

</body>
<script src="{YOUR_APPLICATION_NAME}.js"></script>
<script src="sodium.js" async></script>
</html>
```
Then to use libsodium, in the main method, do:
```
fun main() {

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
```


## Extensions

### Private multi box
An extension that allows creation of encrypted boxes with several recipients.

An example with 1 recipient:
```
fun main() {

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
```

## Development
The definitions for libsodium goes in ```libsodium.kt```, while
extensions that implement things on top of libsodium goes on their own
.kt file. See ```privatebox.kt```
