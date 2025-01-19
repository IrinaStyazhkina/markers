package ru.netology.markers.repository

import androidx.lifecycle.LiveData
import ru.netology.markers.model.Point

interface PointRepository {
    val data: LiveData<List<Point>>
    suspend fun save(point: Point)
    suspend fun removeById(id: Long)
}