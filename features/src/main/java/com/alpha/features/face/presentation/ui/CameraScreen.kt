package com.alpha.features.face.presentation.ui

import android.util.Log
import android.widget.FrameLayout
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import com.alpha.features.face.domain.FaceDetectorHelper
import com.alpha.features.face.domain.OverlayView
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.facedetector.FaceDetectorResult
import java.util.concurrent.Executors

@Composable
fun CameraPreview(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    var faceResults by remember { mutableStateOf<FaceDetectorResult?>(null) }
    var imageWidth by remember { mutableStateOf(1) }
    var imageHeight by remember { mutableStateOf(1) }

    val detectorHelper = remember {
        FaceDetectorHelper(context = context,
            runningMode = RunningMode.LIVE_STREAM,
            faceDetectorListener = object : FaceDetectorHelper.DetectorListener {
                override fun onResults(resultBundle: FaceDetectorHelper.ResultBundle) {
                    faceResults = resultBundle.results.firstOrNull()
                    imageWidth = resultBundle.inputImageWidth
                    imageHeight = resultBundle.inputImageHeight
                }

                override fun onError(error: String, errorCode: Int) {
                    Log.e("FaceDetection", "Error: $error ($errorCode)")
                }
            })
    }

    val previewView = remember { PreviewView(context) }
    val overlayView = remember { OverlayView(context, null) }

    AndroidView(factory = {
        FrameLayout(it).apply {
            addView(previewView)
            addView(overlayView)
        }
    }, modifier = modifier.fillMaxSize(), update = {
        overlayView.setResults(faceResults, imageHeight, imageWidth)
    })

    LaunchedEffect(Unit) {
        val cameraProvider = ProcessCameraProvider.getInstance(context).get()

        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val imageAnalysis =
            ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build().apply {
                    setAnalyzer(cameraExecutor) { imageProxy ->
                        detectorHelper.detectLivestreamFrame(imageProxy)
                    }
                }

        val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner, cameraSelector, preview, imageAnalysis
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
            detectorHelper.clearFaceDetector()
        }
    }
}


