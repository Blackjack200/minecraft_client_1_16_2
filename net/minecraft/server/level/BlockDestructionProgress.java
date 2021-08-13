package net.minecraft.server.level;

import net.minecraft.core.BlockPos;

public class BlockDestructionProgress implements Comparable<BlockDestructionProgress> {
    private final int id;
    private final BlockPos pos;
    private int progress;
    private int updatedRenderTick;
    
    public BlockDestructionProgress(final int integer, final BlockPos fx) {
        this.id = integer;
        this.pos = fx;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public void setProgress(int integer) {
        if (integer > 10) {
            integer = 10;
        }
        this.progress = integer;
    }
    
    public int getProgress() {
        return this.progress;
    }
    
    public void updateTick(final int integer) {
        this.updatedRenderTick = integer;
    }
    
    public int getUpdatedRenderTick() {
        return this.updatedRenderTick;
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final BlockDestructionProgress zq3 = (BlockDestructionProgress)object;
        return this.id == zq3.id;
    }
    
    public int hashCode() {
        return Integer.hashCode(this.id);
    }
    
    public int compareTo(final BlockDestructionProgress zq) {
        if (this.progress != zq.progress) {
            return Integer.compare(this.progress, zq.progress);
        }
        return Integer.compare(this.id, zq.id);
    }
}
