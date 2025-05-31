package org.dvulist.restproject.services

import org.dvulist.restproject.database.WasteType
import org.dvulist.restproject.database.WasteTypeDao
import javax.swing.text.StyledEditorKit.BoldAction

class WasteTypeService {
    fun getAll(): List<WasteType> = WasteTypeDao.getAll()

    fun get(id: Int): WasteType? = WasteTypeDao.get(id)

    fun add(name: String, description: String?): Int = WasteTypeDao.add(name, description)

    fun update(id: Int, name: String?, description: String?): Boolean = WasteTypeDao.update(id, name, description)

    fun delete(id: Int): Boolean = WasteTypeDao.delete(id)
}