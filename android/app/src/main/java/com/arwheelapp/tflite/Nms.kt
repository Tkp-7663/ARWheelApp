package com.arwheelapp.tflite

import kotlin.math.max
import kotlin.math.min

object Nms {
    private fun iou(a: InterpreterManager.Detection, b: InterpreterManager.Detection): Float {
        val ax1 = a.x - a.w/2f; val ay1 = a.y - a.h/2f
        val ax2 = a.x + a.w/2f; val ay2 = a.y + a.h/2f
        val bx1 = b.x - b.w/2f; val by1 = b.y - b.h/2f
        val bx2 = b.x + b.w/2f; val by2 = b.y + b.h/2f

        val interX1 = max(ax1, bx1)
        val interY1 = max(ay1, by1)
        val interX2 = min(ax2, bx2)
        val interY2 = min(ay2, by2)
        val interArea = max(0f, interX2 - interX1) * max(0f, interY2 - interY1)
        val aArea = (ax2 - ax1) * (ay2 - ay1)
        val bArea = (bx2 - bx1) * (by2 - by1)
        val union = aArea + bArea - interArea
        return if (union <= 0f) 0f else interArea / union
    }

    fun nms(dets: List<InterpreterManager.Detection>, iouTh: Float, topK: Int): List<InterpreterManager.Detection> {
        val byClass = dets.groupBy { it.cls }
        val kept = ArrayList<InterpreterManager.Detection>()
        for ((_, group) in byClass) {
            val sorted = group.sortedByDescending { it.score }.toMutableList()
            val keepClass = ArrayList<InterpreterManager.Detection>()
            while (sorted.isNotEmpty() && keepClass.size < topK) {
                val best = sorted.removeAt(0)
                keepClass.add(best)
                val it = sorted.iterator()
                while (it.hasNext()) {
                    val d = it.next()
                    if (iou(best, d) > iouTh) it.remove()
                }
            }
            kept.addAll(keepClass)
        }
        return kept
    }
}
