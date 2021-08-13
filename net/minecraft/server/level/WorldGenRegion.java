package net.minecraft.server.level;

import org.apache.logging.log4j.LogManager;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import java.util.stream.Stream;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.core.SectionPos;
import java.util.Collections;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.core.Direction;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import java.util.function.Predicate;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nullable;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.Util;
import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import java.util.function.Function;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.TickList;
import net.minecraft.world.level.dimension.DimensionType;
import java.util.Random;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.chunk.ChunkAccess;
import java.util.List;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.level.WorldGenLevel;

public class WorldGenRegion implements WorldGenLevel {
    private static final Logger LOGGER;
    private final List<ChunkAccess> cache;
    private final int x;
    private final int z;
    private final int size;
    private final ServerLevel level;
    private final long seed;
    private final LevelData levelData;
    private final Random random;
    private final DimensionType dimensionType;
    private final TickList<Block> blockTicks;
    private final TickList<Fluid> liquidTicks;
    private final BiomeManager biomeManager;
    private final ChunkPos firstPos;
    private final ChunkPos lastPos;
    
    public WorldGenRegion(final ServerLevel aag, final List<ChunkAccess> list) {
        this.blockTicks = new WorldGenTickList<Block>((java.util.function.Function<BlockPos, TickList<Block>>)(fx -> this.getChunk(fx).getBlockTicks()));
        this.liquidTicks = new WorldGenTickList<Fluid>((java.util.function.Function<BlockPos, TickList<Fluid>>)(fx -> this.getChunk(fx).getLiquidTicks()));
        final int integer4 = Mth.floor(Math.sqrt((double)list.size()));
        if (integer4 * integer4 != list.size()) {
            throw Util.<IllegalStateException>pauseInIde(new IllegalStateException("Cache size is not a square."));
        }
        final ChunkPos bra5 = ((ChunkAccess)list.get(list.size() / 2)).getPos();
        this.cache = list;
        this.x = bra5.x;
        this.z = bra5.z;
        this.size = integer4;
        this.level = aag;
        this.seed = aag.getSeed();
        this.levelData = aag.getLevelData();
        this.random = aag.getRandom();
        this.dimensionType = aag.dimensionType();
        this.biomeManager = new BiomeManager(this, BiomeManager.obfuscateSeed(this.seed), aag.dimensionType().getBiomeZoomer());
        this.firstPos = ((ChunkAccess)list.get(0)).getPos();
        this.lastPos = ((ChunkAccess)list.get(list.size() - 1)).getPos();
    }
    
    public int getCenterX() {
        return this.x;
    }
    
    public int getCenterZ() {
        return this.z;
    }
    
    public ChunkAccess getChunk(final int integer1, final int integer2) {
        return this.getChunk(integer1, integer2, ChunkStatus.EMPTY);
    }
    
    @Nullable
    public ChunkAccess getChunk(final int integer1, final int integer2, final ChunkStatus cfx, final boolean boolean4) {
        ChunkAccess cft6;
        if (this.hasChunk(integer1, integer2)) {
            final int integer3 = integer1 - this.firstPos.x;
            final int integer4 = integer2 - this.firstPos.z;
            cft6 = (ChunkAccess)this.cache.get(integer3 + integer4 * this.size);
            if (cft6.getStatus().isOrAfter(cfx)) {
                return cft6;
            }
        }
        else {
            cft6 = null;
        }
        if (!boolean4) {
            return null;
        }
        WorldGenRegion.LOGGER.error("Requested chunk : {} {}", integer1, integer2);
        WorldGenRegion.LOGGER.error("Region bounds : {} {} | {} {}", this.firstPos.x, this.firstPos.z, this.lastPos.x, this.lastPos.z);
        if (cft6 != null) {
            throw Util.<RuntimeException>pauseInIde(new RuntimeException(String.format("Chunk is not of correct status. Expecting %s, got %s | %s %s", new Object[] { cfx, cft6.getStatus(), integer1, integer2 })));
        }
        throw Util.<RuntimeException>pauseInIde(new RuntimeException(String.format("We are asking a region for a chunk out of bound | %s %s", new Object[] { integer1, integer2 })));
    }
    
