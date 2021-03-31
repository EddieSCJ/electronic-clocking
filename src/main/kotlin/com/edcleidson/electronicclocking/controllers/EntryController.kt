package com.edcleidson.electronicclocking.controllers

import com.edcleidson.electronicclocking.domain.Entry
import com.edcleidson.electronicclocking.domain.dto.EntryDTO
import com.edcleidson.electronicclocking.domain.enums.EntryType
import com.edcleidson.electronicclocking.domain.enums.Role
import com.edcleidson.electronicclocking.domain.enums.constants.HttpResponses
import com.edcleidson.electronicclocking.response.Response
import com.edcleidson.electronicclocking.services.EntryService
import com.edcleidson.electronicclocking.utils.helpers.BasicOperationsHelper.Companion.API
import com.edcleidson.electronicclocking.utils.helpers.BasicOperationsHelper.Companion.getValidatedResponseInstance
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid
import kotlin.math.E

@RestController
@RequestMapping("/$API/entries")
class EntryController(val entryService: EntryService) {

    @Value("\${pagination.quantity_per_page}")
    private val quantityPerPage: Int = 0

    @GetMapping
    fun getAll(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "order", defaultValue = "date") order: String,
        @RequestParam(value = "direction", defaultValue = "ASC") direction: String
    ): ResponseEntity<Response<List<Entry>>> {
        val pageResult: Page<Entry> = entryService.findAll(page, quantityPerPage, order, direction)
        val response: Response<List<Entry>> = Response(data = pageResult.content)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/entry")
    fun getBySpecialAttribute(
        @RequestParam(value = "id", defaultValue = "") id: String,
        @RequestParam(value = "employeeId", defaultValue = "") employeeId: String
    ): ResponseEntity<Response<List<Entry>>> {
        val response: Response<List<Entry>> = Response()
        val resultEntry: List<Entry> = entryService.findByIdOrEmployeeId(id, employeeId)
        response.data = resultEntry

        return ResponseEntity.ok(response)
    }

    @PostMapping
    fun post(
        @Valid @RequestBody entryDTO: EntryDTO,
        result: BindingResult
    ): ResponseEntity<Response<Entry>> {
        val entry: Entry = Entry.fromDTO(entryDTO)
        val alreadyExists: Boolean = entryService.entryExists(entry)
        val response: Response<Entry> =
            getValidatedResponseInstance(alreadyExists = alreadyExists, shouldExists = false, result = result)
        val responseEntity: ResponseEntity<Response<Entry>> = ResponseEntity.status(response.status).body(response)

        if (response.errors.isEmpty()) response.data = entryService.save(entry)

        return responseEntity
    }

    @PutMapping("/{id}")
    fun put(
        @PathVariable id:String,
        @Valid @RequestBody entryDTO: EntryDTO,
        result: BindingResult
    ) : ResponseEntity<Response<Entry>> {
        entryDTO.id = id
        val entry: Entry = Entry.fromDTO(entryDTO)
        val alreadyExists: Boolean = entryService.entryExists(entry)
        val response: Response<Entry> = getValidatedResponseInstance(shouldExists = true, alreadyExists = alreadyExists, result = result)
        val responseEntity: ResponseEntity<Response<Entry>> = ResponseEntity.status(response.status).body(response)

        if(response.errors.isEmpty()) response.data = entryService.save(entry)

        return responseEntity
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: String
    ) : ResponseEntity<Response<Entry>>{
        val findableEntry = Entry(Date(), EntryType.DAY_ENDING, "", id = id)
        val alreadyExists: Boolean = entryService.entryExists(findableEntry)
        val response: Response<Entry> = getValidatedResponseInstance(alreadyExists = alreadyExists, shouldExists = true)

        if(response.errors.isEmpty()) {
            val completeEntry = entryService.findById(id)
            val deleted: Boolean = entryService.deleteById(id)

            if (deleted) response.data = completeEntry
            else {
                response.errors.add(HttpResponses.UNABLE_PERFORM_DELETE.getDescription())
                response.status = HttpResponses.UNABLE_PERFORM_DELETE.getCode()
            }
        }

        return ResponseEntity.status(response.status).body(response)
    }

}