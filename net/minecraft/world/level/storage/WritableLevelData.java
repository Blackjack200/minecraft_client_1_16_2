package net.minecraft.world.level.storage;

import net.minecraft.core.BlockPos;

public interface WritableLevelData extends LevelData {
    void setXSpawn(final int integer);
    
    void setYSpawn(final int integer);
    
    void setZSpawn(final int integer);
    
    void setSpawnAngle(final float float1);
    
    default void setSpawn(final BlockPos fx, final float float2) {
        this.setXSpawn(fx.getX());
        this.setYSpawn(fx.getY());
        this.setZSpawn(fx.getZ());
        this.setSpawnAngle(float2);
    }
}
