package net.minecraft.world.level;

import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.tags.TagContainer;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.chunk.ChunkSource;
import java.util.function.Predicate;
import net.minecraft.world.phys.AABB;
import java.io.IOException;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import java.util.function.Consumer;
import java.util.Iterator;
import java.util.Collection;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.ReportedException;
import net.minecraft.CrashReportCategory;
import net.minecraft.CrashReportDetail;
import net.minecraft.CrashReport;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.Block;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.core.BlockPos;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import com.google.common.collect.Lists;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.util.profiling.ProfilerFiller;
import java.util.function.Supplier;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.level.dimension.DimensionType;
import java.util.Random;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import com.mojang.serialization.Codec;
import org.apache.logging.log4j.Logger;

public abstract class Level implements LevelAccessor, AutoCloseable {
    protected static final Logger LOGGER;
    public static final Codec<ResourceKey<Level>> RESOURCE_KEY_CODEC;
    public static final ResourceKey<Level> OVERWORLD;
    public static final ResourceKey<Level> NETHER;
    public static final ResourceKey<Level> END;
    private static final Direction[] DIRECTIONS;
    public final List<BlockEntity> blockEntityList;
    public final List<BlockEntity> tickableBlockEntities;
    protected final List<BlockEntity> pendingBlockEntities;
    protected final List<BlockEntity> blockEntitiesToUnload;
    private final Thread thread;
    private final boolean isDebug;
    private int skyDarken;
    protected int randValue;
    protected final int addend = 1013904223;
    protected float oRainLevel;
    protected float rainLevel;
    protected float oThunderLevel;
    protected float thunderLevel;
    public final Random random;
    private final DimensionType dimensionType;
    protected final WritableLevelData levelData;
    private final Supplier<ProfilerFiller> profiler;
    public final boolean isClientSide;
    protected boolean updatingBlockEntities;
    private final WorldBorder worldBorder;
    private final BiomeManager biomeManager;
    private final ResourceKey<Level> dimension;
    
    protected Level(final WritableLevelData cyl, final ResourceKey<Level> vj, final DimensionType cha, final Supplier<ProfilerFiller> supplier, final boolean boolean5, final boolean boolean6, final long long7) {
        this.blockEntityList = (List<BlockEntity>)Lists.newArrayList();
        this.tickableBlockEntities = (List<BlockEntity>)Lists.newArrayList();
        this.pendingBlockEntities = (List<BlockEntity>)Lists.newArrayList();
        this.blockEntitiesToUnload = (List<BlockEntity>)Lists.newArrayList();
        this.randValue = new Random().nextInt();
        this.random = new Random();
        this.profiler = supplier;
        this.levelData = cyl;
        this.dimensionType = cha;
        this.dimension = vj;
        this.isClientSide = boolean5;
        if (cha.coordinateScale() != 1.0) {
            this.worldBorder = new WorldBorder() {
                @Override
                public double getCenterX() {
                    return super.getCenterX() / cha.coordinateScale();
                }
                
                @Override
                public double getCenterZ() {
                    return super.getCenterZ() / cha.coordinateScale();
                }
            };
        }
        else {
            this.worldBorder = new WorldBorder();
        }
        this.thread = Thread.currentThread();
        this.biomeManager = new BiomeManager(this, long7, cha.getBiomeZoomer());
        this.isDebug = boolean6;
    }
    
    public boolean isClientSide() {
        return this.isClientSide;
    }
    
    @Nullable
    public MinecraftServer getServer() {
        return null;
    }
    
    public static boolean isInWorldBounds(final BlockPos fx) {
        return !isOutsideBuildHeight(fx) && isInWorldBoundsHorizontal(fx);
    }
    
    public static boolean isInSpawnableBounds(final BlockPos fx) {
        return !isOutsideSpawnableHeight(fx.getY()) && isInWorldBoundsHorizontal(fx);
    }
    
    private static boolean isInWorldBoundsHorizontal(final BlockPos fx) {
        return fx.getX() >= -30000000 && fx.getZ() >= -30000000 && fx.getX() < 30000000 && fx.getZ() < 30000000;
    }
    
    private static boolean isOutsideSpawnableHeight(final int integer) {
        return integer < -20000000 || integer >= 20000000;
    }
    
    public static boolean isOutsideBuildHeight(final BlockPos fx) {
        return isOutsideBuildHeight(fx.getY());
    }
    
    public static boolean isOutsideBuildHeight(final int integer) {
        return integer < 0 || integer >= 256;
    }
    
    public LevelChunk getChunkAt(final BlockPos fx) {
        return this.getChunk(fx.getX() >> 4, fx.getZ() >> 4);
    }
    
    public LevelChunk getChunk(final int integer1, final int integer2) {
        return (LevelChunk)this.getChunk(integer1, integer2, ChunkStatus.FULL);
    }
    
