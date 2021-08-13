package net.minecraft.world.level.chunk;

import java.util.function.Consumer;
import net.minecraft.Util;
import java.util.Arrays;
import net.minecraft.data.worldgen.biome.Biomes;
import net.minecraft.server.level.ChunkHolder;
import java.util.function.Predicate;
import java.util.List;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.FluidState;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.IdMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class EmptyLevelChunk extends LevelChunk {
    private static final Biome[] BIOMES;
    
    public EmptyLevelChunk(final Level bru, final ChunkPos bra) {
        super(bru, bra, new ChunkBiomeContainer(bru.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY), EmptyLevelChunk.BIOMES));
    }
    
    @Override
    public BlockState getBlockState(final BlockPos fx) {
        return Blocks.VOID_AIR.defaultBlockState();
    }
    
    @Nullable
    @Override
    public BlockState setBlockState(final BlockPos fx, final BlockState cee, final boolean boolean3) {
        return null;
    }
    
    @Override
    public FluidState getFluidState(final BlockPos fx) {
        return Fluids.EMPTY.defaultFluidState();
    }
    
    @Nullable
    @Override
    public LevelLightEngine getLightEngine() {
        return null;
    }
    
    public int getLightEmission(final BlockPos fx) {
        return 0;
    }
    
    @Override
    public void addEntity(final Entity apx) {
    }
    
    @Override
    public void removeEntity(final Entity apx) {
    }
    
    @Override
    public void removeEntity(final Entity apx, final int integer) {
    }
    
    @Nullable
    @Override
    public BlockEntity getBlockEntity(final BlockPos fx, final EntityCreationType a) {
        return null;
    }
    
    @Override
    public void addBlockEntity(final BlockEntity ccg) {
    }
    
    @Override
    public void setBlockEntity(final BlockPos fx, final BlockEntity ccg) {
    }
    
    @Override
    public void removeBlockEntity(final BlockPos fx) {
    }
    
    @Override
    public void markUnsaved() {
    }
    
    @Override
    public void getEntities(@Nullable final Entity apx, final AABB dcf, final List<Entity> list, final Predicate<? super Entity> predicate) {
    }
    
    @Override
    public <T extends Entity> void getEntitiesOfClass(final Class<? extends T> class1, final AABB dcf, final List<T> list, final Predicate<? super T> predicate) {
    }
    
    @Override
    public boolean isEmpty() {
        return true;
    }
    
    public boolean isYSpaceEmpty(final int integer1, final int integer2) {
        return true;
    }
    
    @Override
    public ChunkHolder.FullChunkStatus getFullStatus() {
        return ChunkHolder.FullChunkStatus.BORDER;
    }
    
    static {
        BIOMES = Util.<Biome[]>make(new Biome[ChunkBiomeContainer.BIOMES_SIZE], (java.util.function.Consumer<Biome[]>)(arr -> Arrays.fill((Object[])arr, Biomes.PLAINS)));
    }
}
