package security

import base.BaseResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureSecurity() {
    val secret = environment.config.propertyOrNull("jwt.secret")?.getString()!!
    val issuer = environment.config.propertyOrNull("jwt.issuer")?.getString()!!
    val audience = environment.config.propertyOrNull("jwt.audience")?.getString()!!
    JwtConfig.initialize(secret, issuer, audience)
    install(Authentication) {
        jwt {
            verifier(JwtConfig.instance.verifier)
            validate {
                val claim = it.payload.getClaim(JwtConfig.CLAIM).asInt()
                if (claim != null) {
                    UserIdPrincipalForUser(claim)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, BaseResponse.Error<Unit>(message = "Invalid auth token"))
            }
        }
    }
}