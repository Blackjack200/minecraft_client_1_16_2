package net.minecraft.world.phys.shapes;

import net.minecraft.core.Direction;

public final class SubShape extends DiscreteVoxelShape {
    private final DiscreteVoxelShape parent;
    private final int startX;
    private final int startY;
    private final int startZ;
    private final int endX;
    private final int endY;
    private final int endZ;
    
    protected SubShape(final DiscreteVoxelShape dct, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7) {
        super(integer5 - integer2, integer6 - integer3, integer7 - integer4);
        this.parent = dct;
        this.startX = integer2;
        this.startY = integer3;
        this.startZ = integer4;
        this.endX = integer5;
        this.endY = integer6;
        this.endZ = integer7;
    }
    
    @Override
    public boolean isFull(final int integer1, final int integer2, final int integer3) {
        return this.parent.isFull(this.startX + integer1, this.startY + integer2, this.startZ + integer3);
    }
    
    @Override
    public void setFull(final int integer1, final int integer2, final int integer3, final boolean boolean4, final boolean boolean5) {
        this.parent.setFull(this.startX + integer1, this.startY + integer2, this.startZ + integer3, boolean4, boolean5);
    }
    
    @Override
    public int firstFull(final Direction.Axis a) {
        return Math.max(0, this.parent.firstFull(a) - a.choose(this.startX, this.startY, this.startZ));
    }
    
    @Override
    public int lastFull(final Direction.Axis a) {
        return Math.min(a.choose(this.endX, this.endY, this.endZ), this.parent.lastFull(a) - a.choose(this.startX, this.startY, this.startZ));
    }
}
