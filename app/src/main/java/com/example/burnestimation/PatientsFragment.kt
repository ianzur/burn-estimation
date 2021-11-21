package com.example.burnestimation

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.burnestimation.adapters.PatientListAdapter
import com.example.burnestimation.viewmodels.PatientViewModel
import com.example.burnestimation.viewmodels.PatientViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

const val TAG = "patientsFragment"
/*
 * This class is the start (home) fragment containing:
 *   - scrollable list of previously recorded participants
 *   - floating action button to create a new participant
 *   - options menu: [about]
 */
class PatientsFragment : Fragment() {

    // link to the single database application
    private val patientViewModel: PatientViewModel by viewModels {
        PatientViewModelFactory((requireActivity().application as PatientsApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d(TAG, "onCreateView")

        val view = inflater.inflate(R.layout.fragment_patients, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = PatientListAdapter()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        patientViewModel.allPatients.observe(requireActivity()) { patients ->
            // Update the cached copy of the words in the adapter.
            patients.let { adapter.submitList(it) }
        }

        setHasOptionsMenu(true)

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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.patients_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
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