package com.edcleidson.electronicclocking.services

import com.edcleidson.electronicclocking.domain.Company
import com.edcleidson.electronicclocking.domain.Employee
import com.edcleidson.electronicclocking.domain.Password
import com.edcleidson.electronicclocking.domain.enums.Role
import com.edcleidson.electronicclocking.repositories.EmployeeRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import java.util.*

@SpringBootTest
class EmployeeServiceTest {

    @Autowired
    private val employeeService: EmployeeService? = null

    @MockBean
    private val employeeRepository: EmployeeRepository? = null

    private val ID = "2321jh31ijh31igh31"
    private val CPF: String = "748.395.260-10"
    private val EMAIL: String = "mock@gmail.com"

    private val WRONG_ID = "sdkfhi1u23bikfsdui"
    private val WRONG_CPF: String = "109.194.440-75"
    private val WRONG_EMAIL: String = "junim@gmail.com"

    private val PAGE_REQUEST: PageRequest = PageRequest.of(0, 10, Sort.by("name").ascending())

    @BeforeEach
    @Throws(Exception::class)
    fun setUp() {
        BDDMockito.given(employeeRepository?.findAll(PAGE_REQUEST)).willReturn(PageImpl(Arrays.asList(employee())))
        BDDMockito.given(employeeRepository?.findById(ID)).willReturn(Optional.of(employee()))
        BDDMockito.given(employeeRepository?.findByCpf(CPF)).willReturn(employee())
        BDDMockito.given(employeeRepository?.findByEmail(EMAIL)).willReturn(employee())
        BDDMockito.given(employeeRepository?.save(employee())).willReturn(employee())
        BDDMockito.given(employeeRepository?.findByIdOrCpfOrEmail(ID, CPF, EMAIL)).willReturn(employee())
    }

    @Test
    fun shouldReturnAPageWhenFindAll() {
        val page: Page<Employee>? = employeeService?.findAll(0, 10, "name", "ASC")
        assertNotNull(page)
    }

    @Test
    fun shouldFindAnEmployeeById() {
        val employee: Employee? = employeeService?.findById(ID)
        assertNotNull(employee)
    }

    @Test
    fun shouldReturnNullWhenSearchEmployeeByWrongId() {
        val employee: Employee? = employeeService?.findById(WRONG_ID)
        assertNull(employee)
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

    @Test
    fun shouldDeleteAnEmployeeById() {
        employeeService?.deleteById(ID)
        Mockito.verify(employeeRepository, Mockito.times(1))?.deleteById(ID)
    }

    @Test
    fun shouldReturnTrueWhenVerifyIfEmployeeExists() {
        assertNotNull(employeeService)

        val employeeExists: Boolean = employeeService!!.employeeExists(employee())
        assertTrue(employeeExists)
    }

    private fun employee(): Employee = Employee(
        "Edcleidson Jr",
        EMAIL,
        Password("12345"),
        CPF,
        Role.ADMIN,
        "12djsanidubef",
        12.9,
        8.0f,
        2.0f,
        ID
    )

}