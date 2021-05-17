package io.dmeow.contributiongateway.interfaces

import io.dmeow.contributiongateway.domain.ValidationService
import io.dmeow.contributiongateway.domain.model.ValidationResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/api/v1/validations")
class ValidationController(
    private val validationService: ValidationService,
) {
    @Operation(
        description = "Get validation result by UUID",
        parameters = [Parameter(`in` = ParameterIn.PATH, name = "uuid")],
        responses = [
            ApiResponse(
                responseCode = "200",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = ValidationResult::class),
                )],
            ),
            ApiResponse(responseCode = "404", description = "ValidationResult Not Found", content = [Content()]),
            ApiResponse(responseCode = "500", description = "Unexpected Error from service", content = [Content()]),
        ],
    )
    @GetMapping("/{uuid}")
    fun getValidationResult(@PathVariable("uuid") uuidStr: String): ValidationResult {
        val uuid = uuidStr.toUUID()
        return validationService.findValidationResult(uuid) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Validation result UUID [$uuidStr] not found",
        )
    }

    private fun String.toUUID(): UUID = try {
        UUID.fromString(this)
    } catch (e: Exception) {
        throw IllegalArgumentException("Invalid UUID: $this")
    }
}
