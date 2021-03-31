package com.edcleidson.electronicclocking.domain

import com.edcleidson.electronicclocking.domain.dto.EntryDTO
import com.edcleidson.electronicclocking.domain.enums.EntryType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.format.annotation.DateTimeFormat
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Document
data class Entry(

    @field:DateTimeFormat(pattern = "MM/dd/yyyy HH:mm:ss")
    @field:NotNull(message = "The date might be not null")
    val date: Date,

    @field:NotNull(message = "The entry type is required")
    val type: EntryType,

    @field:NotEmpty(message = "The employee is required")
    val employeeId: String,
    val description: String? = "",
    val localization: String? = "",
    @Id val id: String? = null
) {

    companion object {
        fun fromDTO(entryDTO: EntryDTO): Entry {
            return Entry(
                date = if (entryDTO.date == "") Date() else Date(entryDTO.date),
                EntryType.toEnum(entryDTO.type),
                employeeId = entryDTO.employeeId,
                description = entryDTO.description,
                localization = entryDTO.localization,
                id = entryDTO.id
            )
        }
    }

}
