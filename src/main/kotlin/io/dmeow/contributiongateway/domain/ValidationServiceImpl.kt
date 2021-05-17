package io.dmeow.contributiongateway.domain

import io.dmeow.contributiongateway.domain.model.FxQuote
import io.dmeow.contributiongateway.domain.model.ValidationResult
import io.dmeow.contributiongateway.domain.model.ValidationResult.Status.SUCCEEDED
import org.springframework.stereotype.Service
import java.util.*

// Can be implemented/swapped with prod component later
@Service
class ValidationServiceImpl : ValidationService {
    override fun findValidationResult(uuid: UUID): ValidationResult? = TODO()

    override fun validate(quote: FxQuote): ValidationResult = ValidationResult(
        uuid = UUID.randomUUID(),
        status = SUCCEEDED,
    )
}
