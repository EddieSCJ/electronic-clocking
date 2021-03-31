package com.edcleidson.electronicclocking.domain.dto

import com.edcleidson.electronicclocking.domain.enums.EntryType
import org.springframework.data.annotation.Id
import org.springframework.format.annotation.DateTimeFormat
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class EntryDTO(
    @field:DateTimeFormat(pattern = "MM/dd/yyyy HH:mm:ss")
    @field:NotNull(message = "The date might be not null")
    val date: String,

    @field:NotNull(message = "The entry type is required")
    val type: Int,

    @field:NotEmpty(message = "The employee is required")
    val employeeId: String,
    val description: String? = "",
    val localization: String? = "",
    @Id
    var id: String? = null
)