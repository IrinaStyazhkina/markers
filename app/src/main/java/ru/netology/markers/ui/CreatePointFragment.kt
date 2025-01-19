package ru.netology.markers.ui

import ru.netology.markers.R


import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.markers.model.Point
import ru.netology.markers.viewModel.PointsViewModel

@AndroidEntryPoint
class CreatePointFragment : DialogFragment() {
    private val viewModel: PointsViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inputView: View = layoutInflater.inflate(R.layout.fragment_create_point, null)

        return AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.create_point_title))
            .setView(inputView)
            .setPositiveButton(getString(R.string.create)) { dialog, _ ->
                val editField = inputView.findViewById<EditText>(R.id.tvCreatePoint)
                val pointName = editField.text
                val currentPoint = viewModel.edited.value ?: Point(
                    id = 0,
                    name = pointName.toString(),
                    latitude = 0.0,
                    longitude = 0.0,
                )
                viewModel.edit(currentPoint.copy(name = pointName.toString()))
                viewModel.save()
                Log.d("POINT", pointName.toString())
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                viewModel.cancelEdit()
                dialog.dismiss()
            }
            .create()
    }

    companion object {
        const val TAG = "CreatePointDialog"
    }
}