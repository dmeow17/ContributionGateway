package io.dmeow.contributiongateway.domain

import io.dmeow.contributiongateway.domain.ValidationResult.Status.SUCCEEDED
import org.springframework.stereotype.Service
import java.util.*

@Service
class ValidationService {
    fun findValidationResult(uuid: UUID): ValidationResult? = TODO()

    fun validate(quote: FxQuote): ValidationResult = ValidationResult(
        uuid = UUID.randomUUID(),
        status = SUCCEEDED,
    )
}
