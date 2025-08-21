package com.arwheelapp

import android.graphics.Bitmap
import com.facebook.react.bridge.*
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.gpu.GpuDelegate
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class TFLiteModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    private var interpreter: Interpreter? = null
    private var gpuDelegate: GpuDelegate? = null

    override fun getName(): String {
        return "TFLiteModule"
    }

    @ReactMethod
    fun loadModel(modelPath: String, promise: Promise) {
        try {
            val fileDescriptor = reactApplicationContext.assets.openFd(modelPath)
            val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
            val fileChannel = inputStream.channel
            val startOffset = fileDescriptor.startOffset
            val declaredLength = fileDescriptor.declaredLength
            val modelBuffer: MappedByteBuffer =
                fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)

            val options = Interpreter.Options()
            gpuDelegate = GpuDelegate()
            options.addDelegate(gpuDelegate)
            interpreter = Interpreter(modelBuffer, options)

            promise.resolve("Model loaded with GPU delegate")
        } catch (e: Exception) {
            promise.reject("LOAD_FAILED", e)
        }
    }

    @ReactMethod
    fun runModel(bitmap: Bitmap, promise: Promise) {
        try {
            val inputImage = TensorImage.fromBitmap(bitmap)
            val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 25200, 7), org.tensorflow.lite.DataType.FLOAT32)

            interpreter?.run(inputImage.buffer, outputBuffer.buffer.rewind())
            val outputArray = outputBuffer.floatArray

            val result = Arguments.createArray()
            // TODO: map outputArray → detection box
            // Simplify: push raw array back to JS
            for (v in outputArray) {
                result.pushDouble(v.toDouble())
            }
            promise.resolve(result)
        } catch (e: Exception) {
            promise.reject("RUN_FAILED", e)
        }
    }
}
