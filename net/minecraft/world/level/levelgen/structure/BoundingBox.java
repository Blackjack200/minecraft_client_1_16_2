package net.minecraft.world.level.levelgen.structure;

import net.minecraft.nbt.IntArrayTag;
import com.google.common.base.MoreObjects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.Direction;

public class BoundingBox {
    public int x0;
    public int y0;
    public int z0;
    public int x1;
    public int y1;
    public int z1;
    
    public BoundingBox() {
    }
    
    public BoundingBox(final int[] arr) {
        if (arr.length == 6) {
            this.x0 = arr[0];
            this.y0 = arr[1];
            this.z0 = arr[2];
            this.x1 = arr[3];
            this.y1 = arr[4];
            this.z1 = arr[5];
        }
    }
    
    public static BoundingBox getUnknownBox() {
        return new BoundingBox(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }
    
    public static BoundingBox infinite() {
        return new BoundingBox(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
    
    public static BoundingBox orientBox(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final int integer9, final Direction gc) {
        switch (gc) {
            default: {
                return new BoundingBox(integer1 + integer4, integer2 + integer5, integer3 + integer6, integer1 + integer7 - 1 + integer4, integer2 + integer8 - 1 + integer5, integer3 + integer9 - 1 + integer6);
            }
            case NORTH: {
                return new BoundingBox(integer1 + integer4, integer2 + integer5, integer3 - integer9 + 1 + integer6, integer1 + integer7 - 1 + integer4, integer2 + integer8 - 1 + integer5, integer3 + integer6);
            }
            case SOUTH: {
                return new BoundingBox(integer1 + integer4, integer2 + integer5, integer3 + integer6, integer1 + integer7 - 1 + integer4, integer2 + integer8 - 1 + integer5, integer3 + integer9 - 1 + integer6);
            }
            case WEST: {
                return new BoundingBox(integer1 - integer9 + 1 + integer6, integer2 + integer5, integer3 + integer4, integer1 + integer6, integer2 + integer8 - 1 + integer5, integer3 + integer7 - 1 + integer4);
            }
            case EAST: {
                return new BoundingBox(integer1 + integer6, integer2 + integer5, integer3 + integer4, integer1 + integer9 - 1 + integer6, integer2 + integer8 - 1 + integer5, integer3 + integer7 - 1 + integer4);
            }
        }
    }
    
    public static BoundingBox createProper(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6) {
        return new BoundingBox(Math.min(integer1, integer4), Math.min(integer2, integer5), Math.min(integer3, integer6), Math.max(integer1, integer4), Math.max(integer2, integer5), Math.max(integer3, integer6));
    }
    
    public BoundingBox(final BoundingBox cqx) {
        this.x0 = cqx.x0;
        this.y0 = cqx.y0;
        this.z0 = cqx.z0;
        this.x1 = cqx.x1;
        this.y1 = cqx.y1;
        this.z1 = cqx.z1;
    }
    
    public BoundingBox(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6) {
        this.x0 = integer1;
        this.y0 = integer2;
        this.z0 = integer3;
        this.x1 = integer4;
        this.y1 = integer5;
        this.z1 = integer6;
    }
    
    public BoundingBox(final Vec3i gr1, final Vec3i gr2) {
        this.x0 = Math.min(gr1.getX(), gr2.getX());
        this.y0 = Math.min(gr1.getY(), gr2.getY());
        this.z0 = Math.min(gr1.getZ(), gr2.getZ());
        this.x1 = Math.max(gr1.getX(), gr2.getX());
        this.y1 = Math.max(gr1.getY(), gr2.getY());
        this.z1 = Math.max(gr1.getZ(), gr2.getZ());
    }
    
    public BoundingBox(final int integer1, final int integer2, final int integer3, final int integer4) {
        this.x0 = integer1;
        this.z0 = integer2;
        this.x1 = integer3;
        this.z1 = integer4;
        this.y0 = 1;
        this.y1 = 512;
    }
    
    public boolean intersects(final BoundingBox cqx) {
        return this.x1 >= cqx.x0 && this.x0 <= cqx.x1 && this.z1 >= cqx.z0 && this.z0 <= cqx.z1 && this.y1 >= cqx.y0 && this.y0 <= cqx.y1;
    }
    
    public boolean intersects(final int integer1, final int integer2, final int integer3, final int integer4) {
        return this.x1 >= integer1 && this.x0 <= integer3 && this.z1 >= integer2 && this.z0 <= integer4;
    }
    
    public void expand(final BoundingBox cqx) {
        this.x0 = Math.min(this.x0, cqx.x0);
        this.y0 = Math.min(this.y0, cqx.y0);
        this.z0 = Math.min(this.z0, cqx.z0);
        this.x1 = Math.max(this.x1, cqx.x1);
        this.y1 = Math.max(this.y1, cqx.y1);
        this.z1 = Math.max(this.z1, cqx.z1);
    }
    
    public void move(final int integer1, final int integer2, final int integer3) {
        this.x0 += integer1;
        this.y0 += integer2;
        this.z0 += integer3;
        this.x1 += integer1;
        this.y1 += integer2;
        this.z1 += integer3;
    }
    
    public BoundingBox moved(final int integer1, final int integer2, final int integer3) {
        return new BoundingBox(this.x0 + integer1, this.y0 + integer2, this.z0 + integer3, this.x1 + integer1, this.y1 + integer2, this.z1 + integer3);
    }
    
    public void move(final Vec3i gr) {
        this.move(gr.getX(), gr.getY(), gr.getZ());
    }
    
    public boolean isInside(final Vec3i gr) {
        return gr.getX() >= this.x0 && gr.getX() <= this.x1 && gr.getZ() >= this.z0 && gr.getZ() <= this.z1 && gr.getY() >= this.y0 && gr.getY() <= this.y1;
    }
    
    public Vec3i getLength() {
        return new Vec3i(this.x1 - this.x0, this.y1 - this.y0, this.z1 - this.z0);
    }
    
    public int getXSpan() {
        return this.x1 - this.x0 + 1;
    }
    
    public int getYSpan() {
        return this.y1 - this.y0 + 1;
    }
    
    public int getZSpan() {
        return this.z1 - this.z0 + 1;
    }
    
    public Vec3i getCenter() {
        return new BlockPos(this.x0 + (this.x1 - this.x0 + 1) / 2, this.y0 + (this.y1 - this.y0 + 1) / 2, this.z0 + (this.z1 - this.z0 + 1) / 2);
    }
    
    public String toString() {
        return MoreObjects.toStringHelper(this).add("x0", this.x0).add("y0", this.y0).add("z0", this.z0).add("x1", this.x1).add("y1", this.y1).add("z1", this.z1).toString();
    }
    
    public IntArrayTag createTag() {
        return new IntArrayTag(new int[] { this.x0, this.y0, this.z0, this.x1, this.y1, this.z1 });
    }
}
