package io.dmeow.contributiongateway.`interface`

import io.dmeow.contributiongateway.domain.ValidationResult
import io.dmeow.contributiongateway.domain.ValidationService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/api/v1/validation")
class ValidationController(
    private val validationService: ValidationService,
) {
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