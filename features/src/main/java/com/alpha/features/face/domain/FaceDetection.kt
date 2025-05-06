package com.alpha.features.face.domain

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alpha.features.face.presentation.ui.CameraPreview

@Composable
fun FaceDetection() {
    Surface(modifier = Modifier.fillMaxSize()) {
        CameraPreview()
    }
}
