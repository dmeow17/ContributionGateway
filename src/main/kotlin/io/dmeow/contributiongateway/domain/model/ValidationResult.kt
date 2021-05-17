package io.dmeow.contributiongateway.domain.model

import java.util.*

data class ValidationResult(
    val uuid: UUID,
    val status: Status,
) {
    enum class Status { SUCCEEDED, FAILED }
}
