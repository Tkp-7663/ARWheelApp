package com.arwheelapp

import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.media.Image
import com.mrousavy.camera.frameprocessor.Frame
import com.mrousavy.camera.frameprocessor.FrameProcessorPlugin

class YuvToBitmapFrameProcessor : FrameProcessorPlugin("yuvToBitmap") {

    override fun callback(frame: Frame, params: Map<String, Any>?): Any? {
        val image = frame.image ?: return null

        if (image.format != ImageFormat.YUV_420_888) {
            return null
        }

        return yuvToBitmap(image)
    }

    private fun yuvToBitmap(image: Image): Bitmap {
        val yBuffer = image.planes[0].buffer // Y
        val uBuffer = image.planes[1].buffer // U
        val vBuffer = image.planes[2].buffer // V

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = android.graphics.YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val out = java.io.ByteArrayOutputStream()
        yuvImage.compressToJpeg(android.graphics.Rect(0, 0, image.width, image.height), 100, out)
        val jpegBytes = out.toByteArray()

        return android.graphics.BitmapFactory.decodeByteArray(jpegBytes, 0, jpegBytes.size)
    }
}
