package com.khun.movievalut.ui.util

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

val SecretKey = "AABBCCDD12345EEFF445"
fun encryptPassword(secretKey: String, password: String): String {
    val md = MessageDigest.getInstance("SHA-256")
    val bytes = password.toByteArray(StandardCharsets.UTF_8)
    md.update(bytes, 0, bytes.size)
    val key = md.digest()
    val secretKeySpec = SecretKeySpec(key, "AES")
    val c = Cipher.getInstance("AES")
    c.init(Cipher.ENCRYPT_MODE, secretKeySpec)
    val encVal = c.doFinal(secretKey.toByteArray())

    return Base64.encodeToString(encVal, Base64.DEFAULT)
}