package com.edcleidson.electronicclocking.services

import com.edcleidson.electronicclocking.domain.Entry
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

interface EntryService {

    fun findAll(page: Int, quantityPerPage: Int, order: String, direction: String): Page<Entry>

    fun findByIdOrEmployeeId(id: String, employeeId: String) : List<Entry>

    fun findById(id: String): Entry?

    fun findByEmployeeId(id: String, pageRequest: PageRequest): Page<Entry>

    fun save(entry: Entry): Entry

    fun deleteById(id: String): Boolean

    fun entryExists(entry: Entry?) : Boolean

}
