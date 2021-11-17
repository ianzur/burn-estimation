package com.example.burnestimation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.burnestimation.datamodel.Patient
import com.example.burnestimation.viewmodels.PatientViewModel
import com.example.burnestimation.viewmodels.PatientViewModelFactory
import com.google.android.material.snackbar.Snackbar

// TODO: read input data, create new patient, add to dataset / local database.
/**
 * New patient fragment
 * set new patient info here before taking picture for Burn Area Estimation
 */
class PatientNewFragment : Fragment() {
//
//    var db: DatabaseHandler = DatabaseHandler(activity)
    private val model: PatientViewModel by activityViewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
            } else {
                Log.i("Permission: ", "Denied")
            }
        }

    private fun requestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission has already been granted
                Log.d("Permission: ", "Permissions have already been granted")
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.camera_permission_required),
                    Snackbar.LENGTH_LONG
                ).show()
                requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA)
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA)
            }
        }

    }

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

        // request camera permissions
        requestPermission()



        val cameraManager =
            requireContext().getSystemService(Context.CAMERA_SERVICE) as CameraManager

        val genIDButton = view.findViewById<Button>(R.id.buttonGenerateID)
        val pIDField = view.findViewById<TextView>(R.id.patientID)
        genIDButton.setOnClickListener {
            pIDField.text = generateID(12)
        }

        // record patient data and launch fullscreen camera widget
        val cameraBtn = view.findViewById<Button>(R.id.buttonCamera)
        cameraBtn.setOnClickListener {
            // TODO: check required fields

            Log.d("cameraBtn: ", "about to insert patient")
            val patient = Patient(pIDField.text.toString())

            Log.d("cameraBtn: ", "pre patient inserted")

            model.insert(patient)

            Log.d("cameraBtn: ", "post patient inserted")


            // get back facing camera ID
            val cameraID = getFirstCameraIdFacing(cameraManager, CameraCharacteristics.LENS_FACING_BACK)

            if (cameraID != null) {
                val action = PatientNewFragmentDirections.actionPatientNewFragmentToCameraFragment(cameraId = cameraID, pixelFormat = ImageFormat.JPEG )
                view.findNavController().navigate(action)
            } else {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    "failed find back facing camera!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
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

    fun getFirstCameraIdFacing(cameraManager: CameraManager,
                               facing: Int = CameraMetadata.LENS_FACING_BACK): String? {
        // Get list of all compatible cameras
        val cameraIds = cameraManager.cameraIdList.filter {
            val characteristics = cameraManager.getCameraCharacteristics(it)
            val capabilities = characteristics.get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)
            capabilities?.contains(
                CameraMetadata.REQUEST_AVAILABLE_CAPABILITIES_BACKWARD_COMPATIBLE) ?: false
        }

        // Iterate over the list of cameras and return the first one matching desired
        // lens-facing configuration
        cameraIds.forEach {
            val characteristics = cameraManager.getCameraCharacteristics(it)
            if (characteristics.get(CameraCharacteristics.LENS_FACING) == facing) {
                return it
            }
        }
        // If no camera matched desired orientation, return the first one from the list
        return cameraIds.firstOrNull()
    }


    companion object {

    }
}