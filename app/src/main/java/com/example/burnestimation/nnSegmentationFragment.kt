package com.example.burnestimation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import com.example.burnestimation.viewmodels.SegmentationViewModel

// Ignore warning that this class name starts with lower case
@Suppress("ClassName")
class nnSegmentationFragment : Fragment() {

    companion object {
        fun newInstance() = nnSegmentationFragment()
    }

    private val viewModel: SegmentationViewModel by activityViewModels()

    private lateinit var runModelButton: Button
    private lateinit var cancelButton: Button
    private lateinit var continueButton: Button
//    private lateinit var ...

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.nn_sementation_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        runModelButton = view.findViewById<Button>(R.id.button_run_model)
        cancelButton = view.findViewById<Button>(R.id.button_cancel)
        continueButton = view.findViewById<Button>(R.id.button_continue)




        setupButtons()
    }

    private fun setupButtons() {

        runModelButton.setOnClickListener {
            // execute model
        }

        cancelButton.setOnClickListener {
            // action to previous fragment
        }

        continueButton.setOnClickListener {
            // action to burn segmentation fragment
        }

    }

}