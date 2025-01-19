package ru.netology.markers.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.markers.R
import ru.netology.markers.adapter.OnInteractionListener
import ru.netology.markers.adapter.PointsAdapter
import ru.netology.markers.databinding.FragmentPointsBinding
import ru.netology.markers.model.Point
import ru.netology.markers.ui.MapFragment.Companion.pointLatArg
import ru.netology.markers.ui.MapFragment.Companion.pointLongArg
import ru.netology.markers.viewModel.PointsViewModel

@AndroidEntryPoint
class PointsFragment: Fragment(){
    private val viewModel: PointsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPointsBinding.inflate(
            inflater,
            container,
            false,
        )

        val adapter = PointsAdapter(object : OnInteractionListener {
            override fun onRemove(point: Point) {
                viewModel.deleteById(point.id)
            }

            override fun onEdit(post: Point) {
                viewModel.edit(post)
                EditPointNameFragment().show(
                    childFragmentManager, EditPointNameFragment.TAG
                )
            }

            override fun onSelect(point: Point) {
                findNavController()
                    .navigate(
                        R.id.action_pointsFragment_to_mapFragment,
                        Bundle().apply {
                            pointLatArg = point.latitude
                            pointLongArg = point.longitude
                        }
                    )
            }
        })

        binding.list.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { state ->
            adapter.submitList(state.points)
            binding.tvEmptyText.isVisible = state.empty
        }

        val toMapButton = binding.toMap
        toMapButton.setOnClickListener {
            findNavController()
                .navigate(
                    R.id.action_pointsFragment_to_mapFragment,
                )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}