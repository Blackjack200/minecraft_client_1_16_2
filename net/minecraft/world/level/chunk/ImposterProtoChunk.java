package net.minecraft.world.level.chunk;

import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.TickList;
import net.minecraft.Util;
import java.util.BitSet;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.material.Fluid;
import java.util.function.Predicate;
import net.minecraft.world.level.block.Block;
import java.util.stream.Stream;
import net.minecraft.nbt.CompoundTag;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.Map;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;

public class ImposterProtoChunk extends ProtoChunk {
    private final LevelChunk wrapped;
    
    public ImposterProtoChunk(final LevelChunk cge) {
        super(cge.getPos(), UpgradeData.EMPTY);
        this.wrapped = cge;
    }
    
    @Nullable
    @Override
    public BlockEntity getBlockEntity(final BlockPos fx) {
        return this.wrapped.getBlockEntity(fx);
    }
    
    @Nullable
    @Override
    public BlockState getBlockState(final BlockPos fx) {
        return this.wrapped.getBlockState(fx);
    }
    
    @Override
    public FluidState getFluidState(final BlockPos fx) {
        return this.wrapped.getFluidState(fx);
    }
    
    public int getMaxLightLevel() {
        return this.wrapped.getMaxLightLevel();
    }
    
    @Nullable
    @Override
    public BlockState setBlockState(final BlockPos fx, final BlockState cee, final boolean boolean3) {
        return null;
    }
    
    @Override
    public void setBlockEntity(final BlockPos fx, final BlockEntity ccg) {
    }
    
    @Override
    public void addEntity(final Entity apx) {
    }
    
    @Override
    public void setStatus(final ChunkStatus cfx) {
    }
    
    @Override
    public LevelChunkSection[] getSections() {
        return this.wrapped.getSections();
    }
    
    @Nullable
    @Override
    public LevelLightEngine getLightEngine() {
        return this.wrapped.getLightEngine();
    }
    
    @Override
    public void setHeightmap(final Heightmap.Types a, final long[] arr) {
    }
    
    private Heightmap.Types fixType(final Heightmap.Types a) {
        if (a == Heightmap.Types.WORLD_SURFACE_WG) {
            return Heightmap.Types.WORLD_SURFACE;
        }
        if (a == Heightmap.Types.OCEAN_FLOOR_WG) {
            return Heightmap.Types.OCEAN_FLOOR;
        }
        return a;
    }
    
    @Override
    public int getHeight(final Heightmap.Types a, final int integer2, final int integer3) {
        return this.wrapped.getHeight(this.fixType(a), integer2, integer3);
    }
    
    @Override
    public ChunkPos getPos() {
        return this.wrapped.getPos();
    }
    
    @Override
    public void setLastSaveTime(final long long1) {
    }
    
    @Nullable
    @Override
    public StructureStart<?> getStartForFeature(final StructureFeature<?> ckx) {
        return this.wrapped.getStartForFeature(ckx);
    }
    
    @Override
    public void setStartForFeature(final StructureFeature<?> ckx, final StructureStart<?> crs) {
    }
    
    @Override
    public Map<StructureFeature<?>, StructureStart<?>> getAllStarts() {
        return this.wrapped.getAllStarts();
    }
    
    @Override
    public void setAllStarts(final Map<StructureFeature<?>, StructureStart<?>> map) {
    }
    
    @Override
    public LongSet getReferencesForFeature(final StructureFeature<?> ckx) {
        return this.wrapped.getReferencesForFeature(ckx);
    }
    
    @Override
    public void addReferenceForFeature(final StructureFeature<?> ckx, final long long2) {
    }
    
    @Override
    public Map<StructureFeature<?>, LongSet> getAllReferences() {
        return this.wrapped.getAllReferences();
    }
    
    @Override
    public void setAllReferences(final Map<StructureFeature<?>, LongSet> map) {
    }
    
    @Override
    public ChunkBiomeContainer getBiomes() {
        return this.wrapped.getBiomes();
    }
    
    @Override
    public void setUnsaved(final boolean boolean1) {
    }
    
    @Override
    public boolean isUnsaved() {
        return false;
    }
    
    @Override
    public ChunkStatus getStatus() {
        return this.wrapped.getStatus();
    }
    
    @Override
    public void removeBlockEntity(final BlockPos fx) {
    }
    
    @Override
    public void markPosForPostprocessing(final BlockPos fx) {
    }
    
    @Override
    public void setBlockEntityNbt(final CompoundTag md) {
    }
    
    @Nullable
    @Override
    public CompoundTag getBlockEntityNbt(final BlockPos fx) {
        return this.wrapped.getBlockEntityNbt(fx);
    }
    
    @Nullable
    @Override
    public CompoundTag getBlockEntityNbtForSaving(final BlockPos fx) {
        return this.wrapped.getBlockEntityNbtForSaving(fx);
    }
    
    @Override
    public void setBiomes(final ChunkBiomeContainer cfu) {
    }
    
    @Override
    public Stream<BlockPos> getLights() {
        return this.wrapped.getLights();
    }
    
    @Override
    public ProtoTickList<Block> getBlockTicks() {
        return new ProtoTickList<Block>((java.util.function.Predicate<Block>)(bul -> bul.defaultBlockState().isAir()), this.getPos());
    }
    
    @Override
    public ProtoTickList<Fluid> getLiquidTicks() {
        return new ProtoTickList<Fluid>((java.util.function.Predicate<Fluid>)(cut -> cut == Fluids.EMPTY), this.getPos());
    }
    
    @Override
    public BitSet getCarvingMask(final GenerationStep.Carving a) {
        throw Util.<UnsupportedOperationException>pauseInIde(new UnsupportedOperationException("Meaningless in this context"));
    }
    
    @Override
    public BitSet getOrCreateCarvingMask(final GenerationStep.Carving a) {
        throw Util.<UnsupportedOperationException>pauseInIde(new UnsupportedOperationException("Meaningless in this context"));
    }
    
    public LevelChunk getWrapped() {
        return this.wrapped;
    }
    
    @Override
    public boolean isLightCorrect() {
        return this.wrapped.isLightCorrect();
    }
    
    @Override
    public void setLightCorrect(final boolean boolean1) {
        this.wrapped.setLightCorrect(boolean1);
    }
}
