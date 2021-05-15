package io.dmeow.contributiongateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ContributionGatewayApplication

fun main(args: Array<String>) {
	runApplication<ContributionGatewayApplication>(*args)
}