    public ChunkAccess getChunk(final int integer1, final int integer2, final ChunkStatus cfx, final boolean boolean4) {
        final ChunkAccess cft6 = this.getChunkSource().getChunk(integer1, integer2, cfx, boolean4);
        if (cft6 == null && boolean4) {
            throw new IllegalStateException("Should always be able to create a chunk!");
        }
        return cft6;
    }
    
    public boolean setBlock(final BlockPos fx, final BlockState cee, final int integer) {
        return this.setBlock(fx, cee, integer, 512);
    }
    
    public boolean setBlock(final BlockPos fx, final BlockState cee, final int integer3, final int integer4) {
        if (isOutsideBuildHeight(fx)) {
            return false;
        }
        if (!this.isClientSide && this.isDebug()) {
            return false;
        }
        final LevelChunk cge6 = this.getChunkAt(fx);
        final Block bul7 = cee.getBlock();
        final BlockState cee2 = cge6.setBlockState(fx, cee, (integer3 & 0x40) != 0x0);
        if (cee2 != null) {
            final BlockState cee3 = this.getBlockState(fx);
            if ((integer3 & 0x80) == 0x0 && cee3 != cee2 && (cee3.getLightBlock(this, fx) != cee2.getLightBlock(this, fx) || cee3.getLightEmission() != cee2.getLightEmission() || cee3.useShapeForLightOcclusion() || cee2.useShapeForLightOcclusion())) {
                this.getProfiler().push("queueCheckLight");
                this.getChunkSource().getLightEngine().checkBlock(fx);
                this.getProfiler().pop();
            }
            if (cee3 == cee) {
                if (cee2 != cee3) {
                    this.setBlocksDirty(fx, cee2, cee3);
                }
                if ((integer3 & 0x2) != 0x0 && (!this.isClientSide || (integer3 & 0x4) == 0x0) && (this.isClientSide || (cge6.getFullStatus() != null && cge6.getFullStatus().isOrAfter(ChunkHolder.FullChunkStatus.TICKING)))) {
                    this.sendBlockUpdated(fx, cee2, cee, integer3);
                }
                if ((integer3 & 0x1) != 0x0) {
                    this.blockUpdated(fx, cee2.getBlock());
                    if (!this.isClientSide && cee.hasAnalogOutputSignal()) {
                        this.updateNeighbourForOutputSignal(fx, bul7);
                    }
                }
                if ((integer3 & 0x10) == 0x0 && integer4 > 0) {
                    final int integer5 = integer3 & 0xFFFFFFDE;
                    cee2.updateIndirectNeighbourShapes(this, fx, integer5, integer4 - 1);
                    cee.updateNeighbourShapes(this, fx, integer5, integer4 - 1);
                    cee.updateIndirectNeighbourShapes(this, fx, integer5, integer4 - 1);
                }
                this.onBlockStateChange(fx, cee2, cee3);
            }
            return true;
        }
        return false;
    }
    
    public void onBlockStateChange(final BlockPos fx, final BlockState cee2, final BlockState cee3) {
    }
    
    public boolean removeBlock(final BlockPos fx, final boolean boolean2) {
        final FluidState cuu4 = this.getFluidState(fx);
        return this.setBlock(fx, cuu4.createLegacyBlock(), 0x3 | (boolean2 ? 64 : 0));
    }
    
    public boolean destroyBlock(final BlockPos fx, final boolean boolean2, @Nullable final Entity apx, final int integer) {
        final BlockState cee6 = this.getBlockState(fx);
        if (cee6.isAir()) {
            return false;
        }
        final FluidState cuu7 = this.getFluidState(fx);
        if (!(cee6.getBlock() instanceof BaseFireBlock)) {
            this.levelEvent(2001, fx, Block.getId(cee6));
        }
        if (boolean2) {
            final BlockEntity ccg8 = cee6.getBlock().isEntityBlock() ? this.getBlockEntity(fx) : null;
            Block.dropResources(cee6, this, fx, ccg8, apx, ItemStack.EMPTY);
        }
        return this.setBlock(fx, cuu7.createLegacyBlock(), 3, integer);
    }
    
    public boolean setBlockAndUpdate(final BlockPos fx, final BlockState cee) {
        return this.setBlock(fx, cee, 3);
    }
    
    public abstract void sendBlockUpdated(final BlockPos fx, final BlockState cee2, final BlockState cee3, final int integer);
    
    public void setBlocksDirty(final BlockPos fx, final BlockState cee2, final BlockState cee3) {
    }
    
    public void updateNeighborsAt(final BlockPos fx, final Block bul) {
        this.neighborChanged(fx.west(), bul, fx);
        this.neighborChanged(fx.east(), bul, fx);
        this.neighborChanged(fx.below(), bul, fx);
        this.neighborChanged(fx.above(), bul, fx);
        this.neighborChanged(fx.north(), bul, fx);
        this.neighborChanged(fx.south(), bul, fx);
    }
    
