package com.edcleidson.electronicclocking.utils.helpers

import com.edcleidson.electronicclocking.domain.enums.constants.HttpResponses
import com.edcleidson.electronicclocking.response.Response
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.validation.BindingResult

class BasicOperationsHelper {
    companion object {
        val bCryptPasswordEncoder = BCryptPasswordEncoder()
        const val API = "api"

        fun <T> getValidatedResponseInstance(
            alreadyExists: Boolean,
            shouldExists: Boolean,
            result: BindingResult? = null,
            response: Response<T> = Response()
        ): Response<T> {
            if (!shouldExists && alreadyExists) {
                response.errors.add(HttpResponses.ALREADY_EXISTS.getDescription())
                response.status = HttpResponses.ALREADY_EXISTS.getCode()
            } else if (shouldExists && !alreadyExists) {
                response.errors.add(HttpResponses.NOT_FOUND.getDescription())
                response.status = HttpResponses.NOT_FOUND.getCode()
            }
            if (result != null && result.hasErrors()) {
                result.allErrors.forEach {
                    response.errors.add(it.defaultMessage!!)
                }
                response.status = HttpResponses.BAD_REQUEST.getCode()
            }

            return response
        }
    }
}