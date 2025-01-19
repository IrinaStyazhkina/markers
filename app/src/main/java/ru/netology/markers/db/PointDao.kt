package ru.netology.markers.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.markers.entity.PointEntity

@Dao
interface PointDao {

    @Query("SELECT * FROM PointEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PointEntity>>

    @Insert
    suspend fun insert(point: PointEntity)

    @Query("UPDATE PointEntity SET name=:name, latitude=:latitude, longitude=:longitude WHERE id=:id")
    suspend fun updatePointById(id: Long, name: String, latitude: Double, longitude: Double)

    suspend fun save(point: PointEntity) = if(point.id == 0L) insert(point) else updatePointById(point.id, point.name, point.latitude, point.longitude)

    @Query("DELETE FROM PointEntity WHERE id=:id")
    suspend fun removeById(id: Long)

}