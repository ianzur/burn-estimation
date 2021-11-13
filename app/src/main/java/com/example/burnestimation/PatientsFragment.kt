package com.example.burnestimation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.burnestimation.adapters.PatientAdapter
import com.example.burnestimation.data.Datasource
import com.google.android.material.floatingactionbutton.FloatingActionButton

const val TAG = "patientsFragment"
/*
 * This class is the start (home) fragment containing:
 *   - scrollable list of previously recorded participants
 *   - floating action button to create a new participant
 *   - options menu: [about]
 */
class PatientsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val myDataset = Datasource().loadPatients()

        val view = inflater.inflate(R.layout.fragment_patients, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = PatientAdapter(requireContext(), myDataset)
        recyclerView.setHasFixedSize(true)

        return view
    }

    /**
     * setup floating action button, options menu, and search buttons
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOptionsMenu()
        setupFab()
    }

    /**
     * Setup options menu
     */
    private fun setupOptionsMenu() {

    }

    /**
     * Setup floating action button
     * onClickListener navigates to add new patient fragment.
     */
    private fun setupFab() {
        requireView().findViewById<FloatingActionButton>(R.id.new_patient_fab)?.let {
            it.setOnClickListener {
                navigateToAddNewTask()
            }
        }
    }

    private fun navigateToAddNewTask() {
        val action = PatientsFragmentDirections.actionPatientsFragmentToPatientNewFragment()
        findNavController().navigate(action)
    }

}