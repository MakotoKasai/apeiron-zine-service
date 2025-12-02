package art.kasai.apeiron.zine.infrastructure.db

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.nio.file.Files
import java.nio.file.Paths

object DatabaseFactory {

    //ExposedのDatabaseハンドル
    lateinit var database: Database
        private set

    /**
     * constructor
     * Run once at startup
     */
    fun init(dbFilePath: String = "data/apeiron-zine.db") {

        // Create the directory if it does not exist.
        val dbDir = Paths.get("data")
        if(!Files.exists(dbDir)) {
            Files.createDirectories(dbDir)
        }

        // connect to SQLite
        database = Database.connect(
            url = "jdbc:sqlite:$dbFilePath",
            driver = "org.sqlite.JDBC"
        )

        // create Schema
        transaction(database) {
            SchemaUtils.create(ProjectTable)
        }

        //
        TransactionManager.defaultDatabase = database
    }

    suspend fun <T> dbQuery(block: suspend Transaction.() -> T): T =
        withContext(Dispatchers.IO) {
            newSuspendedTransaction(Dispatchers.IO, database) {
                block()
            }
        }
}