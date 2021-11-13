package com.example.burnestimation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController

// TODO: read input data, create new patient, add to dataset.
/**
 * New patient fragment
 * set new patient info here before taking picture for Burn Area Estimation
 */
class PatientNewFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patient_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val genIDButton = view.findViewById<Button>(R.id.buttonGenerateID)
        val pIDField = view.findViewById<TextView>(R.id.patientID)
        genIDButton.setOnClickListener {
            pIDField.text = generateID(12)
        }

        // record patient data and launch fullscreen camera widget
        val cameraBtn = view.findViewById<Button>(R.id.buttonCamera)
        cameraBtn.setOnClickListener {
            // TODO: check required fields
            // TODO: create full screen camera application, launch explicit intent
            Toast.makeText(requireContext(), "launch camera widget", Toast.LENGTH_SHORT).show()
        }

        // cancel new patient create
        val cancelBtn = view.findViewById<Button>(R.id.buttonCancel)
        cancelBtn.setOnClickListener {
            val action = PatientNewFragmentDirections.actionPatientNewFragmentToPatientsFragment()
            view.findNavController().navigate(action)
        }
    }

    // TODO: check that this ID is not a key in local database (non-existent)
    private fun generateID(length: Int = 12) : String {
        val charPool = ('A'..'Z') +  ('0'..'9')
        return (1..length)
            .map { charPool.random() }
            .joinToString("")
    }

    companion object {

    }
}