    public void updateNeighborsAtExceptFromFacing(final BlockPos fx, final Block bul, final Direction gc) {
        if (gc != Direction.WEST) {
            this.neighborChanged(fx.west(), bul, fx);
        }
        if (gc != Direction.EAST) {
            this.neighborChanged(fx.east(), bul, fx);
        }
        if (gc != Direction.DOWN) {
            this.neighborChanged(fx.below(), bul, fx);
        }
        if (gc != Direction.UP) {
            this.neighborChanged(fx.above(), bul, fx);
        }
        if (gc != Direction.NORTH) {
            this.neighborChanged(fx.north(), bul, fx);
        }
        if (gc != Direction.SOUTH) {
            this.neighborChanged(fx.south(), bul, fx);
        }
    }
    
    public void neighborChanged(final BlockPos fx1, final Block bul, final BlockPos fx3) {
        if (this.isClientSide) {
            return;
        }
        final BlockState cee5 = this.getBlockState(fx1);
        try {
            cee5.neighborChanged(this, fx1, bul, fx3, false);
        }
        catch (Throwable throwable6) {
            final CrashReport l7 = CrashReport.forThrowable(throwable6, "Exception while updating neighbours");
            final CrashReportCategory m8 = l7.addCategory("Block being updated");
            m8.setDetail("Source block type", (CrashReportDetail<String>)(() -> {
                try {
                    return String.format("ID #%s (%s // %s)", new Object[] { Registry.BLOCK.getKey(bul), bul.getDescriptionId(), bul.getClass().getCanonicalName() });
                }
                catch (Throwable throwable2) {
                    return new StringBuilder().append("ID #").append(Registry.BLOCK.getKey(bul)).toString();
                }
            }));
            CrashReportCategory.populateBlockDetails(m8, fx1, cee5);
            throw new ReportedException(l7);
        }
    }
    
    public int getHeight(final Heightmap.Types a, final int integer2, final int integer3) {
        int integer4;
        if (integer2 < -30000000 || integer3 < -30000000 || integer2 >= 30000000 || integer3 >= 30000000) {
            integer4 = this.getSeaLevel() + 1;
        }
        else if (this.hasChunk(integer2 >> 4, integer3 >> 4)) {
            integer4 = this.getChunk(integer2 >> 4, integer3 >> 4).getHeight(a, integer2 & 0xF, integer3 & 0xF) + 1;
        }
        else {
            integer4 = 0;
        }
        return integer4;
    }
    
    public LevelLightEngine getLightEngine() {
        return this.getChunkSource().getLightEngine();
    }
    
    public BlockState getBlockState(final BlockPos fx) {
        if (isOutsideBuildHeight(fx)) {
            return Blocks.VOID_AIR.defaultBlockState();
        }
        final LevelChunk cge3 = this.getChunk(fx.getX() >> 4, fx.getZ() >> 4);
        return cge3.getBlockState(fx);
    }
    
    public FluidState getFluidState(final BlockPos fx) {
        if (isOutsideBuildHeight(fx)) {
            return Fluids.EMPTY.defaultFluidState();
        }
        final LevelChunk cge3 = this.getChunkAt(fx);
        return cge3.getFluidState(fx);
    }
    
    public boolean isDay() {
        return !this.dimensionType().hasFixedTime() && this.skyDarken < 4;
    }
    
    public boolean isNight() {
        return !this.dimensionType().hasFixedTime() && !this.isDay();
    }
    
    public void playSound(@Nullable final Player bft, final BlockPos fx, final SoundEvent adn, final SoundSource adp, final float float5, final float float6) {
        this.playSound(bft, fx.getX() + 0.5, fx.getY() + 0.5, fx.getZ() + 0.5, adn, adp, float5, float6);
    }
    
    public abstract void playSound(@Nullable final Player bft, final double double2, final double double3, final double double4, final SoundEvent adn, final SoundSource adp, final float float7, final float float8);
    
    public abstract void playSound(@Nullable final Player bft, final Entity apx, final SoundEvent adn, final SoundSource adp, final float float5, final float float6);
    
    public void playLocalSound(final double double1, final double double2, final double double3, final SoundEvent adn, final SoundSource adp, final float float6, final float float7, final boolean boolean8) {
    }
    
    public void addParticle(final ParticleOptions hf, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7) {
    }
    
    public void addParticle(final ParticleOptions hf, final boolean boolean2, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
    }
    
    public void addAlwaysVisibleParticle(final ParticleOptions hf, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7) {
    }
    
    public void addAlwaysVisibleParticle(final ParticleOptions hf, final boolean boolean2, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
    }
    
    public float getSunAngle(final float float1) {
        final float float2 = this.getTimeOfDay(float1);
        return float2 * 6.2831855f;
    }
    
