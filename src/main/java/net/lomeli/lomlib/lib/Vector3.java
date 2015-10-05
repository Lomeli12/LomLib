package net.lomeli.lomlib.lib;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

import net.lomeli.lomlib.util.MathHelper;

public class Vector3 {
    private double x, y, z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(Vector3 vec) {
        this(vec.x, vec.y, vec.z);
    }

    public Vector3 set(Vector3 vec) {
        x = vec.x;
        y = vec.y;
        z = vec.z;
        return this;
    }

    public Vector3 set(double d, double d1, double d2) {
        x = d;
        y = d1;
        z = d2;
        return this;
    }

    public Vec3 toVec3() {
        return new Vec3(x, y, z);
    }

    public BlockPos toBlockPos() {
        return new BlockPos(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
    }

    public Vector3 add(double d, double d1, double d2) {
        return new Vector3(x + d, y + d1, z + d2);
    }

    public Vector3 add(Vector3 vec) {
        return add(vec.x, vec.y, vec.z);
    }

    public Vector3 add(double d) {
        return add(d, d, d);
    }

    public Vector3 sub(double d, double d1, double d2) {
        return new Vector3(x - d, y - d1, z - d2);
    }

    public Vector3 sub(Vector3 vec) {
        return sub(vec.x, vec.y, vec.z);
    }

    public Vector3 sub(double d) {
        return sub(d, d, d);
    }

    public Vector3 mul(double d, double d1, double d2) {
        return new Vector3(x * d, y * d1, z * d2);
    }

    public Vector3 mul(Vector3 vec) {
        return mul(vec.x, vec.y, vec.z);
    }

    public Vector3 mul(double d) {
        return mul(d, d, d);
    }

    public Vector3 div(double d, double d1, double d2) {
        return new Vector3(x / d, y / d1, z / d2);
    }

    public Vector3 div(Vector3 vec) {
        return div(vec.x, vec.y, vec.z);
    }

    public Vector3 div(double d) {
        return div(d, d, d);
    }

    public Vector3 eulerAngles() {
        return new Vector3(Math.toDegrees(Math.atan2(x, z)), Math.toDegrees(-Math.atan2(y, Math.hypot(z, x))), 0);
    }

    public double magnitudeSquared() {
        return x * x + y * y + z * z;
    }

    public double magnitude() {
        return Math.sqrt(magnitudeSquared());
    }

    public double distance(Vector3 v) {
        return this.clone().sub(v).magnitude();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector3) {
            Vector3 vec = (Vector3) obj;
            return x == vec.x && y == vec.y && z == vec.z;
        }
        return false;
    }

    public double angle(Vector3 vec) {
        return Math.acos(clone().normalize().dotProduct(vec.clone().normalize()));
    }

    public Vector3 normalize() {
        double d = magnitudeSquared();
        if (d != 0)
            mul(1 / d);
        return this;
    }

    public double dotProduct(Vector3 vec) {
        double d = vec.x * x + vec.y * y + vec.z * z;

        if (d > 1 && d < 1.00001)
            d = 1;
        else if (d < -1 && d > -1.00001)
            d = -1;
        return d;
    }

    public double dotProduct(double d, double d1, double d2) {
        return d * x + d1 * y + d2 * z;
    }

    public Vector3 crossProduct(Vector3 vec) {
        double d = y * vec.z - z * vec.y;
        double d1 = z * vec.x - x * vec.z;
        double d2 = x * vec.y - y * vec.x;
        x = d;
        y = d1;
        z = d2;
        return this;
    }

    @Override
    public String toString() {
        return "Vector3[" + x + "," + y + "," + z + "]";
    }

    @Override
    public int hashCode() {
        long hx = Double.doubleToLongBits(x);
        long hy = Double.doubleToLongBits(y);
        long hz = Double.doubleToLongBits(z);
        Long hash = (hx ^ (hx >>> 32));
        hash = 31 * hash + hy ^ (hy >>> 32);
        hash = 31 * hash + hz ^ (hz >>> 32);
        return hash.intValue();
    }

    public Vector3 clone() {
        return new Vector3(x, y, z);
    }

    public static Vector3 fromEntity(Entity e) {
        return new Vector3(e.posX, e.posY, e.posZ);
    }

    public static Vector3 fromEntityCenter(Entity e) {
        return new Vector3(e.posX, e.posY - e.getYOffset() + e.height / 2, e.posZ);
    }

    public static Vector3 fromPlayer(EntityPlayer player, float partialTicks) {
        return new Vector3(player.prevPosX + (player.posX - player.prevPosX) * partialTicks, player.prevPosY + (player.posY - player.prevPosY) * partialTicks, player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks);
    }

    public static Vector3 fromTile(TileEntity tile) {
        return fromBlockPos(tile.getPos());
    }

    public static Vector3 fromTileCenter(TileEntity tile) {
        return fromTile(tile).add(0.5);
    }

    public static Vector3 fromBlockPos(BlockPos pos) {
        return new Vector3(pos.getX(), pos.getY(), pos.getZ());
    }

    public static Vector3 fromBlockCenter(BlockPos pos) {
        return fromBlockPos(pos).add(0.5);
    }

    public static Vector3 fromAxes(double[] da) {
        return new Vector3(da[2], da[0], da[1]);
    }
}
