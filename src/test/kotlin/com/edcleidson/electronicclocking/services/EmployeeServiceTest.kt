package com.edcleidson.electronicclocking.services

import com.edcleidson.electronicclocking.domain.Employee
import com.edcleidson.electronicclocking.domain.enums.Role
import com.edcleidson.electronicclocking.repositories.EmployeeRepository
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import kotlin.math.E

@SpringBootTest
class EmployeeServiceTest {

    @Autowired
    private val employeeService: EmployeeService? = null

    @MockBean
    private val employeeRepository: EmployeeRepository? = null

    private val CPF: String = "13030931463"
    private val WRONG_CPF: String = "12233121313"
    private val EMAIL: String = "mock@gmail.com"
    private val WRONG_EMAIL: String = "junim@gmail.com"

    @BeforeEach
    @Throws(Exception::class)
    fun setUp() {

        BDDMockito.given(employeeRepository?.findByCpf(CPF)).willReturn(employee())
        BDDMockito.given(employeeRepository?.findByEmail(EMAIL)).willReturn(employee())
        BDDMockito.given(employeeRepository?.save(employee())).willReturn(employee())
    }

    @Test
    fun shouldFindAnEmployeeByCPF() {
        val employee: Employee? = employeeService?.findByCpf(CPF)
        assertNotNull(employee)
    }

    @Test
    fun shouldReturnNullWhenSearchEmployeeByWrongCPF() {
        val employee: Employee? = employeeService?.findByCpf(WRONG_CPF)
        assertNull(employee)
    }

    @Test
    fun shouldFindAnEmployeeByEmail() {
        val employee: Employee? = employeeService?.findByEmail(EMAIL)
        assertNotNull(employee)
    }

    @Test
    fun shouldReturnNullWhenSearchEmployeeByWrongEmail() {
        val employee: Employee? = employeeService?.findByEmail(WRONG_EMAIL)
        assertNull(employee)
    }

    @Test
    fun shouldSaveAndReturnAnEmployee() {
        val employee: Employee? = employeeService?.save(employee())
        assertNotNull(employee)
    }

    private fun employee(): Employee =
        Employee("Edcleidson Jr", "mock@gmail.com", "12345", "13030931463", Role.ROLE_ADMIN, "12djsanidubef")

}