    public boolean hasChunk(final int integer1, final int integer2) {
        return integer1 >= this.firstPos.x && integer1 <= this.lastPos.x && integer2 >= this.firstPos.z && integer2 <= this.lastPos.z;
    }
    
    public BlockState getBlockState(final BlockPos fx) {
        return this.getChunk(fx.getX() >> 4, fx.getZ() >> 4).getBlockState(fx);
    }
    
    public FluidState getFluidState(final BlockPos fx) {
        return this.getChunk(fx).getFluidState(fx);
    }
    
    @Nullable
    public Player getNearestPlayer(final double double1, final double double2, final double double3, final double double4, final Predicate<Entity> predicate) {
        return null;
    }
    
    public int getSkyDarken() {
        return 0;
    }
    
    public BiomeManager getBiomeManager() {
        return this.biomeManager;
    }
    
    public Biome getUncachedNoiseBiome(final int integer1, final int integer2, final int integer3) {
        return this.level.getUncachedNoiseBiome(integer1, integer2, integer3);
    }
    
    public float getShade(final Direction gc, final boolean boolean2) {
        return 1.0f;
    }
    
    public LevelLightEngine getLightEngine() {
        return this.level.getLightEngine();
    }
    
    public boolean destroyBlock(final BlockPos fx, final boolean boolean2, @Nullable final Entity apx, final int integer) {
        final BlockState cee6 = this.getBlockState(fx);
        if (cee6.isAir()) {
            return false;
        }
        if (boolean2) {
            final BlockEntity ccg7 = cee6.getBlock().isEntityBlock() ? this.getBlockEntity(fx) : null;
            Block.dropResources(cee6, this.level, fx, ccg7, apx, ItemStack.EMPTY);
        }
        return this.setBlock(fx, Blocks.AIR.defaultBlockState(), 3, integer);
    }
    
    @Nullable
    public BlockEntity getBlockEntity(final BlockPos fx) {
        final ChunkAccess cft3 = this.getChunk(fx);
        BlockEntity ccg4 = cft3.getBlockEntity(fx);
        if (ccg4 != null) {
            return ccg4;
        }
        final CompoundTag md5 = cft3.getBlockEntityNbt(fx);
        final BlockState cee6 = cft3.getBlockState(fx);
        if (md5 != null) {
            if ("DUMMY".equals(md5.getString("id"))) {
                final Block bul7 = cee6.getBlock();
                if (!(bul7 instanceof EntityBlock)) {
                    return null;
                }
                ccg4 = ((EntityBlock)bul7).newBlockEntity(this.level);
            }
            else {
                ccg4 = BlockEntity.loadStatic(cee6, md5);
            }
            if (ccg4 != null) {
                cft3.setBlockEntity(fx, ccg4);
                return ccg4;
            }
        }
        if (cee6.getBlock() instanceof EntityBlock) {
            WorldGenRegion.LOGGER.warn("Tried to access a block entity before it was created. {}", fx);
        }
        return null;
    }
    
    public boolean setBlock(final BlockPos fx, final BlockState cee, final int integer3, final int integer4) {
        final ChunkAccess cft6 = this.getChunk(fx);
        final BlockState cee2 = cft6.setBlockState(fx, cee, false);
        if (cee2 != null) {
            this.level.onBlockStateChange(fx, cee2, cee);
        }
        final Block bul8 = cee.getBlock();
        if (bul8.isEntityBlock()) {
            if (cft6.getStatus().getChunkType() == ChunkStatus.ChunkType.LEVELCHUNK) {
                cft6.setBlockEntity(fx, ((EntityBlock)bul8).newBlockEntity(this));
            }
            else {
                final CompoundTag md9 = new CompoundTag();
                md9.putInt("x", fx.getX());
                md9.putInt("y", fx.getY());
                md9.putInt("z", fx.getZ());
                md9.putString("id", "DUMMY");
                cft6.setBlockEntityNbt(md9);
            }
        }
        else if (cee2 != null && cee2.getBlock().isEntityBlock()) {
            cft6.removeBlockEntity(fx);
        }
        if (cee.hasPostProcess(this, fx)) {
            this.markPosForPostprocessing(fx);
        }
        return true;
    }
    
