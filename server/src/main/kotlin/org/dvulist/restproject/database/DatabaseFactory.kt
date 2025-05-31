package org.dvulist.restproject.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

object DatabaseFactory {

    fun init() {
        val dbFile = File("./data/rest_project.db") // Путь к файлу SQLite базы данных
        dbFile.parentFile?.mkdirs() // Создаем директории, если их нет
        val jdbcURL = "jdbc:sqlite:${dbFile.absolutePath}"
        val driverClassName = "org.sqlite.JDBC"
        Database.connect(jdbcURL, driverClassName)

        transaction {
            SchemaUtils.create(CollectionPoints)
            SchemaUtils.create(WasteTypes)
            SchemaUtils.create(Users)
            SchemaUtils.create(Reports)
        }
    }
}