package de.hexad.hexadtimetracker.models

import org.springframework.data.util.ProxyUtils
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@MappedSuperclass
@IdClass(CompositeKey::class)
abstract class AbstractJpaPersistable {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private var id: Int? = null
        get() = field

    @Id
    @Column(name = "timestamp")
    val timestamp: LocalDateTime = LocalDateTime.now()

    override fun equals(other: Any?): Boolean {
        other ?: return false

        if (this === other) return true
        if (javaClass != ProxyUtils.getUserClass(other)) return false
        other as AbstractJpaPersistable

        return this.id == other.id && this.id != null
    }

    override fun hashCode(): Int = Objects.hash(id)

    override fun toString() = "Entity of type ${javaClass.name} with id: $id"
}