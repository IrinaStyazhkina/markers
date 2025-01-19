package ru.netology.markers.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.netology.markers.model.Point

class PointDiffCallback: DiffUtil.ItemCallback<Point>() {
    override fun areItemsTheSame(oldItem: Point, newItem: Point): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Point, newItem: Point): Boolean {
        return oldItem == newItem
    }
}