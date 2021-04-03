package com.edcleidson.electronicclocking.controllers

import com.edcleidson.electronicclocking.domain.Employee
import com.edcleidson.electronicclocking.domain.Password
import com.edcleidson.electronicclocking.domain.dto.EmployeeDTO
import com.edcleidson.electronicclocking.domain.enums.Role
import com.edcleidson.electronicclocking.services.EmployeeService
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
class EmployeeControllerTest {

    @Autowired
    private val mockMvc: MockMvc? = null

    @MockBean
    private val employeeService: EmployeeService? = null

    private val BASE_URL = "/api/employees"

    private val ID = "2321jh31ijh31igh31"
    private val CPF: String = "748.395.260-10"
    private val EMAIL: String = "mock@gmail.com"

    private val UPDATED_NAME = "Edcleidson atualizado"
    private val UPDATED_EMAIL = "updated@gmail.com"
    private val UPDATED_CPF = "373.596.970-42"

    private val ORDER: String = "name"
    private val SORT = Sort.by(ORDER).ascending()
    private val PAGE_REQUEST: PageRequest = PageRequest.of(0, 10, SORT)

    private val FINDABLE_EMPLOYEE = Employee("", "", Password(""), "", Role.ADMIN, "", id = ID)
    private val WRONG_ID = "sdkfhi1u23bikfsdui"
    private val WRONG_CPF: String = "109.194.440-75"
    private val WRONG_EMAIL: String = "junim@gmail.com"
    private val WRONG_FINDABLE_EMPLOYEE = Employee("", "", Password(""), "", Role.ADMIN, "", id = WRONG_ID)


    @Test
    @WithMockUser
    fun shouldGetAllEmployees() {
        BDDMockito.given(employeeService?.findAll(PAGE_REQUEST.pageNumber, PAGE_REQUEST.pageSize, ORDER, "ASC"))
            .willReturn(
                PageImpl(
                    listOf(Employee.fromDto(employee()))
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
        BDDMockito.given(employeeService?.findByIdOrCpfOrEmail(ID, CPF, EMAIL)).willReturn(Employee.fromDto(employee()))

        mockMvc!!.perform(
            get("$BASE_URL/employee?id=$ID&cpf=$CPF&email=$EMAIL")
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.errors").isEmpty)
            .andExpect(jsonPath("$.data.id").value(ID))
    }

    @Test
    @WithMockUser
    fun shouldGetNotFoundByWrongSpecialAttributes() {
        BDDMockito.given(employeeService?.findByIdOrCpfOrEmail(ID, CPF, EMAIL)).willReturn(Employee.fromDto(employee()))
        BDDMockito.given(employeeService?.findByIdOrCpfOrEmail(WRONG_ID, WRONG_CPF, WRONG_EMAIL)).willReturn(null)

        mockMvc!!.perform(
            get("$BASE_URL/employee?id=$WRONG_ID&cpf=$WRONG_CPF&email=$WRONG_EMAIL")
        ).andExpect(status().isNotFound)
            .andExpect(jsonPath("$.errors").isNotEmpty)
            .andExpect(jsonPath("$.data").value(null))
    }

    @Test
    @WithMockUser
    fun shouldSaveAEmployee() {

        BDDMockito.given(employeeService?.save(Employee.fromDto(employee()))).willReturn(Employee.fromDto(employee()))
        BDDMockito.given(employeeService?.employeeExists(Employee.fromDto(employee()))).willReturn(false)

        mockMvc!!.perform(
            post(BASE_URL)
                .content(employeeJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.errors").isEmpty)
    }

    @Test
    @WithMockUser
    fun shouldUpdateAEmployeeByID() {
        BDDMockito.given(employeeService?.save(Employee.fromDto(updatedEmployee())))
            .willReturn(Employee.fromDto(updatedEmployee()))
        BDDMockito.given(employeeService?.employeeExists(FINDABLE_EMPLOYEE)).willReturn(true)

        mockMvc!!.perform(
            put("$BASE_URL/$ID")
                .content(updatedEmployeeJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.errors").isEmpty)
    }

    @Test
    @WithMockUser
    fun shouldGetNotFoundWhenUpdateAEmployeeByWrongID() {
        BDDMockito.given(employeeService?.save(Employee.fromDto(updatedEmployee())))
            .willReturn(Employee.fromDto(updatedEmployee()))
        BDDMockito.given(employeeService?.employeeExists(FINDABLE_EMPLOYEE)).willReturn(true)
        BDDMockito.given(employeeService?.employeeExists(WRONG_FINDABLE_EMPLOYEE)).willReturn(false)

        mockMvc!!.perform(
            put("$BASE_URL/$WRONG_ID")
                .content(updatedEmployeeJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.errors").isNotEmpty)
    }

    @Test
    @WithMockUser
    fun shouldDeleteAEmployeeByID() {
        BDDMockito.given(employeeService?.deleteById(ID)).willReturn(true)
        BDDMockito.given(employeeService?.employeeExists(FINDABLE_EMPLOYEE)).willReturn(true)

        mockMvc!!.perform(
            delete("$BASE_URL/$ID")
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.errors").isEmpty)
    }


    @Test
    @WithMockUser
    fun shouldGetNotFoundWhenDeleteAEmployeeByWrongID() {
        BDDMockito.given(employeeService?.deleteById(WRONG_ID)).willReturn(false)
        BDDMockito.given(employeeService?.employeeExists(WRONG_FINDABLE_EMPLOYEE)).willReturn(false)

        mockMvc!!.perform(
            delete("$BASE_URL/$WRONG_ID")
        ).andExpect(status().isNotFound)
            .andExpect(jsonPath("$.errors").isNotEmpty)
    }

    private fun employee(): EmployeeDTO = EmployeeDTO(
        "Edcleidson Jr",
        EMAIL,
        "12345",
        CPF,
        1,
        "12djsanidubef",
        12.9,
        8.0f,
        2.0f,
        ID
    )

    private fun updatedEmployee(): EmployeeDTO = EmployeeDTO(
        UPDATED_NAME,
        UPDATED_EMAIL,
        "12345",
        UPDATED_CPF,
        1,
        "12djsanidubef",
        12.9,
        8.0f,
        2.0f,
        ID
    )

    private fun employeeJson(): String = ObjectMapper().writeValueAsString(employee())

    private fun updatedEmployeeJson(): String = ObjectMapper().writeValueAsString(updatedEmployee())


}