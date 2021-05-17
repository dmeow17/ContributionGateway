package io.dmeow.contributiongateway.domain

import io.dmeow.contributiongateway.domain.model.FxQuote
import io.dmeow.contributiongateway.domain.model.ValidationResult
import java.util.*

interface ValidationService {
    fun findValidationResult(uuid: UUID): ValidationResult?
    fun validate(quote: FxQuote): ValidationResult
}
