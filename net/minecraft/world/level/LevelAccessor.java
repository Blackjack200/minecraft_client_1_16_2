package net.minecraft.world.level;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.Block;

public interface LevelAccessor extends CommonLevelAccessor, LevelTimeAccess {
    default long dayTime() {
        return this.getLevelData().getDayTime();
    }
    
    TickList<Block> getBlockTicks();
    
    TickList<Fluid> getLiquidTicks();
    
    LevelData getLevelData();
    
    DifficultyInstance getCurrentDifficultyAt(final BlockPos fx);
    
    default Difficulty getDifficulty() {
        return this.getLevelData().getDifficulty();
    }
    
    ChunkSource getChunkSource();
    
    default boolean hasChunk(final int integer1, final int integer2) {
        return this.getChunkSource().hasChunk(integer1, integer2);
    }
    
    Random getRandom();
    
    default void blockUpdated(final BlockPos fx, final Block bul) {
    }
    
    void playSound(@Nullable final Player bft, final BlockPos fx, final SoundEvent adn, final SoundSource adp, final float float5, final float float6);
    
    void addParticle(final ParticleOptions hf, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7);
    
    void levelEvent(@Nullable final Player bft, final int integer2, final BlockPos fx, final int integer4);
    
    default int getHeight() {
        return this.dimensionType().logicalHeight();
    }
    
    default void levelEvent(final int integer1, final BlockPos fx, final int integer3) {
        this.levelEvent(null, integer1, fx, integer3);
    }
}
