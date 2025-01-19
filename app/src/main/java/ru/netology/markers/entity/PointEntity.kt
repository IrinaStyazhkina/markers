package ru.netology.markers.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.markers.model.Point

@Entity
data class PointEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val latitude: Double,
    val longitude: Double,
) {
    fun toDto() = Point(id, name, latitude, longitude)

    companion object {
        fun fromDto(dto: Point) =
            PointEntity(dto.id, dto.name, dto.latitude, dto.longitude)
    }
}