    public boolean addBlockEntity(final BlockEntity ccg) {
        if (this.updatingBlockEntities) {
            Level.LOGGER.error("Adding block entity while ticking: {} @ {}", new org.apache.logging.log4j.util.Supplier[] { () -> Registry.BLOCK_ENTITY_TYPE.getKey(ccg.getType()), ccg::getBlockPos });
        }
        final boolean boolean3 = this.blockEntityList.add(ccg);
        if (boolean3 && ccg instanceof TickableBlockEntity) {
            this.tickableBlockEntities.add(ccg);
        }
        if (this.isClientSide) {
            final BlockPos fx4 = ccg.getBlockPos();
            final BlockState cee5 = this.getBlockState(fx4);
            this.sendBlockUpdated(fx4, cee5, cee5, 2);
        }
        return boolean3;
    }
    
    public void addAllPendingBlockEntities(final Collection<BlockEntity> collection) {
        if (this.updatingBlockEntities) {
            this.pendingBlockEntities.addAll((Collection)collection);
        }
        else {
            for (final BlockEntity ccg4 : collection) {
                this.addBlockEntity(ccg4);
            }
        }
    }
    
    public void tickBlockEntities() {
        final ProfilerFiller ant2 = this.getProfiler();
        ant2.push("blockEntities");
        if (!this.blockEntitiesToUnload.isEmpty()) {
            this.tickableBlockEntities.removeAll((Collection)this.blockEntitiesToUnload);
            this.blockEntityList.removeAll((Collection)this.blockEntitiesToUnload);
            this.blockEntitiesToUnload.clear();
        }
        this.updatingBlockEntities = true;
        final Iterator<BlockEntity> iterator3 = (Iterator<BlockEntity>)this.tickableBlockEntities.iterator();
        while (iterator3.hasNext()) {
            final BlockEntity ccg4 = (BlockEntity)iterator3.next();
            if (!ccg4.isRemoved() && ccg4.hasLevel()) {
                final BlockPos fx5 = ccg4.getBlockPos();
                if (this.getChunkSource().isTickingChunk(fx5) && this.getWorldBorder().isWithinBounds(fx5)) {
                    try {
                        ant2.push((Supplier<String>)(() -> String.valueOf(BlockEntityType.getKey(ccg4.getType()))));
                        if (ccg4.getType().isValid(this.getBlockState(fx5).getBlock())) {
                            ((TickableBlockEntity)ccg4).tick();
                        }
                        else {
                            ccg4.logInvalidState();
                        }
                        ant2.pop();
                    }
                    catch (Throwable throwable6) {
                        final CrashReport l7 = CrashReport.forThrowable(throwable6, "Ticking block entity");
                        final CrashReportCategory m8 = l7.addCategory("Block entity being ticked");
                        ccg4.fillCrashReportCategory(m8);
                        throw new ReportedException(l7);
                    }
                }
            }
            if (ccg4.isRemoved()) {
                iterator3.remove();
                this.blockEntityList.remove(ccg4);
                if (!this.hasChunkAt(ccg4.getBlockPos())) {
                    continue;
                }
                this.getChunkAt(ccg4.getBlockPos()).removeBlockEntity(ccg4.getBlockPos());
            }
        }
        this.updatingBlockEntities = false;
        ant2.popPush("pendingBlockEntities");
        if (!this.pendingBlockEntities.isEmpty()) {
            for (int integer4 = 0; integer4 < this.pendingBlockEntities.size(); ++integer4) {
                final BlockEntity ccg5 = (BlockEntity)this.pendingBlockEntities.get(integer4);
                if (!ccg5.isRemoved()) {
                    if (!this.blockEntityList.contains(ccg5)) {
                        this.addBlockEntity(ccg5);
                    }
                    if (this.hasChunkAt(ccg5.getBlockPos())) {
                        final LevelChunk cge6 = this.getChunkAt(ccg5.getBlockPos());
                        final BlockState cee7 = cge6.getBlockState(ccg5.getBlockPos());
                        cge6.setBlockEntity(ccg5.getBlockPos(), ccg5);
                        this.sendBlockUpdated(ccg5.getBlockPos(), cee7, cee7, 3);
                    }
                }
            }
            this.pendingBlockEntities.clear();
        }
        ant2.pop();
    }
    
    public void guardEntityTick(final Consumer<Entity> consumer, final Entity apx) {
        try {
            consumer.accept(apx);
        }
        catch (Throwable throwable4) {
            final CrashReport l5 = CrashReport.forThrowable(throwable4, "Ticking entity");
            final CrashReportCategory m6 = l5.addCategory("Entity being ticked");
            apx.fillCrashReportCategory(m6);
            throw new ReportedException(l5);
        }
    }
    
    public Explosion explode(@Nullable final Entity apx, final double double2, final double double3, final double double4, final float float5, final Explosion.BlockInteraction a) {
        return this.explode(apx, null, null, double2, double3, double4, float5, false, a);
    }
    
    public Explosion explode(@Nullable final Entity apx, final double double2, final double double3, final double double4, final float float5, final boolean boolean6, final Explosion.BlockInteraction a) {
        return this.explode(apx, null, null, double2, double3, double4, float5, boolean6, a);
    }
    
