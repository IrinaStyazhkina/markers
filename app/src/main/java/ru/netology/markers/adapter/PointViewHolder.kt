package ru.netology.markers.adapter

import androidx.recyclerview.widget.RecyclerView
import ru.netology.markers.databinding.CardPointBinding
import ru.netology.markers.model.Point

class PointViewHolder(
    private val binding: CardPointBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(point: Point) {
        binding.apply {
            tvPointName.text = point.name
            tvLatitude.text = point.latitude.toString()
            tvLongitude.text = point.longitude.toString()

            tvPointName.setOnClickListener {
                onInteractionListener.onSelect(point)
            }

            btnEditPoint.setOnClickListener {
                onInteractionListener.onEdit(point)
            }
            btnDeletePoint.setOnClickListener {
                onInteractionListener.onRemove(point)
            }
        }
    }
}