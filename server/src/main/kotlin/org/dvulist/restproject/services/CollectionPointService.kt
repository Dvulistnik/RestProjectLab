package org.dvulist.restproject.services

import org.dvulist.restproject.database.CollectionPoint
import org.dvulist.restproject.database.CollectionPointDao

class CollectionPointService {
    fun getAll(): List<CollectionPoint> = CollectionPointDao.getAll()

    fun get(id: Int): CollectionPoint? = CollectionPointDao.get(id)

    fun add(name: String, address: String, latitude: Double?, longitude: Double?): Int =
        CollectionPointDao.add(name, address, latitude, longitude)

    fun update(id: Int, name: String?, address: String?, latitude: Double?, longitude: Double?): Boolean =
        CollectionPointDao.update(id, name, address, latitude, longitude)

    fun delete(id: Int): Boolean = CollectionPointDao.delete(id)
}