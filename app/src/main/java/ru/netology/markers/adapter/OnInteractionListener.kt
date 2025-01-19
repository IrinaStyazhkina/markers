package ru.netology.markers.adapter

import ru.netology.markers.model.Point

interface OnInteractionListener {
    fun onEdit(point: Point) {}
    fun onRemove(point: Point) {}
    fun onSelect(point: Point) {}
}