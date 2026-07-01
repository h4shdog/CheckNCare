package com.example.checkncare.util

import kotlin.math.*

class FFT(private val n: Int) {
    private val cos = FloatArray(n / 2)
    private val sin = FloatArray(n / 2)

    init {
        for (i in 0 until n / 2) {
            cos[i] = cos(2 * PI * i / n).toFloat()
            sin[i] = sin(2 * PI * i / n).toFloat()
        }
    }

    fun transform(real: FloatArray, imag: FloatArray) {
        var j = 0
        for (i in 0 until n) {
            if (i < j) {
                val tempReal = real[i]
                real[i] = real[j]
                real[j] = tempReal
                val tempImag = imag[i]
                imag[i] = imag[j]
                imag[j] = tempImag
            }
            var m = n shr 1
            while (m >= 1 && j >= m) {
                j -= m
                m = m shr 1
            }
            j += m
        }

        var m = 2
        while (m <= n) {
            val halfM = m / 2
            val step = n / m
            for (k in 0 until n step m) {
                for (i in 0 until halfM) {
                    val c = cos[i * step]
                    val s = sin[i * step]
                    val tr = real[k + i + halfM] * c + imag[k + i + halfM] * s
                    val ti = imag[k + i + halfM] * c - real[k + i + halfM] * s
                    real[k + i + halfM] = real[k + i] - tr
                    imag[k + i + halfM] = imag[k + i] - ti
                    real[k + i] += tr
                    imag[k + i] += ti
                }
            }
            m *= 2
        }
    }
}
