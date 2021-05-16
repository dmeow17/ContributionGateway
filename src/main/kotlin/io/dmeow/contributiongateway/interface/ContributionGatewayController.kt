package io.dmeow.contributiongateway.`interface`

import io.dmeow.contributiongateway.domain.ContributionGateway
import io.dmeow.contributiongateway.domain.ValidationResult
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/contribution")
class ContributionGatewayController(
    private val contributionGateway: ContributionGateway,
    private val nowGenerator: () -> LocalDateTime,
) {
    private companion object {
        const val CCY_PAIR_VARIABLE = "{ccyPair}"
    }

    @GetMapping("/$CCY_PAIR_VARIABLE")
    fun getLatestQuote(@PathVariable ccyPair: String): FxQuoteDto =
        contributionGateway.findLatestQuote(ccyPair)?.toDto() ?: throw CcyPairNotFoundException(ccyPair)

    @GetMapping("/$CCY_PAIR_VARIABLE/all-quotes")
    fun getAllQuotes(@PathVariable ccyPair: String): List<FxQuoteDto> =
        contributionGateway.findFxQuotes(ccyPair)?.map { it.toDto() } ?: throw CcyPairNotFoundException(ccyPair)

    @PostMapping
    fun contributeFxQuote(@RequestBody fxQuoteInput: FxQuoteInputDto): ValidationResult {
        val fxQuote = fxQuoteInput.toDomain(nowGenerator())
        return contributionGateway.contribute(fxQuote).validationResult!!
    }

    class CcyPairNotFoundException(ccyPair: String) : ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "Currency pair [$ccyPair] cannot be found",
    )
}