    public Explosion explode(@Nullable final Entity apx, @Nullable final DamageSource aph, @Nullable final ExplosionDamageCalculator brn, final double double4, final double double5, final double double6, final float float7, final boolean boolean8, final Explosion.BlockInteraction a) {
        final Explosion brm14 = new Explosion(this, apx, aph, brn, double4, double5, double6, float7, boolean8, a);
        brm14.explode();
        brm14.finalizeExplosion(true);
        return brm14;
    }
    
    public String gatherChunkSourceStats() {
        return this.getChunkSource().gatherStats();
    }
    
    @Nullable
    public BlockEntity getBlockEntity(final BlockPos fx) {
        if (isOutsideBuildHeight(fx)) {
            return null;
        }
        if (!this.isClientSide && Thread.currentThread() != this.thread) {
            return null;
        }
        BlockEntity ccg3 = null;
        if (this.updatingBlockEntities) {
            ccg3 = this.getPendingBlockEntityAt(fx);
        }
        if (ccg3 == null) {
            ccg3 = this.getChunkAt(fx).getBlockEntity(fx, LevelChunk.EntityCreationType.IMMEDIATE);
        }
        if (ccg3 == null) {
            ccg3 = this.getPendingBlockEntityAt(fx);
        }
        return ccg3;
    }
    
    @Nullable
    private BlockEntity getPendingBlockEntityAt(final BlockPos fx) {
        for (int integer3 = 0; integer3 < this.pendingBlockEntities.size(); ++integer3) {
            final BlockEntity ccg4 = (BlockEntity)this.pendingBlockEntities.get(integer3);
            if (!ccg4.isRemoved() && ccg4.getBlockPos().equals(fx)) {
                return ccg4;
            }
        }
        return null;
    }
    
    public void setBlockEntity(final BlockPos fx, @Nullable final BlockEntity ccg) {
        if (isOutsideBuildHeight(fx)) {
            return;
        }
        if (ccg != null && !ccg.isRemoved()) {
            if (this.updatingBlockEntities) {
                ccg.setLevelAndPosition(this, fx);
                final Iterator<BlockEntity> iterator4 = (Iterator<BlockEntity>)this.pendingBlockEntities.iterator();
                while (iterator4.hasNext()) {
                    final BlockEntity ccg2 = (BlockEntity)iterator4.next();
                    if (ccg2.getBlockPos().equals(fx)) {
                        ccg2.setRemoved();
                        iterator4.remove();
                    }
                }
                this.pendingBlockEntities.add(ccg);
            }
            else {
                this.getChunkAt(fx).setBlockEntity(fx, ccg);
                this.addBlockEntity(ccg);
            }
        }
    }
    
    public void removeBlockEntity(final BlockPos fx) {
        final BlockEntity ccg3 = this.getBlockEntity(fx);
        if (ccg3 != null && this.updatingBlockEntities) {
            ccg3.setRemoved();
            this.pendingBlockEntities.remove(ccg3);
        }
        else {
            if (ccg3 != null) {
                this.pendingBlockEntities.remove(ccg3);
                this.blockEntityList.remove(ccg3);
                this.tickableBlockEntities.remove(ccg3);
            }
            this.getChunkAt(fx).removeBlockEntity(fx);
        }
    }
    
    public boolean isLoaded(final BlockPos fx) {
        return !isOutsideBuildHeight(fx) && this.getChunkSource().hasChunk(fx.getX() >> 4, fx.getZ() >> 4);
    }
    
    public boolean loadedAndEntityCanStandOnFace(final BlockPos fx, final Entity apx, final Direction gc) {
        if (isOutsideBuildHeight(fx)) {
            return false;
        }
        final ChunkAccess cft5 = this.getChunk(fx.getX() >> 4, fx.getZ() >> 4, ChunkStatus.FULL, false);
        return cft5 != null && cft5.getBlockState(fx).entityCanStandOnFace(this, fx, apx, gc);
    }
    
    public boolean loadedAndEntityCanStandOn(final BlockPos fx, final Entity apx) {
        return this.loadedAndEntityCanStandOnFace(fx, apx, Direction.UP);
    }
    
    public void updateSkyBrightness() {
        final double double2 = 1.0 - this.getRainLevel(1.0f) * 5.0f / 16.0;
        final double double3 = 1.0 - this.getThunderLevel(1.0f) * 5.0f / 16.0;
        final double double4 = 0.5 + 2.0 * Mth.clamp(Mth.cos(this.getTimeOfDay(1.0f) * 6.2831855f), -0.25, 0.25);
        this.skyDarken = (int)((1.0 - double4 * double2 * double3) * 11.0);
    }
    
    public void setSpawnSettings(final boolean boolean1, final boolean boolean2) {
        this.getChunkSource().setSpawnSettings(boolean1, boolean2);
    }
    
