package com.edcleidson.electronicclocking.services.implementation

import com.edcleidson.electronicclocking.domain.Company
import com.edcleidson.electronicclocking.domain.Employee
import com.edcleidson.electronicclocking.repositories.CompanyRepository
import com.edcleidson.electronicclocking.repositories.EmployeeRepository
import com.edcleidson.electronicclocking.services.CompanyService
import com.edcleidson.electronicclocking.services.EmployeeService
import org.springframework.stereotype.Service

@Service
class EmployeeServiceImpl(val employeeRepository: EmployeeRepository) : EmployeeService {
    override fun findByEmail(email: String): Employee? = employeeRepository.findByEmail(email)

    override fun findByCpf(cpf: String): Employee? = employeeRepository.findByCpf(cpf)

    override fun findById(id: String): Employee? = employeeRepository.findById(id).orElse(null)

    override fun save(employee: Employee): Employee = employeeRepository.save(employee)
}