package de.hexad.hexadtimetracker.models

import org.springframework.data.util.ProxyUtils
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class AbstractJpaPersistable {

    @Id
    @Column(name = "pk")
    @GeneratedValue
    private var pk: Int? = null
        get() = field

    @Column(name = "timestamp")
    val timestamp: LocalDateTime = LocalDateTime.now()

    override fun equals(other: Any?): Boolean {
        other ?: return false

        if (this === other) return true
        if (javaClass != ProxyUtils.getUserClass(other)) return false
        other as AbstractJpaPersistable

        return this.pk == other.pk && this.pk != null
    }

    override fun hashCode(): Int = Objects.hash(pk)

    override fun toString() = "Entity of type ${javaClass.name} with pk: $pk"
}