package de.hexad.hexadtimetracker.models

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Embeddable

@Embeddable
class CompositeKey(
        val pk: Int? = null,
        val timestamp: LocalDateTime? = LocalDateTime.now()
) : Serializable