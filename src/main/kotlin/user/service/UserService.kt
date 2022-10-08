package user.service

import user.model.CreateUserModel
import user.model.User
import user.model.UserTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.InsertStatement
import security.PasswordEncryptor
import db.DatabaseFactory
import org.jetbrains.exposed.sql.selectAll

class UserService {
    suspend fun registerUser(params: CreateUserModel): User? {
        var statement: InsertStatement<Number>? = null
        DatabaseFactory.dbExec {
            statement = UserTable.insert {
                it[name] = params.name
                it[email] = params.email
                it[avatar] = params.avatar
                it[createdAt] = System.currentTimeMillis()
                it[password] = PasswordEncryptor.hash(params.password)
            }
        }
        return rowToUser(statement?.resultedValues?.get(0))
    }

    suspend fun findUserByEmail(email: String): User? {
        return DatabaseFactory.dbExec {
            UserTable.select { UserTable.email.eq(email) }.map { rowToUser(it) }.singleOrNull()
        }
    }

    suspend fun getAllUsers(): List<User> {
        return DatabaseFactory.dbExec {
            return@dbExec UserTable.selectAll().mapNotNull { rowToUser(it) }
        }
    }

    private fun rowToUser(row: ResultRow?): User? {
        row ?: return null
        return User(
            id = row[UserTable.id],
            name = row[UserTable.name],
            createdAt = row[UserTable.createdAt],
            email = row[UserTable.email],
            avatar = row[UserTable.avatar]
        )
    }
}