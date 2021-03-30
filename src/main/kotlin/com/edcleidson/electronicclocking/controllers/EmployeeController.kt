package com.edcleidson.electronicclocking.controllers

import com.edcleidson.electronicclocking.domain.Employee
import com.edcleidson.electronicclocking.response.Response
import com.edcleidson.electronicclocking.services.EmployeeService
import com.edcleidson.electronicclocking.utils.helpers.BasicOperationsHelper.Companion.API
import com.edcleidson.electronicclocking.domain.enums.constants.HttpResponses.NOT_FOUND
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/$API/employees")
class EmployeeController(val employeeService: EmployeeService) {

    @Value("\${pagination.quantity_per_page}")
    private val quantityPerPage: Int = 0

    @GetMapping
    fun getAll(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "order", defaultValue = "name") order: String,
        @RequestParam(name = "direction", defaultValue = "ASC") direction: String

    ): ResponseEntity<Response<*>> {
        val pageResult: Page<Employee> = employeeService.findAll(page, quantityPerPage, order, direction)
        val response: Response<List<Employee>> = Response(data = pageResult.content)
        return ResponseEntity.ok(response)
    }

    @GetMapping
    fun getBySpecialAttribute(
        @RequestParam(value = "email", defaultValue = "") email: String,
        @RequestParam(value = "cpf", defaultValue = "") cpf: String,
        @RequestParam(value = "id", defaultValue = "") id: String
    ) : ResponseEntity<Response<Employee>> {
        val response: Response<Employee> = Response()
        val resultEmployee: Employee? = employeeService.findByIdOrCpfOrEmail(id, cpf, email)

        if (resultEmployee == null) {
            response.errors.add(NOT_FOUND.getDescription())
            response.status = NOT_FOUND.getCode()
            return ResponseEntity.status(response.status).body(response)
        }

        response.data = resultEmployee
        return Response




















































        Entity.ok(response)
    }
//
//    @PutMapping
//    fun put() {
//
//    }
//
//    @PostMapping
//    fun post() {
//
//    }
//
//    @DeleteMapping
//    fun delete() {
//
//    }

}