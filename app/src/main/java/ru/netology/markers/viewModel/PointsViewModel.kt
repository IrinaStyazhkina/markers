package ru.netology.markers.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.markers.model.Point
import ru.netology.markers.model.PointsModel
import ru.netology.markers.repository.PointRepository
import javax.inject.Inject

private val empty = Point(
    id= 0L,
    name = "",
    latitude = 0.0,
    longitude = 0.0,
)

@HiltViewModel
class PointsViewModel @Inject constructor(private val repository: PointRepository ): ViewModel(){

    val data: LiveData<PointsModel> = repository.data.map {
        PointsModel(
            points = it,
            empty = it.isEmpty()
        )
    }

    private val _state = MutableLiveData(PointsModel())
    val state: LiveData<PointsModel>
        get() = _state

    private val _edited = MutableLiveData(empty)
    val edited: LiveData<Point>
        get() = _edited

    fun save() {
        viewModelScope.launch {
            _edited.value?.let {
                repository.save(it)
            }
            _edited.value = empty
        }
    }

    fun edit(point: Point) {
        _edited.value = point
    }

    fun cancelEdit() {
        _edited.value = empty
    }

    fun deleteById(id: Long) {
        viewModelScope.launch {
            repository.removeById(id)
        }
    }
}