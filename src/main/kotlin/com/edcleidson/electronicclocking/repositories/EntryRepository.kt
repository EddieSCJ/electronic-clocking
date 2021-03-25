package com.edcleidson.electronicclocking.repositories

import com.edcleidson.electronicclocking.domain.Entry
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface EntryRepository : MongoRepository<Entry, String> {

    fun findByEmployeeId(id: String, pageable: Pageable) : Page<Entry>

}