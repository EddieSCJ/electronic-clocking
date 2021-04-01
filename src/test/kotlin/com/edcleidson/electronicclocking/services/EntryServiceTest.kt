package com.edcleidson.electronicclocking.services

import com.edcleidson.electronicclocking.domain.Entry
import com.edcleidson.electronicclocking.domain.enums.EntryType
import com.edcleidson.electronicclocking.repositories.EntryRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*
import kotlin.collections.ArrayList

@SpringBootTest
class EntryServiceTest {

    @Autowired
    private val entryService: EntryService? = null

    @MockBean
    private val entryRepository: EntryRepository? = null

    private val ID: String = "12h3bui21ebhi123b"
    private val EMPLOYEE_ID = "fsduhf13b4ubwqbwedfs"

    private val WRONG_ID = "fuhsi9dufhi12i4"
    private val WRONG_EMPLOYEE_ID = "gfisudhuwnr3e45"

    @BeforeEach
    @Throws(Exception::class)
    fun setUp() {
        BDDMockito.given(entryRepository?.findById(ID)).willReturn(Optional.of(entry()))
        BDDMockito.given<Page<Entry>>(entryRepository?.findByEmployeeId(EMPLOYEE_ID, PageRequest.of(0, 10)))
            .willReturn(PageImpl(Arrays.asList(entry())))
        BDDMockito.given<Page<Entry>>(entryRepository?.findByEmployeeId(WRONG_EMPLOYEE_ID, PageRequest.of(0, 10)))
            .willReturn(PageImpl(ArrayList()))
        BDDMockito.given(entryRepository?.save(entry())).willReturn(entry())
        BDDMockito.given(entryRepository?.findByIdOrEmployeeId(ID, EMPLOYEE_ID)).willReturn(arrayListOf())

    }

    @Test
    fun shouldReturnAPageWhenFindAll() {
        val page: Page<Entry>? = entryService?.findAll(0, 10, "type", "ASC")
        assertNotNull(page)
    }

    @Test
    fun shouldFindAnEntryByID() {
        val entry: Entry? = entryService?.findById(ID)
        assertNotNull(entry)
    }

    @Test
    fun shouldFindAnEntryByIDOrEmployeeId() {
        val entry: List<Entry>? = entryService?.findByIdOrEmployeeId(ID, EMPLOYEE_ID)
        assertNotNull(entry)
    }

    @Test
    fun shouldReturnEmptyListWhenFindByWrongIDOrEmployeeId() {
        val entry: List<Entry>? = entryService?.findByIdOrEmployeeId(WRONG_ID, WRONG_EMPLOYEE_ID)
        assertTrue(entry!!.isEmpty())
    }

    @Test
    fun shouldReturnNullWhenFindAnEntryByWrongID() {
        val entry: Entry? = entryService?.findById(WRONG_ID)
        assertNull(entry)
    }

    @Test
    fun shouldFindAnEmployeePageByEmployeeID() {
        val entriesPage: Page<Entry>? = entryService?.findByEmployeeId(EMPLOYEE_ID, PageRequest.of(0, 10))
        val content: List<Entry> = entriesPage?.content as List<Entry>
        val isEmpty = content.isEmpty()

        assertNotNull(entriesPage)
        assertFalse(isEmpty)
    }

    @Test
    fun shouldReturnEmptyPageContentByWrongEmployeeID() {
        val entriesPage: Page<Entry>? = entryService?.findByEmployeeId(WRONG_EMPLOYEE_ID, PageRequest.of(0, 10))
        println(entriesPage)
        val content: List<Entry> = entriesPage?.content as List<Entry>
        val isEmpty = content.isEmpty()

        assertNotNull(entriesPage)
        assertTrue(isEmpty)
    }

    @Test
    fun shouldSaveAnEntryAndReturn() {
        val entry: Entry? = entryService?.save(entry())
        assertNotNull(entry)
    }

    @Test
    fun shouldDeleteAnEntry() {
        entryService?.deleteById(ID)
        verify(entryRepository, times(1))?.deleteById(ID)
    }

    @Test
    fun shouldReturnTrueWhenVerifyIfEntryExists() {
        assertNotNull(entryService)

        val entryExists: Boolean = entryService!!.entryExists(entry())
        assertTrue(entryExists)
    }

    private fun entry(): Entry = Entry(Date(), EntryType.DAY_STARTING, EMPLOYEE_ID, "", "", ID)

}