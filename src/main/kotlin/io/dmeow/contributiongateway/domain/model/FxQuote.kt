package io.dmeow.contributiongateway.domain.model

import java.time.LocalDateTime

data class FxQuote(
    val ccyPair: String,
    val bid: Double,
    val ask: Double,
    var validationResult: ValidationResult? = null,
    val lastUpdatedTime: LocalDateTime = LocalDateTime.now(),
) {
    init {
        require(bid >= 0.0 && ask >= 0.0) { "High/low prices must be non-negative" }
    }
}
