package org.dvulist.restproject.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object WasteTypes : Table("waste_types") {
    val id = integer("waste_type_id").autoIncrement().uniqueIndex()
    override val primaryKey = PrimaryKey(id)
    val name = text("name").uniqueIndex()
    val description = text("description").nullable()
}

object CollectionPoints : Table("collection_points") {
    val id = integer("collection_point_id").autoIncrement().uniqueIndex()
    override val primaryKey = PrimaryKey(id)
    val name = text("name")
    val address = text("address")
    val latitude = double("latitude").nullable()
    val longitude = double("longitude").nullable()
}

object Users : Table("users") {
    val id = integer("user_id").autoIncrement().uniqueIndex()
    override val primaryKey = PrimaryKey(id)
    val username = text("username").uniqueIndex()
    val email = text("email").uniqueIndex()
    val registrationDate = datetime("registration_date").nullable()
}

object Reports : Table("reports") {
    val id = integer("report_id").autoIncrement().uniqueIndex()
    override val primaryKey = PrimaryKey(id)
    val userId = integer("user_id").references(Users.id)
    val collectionPointId = integer("collection_point_id").references(CollectionPoints.id)
    val wasteTypeId = integer("waste_type_id").references(WasteTypes.id)
    val quantity = double("quantity")
    val submissionDate = datetime("submission_date").nullable()

    // Определение внешних ключей (хотя они уже определены через references())
    // index(listOf(userId))
    // index(listOf(collectionPointId))
    // index(listOf(wasteTypeId))
}