package org.dvulist.restproject.database

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object CollectionPointDao {
    fun getAll() = transaction {
        CollectionPoints.selectAll().map { it.toCollectionPoint() }
    }

    fun get(id: Int) = transaction {
        CollectionPoints.selectAll().where(CollectionPoints.id eq id).map { it.toCollectionPoint() }.singleOrNull()
    }

    fun add(name: String, address: String, latitude: Double?, longitude: Double?): Int = transaction {
        CollectionPoints.insert {
            it[CollectionPoints.name] = name
            it[CollectionPoints.address] = address
            it[CollectionPoints.latitude] = latitude
            it[CollectionPoints.longitude] = longitude
        }[CollectionPoints.id]
    }

    fun update(id: Int, name: String?, address: String?, latitude: Double?, longitude: Double?): Boolean = transaction {
        CollectionPoints.update({ CollectionPoints.id eq id }) {
            if (name != null) it[CollectionPoints.name] = name
            if (address != null) it[CollectionPoints.address] = address
            if (latitude != null) it[CollectionPoints.latitude] = latitude
            if (longitude != null) it[CollectionPoints.longitude] = longitude
        } > 0
    }

    fun delete(id: Int): Boolean = transaction {
        CollectionPoints.deleteWhere { CollectionPoints.id eq id } > 0
    }

    private fun ResultRow.toCollectionPoint() = CollectionPoint(
        id = this[CollectionPoints.id],
        name = this[CollectionPoints.name],
        address = this[CollectionPoints.address],
        latitude = this[CollectionPoints.latitude],
        longitude = this[CollectionPoints.longitude]
    )
}

object WasteTypeDao {
    fun getAll() = transaction {
        WasteTypes.selectAll().map { it.toWasteType() }
    }

    fun get(id: Int) = transaction {
        WasteTypes.selectAll().where(WasteTypes.id eq id).map { it.toWasteType() }.singleOrNull()
    }

    fun add(name: String, description: String?): Int = transaction {
        WasteTypes.insert {
            it[WasteTypes.name] = name
            it[WasteTypes.description] = description
        }[WasteTypes.id]
    }

    fun update(id: Int, name: String?, description: String?): Boolean = transaction {
        WasteTypes.update({WasteTypes.id eq id}) {
            if (name != null) it[WasteTypes.name] = name
            if (description != null) it[WasteTypes.description] = description
        } > 0
    }

    fun delete(id: Int): Boolean = transaction {
        WasteTypes.deleteWhere { WasteTypes.id eq id } > 0
    }

    private fun ResultRow.toWasteType() = WasteType(
        id = this[WasteTypes.id],
        name = this[WasteTypes.name],
        description = this[WasteTypes.description]
    )
}

@Serializable
data class CollectionPoint(
    val id: Int,
    val name: String,
    val address: String,
    val latitude: Double?,
    val longitude: Double?
)

@Serializable
data class WasteType(
    val id: Int,
    val name: String,
    val description: String?
)