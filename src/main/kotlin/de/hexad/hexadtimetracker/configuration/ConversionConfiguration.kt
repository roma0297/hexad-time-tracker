package de.hexad.hexadtimetracker.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class ConversionConfiguration(private val converters: List<Converter<Any, Any>>) : WebMvcConfigurer {
    override fun addFormatters(registry: FormatterRegistry) {
        converters.forEach {
            registry.addConverter(it)
        }
    }
}