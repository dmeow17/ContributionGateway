package io.dmeow.contributiongateway.domain

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class ContributionGatewayTest {
    private val validationService: ValidationService = mockk()
    private var now = LocalDateTime.of(2021, 5, 17, 12, 0)

    private lateinit var gateway: ContributionGateway

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        gateway = ContributionGateway(validationService = validationService)
        every { validationService.validate(any()) } returns mockk()
    }

    @Test
    fun `test contribute, when user contributes 1 fxQuote, expect single fxQuote can be retrieved`() {
        val validationResult: ValidationResult = mockk()
        every { validationService.validate(any()) } returns validationResult

        val ccyPair = "HKDUSD"
        val fxQuote = FxQuote(ccyPair = ccyPair, bid = 5.4, ask = 3.2, lastUpdatedTime = now)
        gateway.contribute(fxQuote)

        val allQuotes = gateway.findFxQuotes(ccyPair)!!
        val latestQuote = gateway.findLatestQuote(ccyPair)!!

        assertThat(allQuotes).hasSize(1)
        assertThat(allQuotes[0])
            .isEqualTo(fxQuote.copy(validationResult = validationResult))
            .isEqualTo(latestQuote.copy(validationResult = validationResult))
    }

    @Test
    fun `test contribute, when user contributes 2 fxQuotes with same ccyPair, expect 2 fxQuotes can be retrieved and latest one is correct`() {
        val ccyPair = "HKDUSD"
        val fxQuote = FxQuote(ccyPair = ccyPair, bid = 2.4, ask = 3.2, lastUpdatedTime = now)
        gateway.contribute(fxQuote)
        val newFxQuote = FxQuote(ccyPair = ccyPair, bid = 2.6, ask = 3.0, lastUpdatedTime = now.plusMinutes(1))
        gateway.contribute(newFxQuote)

        val allQuotes = gateway.findFxQuotes(ccyPair)!!
        val latestQuote = gateway.findLatestQuote(ccyPair)!!

        assertThat(allQuotes)
            .usingElementComparatorIgnoringFields("validationResult")
            .containsExactly(newFxQuote, fxQuote)
        assertThat(latestQuote)
            .usingRecursiveComparison()
            .ignoringFields("validationResult")
            .isEqualTo(newFxQuote)
    }

    @Test
    fun `test contribute, when user contributes 2 different fxQuotes, expect 2 fxQuotes can be retrieved accordingly`() {
        val hkdFxQuote = FxQuote(ccyPair = "HKDUSD", bid = 2.4, ask = 3.2, lastUpdatedTime = now)
        gateway.contribute(hkdFxQuote)
        val gbpFxQuote = FxQuote(ccyPair = "GBPUSD", bid = 2.6, ask = 3.0, lastUpdatedTime = now.plusMinutes(1))
        gateway.contribute(gbpFxQuote)

        val hkdQuotes = gateway.findFxQuotes("HKDUSD")!!
        val gbpQuotes = gateway.findFxQuotes("GBPUSD")!!

        assertThat(hkdQuotes)
            .hasSize(1)
            .singleElement()
            .usingRecursiveComparison()
            .ignoringFields("validationResult")
            .isEqualTo(hkdFxQuote)

        assertThat(gbpQuotes)
            .hasSize(1)
            .singleElement()
            .usingRecursiveComparison()
            .ignoringFields("validationResult")
            .isEqualTo(gbpFxQuote)
    }
}