    protected void prepareWeather() {
        if (this.levelData.isRaining()) {
            this.rainLevel = 1.0f;
            if (this.levelData.isThundering()) {
                this.thunderLevel = 1.0f;
            }
        }
    }
    
    public void close() throws IOException {
        this.getChunkSource().close();
    }
    
    @Nullable
    public BlockGetter getChunkForCollisions(final int integer1, final int integer2) {
        return this.getChunk(integer1, integer2, ChunkStatus.FULL, false);
    }
    
    public List<Entity> getEntities(@Nullable final Entity apx, final AABB dcf, @Nullable final Predicate<? super Entity> predicate) {
        this.getProfiler().incrementCounter("getEntities");
        final List<Entity> list5 = (List<Entity>)Lists.newArrayList();
        final int integer6 = Mth.floor((dcf.minX - 2.0) / 16.0);
        final int integer7 = Mth.floor((dcf.maxX + 2.0) / 16.0);
        final int integer8 = Mth.floor((dcf.minZ - 2.0) / 16.0);
        final int integer9 = Mth.floor((dcf.maxZ + 2.0) / 16.0);
        final ChunkSource cfw10 = this.getChunkSource();
        for (int integer10 = integer6; integer10 <= integer7; ++integer10) {
            for (int integer11 = integer8; integer11 <= integer9; ++integer11) {
                final LevelChunk cge13 = cfw10.getChunk(integer10, integer11, false);
                if (cge13 != null) {
                    cge13.getEntities(apx, dcf, list5, predicate);
                }
            }
        }
        return list5;
    }
    
    public <T extends Entity> List<T> getEntities(@Nullable final EntityType<T> aqb, final AABB dcf, final Predicate<? super T> predicate) {
        this.getProfiler().incrementCounter("getEntities");
        final int integer5 = Mth.floor((dcf.minX - 2.0) / 16.0);
        final int integer6 = Mth.ceil((dcf.maxX + 2.0) / 16.0);
        final int integer7 = Mth.floor((dcf.minZ - 2.0) / 16.0);
        final int integer8 = Mth.ceil((dcf.maxZ + 2.0) / 16.0);
        final List<T> list9 = (List<T>)Lists.newArrayList();
        for (int integer9 = integer5; integer9 < integer6; ++integer9) {
            for (int integer10 = integer7; integer10 < integer8; ++integer10) {
                final LevelChunk cge12 = this.getChunkSource().getChunk(integer9, integer10, false);
                if (cge12 != null) {
                    cge12.<Entity>getEntities(aqb, dcf, (java.util.List<? super Entity>)list9, (java.util.function.Predicate<? super Entity>)predicate);
                }
            }
        }
        return list9;
    }
    
    public <T extends Entity> List<T> getEntitiesOfClass(final Class<? extends T> class1, final AABB dcf, @Nullable final Predicate<? super T> predicate) {
        this.getProfiler().incrementCounter("getEntities");
        final int integer5 = Mth.floor((dcf.minX - 2.0) / 16.0);
        final int integer6 = Mth.ceil((dcf.maxX + 2.0) / 16.0);
        final int integer7 = Mth.floor((dcf.minZ - 2.0) / 16.0);
        final int integer8 = Mth.ceil((dcf.maxZ + 2.0) / 16.0);
        final List<T> list9 = (List<T>)Lists.newArrayList();
        final ChunkSource cfw10 = this.getChunkSource();
        for (int integer9 = integer5; integer9 < integer6; ++integer9) {
            for (int integer10 = integer7; integer10 < integer8; ++integer10) {
                final LevelChunk cge13 = cfw10.getChunk(integer9, integer10, false);
                if (cge13 != null) {
                    cge13.<T>getEntitiesOfClass(class1, dcf, list9, predicate);
                }
            }
        }
        return list9;
    }
    
    public <T extends Entity> List<T> getLoadedEntitiesOfClass(final Class<? extends T> class1, final AABB dcf, @Nullable final Predicate<? super T> predicate) {
        this.getProfiler().incrementCounter("getLoadedEntities");
        final int integer5 = Mth.floor((dcf.minX - 2.0) / 16.0);
        final int integer6 = Mth.ceil((dcf.maxX + 2.0) / 16.0);
        final int integer7 = Mth.floor((dcf.minZ - 2.0) / 16.0);
        final int integer8 = Mth.ceil((dcf.maxZ + 2.0) / 16.0);
        final List<T> list9 = (List<T>)Lists.newArrayList();
        final ChunkSource cfw10 = this.getChunkSource();
        for (int integer9 = integer5; integer9 < integer6; ++integer9) {
            for (int integer10 = integer7; integer10 < integer8; ++integer10) {
                final LevelChunk cge13 = cfw10.getChunkNow(integer9, integer10);
                if (cge13 != null) {
                    cge13.<T>getEntitiesOfClass(class1, dcf, list9, predicate);
                }
            }
        }
        return list9;
    }
    
    @Nullable
    public abstract Entity getEntity(final int integer);
    
