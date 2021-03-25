package com.edcleidson.electronicclocking.repositories

import com.edcleidson.electronicclocking.domain.Employee
import org.springframework.data.mongodb.repository.MongoRepository

interface EmployeeRepository : MongoRepository<Employee, String> {

    fun findByEmail(email: String): Employee?

    fun findByCpf (cpf: String): Employee?

}