package com.alpha.features.face.domain

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.google.mediapipe.tasks.vision.facedetector.FaceDetectorResult
import kotlin.math.min

class OverlayView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var results: FaceDetectorResult? = null
    private var imageWidth = 1
    private var imageHeight = 1
    private var scaleFactor = 1f

    private var referenceRect = RectF()

    private val referencePaint = Paint().apply {
        strokeWidth = 10f
        style = Paint.Style.STROKE
        color = Color.BLACK
    }

    fun setResults(detectionResults: FaceDetectorResult?, imgHeight: Int, imgWidth: Int) {
        results = detectionResults
        imageHeight = imgHeight
        imageWidth = imgWidth
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (imageWidth <= 0 || imageHeight <= 0) return

        scaleFactor = min(width * 1f / imageWidth, height * 1f / imageHeight)

        val left = width / 2f - 350f
        val top = height / 2f - 500f
        val right = width / 2f + 350f
        val bottom = height / 2f + 500f

        referenceRect.set(left, top, right, bottom)

        var faceInside = false

        results?.detections()?.let { detections ->
            for (detection in detections) {
                val box = detection.boundingBox()

                val scaledBox = RectF(
                    box.left * scaleFactor,
                    box.top * scaleFactor,
                    box.right * scaleFactor,
                    box.bottom * scaleFactor
                )

                if (referenceRect.contains(scaledBox)) {
                    faceInside = true
                    break
                }
            }

            referencePaint.color = if (faceInside) Color.GREEN else Color.RED
        }

        canvas.drawRect(referenceRect, referencePaint)
    }
}