    private void markPosForPostprocessing(final BlockPos fx) {
        this.getChunk(fx).markPosForPostprocessing(fx);
    }
    
    public boolean addFreshEntity(final Entity apx) {
        final int integer3 = Mth.floor(apx.getX() / 16.0);
        final int integer4 = Mth.floor(apx.getZ() / 16.0);
        this.getChunk(integer3, integer4).addEntity(apx);
        return true;
    }
    
    public boolean removeBlock(final BlockPos fx, final boolean boolean2) {
        return this.setBlock(fx, Blocks.AIR.defaultBlockState(), 3);
    }
    
    public WorldBorder getWorldBorder() {
        return this.level.getWorldBorder();
    }
    
    public boolean isClientSide() {
        return false;
    }
    
    @Deprecated
    public ServerLevel getLevel() {
        return this.level;
    }
    
    public RegistryAccess registryAccess() {
        return this.level.registryAccess();
    }
    
    public LevelData getLevelData() {
        return this.levelData;
    }
    
    public DifficultyInstance getCurrentDifficultyAt(final BlockPos fx) {
        if (!this.hasChunk(fx.getX() >> 4, fx.getZ() >> 4)) {
            throw new RuntimeException("We are asking a region for a chunk out of bound");
        }
        return new DifficultyInstance(this.level.getDifficulty(), this.level.getDayTime(), 0L, this.level.getMoonBrightness());
    }
    
    public ChunkSource getChunkSource() {
        return this.level.getChunkSource();
    }
    
    public long getSeed() {
        return this.seed;
    }
    
    public TickList<Block> getBlockTicks() {
        return this.blockTicks;
    }
    
    public TickList<Fluid> getLiquidTicks() {
        return this.liquidTicks;
    }
    
    public int getSeaLevel() {
        return this.level.getSeaLevel();
    }
    
    public Random getRandom() {
        return this.random;
    }
    
    public int getHeight(final Heightmap.Types a, final int integer2, final int integer3) {
        return this.getChunk(integer2 >> 4, integer3 >> 4).getHeight(a, integer2 & 0xF, integer3 & 0xF) + 1;
    }
    
    public void playSound(@Nullable final Player bft, final BlockPos fx, final SoundEvent adn, final SoundSource adp, final float float5, final float float6) {
    }
    
    public void addParticle(final ParticleOptions hf, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7) {
    }
    
    public void levelEvent(@Nullable final Player bft, final int integer2, final BlockPos fx, final int integer4) {
    }
    
    public DimensionType dimensionType() {
        return this.dimensionType;
    }
    
    public boolean isStateAtPosition(final BlockPos fx, final Predicate<BlockState> predicate) {
        return predicate.test(this.getBlockState(fx));
    }
    
    public <T extends Entity> List<T> getEntitiesOfClass(final Class<? extends T> class1, final AABB dcf, @Nullable final Predicate<? super T> predicate) {
        return (List<T>)Collections.emptyList();
    }
    
    public List<Entity> getEntities(@Nullable final Entity apx, final AABB dcf, @Nullable final Predicate<? super Entity> predicate) {
        return (List<Entity>)Collections.emptyList();
    }
    
    public List<Player> players() {
        return (List<Player>)Collections.emptyList();
    }
    
    public Stream<? extends StructureStart<?>> startsForFeature(final SectionPos gp, final StructureFeature<?> ckx) {
        return this.level.startsForFeature(gp, ckx);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
