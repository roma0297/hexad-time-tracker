package de.hexad.hexadtimetracker.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfiguration() {

    @Bean
    fun zohoApiWebClient () = WebClient.builder().baseUrl("https://people.zoho.com/people/api").build()

    @Bean
    fun zohoAuthTokenWebClient () = WebClient.builder().baseUrl("https://accounts.zoho.com").build()
}