package com.edcleidson.electronicclocking.services.implementation

import com.edcleidson.electronicclocking.domain.Entry
import com.edcleidson.electronicclocking.repositories.EntryRepository
import com.edcleidson.electronicclocking.services.EntryService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class EntryServiceImpl(val entryRepository: EntryRepository) : EntryService {

    override fun findById(id: String): Entry? = entryRepository.findById(id).orElse(null)

    override fun findByEmployeeId(id: String, pageRequest: PageRequest): Page<Entry> = entryRepository.findByEmployeeId(id, pageRequest)

    override fun save(entry: Entry): Entry = entryRepository.save(entry)

    override fun deleteById(id: String) = entryRepository.deleteById(id)
}