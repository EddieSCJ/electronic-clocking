package com.edcleidson.electronicclocking.repositories

import com.edcleidson.electronicclocking.domain.Company
import org.springframework.data.mongodb.repository.MongoRepository

interface CompanyRepository : MongoRepository<Company, Int> {

    fun findByCnpj(cpnj: String) : Company?
}