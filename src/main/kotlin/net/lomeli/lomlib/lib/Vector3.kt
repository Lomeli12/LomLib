package net.lomeli.lomlib.lib

import net.lomeli.lomlib.util.MathUtil
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

class Vector3 {
    private var x: Double = 0.toDouble()
    private var y: Double = 0.toDouble()
    private var z: Double = 0.toDouble()

    constructor()

    constructor(x: Double, y: Double, z: Double) {
        this.x = x
        this.y = y
        this.z = z
    }

    constructor(vec: Vector3) : this(vec.x, vec.y, vec.z)

    constructor(vec3: Vec3d) : this(vec3.xCoord, vec3.yCoord, vec3.zCoord)

    constructor(entity: Entity) : this(entity.posX, entity.posY, entity.posZ)

    operator fun set(d: Double, d1: Double, d2: Double): Vector3 {
        x = d
        y = d1
        z = d2
        return this
    }

    fun toVec3(): Vec3d = Vec3d(x, y, z)

    fun toBlockPos(): BlockPos = BlockPos(MathUtil.floor(x), MathUtil.floor(y), MathUtil.floor(z))

    operator fun plus(vec: Vector3): Vector3 {
        x += vec.x
        y += vec.y
        z += vec.z
        return this
    }

    operator fun plus(d: Double): Vector3 = this + Vector3(d, d, d)

    operator fun minus(vec: Vector3): Vector3 {
        x -= vec.x
        y -= vec.y
        z -= vec.z
        return this
    }

    operator fun minus(d: Double): Vector3 = this - Vector3(d, d, d)

    operator fun times(vec: Vector3): Vector3 {
        x *= vec.x
        y *= vec.y
        z *= vec.z
        return this
    }

    operator fun times(d: Double): Vector3 = this * Vector3(d, d, d)

    operator fun div(vec: Vector3): Vector3 {
        x /= vec.x
        y /= vec.y
        z /= vec.z
        return this
    }

    operator fun div(d: Double): Vector3 = this / Vector3(d, d, d)

    operator fun mod(vec: Vector3): Vector3 {
        x %= vec.x
        y %= vec.y
        z %= vec.z
        return this
    }

    operator fun mod(d: Double): Vector3 = this % Vector3(d, d, d)

    fun eulerAngles(): Vector3 = Vector3(Math.toDegrees(Math.atan2(x, z)), Math.toDegrees(-Math.atan2(y, Math.hypot(z, x))), 0.toDouble())

    fun magnitudeSquared(): Double = x * x + y * y + z * z

    fun magnitude(): Double = Math.sqrt(magnitudeSquared())

    fun distance(v: Vector3): Double = (this.clone() - v).magnitude()

    fun getX(): Double = x

    fun getY(): Double = y

    fun getZ(): Double = z

    override fun equals(obj: Any?): Boolean = if (obj is Vector3) equals(obj) else false

    fun equals(x0: Double, y0: Double, z0: Double): Boolean = x == x0 && y == y0 && z == z0

    fun equals(vec: Vector3): Boolean = equals(vec.x, vec.y, vec.z)

    fun angle(vec: Vector3): Double = Math.acos(clone().normalize().dotProduct(vec.clone().normalize()))

    fun normalize(): Vector3 {
        val d = magnitudeSquared()
        if (d != 0.0)
            times(1 / d)
        return this
    }

    fun dotProduct(vec: Vector3): Double {
        var d = vec.x * x + vec.y * y + vec.z * z

        if (d > 1 && d < 1.00001)
            d = 1.0
        else if (d < -1 && d > -1.00001)
            d = -1.0
        return d
    }

    fun dotProduct(d: Double, d1: Double, d2: Double): Double = d * x + d1 * y + d2 * z

    fun crossProduct(vec: Vector3): Vector3 {
        val d = y * vec.z - z * vec.y
        val d1 = z * vec.x - x * vec.z
        val d2 = x * vec.y - y * vec.x
        x = d
        y = d1
        z = d2
        return this
    }

    override fun toString(): String = "Vector3[$x,$y,$z]"

    override fun hashCode(): Int {
        val hx = java.lang.Double.doubleToLongBits(x)
        val hy = java.lang.Double.doubleToLongBits(y)
        val hz = java.lang.Double.doubleToLongBits(z)
        var hash: Long? = hx xor hx.ushr(32)
        hash = 31 * hash!! + hy xor hy.ushr(32)
        hash = 31 * hash + hz xor hz.ushr(32)
        return hash.toInt()
    }

    fun clone(): Vector3 = Vector3(x, y, z)

    fun fromEntity(e: Entity): Vector3 = Vector3(e.posX, e.posY, e.posZ)

    fun fromEntityCenter(e: Entity): Vector3 = Vector3(e.posX, e.posY - e.yOffset + e.height / 2, e.posZ)

    fun fromPlayer(player: EntityPlayer, partialTicks: Float): Vector3 = Vector3(player.prevPosX + (player.posX - player.prevPosX) * partialTicks, player.prevPosY + (player.posY - player.prevPosY) * partialTicks, player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks)

    fun fromTile(tile: TileEntity): Vector3 = fromBlockPos(tile.pos)

    fun fromTileCenter(tile: TileEntity): Vector3 = fromTile(tile) + 0.5

    fun fromBlockPos(pos: BlockPos): Vector3 = Vector3(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())

    fun fromBlockCenter(pos: BlockPos): Vector3 = fromBlockPos(pos) + 0.5

    fun fromAxes(da: DoubleArray): Vector3 = Vector3(da[2], da[0], da[1])
}