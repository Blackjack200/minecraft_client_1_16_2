package net.minecraft.world.level.chunk;

import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.TickList;
import java.util.stream.Stream;
import net.minecraft.nbt.CompoundTag;
import it.unimi.dsi.fastutil.shorts.ShortList;
import org.apache.logging.log4j.LogManager;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import java.util.Map;
import java.util.Collection;
import java.util.Set;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

public interface ChunkAccess extends BlockGetter, FeatureAccess {
    @Nullable
    BlockState setBlockState(final BlockPos fx, final BlockState cee, final boolean boolean3);
    
    void setBlockEntity(final BlockPos fx, final BlockEntity ccg);
    
    void addEntity(final Entity apx);
    
    @Nullable
    default LevelChunkSection getHighestSection() {
        final LevelChunkSection[] arr2 = this.getSections();
        for (int integer3 = arr2.length - 1; integer3 >= 0; --integer3) {
            final LevelChunkSection cgf4 = arr2[integer3];
            if (!LevelChunkSection.isEmpty(cgf4)) {
                return cgf4;
            }
        }
        return null;
    }
    
    default int getHighestSectionPosition() {
        final LevelChunkSection cgf2 = this.getHighestSection();
        return (cgf2 == null) ? 0 : cgf2.bottomBlockY();
    }
    
    Set<BlockPos> getBlockEntitiesPos();
    
    LevelChunkSection[] getSections();
    
    Collection<Map.Entry<Heightmap.Types, Heightmap>> getHeightmaps();
    
    void setHeightmap(final Heightmap.Types a, final long[] arr);
    
    Heightmap getOrCreateHeightmapUnprimed(final Heightmap.Types a);
    
    int getHeight(final Heightmap.Types a, final int integer2, final int integer3);
    
    ChunkPos getPos();
    
    void setLastSaveTime(final long long1);
    
    Map<StructureFeature<?>, StructureStart<?>> getAllStarts();
    
    void setAllStarts(final Map<StructureFeature<?>, StructureStart<?>> map);
    
    default boolean isYSpaceEmpty(int integer1, int integer2) {
        if (integer1 < 0) {
            integer1 = 0;
        }
        if (integer2 >= 256) {
            integer2 = 255;
        }
        for (int integer3 = integer1; integer3 <= integer2; integer3 += 16) {
            if (!LevelChunkSection.isEmpty(this.getSections()[integer3 >> 4])) {
                return false;
            }
        }
        return true;
    }
    
    @Nullable
    ChunkBiomeContainer getBiomes();
    
    void setUnsaved(final boolean boolean1);
    
    boolean isUnsaved();
    
    ChunkStatus getStatus();
    
    void removeBlockEntity(final BlockPos fx);
    
    default void markPosForPostprocessing(final BlockPos fx) {
        LogManager.getLogger().warn("Trying to mark a block for PostProcessing @ {}, but this operation is not supported.", fx);
    }
    
    ShortList[] getPostProcessing();
    
    default void addPackedPostProcess(final short short1, final int integer) {
        getOrCreateOffsetList(this.getPostProcessing(), integer).add(short1);
    }
    
    default void setBlockEntityNbt(final CompoundTag md) {
        LogManager.getLogger().warn("Trying to set a BlockEntity, but this operation is not supported.");
    }
    
    @Nullable
    CompoundTag getBlockEntityNbt(final BlockPos fx);
    
    @Nullable
    CompoundTag getBlockEntityNbtForSaving(final BlockPos fx);
    
    Stream<BlockPos> getLights();
    
    TickList<Block> getBlockTicks();
    
    TickList<Fluid> getLiquidTicks();
    
    UpgradeData getUpgradeData();
    
    void setInhabitedTime(final long long1);
    
    long getInhabitedTime();
    
    default ShortList getOrCreateOffsetList(final ShortList[] arr, final int integer) {
        if (arr[integer] == null) {
            arr[integer] = (ShortList)new ShortArrayList();
        }
        return arr[integer];
    }
    
    boolean isLightCorrect();
    
    void setLightCorrect(final boolean boolean1);
}
