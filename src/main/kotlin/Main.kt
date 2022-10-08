import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.websocket.*
import db.DatabaseFactory
import security.configureSecurity
import user.repository.UserRepositoryImpl
import user.routes.authRoutes
import user.routes.userRoutes
import user.service.UserService
import util.JsonMapper

fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(JsonMapper.defaultMapper)
    }

    install(ContentNegotiation) {
        json(JsonMapper.defaultMapper)
    }
    configureSecurity()
    DatabaseFactory.connectAndMigrate()
    val userService = UserService()
    val userRepositoryImpl = UserRepositoryImpl(userService)
    authRoutes(userRepositoryImpl)
    userRoutes(userRepositoryImpl)

}

fun main(args: Array<String>) {
    embeddedServer(Netty, commandLineEnvironment(args)).start(wait = true)
}