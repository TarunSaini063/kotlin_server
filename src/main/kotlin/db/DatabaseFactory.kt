package db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import user.model.UserTable
import javax.sql.DataSource

object DatabaseFactory {

    private val log = LoggerFactory.getLogger(this::class.java)

    fun connectAndMigrate() {
        log.info("Initialising database")
        val pool = hikari()
        Database.connect(pool)
        transaction {
            SchemaUtils.create(UserTable)
        }
        runFlyway(pool)
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig().apply {
            driverClassName = "com.mysql.cj.jdbc.Driver"
            jdbcUrl = "jdbc:mysql://localhost:3306/test"
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            password = "Tarun@5896"
            username = "root"
            validate()
        }
        return HikariDataSource(config)
    }

    private fun runFlyway(datasource: DataSource) {
        val flyway = Flyway.configure().apply {
            defaultSchema("defaultUserSchema")
        }.dataSource(datasource).load()
        try {
            flyway.info()
            flyway.migrate()
        } catch (e: Exception) {
            log.error("Exception running flyway migration", e)
            throw e
        }
        log.info("Flyway migration has finished")
    }

    suspend fun <T> dbExec(
        block: () -> T
    ): T = withContext(Dispatchers.IO) {
        transaction { block() }
    }

}