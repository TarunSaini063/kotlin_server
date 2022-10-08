package user.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table


object UserTable : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 255)
    val createdAt = long("createdAt")
    val email = varchar("email", 1024)
    val password = text("password")
    val avatar = text("avatar")
    override val primaryKey = PrimaryKey(id)
}

@Serializable
class User(
    val id: Int,
    val name: String,
    val email: String,
    val avatar: String,
    val createdAt: Long,
    var authToken: String? = null
)