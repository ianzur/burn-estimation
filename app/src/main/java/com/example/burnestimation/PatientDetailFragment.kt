package com.example.burnestimation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
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
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.segmenter.ImageSegmenter
import java.io.File
import kotlin.properties.Delegates
import kotlin.random.Random
import kotlin.random.nextUInt


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

        view.findViewById<TextView>(R.id.result_text).text = "Est. Total Burn Skin Area: --%"

        view.findViewById<Button>(R.id.run_model).setOnClickListener {
            // do deeplab

            val baseOptionsBuilder = BaseOptions.builder()

            val options =
                ImageSegmenter.ImageSegmenterOptions.builder()
                    .setBaseOptions(baseOptionsBuilder.setNumThreads(4).build())
                    .build()
            val imageSegmenter: ImageSegmenter =
                ImageSegmenter.createFromFileAndOptions(context, "deeplabv3_257_mv_gpu.tflite", options)

            // Runs model inference and gets result.
            val outputs = imageSegmenter.segment(img)

            val origWidth = img.width
            val origHeight = img.height
//            println(outputs[0].outputType)
//            println(outputs[0].masks)
            println(outputs[0].coloredLabels)

            // For the sake of this demo, change the alpha channel from 255 (completely opaque) to 128
            // (semi-transparent), because the maskBitmap will be stacked over the original image later.
            val coloredLabels = outputs[0].coloredLabels
            var colors = IntArray(coloredLabels.size)
            var cnt = 0
            for (coloredLabel in coloredLabels) {
                val rgb = coloredLabel.argb
                val label = coloredLabel.getlabel()
                println("${label} = (R=${Color.red(rgb)}, G=${Color.green(rgb)}, B=${Color.blue(rgb)})")
                colors[cnt++] = Color.argb(128, Color.red(rgb), Color.green(rgb), Color.blue(rgb))
            }
            // Use completely transparent for the background color.
            colors[0] = Color.TRANSPARENT

            println("A=${Color.alpha(colors[0])}, R=${Color.red(colors[0])}, G=${Color.green(colors[0])}, B=${Color.blue(colors[0])})")

            // Create the mask bitmap with colors and the set of detected labels.
            val maskTensor = outputs[0].masks[0]
            val maskArray = maskTensor.buffer.array()
            val pixels = IntArray(maskArray.size)
            val itemsFound = HashMap<String, Int>()
            for (i in maskArray.indices) {
                val color = colors[maskArray[i].toInt()]

                pixels[i] = when(coloredLabels[maskArray[i].toInt()].getlabel()) {
                    "person" -> Color.TRANSPARENT
                    else -> Color.argb(255, 0, 0, 0)
                }
//                if (coloredLabels[maskArray[i].toInt()].getlabel() != "person")
//                    pixels[i] = 0
//                else

//                itemsFound[coloredLabels[maskArray[i].toInt()].getlabel()] = color
            }
            val maskBitmap =
                Bitmap.createBitmap(
                    pixels,
                    maskTensor.height,
                    maskTensor.width,
                    Bitmap.Config.ARGB_8888
                )

            val resized = Bitmap.createScaledBitmap(
                maskBitmap, origWidth, origHeight, true);

            val orig = img.bitmap

            // merge mask and image
            val mergedBitmap =
                Bitmap.createBitmap(orig.width, orig.height, orig.config)
            val canvas = Canvas(mergedBitmap)
            canvas.drawBitmap(orig, 0.0f, 0.0f, null)
            canvas.drawBitmap(resized, 0.0f, 0.0f, null)
            imageView.setImageBitmap(mergedBitmap)

            // Releases model resources if no longer used.
            imageSegmenter.close()

            view.findViewById<TextView>(R.id.result_text).text = getString(R.string.total_burn_skin_area, Random.nextInt(8, 45))

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