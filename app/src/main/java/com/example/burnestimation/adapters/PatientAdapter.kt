package com.example.burnestimation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
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
class PatientAdapter(
    private val context: Context,
    private val dataset: List<Patient>
) : RecyclerView.Adapter<PatientAdapter.PatientViewHolder>() {

    class PatientViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val patientID: TextView = view.findViewById(R.id.list_item_patientID)
        val recordingDate: TextView = view.findViewById(R.id.list_item_recordingDate)
        val card: MaterialCardView = view.findViewById(R.id.card)
    }

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
        val patient = dataset[position]
        holder.patientID.text = patient.id
        holder.recordingDate.text = patient.date

        holder.card.setOnClickListener {
            val action = PatientsFragmentDirections.actionPatientsFragmentToPatientDetailFragment(patientID = patient.id)
            holder.view.findNavController().navigate(action)
        }
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    override fun getItemCount() = dataset.size
}

