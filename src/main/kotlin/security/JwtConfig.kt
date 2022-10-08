package security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.StringJoiner


class JwtConfig private constructor(
    private val secret: String,
    private val issuer: String,
    private val audience: String,
    private val claim: String
) {
    private val algorithm = Algorithm.HMAC256(secret)
    val verifier: JWTVerifier = JWT.require(algorithm).withIssuer(issuer).withAudience(audience).build()
    fun createAccessToken(id: Int): String =
        JWT.create().withIssuer(issuer).withAudience(audience).withClaim(claim, id).sign(algorithm)

    companion object {
        lateinit var instance: JwtConfig
            private set
        const val CLAIM = "ID"
        fun initialize(
            secret: String,
            issuer: String,
            audience: String,
        ) {
            synchronized(this) {
                if (!this::instance.isInitialized) {
                    instance = JwtConfig(secret, issuer, audience, CLAIM)
                }
            }
        }
    }
}