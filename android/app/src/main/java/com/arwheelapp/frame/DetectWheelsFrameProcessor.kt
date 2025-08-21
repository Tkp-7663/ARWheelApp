package com.arwheelapp.frame

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.media.Image
import com.mrousavy.camera.frameprocessor.Frame
import com.mrousavy.camera.frameprocessor.FrameProcessorPlugin
import com.arwheelapp.tflite.InterpreterManager
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableArray
import com.facebook.react.bridge.WritableMap

class DetectWheelsFrameProcessor : FrameProcessorPlugin("detectWheels") {

    override fun callback(frame: Frame, params: Map<String, Any>?): Any? {
        // โหลด interpreter ครั้งแรก
        InterpreterManager.load(frame.context)

        val image = frame.image ?: return null
        if (image.format != ImageFormat.YUV_420_888) return null

        val bmp = yuvToBitmap(image)
        image.close()

        val dets = InterpreterManager.infer(bmp)

        // ส่งผลลัพธ์เป็น Array<map> -> [{x,y,w,h,score,cls}]
        val arr: WritableArray = Arguments.createArray()
        for (d in dets) {
            val m: WritableMap = Arguments.createMap()
            m.putDouble("x", d.x.toDouble())
            m.putDouble("y", d.y.toDouble())
            m.putDouble("w", d.w.toDouble())
            m.putDouble("h", d.h.toDouble())
            m.putDouble("score", d.score.toDouble())
            m.putInt("cls", d.cls)
            arr.pushMap(m)
        }
        return arr
    }

    // แปลง YUV_420_888 -> NV21 -> JPEG -> Bitmap (ง่ายและเร็วพอ)
    private fun yuvToBitmap(image: Image): Bitmap {
        val yBuffer = image.planes[0].buffer
        val uBuffer = image.planes[1].buffer
        val vBuffer = image.planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = android.graphics.YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val out = java.io.ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, image.width, image.height), 95, out)
        val jpeg = out.toByteArray()
        return BitmapFactory.decodeByteArray(jpeg, 0, jpeg.size)
    }
}
