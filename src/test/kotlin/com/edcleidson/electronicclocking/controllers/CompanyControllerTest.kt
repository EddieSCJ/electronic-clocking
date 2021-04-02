package com.edcleidson.electronicclocking.controllers

import com.edcleidson.electronicclocking.domain.Company
import com.edcleidson.electronicclocking.services.CompanyService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class CompanyControllerTest {

    @Autowired
    private val mockMvc: MockMvc? = null

    @MockBean
    private val companyService: CompanyService? = null

    private val ID: String = "u4hi2rweih92134gb"
    private val SOCIAL_NAME = "The Cola Cola LTDA"
    private val CNPJ: String = "45.997.418/0001-53"
    private val PAGE_REQUEST: PageRequest = PageRequest.of(0, 10, Sort.by("socialName").ascending())

    private val WRONG_ID: String = "jisadhfuisfasdb"
    private val WRONG_CNPJ = "00.000.000/0000-00"


    @Test
    @WithMockUser
    fun shouldSaveACompany() {

        BDDMockito.given(companyService?.findAll(0, 10, "socialName", "ASC"))
            .willReturn(PageImpl<Company>(Arrays.asList(company())))
        mockMvc!!.perform(MockMvcRequestBuilders.get("/api/companies")).andExpect(status().isOk)
//        assertTrue(true)
    }

    private fun company(): Company = Company(SOCIAL_NAME, CNPJ, ID)


}