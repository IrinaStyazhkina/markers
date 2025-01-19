package ru.netology.markers.ui
import ru.netology.markers.R

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.markers.viewModel.PointsViewModel

@AndroidEntryPoint
class EditPointNameFragment : DialogFragment() {
    private val viewModel: PointsViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inputView: View = layoutInflater.inflate(R.layout.fragment_edit_point_name, null)
        val point = viewModel.edited.value ?: throw RuntimeException("No point provided")
        val editField = inputView.findViewById<EditText>(R.id.tvEditPointName)
        val latitudeField = inputView.findViewById<EditText>(R.id.tvEditPointLatitude)
        val longitudeField = inputView.findViewById<EditText>(R.id.tvEditPointLongitude)
        editField.setText(point.name)
        latitudeField.setText(point.latitude.toString())
        longitudeField.setText(point.longitude.toString())

        return AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.edit_point_title))
            .setView(inputView)
            .setPositiveButton(getString(R.string.save)) { dialog, _ ->
                val pointName = editField.text
                val latitude = latitudeField.text
                val longitude = longitudeField.text
                viewModel.edit(point.copy(
                    name = pointName.toString(),
                    latitude = if(latitude !== null) latitude.toString().toDouble() else point.latitude,
                    longitude = if(longitude !== null) longitude.toString().toDouble() else point.longitude
                ))
                viewModel.save()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                viewModel.cancelEdit()
                dialog.dismiss()
            }
            .create()
    }

    companion object {
        const val TAG = "EditPointNameDialog"
    }
}