package user.routes

import base.BaseResponse
import base.DefaultResponse
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import user.model.CreateUserModel
import user.repository.UserRepository

fun Application.authRoutes(repository: UserRepository) {
    routing {
        route("/auth") {
            post("/register") {
                val params = call.receive<CreateUserModel>()
                when (val response = repository.registerUser(params)) {
                    is BaseResponse.Success -> {
                        call.respond(response.httpStatusCode, response.data)
                    }

                    is BaseResponse.Error -> {
                        call.respond(response.httpStatusCode, response)
                    }
                }
            }

        }
    }

}

