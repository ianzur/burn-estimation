package com.example.burnestimation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.burnestimation.viewmodels.SegmentationViewModel

class ColorThresholdSegmentationFragment : Fragment() {

    companion object {
        fun newInstance() = ColorThresholdSegmentationFragment()
    }

    // delegate class
    private val viewModel: SegmentationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.color_threshold_segmentation_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

}