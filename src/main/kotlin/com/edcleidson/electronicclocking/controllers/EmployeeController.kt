package com.edcleidson.electronicclocking.controllers

import com.edcleidson.electronicclocking.domain.Employee
import com.edcleidson.electronicclocking.domain.Password
import com.edcleidson.electronicclocking.domain.dto.EmployeeDTO
import com.edcleidson.electronicclocking.domain.enums.Role
import com.edcleidson.electronicclocking.domain.enums.constants.HttpResponses
import com.edcleidson.electronicclocking.domain.enums.constants.HttpResponses.NOT_FOUND
import com.edcleidson.electronicclocking.response.Response
import com.edcleidson.electronicclocking.services.EmployeeService
import com.edcleidson.electronicclocking.utils.helpers.BasicOperationsHelper.Companion.API
import com.edcleidson.electronicclocking.utils.helpers.BasicOperationsHelper.Companion.getValidatedResponseInstance
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

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

    ): ResponseEntity<Response<List<Employee>>> {
        val pageResult: Page<Employee> = employeeService.findAll(page, quantityPerPage, order, direction)
        val response: Response<List<Employee>> = Response(data = pageResult.content)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/employee")
    fun getBySpecialAttribute(
        @RequestParam(value = "email", defaultValue = "") email: String,
        @RequestParam(value = "cpf", defaultValue = "") cpf: String,
        @RequestParam(value = "id", defaultValue = "") id: String
    ): ResponseEntity<Response<Employee>> {
        val response: Response<Employee> = Response()
        val resultEmployee: Employee? = employeeService.findByIdOrCpfOrEmail(id, cpf, email)

        if (resultEmployee == null) {
            response.errors.add(NOT_FOUND.getDescription())
            response.status = NOT_FOUND.getCode()
            return ResponseEntity.status(response.status).body(response)
        }

        response.data = resultEmployee
        return ResponseEntity.ok(response)
    }

    @PostMapping
    fun post(
        @Valid @RequestBody employeeDTO: EmployeeDTO,
        result: BindingResult
    ): ResponseEntity<Response<Employee>> {
        val employee: Employee = Employee.fromDto(employeeDTO)
        val alreadyExists: Boolean = employeeService.employeeExists(employee)
        val response: Response<Employee> =
            getValidatedResponseInstance(
                alreadyExists = alreadyExists,
                shouldExists = false,
                result = result
            )
        val responseEntity: ResponseEntity<Response<Employee>> = ResponseEntity.status(response.status).body(response)

        if (response.errors.isEmpty()) response.data = employeeService.save(employee)

        return responseEntity
    }

    @PutMapping("/{id}")
    fun put(
        @PathVariable id: String,
        @Valid @RequestBody employeeDTO: EmployeeDTO,
        result: BindingResult
    ): ResponseEntity<Response<Employee>> {
        employeeDTO.id = id
        val employee: Employee = Employee.fromDto(employeeDTO)
        val findableEmployee = Employee("", "", Password(""), "", Role.ADMIN, "", id = id)
        val alreadyExists = employeeService.employeeExists(findableEmployee)
        val response: Response<Employee> =
            getValidatedResponseInstance(
                alreadyExists = alreadyExists,
                shouldExists = true,
                result = result
            )
        val responseEntity: ResponseEntity<Response<Employee>> = ResponseEntity.status(response.status).body(response)

        if (response.errors.isEmpty()) response.data = employeeService.save(employee)

        return responseEntity
    }


    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Response<Employee>> {
        val findableEmployee = Employee("", "", Password(""), "", Role.ADMIN, "", id = id)
        val alreadyExists: Boolean = employeeService.employeeExists(findableEmployee)
        val response: Response<Employee> =
            getValidatedResponseInstance(alreadyExists = alreadyExists, shouldExists = true)

        if (response.errors.isEmpty()) {
            val completeEmploye = employeeService.findById(id)
            val deleted: Boolean = employeeService.deleteById(id)

            if (deleted) response.data = completeEmploye
            else {
                response.errors.add(HttpResponses.UNABLE_PERFORM_DELETE.getDescription())
                response.status = HttpResponses.UNABLE_PERFORM_DELETE.getCode()
            }
        }

        return ResponseEntity.status(response.status).body(response)
    }


}