package net.minecraft.core;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerLevel;

public class BlockSourceImpl implements BlockSource {
    private final ServerLevel level;
    private final BlockPos pos;
    
    public BlockSourceImpl(final ServerLevel aag, final BlockPos fx) {
        this.level = aag;
        this.pos = fx;
    }
    
    public ServerLevel getLevel() {
        return this.level;
    }
    
    public double x() {
        return this.pos.getX() + 0.5;
    }
    
    public double y() {
        return this.pos.getY() + 0.5;
    }
    
    public double z() {
        return this.pos.getZ() + 0.5;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public BlockState getBlockState() {
        return this.level.getBlockState(this.pos);
    }
    
    public <T extends BlockEntity> T getEntity() {
        return (T)this.level.getBlockEntity(this.pos);
    }
}
