package db.migration

import model.Users
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class V1__create_widgets : BaseJavaMigration() {
    override fun migrate(context: Context?) {
        transaction {
            SchemaUtils.create(Users)

            Users.insert {
                it[name] = "UserOne"
                it[dateUpdated] = System.currentTimeMillis()
            }
            Users.insert {
                it[name] = "UserTwo"
                it[dateUpdated] = System.currentTimeMillis()
            }
        }
    }
}