    public void blockEntityChanged(final BlockPos fx, final BlockEntity ccg) {
        if (this.hasChunkAt(fx)) {
            this.getChunkAt(fx).markUnsaved();
        }
    }
    
    public int getSeaLevel() {
        return 63;
    }
    
    public int getDirectSignalTo(final BlockPos fx) {
        int integer3 = 0;
        integer3 = Math.max(integer3, this.getDirectSignal(fx.below(), Direction.DOWN));
        if (integer3 >= 15) {
            return integer3;
        }
        integer3 = Math.max(integer3, this.getDirectSignal(fx.above(), Direction.UP));
        if (integer3 >= 15) {
            return integer3;
        }
        integer3 = Math.max(integer3, this.getDirectSignal(fx.north(), Direction.NORTH));
        if (integer3 >= 15) {
            return integer3;
        }
        integer3 = Math.max(integer3, this.getDirectSignal(fx.south(), Direction.SOUTH));
        if (integer3 >= 15) {
            return integer3;
        }
        integer3 = Math.max(integer3, this.getDirectSignal(fx.west(), Direction.WEST));
        if (integer3 >= 15) {
            return integer3;
        }
        integer3 = Math.max(integer3, this.getDirectSignal(fx.east(), Direction.EAST));
        if (integer3 >= 15) {
            return integer3;
        }
        return integer3;
    }
    
    public boolean hasSignal(final BlockPos fx, final Direction gc) {
        return this.getSignal(fx, gc) > 0;
    }
    
    public int getSignal(final BlockPos fx, final Direction gc) {
        final BlockState cee4 = this.getBlockState(fx);
        final int integer5 = cee4.getSignal(this, fx, gc);
        if (cee4.isRedstoneConductor(this, fx)) {
            return Math.max(integer5, this.getDirectSignalTo(fx));
        }
        return integer5;
    }
    
    public boolean hasNeighborSignal(final BlockPos fx) {
        return this.getSignal(fx.below(), Direction.DOWN) > 0 || this.getSignal(fx.above(), Direction.UP) > 0 || this.getSignal(fx.north(), Direction.NORTH) > 0 || this.getSignal(fx.south(), Direction.SOUTH) > 0 || this.getSignal(fx.west(), Direction.WEST) > 0 || this.getSignal(fx.east(), Direction.EAST) > 0;
    }
    
    public int getBestNeighborSignal(final BlockPos fx) {
        int integer3 = 0;
        for (final Direction gc7 : Level.DIRECTIONS) {
            final int integer4 = this.getSignal(fx.relative(gc7), gc7);
            if (integer4 >= 15) {
                return 15;
            }
            if (integer4 > integer3) {
                integer3 = integer4;
            }
        }
        return integer3;
    }
    
    public void disconnect() {
    }
    
    public long getGameTime() {
        return this.levelData.getGameTime();
    }
    
    public long getDayTime() {
        return this.levelData.getDayTime();
    }
    
    public boolean mayInteract(final Player bft, final BlockPos fx) {
        return true;
    }
    
    public void broadcastEntityEvent(final Entity apx, final byte byte2) {
    }
    
    public void blockEvent(final BlockPos fx, final Block bul, final int integer3, final int integer4) {
        this.getBlockState(fx).triggerEvent(this, fx, integer3, integer4);
    }
    
    public LevelData getLevelData() {
        return this.levelData;
    }
    
    public GameRules getGameRules() {
        return this.levelData.getGameRules();
    }
    
    public float getThunderLevel(final float float1) {
        return Mth.lerp(float1, this.oThunderLevel, this.thunderLevel) * this.getRainLevel(float1);
    }
    
    public void setThunderLevel(final float float1) {
        this.oThunderLevel = float1;
        this.thunderLevel = float1;
    }
    
    public float getRainLevel(final float float1) {
        return Mth.lerp(float1, this.oRainLevel, this.rainLevel);
    }
    
    public void setRainLevel(final float float1) {
        this.oRainLevel = float1;
        this.rainLevel = float1;
    }
    
    public boolean isThundering() {
        return this.dimensionType().hasSkyLight() && !this.dimensionType().hasCeiling() && this.getThunderLevel(1.0f) > 0.9;
    }
    
    public boolean isRaining() {
        return this.getRainLevel(1.0f) > 0.2;
    }
    
