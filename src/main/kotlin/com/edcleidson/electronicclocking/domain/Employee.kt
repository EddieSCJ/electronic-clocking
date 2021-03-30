package com.edcleidson.electronicclocking.domain

import com.edcleidson.electronicclocking.domain.enums.Role
import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.br.CPF
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Document
data class Employee(
    @field:NotEmpty(message = "The name might be not empty")
    @field:Length(min = 3, max= 200, message = "The name might have a length between 3 and 200 characters")
    val name: String,

    @field:NotEmpty(message = "The email might be not empty")
    @field:Email(message = "You provided an invalid mail")
    val email: String,

    @field:NotNull(message = "The password might be not null")
    val password: Password,

    @field:CPF(message = "You provided an invalid CPF")
    val cpf: String,

    @field:NotNull(message = "The Role is required")
    val role: Role,

    @field:NotEmpty(message = "The company is required")
    val companyId: String,
    val hourPrice: Double? = 0.0,
    val defaultWorkTime: Float? = 0.0f,
    val defaultLunchTime: Float? = 0.0f,
    @Id val id: String? = null
)
