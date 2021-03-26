package com.edcleidson.electronicclocking.domain

import com.edcleidson.electronicclocking.domain.enums.EntryType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document
data class Entry(
    val date: Date,
    val type: EntryType,
    val employeeId: String,
    val description: String? = "",
    val localization: String? = "",
    @Id val id: String? = null
)
