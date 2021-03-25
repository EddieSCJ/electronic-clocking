package com.edcleidson.electronicclocking.services

import com.edcleidson.electronicclocking.domain.Employee

interface EmployeeService {
    fun findByEmail(email: String): Employee?

    fun findByCpf (cpf: String): Employee?

    fun save(employee: Employee): Employee
}