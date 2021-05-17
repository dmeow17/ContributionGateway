package io.dmeow.contributiongateway.interfaces

import io.dmeow.contributiongateway.domain.ContributionGateway
import io.dmeow.contributiongateway.domain.model.ValidationResult
import io.dmeow.contributiongateway.interfaces.model.FxQuoteDto
import io.dmeow.contributiongateway.interfaces.model.FxQuoteInputDto
import io.dmeow.contributiongateway.interfaces.model.toDomain
import io.dmeow.contributiongateway.interfaces.model.toDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/quotes")
class ContributionGatewayController(
    private val contributionGateway: ContributionGateway,
    private val nowGenerator: () -> LocalDateTime,
) {
    private companion object {
        const val CCY_PAIR_VARIABLE = "{ccyPair}"
    }

    @Operation(
        responses = [
            ApiResponse(
                responseCode = "200",
                content = [Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = FxQuoteDto::class),
                )],
            ),
            ApiResponse(responseCode = "404", content = [Content()]),
        ],
    )
    @GetMapping("/$CCY_PAIR_VARIABLE")
    fun getLatestQuote(@PathVariable ccyPair: String): FxQuoteDto =
        contributionGateway.findLatestQuote(ccyPair)?.toDto() ?: throw CcyPairNotFoundException(ccyPair)

    @Operation(
        responses = [
            ApiResponse(
                responseCode = "200",
                content = [Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    array = ArraySchema(schema = Schema(implementation = FxQuoteDto::class))
                )],
            ),
            ApiResponse(responseCode = "404", content = [Content()]),
        ],
    )
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
