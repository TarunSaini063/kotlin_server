package service

import model.*
import org.jetbrains.exposed.sql.*

class UserService {
    private val listeners = mutableMapOf<Int, suspend (UserNotification) -> Unit>()
    fun addChangeListener(id: Int, listener: suspend (UserNotification) -> Unit) {
        listeners[id] = listener
    }

    fun removeChangeListener(id: Int) = listeners.remove(id)
    private suspend fun onChange(type: ChangeType, id: Int, entity: User? = null) {
        listeners.values.forEach {
            it.invoke(Notification(type, id, entity))
        }
    }

    suspend fun getAllUsers(): List<User> = DatabaseFactory.dbExec {
        Users.selectAll().map { toUser(it) }
    }

    private fun toUser(resultRow: ResultRow): User {
        return User(
            userId = resultRow[Users.userId],
            name = resultRow[Users.name],
            dateUpdated = resultRow[Users.dateUpdated],
        )
    }

    suspend fun deleteUser(id: Int): Boolean {
        return DatabaseFactory.dbExec {
            Users.deleteWhere { Users.userId eq id } > 0
        }.also {
            if (it) onChange(ChangeType.DELETE, id)
        }
    }

    suspend fun addUser(user: NewUser): User {
        var key = 0
        DatabaseFactory.dbExec {

            key = (Users.insert {
                it[name] = user.name
                it[dateUpdated] = System.currentTimeMillis()
            } get Users.userId)
        }
        return getUser(key)!!.also {
            onChange(ChangeType.CREATE, key, it)
        }
    }

    suspend fun updateUser(user: NewUser): User? {
        val id = user.userId
        return if (id == null) {
            addUser(user)
        } else run {
            DatabaseFactory.dbExec {
                Users.update({ Users.userId eq id }) {
                    it[name] = user.name
                    it[dateUpdated] = System.currentTimeMillis()
                }
            }
            getUser(id).also {
                onChange(ChangeType.UPDATE, id, it)
            }
        }
    }

    suspend fun getUser(id: Int): User? = DatabaseFactory.dbExec {
        Users.select {
            (Users.userId eq id)
        }.map { toUser(it) }.singleOrNull()
    }
}