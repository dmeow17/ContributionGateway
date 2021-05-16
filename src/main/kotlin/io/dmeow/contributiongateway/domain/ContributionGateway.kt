package io.dmeow.contributiongateway.domain

import org.springframework.stereotype.Service

@Service
class ContributionGateway(
    private val validationService: ValidationService,
) {
    private val fxQuotesByCcyPair = mutableMapOf<String, MutableList<FxQuote>>()

    fun findLatestQuote(ccyPair: String): FxQuote? = findFxQuotes(ccyPair)?.first()

    fun findFxQuotes(ccyPair: String): List<FxQuote>? =
        fxQuotesByCcyPair[ccyPair]?.sortedByDescending { it.lastUpdatedTime }

    fun contribute(quote: FxQuote): FxQuote {
        quote.validationResult = validationService.validate(quote)
        fxQuotesByCcyPair.putIfAbsent(quote.ccyPair, mutableListOf())
        fxQuotesByCcyPair.getValue(quote.ccyPair).add(quote)
        return quote
    }
}