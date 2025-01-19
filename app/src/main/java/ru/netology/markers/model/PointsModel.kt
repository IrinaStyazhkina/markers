package ru.netology.markers.model

import ru.netology.markers.model.Point

data class PointsModel(
    val points: List<Point> = emptyList(),
    val empty: Boolean = false,
)