package com.edcleidson.electronicclocking.services.implementation

import com.edcleidson.electronicclocking.domain.Company
import com.edcleidson.electronicclocking.repositories.CompanyRepository
import com.edcleidson.electronicclocking.services.CompanyService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class CompanyServiceImpl(val companyRepository: CompanyRepository) : CompanyService {

    override fun findAll(start: Int, end: Int, order: String, direction: String): Page<Company> {
        val sort: Sort = if (direction == "ASC") Sort.by(order).ascending() else Sort.by(order).descending()
        return companyRepository.findAll(PageRequest.of(start, end, sort))
    }

    override fun findById(id: String): Company? = companyRepository.findById(id).orElse(null)

    override fun findByCnpj(cnpj: String): Company? = companyRepository.findByCnpj(cnpj)

    override fun save(company: Company): Company = companyRepository.save(company)

    override fun deleteById(id: String): Boolean {
        var deleted = true

        try {
            companyRepository.deleteById(id)
        } catch (exception: Exception) {
            deleted = false
        }

        return deleted
    }

    override fun companyExists(company: Company?): Boolean {

        if (company == null) return false

        val resultCompany: Company? =
            companyRepository.findByIdOrSocialNameOrCnpj(company.id, company.socialName, company.cnpj)

        return resultCompany != null
    }
}