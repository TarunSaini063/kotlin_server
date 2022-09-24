package model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val userId = integer("userId").autoIncrement()
    val name = varchar("name", 255)
    val dateUpdated = long("dateUpdated")
    override val primaryKey: PrimaryKey = PrimaryKey(userId)
}

@Serializable
data class User(
    val userId: Int, val name: String, val dateUpdated: Long
)

@Serializable
data class NewUser(
    val userId: Int? = null,
    val name: String,
)