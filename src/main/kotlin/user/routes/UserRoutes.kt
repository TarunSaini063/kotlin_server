package user.routes

import base.BaseResponse
import base.DefaultResponse
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import user.repository.UserRepository

fun Application.userRoutes(repository: UserRepository) {
    routing {
        authenticate {
            get("/alluser") {
                when (val response = repository.getAllUsers()) {
                    is BaseResponse.Success -> {
                        call.respond(response.statusCode, response.data)
                    }

                    is BaseResponse.Error -> {
                        call.respond(response.statusCode, response)
                    }
                }
            }
            get("/find") {
                val email = call.receiveParameters()["email"] ?: throw IllegalStateException("Must provide email")
                when (val response = repository.findUserByEmail(email)) {
                    is BaseResponse.Success -> {
                        call.respond(response.statusCode, response.data ?: DefaultResponse)
                    }

                    is BaseResponse.Error -> {
                        call.respond(response.statusCode, response)
                    }
                }
            }
        }
    }
}