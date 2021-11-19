package com.example.burnestimation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.burnestimation.PatientsFragment
import com.example.burnestimation.PatientsFragmentDirections
import com.example.burnestimation.R
import com.example.burnestimation.datamodel.Patient
import com.google.android.material.card.MaterialCardView


// TODO: handle click, long press, and delete patient
/**
 * Patient adaptor, used to populate recyclerView and show patient cards
 */
class PatientListAdapter : ListAdapter<Patient, PatientListAdapter.PatientViewHolder>(PatientComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        return PatientViewHolder(adapterLayout)
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        val patient = getItem(position)
        val context = holder.card.context

        holder.databaseID.text = context.getString(R.string.db_id, patient.id.toString())
        holder.patientID.text = context.getString(R.string.patient_id, patient.hospitalId)
        holder.recordingDate.text = context.getString(R.string.card_date, patient.date)

        holder.card.setOnClickListener {
            val action = PatientsFragmentDirections.actionPatientsFragmentToPatientDetailFragment(patientID = patient.hospitalId)
            holder.view.findNavController().navigate(action)
        }
    }

    class PatientViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val databaseID: TextView = view.findViewById(R.id.list_item_databaseID)
        val patientID: TextView = view.findViewById(R.id.list_item_patientID)
        val recordingDate: TextView = view.findViewById(R.id.list_item_recordingDate)
        val card: MaterialCardView = view.findViewById(R.id.card)
    }

    class PatientComparator : DiffUtil.ItemCallback<Patient>() {
        override fun areItemsTheSame(oldItem: Patient, newItem: Patient): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Patient, newItem: Patient): Boolean {
            return oldItem.id == newItem.id
        }
    }
}

