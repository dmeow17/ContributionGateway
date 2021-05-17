package io.dmeow.contributiongateway.domain

import io.dmeow.contributiongateway.domain.model.FxQuote
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ContributionGateway(
    private val validationService: ValidationService,
) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val fxQuotesByCcyPair = mutableMapOf<String, MutableList<FxQuote>>()

    fun findLatestQuote(ccyPair: String): FxQuote? = findFxQuotes(ccyPair)?.first()

    fun findFxQuotes(ccyPair: String): List<FxQuote>? =
        fxQuotesByCcyPair[ccyPair]?.sortedByDescending { it.lastUpdatedTime }

    fun contribute(quote: FxQuote): FxQuote {
        log.info("Contributing FxQuote [${quote.ccyPair}]")
        quote.validationResult = validationService.validate(quote)
        fxQuotesByCcyPair.putIfAbsent(quote.ccyPair, mutableListOf())
        fxQuotesByCcyPair.getValue(quote.ccyPair).add(quote)
        log.info("Contributed FxQuote [${quote.ccyPair}]")
        return quote
    }
}