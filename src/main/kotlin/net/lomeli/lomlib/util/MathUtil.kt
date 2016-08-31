package net.lomeli.lomlib.util

import java.util.Random

/**
 * From CoFH Lib
 * <p>
 * Contains various math-related helper functions. Often faster than conventional implementations.
 *
 * @author King Lemming
 */
object MathUtil {
    val SIN_TABLE = DoubleArray(65536)

    init {
        for (i in 0..65535) {
            SIN_TABLE[i] = Math.sin(i / 65536.0 * 2.0 * Math.PI)
        }
        SIN_TABLE[0] = 0 as Double
        SIN_TABLE[16384] = 1 as Double
        SIN_TABLE[32768] = 0 as Double
        SIN_TABLE[49152] = 1 as Double
    }

    val RANDOM = Random()
    val PHI = 1.618034

    fun sin(d: Double): Double = SIN_TABLE[(d.toFloat() * 10430.378f).toInt() and 65535]

    fun cos(d: Double): Double = SIN_TABLE[(d.toFloat() * 10430.378f + 16384.0f).toInt() and 65535]

    fun clampI(a: Int, min: Int, max: Int): Int = if (a < min) min else if (a > max) max else a

    fun clampF(a: Float, min: Float, max: Float): Float = if (a < min) min else if (a > max) max else a

    fun approachLinear(a: Float, b: Float, max: Float): Float = if (a > b) if (a - b < max) b else a - max else if (b - a < max) b else a + max

    fun approachLinear(a: Double, b: Double, max: Double): Double = if (a > b) if (a - b < max) b else a - max else if (b - a < max) b else a + max

    fun interpolate(a: Float, b: Float, d: Float): Float = a + (b - a) * d

    fun interpolate(a: Double, b: Double, d: Double): Double = a + (b - a) * d

    fun approachExp(a: Double, b: Double, ratio: Double): Double = a + (b - a) * ratio

    fun approachExp(a: Double, b: Double, ratio: Double, cap: Double): Double {
        var d = (b - a) * ratio
        if (Math.abs(d) > cap)
            d = Math.signum(d) * cap
        return a + d
    }

    fun retreatExp(a: Double, b: Double, c: Double, ratio: Double, kick: Double): Double {
        val d = (Math.abs(c - a) + kick) * ratio
        if (d > Math.abs(b - a))
            return b
        return a + Math.signum(b - a) * d
    }

    fun clip(value: Double, min: Double, max: Double): Double {
        var value = value
        if (value > max)
            value = max
        else if (value < min)
            value = min
        return value
    }

    fun between(a: Double, x: Double, b: Double): Boolean = a <= x && x <= b

    fun approachExpI(a: Int, b: Int, ratio: Double): Int {
        val r = Math.round(approachExp(a.toDouble(), b.toDouble(), ratio)).toInt()
        return if (r == a) b else r
    }

    fun retreatExpI(a: Int, b: Int, c: Int, ratio: Double, kick: Int): Int {
        val r = Math.round(retreatExp(a.toDouble(), b.toDouble(), c.toDouble(), ratio, kick.toDouble())).toInt()
        return if (r == a) b else r
    }

    /**
     * Unchecked implementation to round a number. Parameter should be known to be valid in advance.
     */
    fun round(d: Double): Int = (d + 0.5).toInt()

    /**
     * Unchecked implementation to round a number up. Parameter should be known to be valid in advance.
     */
    fun ceil(d: Double): Int = (d + 0.9999).toInt()

    /**
     * Unchecked implementation to round a number down. Parameter should be known to be valid in advance.
     */
    fun floor(d: Double): Int {
        val i = d.toInt()
        return if (d < i) i - 1 else i
    }

    /**
     * Unchecked implementation to determine the smaller of two Floats. Parameters should be known to be valid in advance.
     */
    fun minF(a: Float, b: Float): Float = if (a < b) a else b

    fun minF(a: Int, b: Float): Float = if (a < b) a as Float else b

    fun minF(a: Float, b: Int): Float = if (a < b) a else b as Float

    /**
     * Unchecked implementation to determine the larger of two Floats. Parameters should be known to be valid in advance.
     */
    fun maxF(a: Float, b: Float): Float = if (a > b) a else b

    fun maxF(a: Int, b: Float): Float = if (a > b) a as Float else b

    fun maxF(a: Float, b: Int): Float = if (a > b) a else b as Float

    fun maxAbs(a: Double, b: Double): Double {
        var a = a
        var b = b
        if (a < 0.0)
            a = -a
        if (b < 0.0)
            b = -b
        return if (a > b) a else b
    }

    fun setBit(mask: Int, bit: Int, value: Boolean): Int {
        var mask = mask
        mask = mask or ((if (value) 1 else 0) shl bit)
        return mask
    }

    fun isBitSet(mask: Int, bit: Int): Boolean = mask and (1 shl bit) != 0
}