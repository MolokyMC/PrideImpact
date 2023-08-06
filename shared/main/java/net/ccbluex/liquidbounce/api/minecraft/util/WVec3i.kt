/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */

package net.ccbluex.liquidbounce.api.minecraft.util

import kotlin.math.floor

open class WVec3i(
    val x: Int,
    val y: Int,
    val z: Int
) {
    constructor(x: Double, y: Double, z: Double) : this(floor(x).toInt(), floor(y).toInt(), floor(z).toInt())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WVec3i

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + z
        return result
    }

    fun distanceSq(zeroedCenteredChunkPos: WBlockPos): Double {
        return this.distanceSq(
            zeroedCenteredChunkPos.x.toDouble(),
            zeroedCenteredChunkPos.y.toDouble(),
            zeroedCenteredChunkPos.z.toDouble()
        )
    }

    fun distanceSq(p_distanceSq_1_: Double, p_distanceSq_3_: Double, p_distanceSq_5_: Double): Double {
        val x: Double = x.toDouble() - p_distanceSq_1_
        val y: Double = y.toDouble() - p_distanceSq_3_
        val z: Double = z.toDouble() - p_distanceSq_5_
        return x * x + y * y + z * z
    }

}