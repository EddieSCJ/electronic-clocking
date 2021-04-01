package com.edcleidson.electronicclocking.services

import com.edcleidson.electronicclocking.domain.Company
import com.edcleidson.electronicclocking.repositories.CompanyRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import java.util.*

@SpringBootTest
class CompanyServiceTest {

    @Autowired
    private val companyService: CompanyService? = null

    @MockBean
    private val companyRepository: CompanyRepository? = null

    private val ID: String = "u4hi2rweih92134gb"
    private val SOCIAL_NAME = "The Cola Cola LTDA"
    private val CNPJ: String = "45.997.418/0001-53"
    private val PAGE_REQUEST: PageRequest = PageRequest.of(0, 10, Sort.by("socialName").ascending())

    private val WRONG_ID: String = "jisadhfuisfasdb"
    private val WRONG_CNPJ = "00.000.000/0000-00"

    @BeforeEach
    @Throws(Exception::class)
    fun setUp() {
        BDDMockito.given(companyRepository?.findAll(PAGE_REQUEST)).willReturn(Page.empty())
        BDDMockito.given(companyRepository?.findById(ID)).willReturn(Optional.of(company()))
        BDDMockito.given(companyRepository?.findByCnpj(CNPJ)).willReturn(company())
        BDDMockito.given(companyRepository?.save(company())).willReturn(company())
        BDDMockito.given(companyRepository?.findByIdOrSocialNameOrCnpj(ID, SOCIAL_NAME, CNPJ)).willReturn(company())
    }

    @Test
    fun shouldReturnAPageWhenFindAll() {
        val page: Page<Company>? = companyService?.findAll(0, 10, "socialName", "ASC")
        assertNotNull(page)
    }

    @Test
    fun shouldReturnACompanyWhenFindById() {
        val company: Company? = companyService?.findById(ID)
        assertNotNull(company)
    }

    @Test
    fun shouldReturnNullWhenFindByWrongId() {
        val company: Company? = companyService?.findById(WRONG_ID)
        assertNull(company)
    }

    @Test
    fun shouldReturnACompanyWhenFindByCnpj() {
        val company: Company? = companyService?.findByCnpj(CNPJ)
        assertNotNull(company)
    }

    @Test
    fun shouldReturnNullWhenFindCompanyByWrongCnpj() {
        val company: Company? = companyService?.findByCnpj(WRONG_CNPJ)
        assertNull(company)
    }

    @Test
    fun shouldSaveAndReturnACompany() {
        val company: Company? = companyService?.save(company())
        assertNotNull(company)
    }

    @Test
    fun shouldDeleteACompanyById() {
        companyService?.deleteById(ID)
        Mockito.verify(companyRepository, Mockito.times(1))?.deleteById(ID)
    }

    @Test
    fun shouldReturnTrueWhenVerifyIfCompanyExists() {
        assertNotNull(companyService)

        val companyExists: Boolean = companyService!!.companyExists(company())
        assertTrue(companyExists)
    }

    private fun company(): Company = Company(SOCIAL_NAME, CNPJ, ID)

}