package ru.netology.markers.repository

import android.util.Log
import androidx.lifecycle.LiveData
import ru.netology.markers.db.PointDao
import ru.netology.markers.entity.PointEntity
import ru.netology.markers.model.Point
import androidx.lifecycle.map
import javax.inject.Inject

class PointRepositoryImpl @Inject constructor(private val pointDao: PointDao) : PointRepository {

    override val data: LiveData<List<Point>> = pointDao.getAll().map {
        it.map(PointEntity::toDto)
    }

    override suspend fun removeById(id: Long) {
        pointDao.removeById(id)
    }

    override suspend fun save(point: Point) {
        pointDao.save(PointEntity.fromDto(point))
    }
}