package com.edcleidson.electronicclocking.services.implementation

import com.edcleidson.electronicclocking.domain.Employee
import com.edcleidson.electronicclocking.repositories.EmployeeRepository
import com.edcleidson.electronicclocking.services.EmployeeService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class EmployeeServiceImpl(val employeeRepository: EmployeeRepository) : EmployeeService {
    override fun findAll(page: Int, quantityPerPage: Int, order: String, direction: String): Page<Employee> {
        val sort: Sort = if (order == "ASC") Sort.by(order).ascending() else Sort.by(order).descending()
        return employeeRepository.findAll(PageRequest.of(page, quantityPerPage, sort))
    }

    override fun findByIdOrCpfOrEmail(id: String, cpf: String, email: String): Employee? =
        employeeRepository.findByIdOrCpfOrEmail(id, cpf, email)

    override fun save(employee: Employee): Employee = employeeRepository.save(employee)
}