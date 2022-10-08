package db.migration

import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import user.model.UserTable

class V1__create_widgets : BaseJavaMigration() {
    override fun migrate(context: Context?) {
        transaction {
            SchemaUtils.create(UserTable)

            UserTable.insert {
                it[name] = "UserOne"
                it[createdAt] = System.currentTimeMillis()
            }
            UserTable.insert {
                it[name] = "UserTwo"
                it[createdAt] = System.currentTimeMillis()
            }
        }
    }
}
