package web

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import model.NewUser
import service.UserService

fun Route.user(userService: UserService) {

    route("/users") {

        get {
            call.respond(userService.getAllUsers())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalStateException("Must provide id")
            val user = userService.getUser(id)
            if (user == null) call.respond(HttpStatusCode.NotFound)
            else call.respond(user)
        }

        post("/addUser") {
            val user = call.receive<NewUser>()
            call.respond(HttpStatusCode.Created, userService.addUser(user))
        }

        put {
            val user = call.receive<NewUser>()
            val updated = userService.updateUser(user)
            if (updated == null) call.respond(HttpStatusCode.NotFound)
            else call.respond(HttpStatusCode.OK, updated)
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalStateException("Must provide id")
            val removed = userService.deleteUser(id)
            if (removed) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.NotFound)
        }

    }

    webSocket("/updates") {
        try {
            userService.addChangeListener(this.hashCode()) {
                sendSerialized(it)
            }
            for (frame in incoming) {
                if (frame.frameType == FrameType.CLOSE) {
                    break
                } else if (frame is Frame.Text) {
                    call.application.environment.log.info("Received websocket message: {}", frame.readText())
                }
            }
        } finally {
            userService.removeChangeListener(this.hashCode())
        }
    }
}
