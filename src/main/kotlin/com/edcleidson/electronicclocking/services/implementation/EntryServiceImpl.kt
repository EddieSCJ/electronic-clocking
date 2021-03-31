package com.edcleidson.electronicclocking.services.implementation

import com.edcleidson.electronicclocking.domain.Entry
import com.edcleidson.electronicclocking.repositories.EntryRepository
import com.edcleidson.electronicclocking.services.EntryService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class EntryServiceImpl(val entryRepository: EntryRepository) : EntryService {
    override fun findAll(page: Int, quantityPerPage: Int, order: String, direction: String): Page<Entry> {
        val sort: Sort = if (direction == "ASC") Sort.by(order).ascending() else Sort.by(order).descending()
        return entryRepository.findAll(PageRequest.of(page, quantityPerPage, sort))
    }

    override fun findByIdOrEmployeeId(id: String, employeeId: String): List<Entry> =
        entryRepository.findByIdOrEmployeeId(id, employeeId)

    override fun findById(id: String): Entry? = entryRepository.findById(id).orElse(null)

    override fun findByEmployeeId(id: String, pageRequest: PageRequest): Page<Entry> =
        entryRepository.findByEmployeeId(id, pageRequest)

    override fun save(entry: Entry): Entry = entryRepository.save(entry)

    override fun deleteById(id: String) : Boolean {
        var deleted = true

        try {
            entryRepository.deleteById(id)
        } catch (exception: Exception) {
            deleted = false
        }

        return deleted
    }

    override fun entryExists(entry: Entry?): Boolean {
        var id = ""
        if (entry == null) return false
        if (entry.id != null) id = entry.id

        val resultEntry: Entry? =
            entryRepository.findById(id).orElse(null)

        return resultEntry != null
    }

}