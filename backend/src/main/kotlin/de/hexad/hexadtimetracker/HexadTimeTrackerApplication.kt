package de.hexad.hexadtimetracker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HexadTimeTrackerApplication

fun main(args: Array<String>) {
	runApplication<HexadTimeTrackerApplication>(*args)
}
