package ru.netology.markers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.netology.markers.databinding.CardPointBinding
import ru.netology.markers.model.Point

class PointsAdapter(private val onInteractionListener: OnInteractionListener): ListAdapter<Point, PointViewHolder>(PointDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointViewHolder {
        val binding = CardPointBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PointViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PointViewHolder, position: Int) {
        val point = getItem(position) ?: return
        holder.bind(point)
    }
}