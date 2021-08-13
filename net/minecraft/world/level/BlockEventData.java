package net.minecraft.world.level;

import net.minecraft.world.level.block.Block;
import net.minecraft.core.BlockPos;

public class BlockEventData {
    private final BlockPos pos;
    private final Block block;
    private final int paramA;
    private final int paramB;
    
    public BlockEventData(final BlockPos fx, final Block bul, final int integer3, final int integer4) {
        this.pos = fx;
        this.block = bul;
        this.paramA = integer3;
        this.paramB = integer4;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public Block getBlock() {
        return this.block;
    }
    
    public int getParamA() {
        return this.paramA;
    }
    
    public int getParamB() {
        return this.paramB;
    }
    
    public boolean equals(final Object object) {
        if (object instanceof BlockEventData) {
            final BlockEventData bqy3 = (BlockEventData)object;
            return this.pos.equals(bqy3.pos) && this.paramA == bqy3.paramA && this.paramB == bqy3.paramB && this.block == bqy3.block;
        }
        return false;
    }
    
    public int hashCode() {
        int integer2 = this.pos.hashCode();
        integer2 = 31 * integer2 + this.block.hashCode();
        integer2 = 31 * integer2 + this.paramA;
        integer2 = 31 * integer2 + this.paramB;
        return integer2;
    }
    
    public String toString() {
        return new StringBuilder().append("TE(").append(this.pos).append("),").append(this.paramA).append(",").append(this.paramB).append(",").append(this.block).toString();
    }
}
