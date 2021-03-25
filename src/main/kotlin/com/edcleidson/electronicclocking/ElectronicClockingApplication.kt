package com.edcleidson.electronicclocking

import com.edcleidson.electronicclocking.domain.Company
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class electronicclockingApplication

fun main(args: Array<String>) {
    val company = Company("Aleatory LTD", "122032142342")
    println(company)
    runApplication<electronicclockingApplication>(*args)
}
