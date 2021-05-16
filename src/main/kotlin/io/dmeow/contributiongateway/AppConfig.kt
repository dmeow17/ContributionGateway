package io.dmeow.contributiongateway

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime

@Configuration
class AppConfig {
    @Bean
    fun nowGenerator(): () -> LocalDateTime = { LocalDateTime.now() }
}
