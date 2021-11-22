package com.example.burnestimation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.graphics.set
import androidx.core.net.UriCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.burnestimation.ml.Deeplabv3257MvGpu
import com.example.burnestimation.viewmodels.PatientViewModel
import com.example.burnestimation.viewmodels.PatientViewModelFactory
import org.tensorflow.lite.support.image.TensorImage
import java.io.File


/**
 * Receives database patient ID as argument,
 * Shows patient details and starts ML model.
 */
class PatientDetailFragment : Fragment() {

    // navigation arguments
    private val args: PatientDetailFragmentArgs by navArgs()

    // link to the database application
    private val patientViewModel: PatientViewModel by viewModels {
        PatientViewModelFactory((requireActivity().application as PatientsApplication).repository)
    }

    private lateinit var img: TensorImage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patient_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageView = view.findViewById<ImageView>(R.id.image_view)

        view.findViewById<Button>(R.id.run_model).setOnClickListener {
            // do deeplab
            val model = Deeplabv3257MvGpu.newInstance(requireContext())

            // Runs model inference and gets result.
            val outputs = model.process(img)

            println(outputs.segmentationMasksAsTensorBuffer.floatArray.indices)
//            for (i in (0..20)) {
//
//            }

//            Log.d(TAG, outputs.segmentationMasksAsTensorBuffer.shape)

//            outputs.

//            val segmentationMasks = outputs.segmentationMasksAsCategoryList

//            Log.d(TAG, segmentationMasks.size.toString())

            // Releases model resources if no longer used.
            model.close()

            // do manual color segmentation

        }

        patientViewModel.getPatient(args.patientID).observe(viewLifecycleOwner, Observer {
            Log.d(TAG, it.imageUri)

            // set image view
            try {
                Log.d(TAG, "from drawable resource (shipped with app) ${it.imageUri}")
                imageView.setImageResource(it.imageUri.toInt())
            } catch(e: NumberFormatException) {
                Log.d(TAG, "from user local app storage")

                // check if the file exists
                if ( ! File(it.imageUri).exists() ) {
                    Log.e(TAG, "File ${it.imageUri} not found!")
                    Toast.makeText(context, "ERROR: File NOT found!\n${it.imageUri}", Toast.LENGTH_LONG).show()
                    imageView.setImageResource(R.drawable.ic_baseline_error_outline_24)
                } else {
                    val bitmap = BitmapFactory.decodeFile(it.imageUri)
                    imageView.setImageBitmap(bitmap)
                    img = TensorImage.fromBitmap(bitmap)
                }
            }

            // set other patient info
            view.findViewById<TextView>(R.id.patient_name).text = it.name
            view.findViewById<TextView>(R.id.patient_height).text = resources.getString(R.string.p_info_height, it.height)
            view.findViewById<TextView>(R.id.patient_weight).text = resources.getString(R.string.p_info_weight, it.weight)
            view.findViewById<TextView>(R.id.title_surface_area).text = resources.getString(R.string.title_body_surface_area, "Du Bois")
            view.findViewById<TextView>(R.id.patient_total_skin_area).text = resources.getString(R.string.surface_area, it.bodySurfaceArea())
            view.findViewById<TextView>(R.id.patient_hospital_id).text = it.hospitalId
            view.findViewById<TextView>(R.id.patient_database_id).text = it.id.toString()
            view.findViewById<TextView>(R.id.institution).text = it.institution
            view.findViewById<TextView>(R.id.health_care_provider).text = it.attendingProvider

        })

        val cardPatient = view.findViewById<CardView>(R.id.card_patient)
        val arrowPatient = view.findViewById<ImageView>(R.id.arrow_button_patient)
        val hiddenGroupPatient = view.findViewById<LinearLayout>(R.id.table_patient)
        val hiddenDividerPatient = view.findViewById<View>(R.id.divider_patient)

        arrowPatient.setOnClickListener { _ ->
            if (hiddenGroupPatient.visibility == View.VISIBLE) {
                hiddenGroupPatient.visibility = View.GONE
                hiddenDividerPatient.visibility = View.GONE
                arrowPatient.setImageResource(R.drawable.ic_baseline_expand_less_24)
            } else {
                hiddenGroupPatient.visibility = View.VISIBLE
                hiddenDividerPatient.visibility = View.VISIBLE
                arrowPatient.setImageResource(R.drawable.ic_baseline_expand_more_24)
            }
        }

        val cardModel = view.findViewById<CardView>(R.id.card_model)
        val arrowModel = view.findViewById<ImageView>(R.id.arrow_button_model)
        val hiddenGroupModel = view.findViewById<LinearLayout>(R.id.hidden_view_model)
        val hiddenDividerModel = view.findViewById<View>(R.id.divider_model)

        arrowModel.setOnClickListener { _ ->
            if (hiddenGroupModel.visibility == View.VISIBLE) {
                hiddenGroupModel.visibility = View.GONE
                hiddenDividerModel.visibility = View.GONE
                arrowModel.setImageResource(R.drawable.ic_baseline_expand_less_24)
            } else {
                hiddenGroupModel.visibility = View.VISIBLE
                hiddenDividerModel.visibility = View.VISIBLE
                arrowModel.setImageResource(R.drawable.ic_baseline_expand_more_24)
            }
        }
    }

    companion object {
        private val TAG = PatientDetailFragment::class.java.simpleName


    }
}