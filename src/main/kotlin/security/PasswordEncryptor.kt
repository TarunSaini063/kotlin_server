package security

import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object PasswordEncryptor {
    private const val SECRET_KEY: String = "123"
    private const val ALGORITHM: String = "HmacSHA1"
    private val HASH_KEY = hex(SECRET_KEY)
    private val HMAC_KEY = SecretKeySpec(HASH_KEY, ALGORITHM)
    private val hMac: Mac by lazy {
        val instance = Mac.getInstance(ALGORITHM)
        instance.init(HMAC_KEY)
        instance
    }

    fun hash(password: String): String = hex(hMac.doFinal(password.toByteArray(Charsets.UTF_8)))
}