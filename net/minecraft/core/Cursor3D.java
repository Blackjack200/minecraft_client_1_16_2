package net.minecraft.core;

public class Cursor3D {
    private int originX;
    private int originY;
    private int originZ;
    private int width;
    private int height;
    private int depth;
    private int end;
    private int index;
    private int x;
    private int y;
    private int z;
    
    public Cursor3D(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6) {
        this.originX = integer1;
        this.originY = integer2;
        this.originZ = integer3;
        this.width = integer4 - integer1 + 1;
        this.height = integer5 - integer2 + 1;
        this.depth = integer6 - integer3 + 1;
        this.end = this.width * this.height * this.depth;
    }
    
    public boolean advance() {
        if (this.index == this.end) {
            return false;
        }
        this.x = this.index % this.width;
        final int integer2 = this.index / this.width;
        this.y = integer2 % this.height;
        this.z = integer2 / this.height;
        ++this.index;
        return true;
    }
    
    public int nextX() {
        return this.originX + this.x;
    }
    
    public int nextY() {
        return this.originY + this.y;
    }
    
    public int nextZ() {
        return this.originZ + this.z;
    }
    
    public int getNextType() {
        int integer2 = 0;
        if (this.x == 0 || this.x == this.width - 1) {
            ++integer2;
        }
        if (this.y == 0 || this.y == this.height - 1) {
            ++integer2;
        }
        if (this.z == 0 || this.z == this.depth - 1) {
            ++integer2;
        }
        return integer2;
    }
}
