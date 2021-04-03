package com.edcleidson.electronicclocking.controllers

import com.edcleidson.electronicclocking.domain.Company
import com.edcleidson.electronicclocking.services.CompanyService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class CompanyControllerTest {

    @Autowired
    private val mockMvc: MockMvc? = null

    @MockBean
    private val companyService: CompanyService? = null

    private val BASE_URL = "/api/companies"
    private val ID: String = "u4hi2rweih92134gb"
    private val SOCIAL_NAME = "The Cola Cola LTDA"
    private val CNPJ: String = "45.997.418/0001-53"

    private val ORDER: String = "socialName"
    private val SORT = Sort.by(ORDER).ascending()
    private val PAGE_REQUEST: PageRequest = PageRequest.of(0, 10, SORT)

    private val WRONG_ID: String = "jisadhfuisfasdb"

    private val UPDATED_SOCIAL_NAME = "The Coca Cola Eirelli"
    private val UPDATED_CNPJ = "42.199.144/0001-69"

    @Test
    @WithMockUser
    fun shouldGetAllCompanies() {
        BDDMockito.given(companyService?.findAll(PAGE_REQUEST.pageNumber, PAGE_REQUEST.pageSize, ORDER, "ASC"))
            .willReturn(
                PageImpl(
                    listOf(company())
                )
            )

        mockMvc!!.perform(
            get(BASE_URL)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.errors").isEmpty)
            .andExpect(jsonPath("$.data").isNotEmpty)
    }

    @Test
    @WithMockUser
    fun shouldGetByID() {
        BDDMockito.given(companyService?.findById(ID)).willReturn(company())

        mockMvc!!.perform(
            get("$BASE_URL/$ID")
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.errors").isEmpty)
            .andExpect(jsonPath("$.data.id").value(ID))
    }

    @Test
    @WithMockUser
    fun shouldGetNotFoundByWrongID() {
        BDDMockito.given(companyService?.findById(ID)).willReturn(company())
        BDDMockito.given(companyService?.findById(WRONG_ID)).willReturn(null)

        mockMvc!!.perform(
            get("$BASE_URL/$WRONG_ID")
        ).andExpect(status().isNotFound)
            .andExpect(jsonPath("$.errors").isNotEmpty)
            .andExpect(jsonPath("$.data").value(null))
    }

    @Test
    @WithMockUser
    fun shouldSaveACompany() {

        BDDMockito.given(companyService?.save(company())).willReturn(company())
        BDDMockito.given(companyService?.companyExists(company())).willReturn(false)

        mockMvc!!.perform(
            post(BASE_URL)
                .content(companyJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.id").value(ID))
            .andExpect(jsonPath("$.data.cnpj").value(CNPJ))
            .andExpect(jsonPath("$.data.socialName").value(SOCIAL_NAME))
            .andExpect(jsonPath("$.errors").isEmpty)
    }

    @Test
    @WithMockUser
    fun shouldUpdateACompanyByID() {
        BDDMockito.given(companyService?.save(updatedCompany())).willReturn(updatedCompany())
        BDDMockito.given(companyService?.companyExists(Company("", "", ID))).willReturn(true)

        mockMvc!!.perform(
            put("$BASE_URL/$ID")
                .content(updatedCompanyJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.id").value(ID))
            .andExpect(jsonPath("$.data.cnpj").value(UPDATED_CNPJ))
            .andExpect(jsonPath("$.data.socialName").value(UPDATED_SOCIAL_NAME))
            .andExpect(jsonPath("$.errors").isEmpty)
    }

    @Test
    @WithMockUser
    fun shouldGetNotFoundWhenUpdateACompanyByWrongID() {
        BDDMockito.given(companyService?.save(updatedCompany())).willReturn(updatedCompany())
        BDDMockito.given(companyService?.companyExists(Company("", "", ID))).willReturn(true)
        BDDMockito.given(companyService?.companyExists(Company("", "", WRONG_ID))).willReturn(false)

        mockMvc!!.perform(
            put("$BASE_URL/$WRONG_ID")
                .content(updatedCompanyJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.errors").isNotEmpty)
    }

    @Test
    @WithMockUser
    fun shouldDeleteACompanyByID() {
        BDDMockito.given(companyService?.deleteById(ID)).willReturn(true)
        BDDMockito.given(companyService?.companyExists(Company("", "", ID))).willReturn(true)

        mockMvc!!.perform(
            delete("$BASE_URL/$ID")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.errors").isEmpty)
    }


    @Test
    @WithMockUser
    fun shouldGetNotFoundWhenDeleteACompanyByWrongID() {
        BDDMockito.given(companyService?.deleteById(WRONG_ID)).willReturn(false)
        BDDMockito.given(companyService?.companyExists(Company("", "", WRONG_ID))).willReturn(false)

        mockMvc!!.perform(
            delete("$BASE_URL/$WRONG_ID")
        ).andExpect(status().isNotFound)
            .andExpect(jsonPath("$.errors").isNotEmpty)
    }

    private fun company(): Company = Company(SOCIAL_NAME, CNPJ, ID)

    private fun updatedCompany(): Company = Company(UPDATED_SOCIAL_NAME, UPDATED_CNPJ, ID)

    private fun companyJson(): String = ObjectMapper().writeValueAsString(company())

    private fun updatedCompanyJson(): String = ObjectMapper().writeValueAsString(updatedCompany())


}