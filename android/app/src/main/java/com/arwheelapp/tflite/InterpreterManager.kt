package com.arwheelapp.tflite

import android.content.Context
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.gpu.GpuDelegate
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ImageProcessor
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

object InterpreterManager {
    private var interpreter: Interpreter? = null
    private var gpuDelegate: GpuDelegate? = null
    private var inputWidth = 640   // ปรับตาม input ของโมเดลคุณ
    private var inputHeight = 640
    private var imageProcessor: ImageProcessor? = null

    fun load(context: Context, assetPath: String = "models/wheel-detector.tflite") {
        if (interpreter != null) return

        val afd = context.assets.openFd(assetPath)
        val inputStream = FileInputStream(afd.fileDescriptor)
        val fileChannel = inputStream.channel
        val modelBuffer: MappedByteBuffer =
            fileChannel.map(FileChannel.MapMode.READ_ONLY, afd.startOffset, afd.declaredLength)

        val options = Interpreter.Options().apply {
            gpuDelegate = GpuDelegate()
            addDelegate(gpuDelegate)
            setNumThreads(Runtime.getRuntime().availableProcessors())
        }
        interpreter = Interpreter(modelBuffer, options)

        // เตรียม ImageProcessor: resize + normalize 0..1
        imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(inputHeight, inputWidth, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0f, 255f))
            .build()
    }

    data class Detection(
        val x: Float, val y: Float, val w: Float, val h: Float,
        val score: Float, val cls: Int
    )

    // อ้างอิง output shape YOLOv11 ที่ export เป็น TFLite: (1, N, 7) = [x,y,w,h,score,class,?] — ปรับตามโมเดลจริง
    fun infer(bitmap: android.graphics.Bitmap): List<Detection> {
        val tfImage = TensorImage(DataType.FLOAT32)
        tfImage.load(bitmap)
        val processed = imageProcessor!!.process(tfImage)

        // เตรียม output buffer (แก้ตาม output ของโมเดลคุณ)
        val outShape = intArrayOf(1, 25200, 7) // N สมมุติ; ปรับตามโมเดล
        val output = TensorBuffer.createFixedSize(outShape, DataType.FLOAT32)

        interpreter?.run(processed.buffer, output.buffer.rewind())

        val floats = output.floatArray
        val n = outShape[1]
        val step = outShape[2]

        val results = ArrayList<Detection>(100)
        var idx = 0
        for (i in 0 until n) {
            val x = floats[idx]; val y = floats[idx+1]
            val w = floats[idx+2]; val h = floats[idx+3]
            val score = floats[idx+4]
            val cls = floats[idx+5].toInt()
            // เงื่อนไขคัดกรองเบื้องต้น
            if (score >= 0.35f) {
                results.add(Detection(x, y, w, h, score, cls))
            }
            idx += step
        }

        // NMS (IoU 0.45) ตามคลาส
        return Nms.nms(results, 0.45f, 100)
    }
}