    public boolean isRainingAt(final BlockPos fx) {
        if (!this.isRaining()) {
            return false;
        }
        if (!this.canSeeSky(fx)) {
            return false;
        }
        if (this.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, fx).getY() > fx.getY()) {
            return false;
        }
        final Biome bss3 = this.getBiome(fx);
        return bss3.getPrecipitation() == Biome.Precipitation.RAIN && bss3.getTemperature(fx) >= 0.15f;
    }
    
    public boolean isHumidAt(final BlockPos fx) {
        final Biome bss3 = this.getBiome(fx);
        return bss3.isHumid();
    }
    
    @Nullable
    public abstract MapItemSavedData getMapData(final String string);
    
    public abstract void setMapData(final MapItemSavedData cxu);
    
    public abstract int getFreeMapId();
    
    public void globalLevelEvent(final int integer1, final BlockPos fx, final int integer3) {
    }
    
    public CrashReportCategory fillReportDetails(final CrashReport l) {
        final CrashReportCategory m3 = l.addCategory("Affected level", 1);
        m3.setDetail("All players", (CrashReportDetail<String>)(() -> new StringBuilder().append(this.players().size()).append(" total; ").append(this.players()).toString()));
        m3.setDetail("Chunk stats", (CrashReportDetail<String>)this.getChunkSource()::gatherStats);
        m3.setDetail("Level dimension", (CrashReportDetail<String>)(() -> this.dimension().location().toString()));
        try {
            this.levelData.fillCrashReportCategory(m3);
        }
        catch (Throwable throwable4) {
            m3.setDetailError("Level Data Unobtainable", throwable4);
        }
        return m3;
    }
    
    public abstract void destroyBlockProgress(final int integer1, final BlockPos fx, final int integer3);
    
    public void createFireworks(final double double1, final double double2, final double double3, final double double4, final double double5, final double double6, @Nullable final CompoundTag md) {
    }
    
    public abstract Scoreboard getScoreboard();
    
    public void updateNeighbourForOutputSignal(final BlockPos fx, final Block bul) {
        for (final Direction gc5 : Direction.Plane.HORIZONTAL) {
            BlockPos fx2 = fx.relative(gc5);
            if (this.hasChunkAt(fx2)) {
                BlockState cee7 = this.getBlockState(fx2);
                if (cee7.is(Blocks.COMPARATOR)) {
                    cee7.neighborChanged(this, fx2, bul, fx, false);
                }
                else {
                    if (!cee7.isRedstoneConductor(this, fx2)) {
                        continue;
                    }
                    fx2 = fx2.relative(gc5);
                    cee7 = this.getBlockState(fx2);
                    if (!cee7.is(Blocks.COMPARATOR)) {
                        continue;
                    }
                    cee7.neighborChanged(this, fx2, bul, fx, false);
                }
            }
        }
    }
    
    public DifficultyInstance getCurrentDifficultyAt(final BlockPos fx) {
        long long3 = 0L;
        float float5 = 0.0f;
        if (this.hasChunkAt(fx)) {
            float5 = this.getMoonBrightness();
            long3 = this.getChunkAt(fx).getInhabitedTime();
        }
        return new DifficultyInstance(this.getDifficulty(), this.getDayTime(), long3, float5);
    }
    
    public int getSkyDarken() {
        return this.skyDarken;
    }
    
    public void setSkyFlashTime(final int integer) {
    }
    
    public WorldBorder getWorldBorder() {
        return this.worldBorder;
    }
    
    public void sendPacketToServer(final Packet<?> oj) {
        throw new UnsupportedOperationException("Can't send packets to server unless you're on the client.");
    }
    
    public DimensionType dimensionType() {
        return this.dimensionType;
    }
    
    public ResourceKey<Level> dimension() {
        return this.dimension;
    }
    
    public Random getRandom() {
        return this.random;
    }
    
    public boolean isStateAtPosition(final BlockPos fx, final Predicate<BlockState> predicate) {
        return predicate.test(this.getBlockState(fx));
    }
    
    public abstract RecipeManager getRecipeManager();
    
    public abstract TagContainer getTagManager();
    
    public BlockPos getBlockRandomPos(final int integer1, final int integer2, final int integer3, final int integer4) {
        this.randValue = this.randValue * 3 + 1013904223;
        final int integer5 = this.randValue >> 2;
        return new BlockPos(integer1 + (integer5 & 0xF), integer2 + (integer5 >> 16 & integer4), integer3 + (integer5 >> 8 & 0xF));
    }
    
    public boolean noSave() {
        return false;
    }
    
    public ProfilerFiller getProfiler() {
        return (ProfilerFiller)this.profiler.get();
    }
    
    public Supplier<ProfilerFiller> getProfilerSupplier() {
        return this.profiler;
    }
    
    public BiomeManager getBiomeManager() {
        return this.biomeManager;
    }
    
    public final boolean isDebug() {
        return this.isDebug;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        RESOURCE_KEY_CODEC = ResourceLocation.CODEC.xmap((Function)ResourceKey.elementKey(Registry.DIMENSION_REGISTRY), ResourceKey::location);
        OVERWORLD = ResourceKey.<Level>create(Registry.DIMENSION_REGISTRY, new ResourceLocation("overworld"));
        NETHER = ResourceKey.<Level>create(Registry.DIMENSION_REGISTRY, new ResourceLocation("the_nether"));
        END = ResourceKey.<Level>create(Registry.DIMENSION_REGISTRY, new ResourceLocation("the_end"));
        DIRECTIONS = Direction.values();
    }
}
