package com.example.burnestimation.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.burnestimation.camerautils.ImageUtils
import com.example.burnestimation.imgsegmentation.ImageSegmentationModelExecutor
import com.example.burnestimation.imgsegmentation.ModelExecutionResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

private const val TAG = "nnSegViewModel"

// ignore warning that this class name starts with lower case
@Suppress("ClassName")
class nnSegmentationViewModel : ViewModel() {

    private val _resultingBitmap = MutableLiveData<ModelExecutionResult>()

    val resultingBitmap: LiveData<ModelExecutionResult>
        get() = _resultingBitmap

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob)

    // the execution of the model has to be on the same thread where the interpreter
    // was created
    @SuppressLint("NullSafeMutableLiveData")
    fun onApplyModel(
        filePath: String,
        imageSegmentationModel: ImageSegmentationModelExecutor?,
        inferenceThread: ExecutorCoroutineDispatcher
    ) {
        viewModelScope.launch(inferenceThread) {
            val contentImage = ImageUtils.decodeBitmap(File(filePath))
            try {
                val result = imageSegmentationModel?.execute(contentImage)
                _resultingBitmap.postValue(result)
            } catch (e: Exception) {
                Log.e(TAG, "Fail to execute ImageSegmentationModelExecutor: ${e.message}")
                _resultingBitmap.postValue(null)
            }
        }
    }
}
