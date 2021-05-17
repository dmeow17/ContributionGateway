package io.dmeow.contributiongateway.interfaces.model

import io.dmeow.contributiongateway.domain.model.FxQuote
import java.time.LocalDateTime

class FxQuoteInputDto(
    val ccyPair: String,
    val bid: Double,
    val ask: Double,
)

class FxQuoteDto(
    val ccyPair: String,
    val bid: Double,
    val ask: Double,
    val lastUpdatedTime: LocalDateTime,
)

internal fun FxQuoteInputDto.toDomain(asOfTime: LocalDateTime): FxQuote = FxQuote(
    ccyPair = ccyPair,
    bid = bid,
    ask = ask,
    lastUpdatedTime = asOfTime,
)

internal fun FxQuote.toDto(): FxQuoteDto = FxQuoteDto(
    ccyPair = ccyPair,
    bid = bid,
    ask = ask,
    lastUpdatedTime = lastUpdatedTime,
)
