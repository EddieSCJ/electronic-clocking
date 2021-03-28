package com.edcleidson.electronicclocking.services

import com.edcleidson.electronicclocking.domain.Company
import org.springframework.data.domain.Page

interface CompanyService {

    fun findAll(start: Int, end: Int, order: String, direction: String): Page<Company>

    fun findById(id: String): Company?

    fun findByCnpj(cnpj: String): Company?

    fun save(company: Company): Company

    fun deleteById(id: String): Boolean

    fun companyExists(company: Company?): Boolean
}