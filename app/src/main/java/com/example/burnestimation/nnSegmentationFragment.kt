package com.example.burnestimation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.burnestimation.viewmodels.nnSegmentationViewModel

// Ignore warning that this class name starts with lower case
@Suppress("ClassName")
class nnSegmentationFragment : Fragment() {

    companion object {
        fun newInstance() = nnSegmentationFragment()
    }

    private lateinit var viewModel: nnSegmentationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.nn_sementation_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(nnSegmentationViewModel::class.java)
        // TODO: Use the ViewModel
    }

}