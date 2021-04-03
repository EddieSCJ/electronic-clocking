package com.edcleidson.electronicclocking.controllers

import com.edcleidson.electronicclocking.domain.Entry
import com.edcleidson.electronicclocking.domain.dto.EntryDTO
import com.edcleidson.electronicclocking.services.EntryService
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
class EntryControllerTest {

    @Autowired
    private val mockMvc: MockMvc? = null

    @MockBean
    private val entryService: EntryService? = null

    private val BASE_URL = "/api/entries"

    private val ID: String = "12h3bui21ebhi123b"
    private val EMPLOYEE_ID = "fsduhf13b4ubwqbwedfs"
    private val UPDATED_EMPLOYEE_ID = "wyrqwrb823rjhwbeu45y2"

    private val WRONG_ID = "fuhsi9dufhi12i4"
    private val WRONG_EMPLOYEE_ID = "gfisudhuwnr3e45"

    private val ORDER: String = "date"
    private val SORT = Sort.by(ORDER).ascending()
    private val PAGE_REQUEST: PageRequest = PageRequest.of(0, 10, SORT)


    @Test
    @WithMockUser
    fun shouldGetAllEntries() {
        BDDMockito.given(entryService?.findAll(PAGE_REQUEST.pageNumber, PAGE_REQUEST.pageSize, ORDER, "ASC"))
            .willReturn(
                PageImpl(
                    listOf(Entry.fromDTO(entry()))
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
    fun shouldGetBySpecialAttributes() {
        BDDMockito.given(entryService?.findByIdOrEmployeeId(ID, EMPLOYEE_ID)).willReturn(listOf(Entry.fromDTO(entry())))

        mockMvc!!.perform(
            get("$BASE_URL/entry?id=$ID&employeeId=$EMPLOYEE_ID")
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.errors").isEmpty)
            .andExpect(jsonPath("$.data").isNotEmpty)
    }

    @Test
    @WithMockUser
    fun shouldGetNotFoundByWrongSpecialAttributes() {
        BDDMockito.given(entryService?.findByIdOrEmployeeId(ID, EMPLOYEE_ID)).willReturn(listOf(Entry.fromDTO(entry())))
        BDDMockito.given(entryService?.findByIdOrEmployeeId(WRONG_ID, WRONG_EMPLOYEE_ID))
            .willReturn(listOf())

        mockMvc!!.perform(
            get("$BASE_URL/entry?id=$WRONG_ID&employeeId=$WRONG_EMPLOYEE_ID")
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.errors").isEmpty)
            .andExpect(jsonPath("$.data").isEmpty)
    }

    @Test
    @WithMockUser
    fun shouldSaveAnEntry() {

        BDDMockito.given(entryService?.save(Entry.fromDTO(entry()))).willReturn(Entry.fromDTO(entry()))
        BDDMockito.given(entryService?.entryExists(Entry.fromDTO(entry()))).willReturn(false)

        mockMvc!!.perform(
            post(BASE_URL)
                .content(entryJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.errors").isEmpty)
    }

    @Test
    @WithMockUser
    fun shouldUpdateAnEntryByID() {
        BDDMockito.given(entryService?.save(Entry.fromDTO(updatedEntry())))
            .willReturn(Entry.fromDTO(updatedEntry()))
        BDDMockito.given(entryService?.entryExists(Entry.fromDTO(updatedEntry()))).willReturn(true)

        mockMvc!!.perform(
            put("$BASE_URL/$ID")
                .content(updatedEntryJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.errors").isEmpty)
    }

    @Test
    @WithMockUser
    fun shouldGetNotFoundWhenUpdateAnEntryByWrongID() {
        BDDMockito.given(entryService?.save(Entry.fromDTO(updatedEntry())))
            .willReturn(Entry.fromDTO(updatedEntry()))
        BDDMockito.given(entryService?.entryExists(Entry.fromDTO(updatedEntry()))).willReturn(true)
        BDDMockito.given(entryService?.entryExists(Entry.fromDTO(wrongUpdatedEntry()))).willReturn(false)

        mockMvc!!.perform(
            put("$BASE_URL/$WRONG_ID")
                .content(updatedEntryJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.errors").isNotEmpty)
    }

    @Test
    @WithMockUser
    fun shouldDeleteAnEntryByID() {
        BDDMockito.given(entryService?.deleteById(ID)).willReturn(true)
        BDDMockito.given(entryService?.entryExists(Entry.fromDTO(entry()))).willReturn(true)

        mockMvc!!.perform(
            delete("$BASE_URL/$ID")
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.errors").isEmpty)
    }


    @Test
    @WithMockUser
    fun shouldGetNotFoundWhenDeleteAnEntryByWrongID() {
        BDDMockito.given(entryService?.deleteById(WRONG_ID)).willReturn(false)
        BDDMockito.given(entryService?.entryExists(Entry.fromDTO(wrongEntry()))).willReturn(false)

        mockMvc!!.perform(
            delete("$BASE_URL/$WRONG_ID")
        ).andExpect(status().isNotFound)
            .andExpect(jsonPath("$.errors").isNotEmpty)
    }

    private fun entry(): EntryDTO = EntryDTO("22/10/2020", 1, EMPLOYEE_ID, "", "", ID)

    private fun wrongEntry(): EntryDTO = EntryDTO("22/10/2020", 1, WRONG_EMPLOYEE_ID, "", "", WRONG_ID)


    private fun updatedEntry(): EntryDTO = EntryDTO("12/01/2021", 1, UPDATED_EMPLOYEE_ID, "", "", ID)

    private fun wrongUpdatedEntry(): EntryDTO = EntryDTO("12/01/2021", 1, WRONG_EMPLOYEE_ID, "", "", WRONG_ID)


    private fun entryJson(): String = ObjectMapper().writeValueAsString(entry())

    private fun updatedEntryJson(): String = ObjectMapper().writeValueAsString(updatedEntry())


}