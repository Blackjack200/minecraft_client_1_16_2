package net.minecraft.world.level;

import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.block.Blocks;
import java.util.List;
import net.minecraft.util.WeighedRandom;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import java.util.Objects;
import net.minecraft.core.Position;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.NearestNeighborBiomeZoomer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.core.BlockPos;
import java.util.Iterator;
import net.minecraft.world.level.chunk.LevelChunk;
import java.util.function.Consumer;
import net.minecraft.world.entity.Mob;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import org.apache.logging.log4j.Logger;

public final class NaturalSpawner {
    private static final Logger LOGGER;
    private static final int MAGIC_NUMBER;
    private static final MobCategory[] SPAWNING_CATEGORIES;
    
    public static SpawnState createState(final int integer, final Iterable<Entity> iterable, final ChunkGetter b) {
        final PotentialCalculator bsg4 = new PotentialCalculator();
        final Object2IntOpenHashMap<MobCategory> object2IntOpenHashMap5 = (Object2IntOpenHashMap<MobCategory>)new Object2IntOpenHashMap();
        for (final Entity apx7 : iterable) {
            if (apx7 instanceof Mob) {
                final Mob aqk8 = (Mob)apx7;
                if (aqk8.isPersistenceRequired()) {
                    continue;
                }
                if (aqk8.requiresCustomPersistence()) {
                    continue;
                }
            }
            final MobCategory aql8 = apx7.getType().getCategory();
            if (aql8 == MobCategory.MISC) {
                continue;
            }
            final BlockPos fx9 = apx7.blockPosition();
            final long long10 = ChunkPos.asLong(fx9.getX() >> 4, fx9.getZ() >> 4);
            b.query(long10, (Consumer<LevelChunk>)(cge -> {
                final MobSpawnSettings.MobSpawnCost b7 = getRoughBiome(fx9, cge).getMobSettings().getMobSpawnCost(apx7.getType());
                if (b7 != null) {
                    bsg4.addCharge(apx7.blockPosition(), b7.getCharge());
                }
                object2IntOpenHashMap5.addTo(aql8, 1);
            }));
        }
        return new SpawnState(integer, (Object2IntOpenHashMap)object2IntOpenHashMap5, bsg4);
    }
    
    private static Biome getRoughBiome(final BlockPos fx, final ChunkAccess cft) {
        return NearestNeighborBiomeZoomer.INSTANCE.getBiome(0L, fx.getX(), fx.getY(), fx.getZ(), cft.getBiomes());
    }
    
    public static void spawnForChunk(final ServerLevel aag, final LevelChunk cge, final SpawnState d, final boolean boolean4, final boolean boolean5, final boolean boolean6) {
        aag.getProfiler().push("spawner");
        for (final MobCategory aql10 : NaturalSpawner.SPAWNING_CATEGORIES) {
            if ((boolean4 || !aql10.isFriendly()) && (boolean5 || aql10.isFriendly()) && (boolean6 || !aql10.isPersistent()) && d.canSpawnForCategory(aql10)) {
                spawnCategoryForChunk(aql10, aag, cge, (aqb, fx, cft) -> d.canSpawn(aqb, fx, cft), (aqk, cft) -> d.afterSpawn(aqk, cft));
            }
        }
        aag.getProfiler().pop();
    }
    
    public static void spawnCategoryForChunk(final MobCategory aql, final ServerLevel aag, final LevelChunk cge, final SpawnPredicate c, final AfterSpawnCallback a) {
        final BlockPos fx6 = getRandomPosWithin(aag, cge);
        if (fx6.getY() < 1) {
            return;
        }
        spawnCategoryForPosition(aql, aag, cge, fx6, c, a);
    }
    
    public static void spawnCategoryForPosition(final MobCategory aql, final ServerLevel aag, final ChunkAccess cft, final BlockPos fx, final SpawnPredicate c, final AfterSpawnCallback a) {
        final StructureFeatureManager bsk7 = aag.structureFeatureManager();
        final ChunkGenerator cfv8 = aag.getChunkSource().getGenerator();
        final int integer9 = fx.getY();
        final BlockState cee10 = cft.getBlockState(fx);
        if (cee10.isRedstoneConductor(cft, fx)) {
            return;
        }
        final BlockPos.MutableBlockPos a2 = new BlockPos.MutableBlockPos();
        int integer10 = 0;
        for (int integer11 = 0; integer11 < 3; ++integer11) {
            int integer12 = fx.getX();
            int integer13 = fx.getZ();
            final int integer14 = 6;
            MobSpawnSettings.SpawnerData c2 = null;
            SpawnGroupData aqz18 = null;
            int integer15 = Mth.ceil(aag.random.nextFloat() * 4.0f);
            int integer16 = 0;
            for (int integer17 = 0; integer17 < integer15; ++integer17) {
                integer12 += aag.random.nextInt(6) - aag.random.nextInt(6);
                integer13 += aag.random.nextInt(6) - aag.random.nextInt(6);
                a2.set(integer12, integer9, integer13);
                final double double22 = integer12 + 0.5;
                final double double23 = integer13 + 0.5;
                final Player bft26 = aag.getNearestPlayer(double22, integer9, double23, -1.0, false);
                if (bft26 != null) {
                    final double double24 = bft26.distanceToSqr(double22, integer9, double23);
                    if (isRightDistanceToPlayerAndSpawnPoint(aag, cft, a2, double24)) {
                        if (c2 == null) {
                            c2 = getRandomSpawnMobAt(aag, bsk7, cfv8, aql, aag.random, a2);
                            if (c2 == null) {
                                break;
                            }
                            integer15 = c2.minCount + aag.random.nextInt(1 + c2.maxCount - c2.minCount);
                        }
                        if (isValidSpawnPostitionForType(aag, aql, bsk7, cfv8, c2, a2, double24)) {
                            if (c.test(c2.type, a2, cft)) {
                                final Mob aqk29 = getMobForSpawn(aag, c2.type);
                                if (aqk29 == null) {
                                    return;
                                }
                                aqk29.moveTo(double22, integer9, double23, aag.random.nextFloat() * 360.0f, 0.0f);
                                if (isValidPositionForMob(aag, aqk29, double24)) {
                                    aqz18 = aqk29.finalizeSpawn(aag, aag.getCurrentDifficultyAt(aqk29.blockPosition()), MobSpawnType.NATURAL, aqz18, null);
                                    ++integer10;
                                    ++integer16;
                                    aag.addFreshEntityWithPassengers(aqk29);
                                    a.run(aqk29, cft);
                                    if (integer10 >= aqk29.getMaxSpawnClusterSize()) {
                                        return;
                                    }
                                    if (aqk29.isMaxGroupSizeReached(integer16)) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private static boolean isRightDistanceToPlayerAndSpawnPoint(final ServerLevel aag, final ChunkAccess cft, final BlockPos.MutableBlockPos a, final double double4) {
        if (double4 <= 576.0) {
            return false;
        }
        if (aag.getSharedSpawnPos().closerThan(new Vec3(a.getX() + 0.5, a.getY(), a.getZ() + 0.5), 24.0)) {
            return false;
        }
        final ChunkPos bra6 = new ChunkPos(a);
        return Objects.equals(bra6, cft.getPos()) || aag.getChunkSource().isEntityTickingChunk(bra6);
    }
    
    private static boolean isValidSpawnPostitionForType(final ServerLevel aag, final MobCategory aql, final StructureFeatureManager bsk, final ChunkGenerator cfv, final MobSpawnSettings.SpawnerData c, final BlockPos.MutableBlockPos a, final double double7) {
        final EntityType<?> aqb9 = c.type;
        if (aqb9.getCategory() == MobCategory.MISC) {
            return false;
        }
        if (!aqb9.canSpawnFarFromPlayer() && double7 > aqb9.getCategory().getDespawnDistance() * aqb9.getCategory().getDespawnDistance()) {
            return false;
        }
        if (!aqb9.canSummon() || !canSpawnMobAt(aag, bsk, cfv, aql, c, a)) {
            return false;
        }
        final SpawnPlacements.Type c2 = SpawnPlacements.getPlacementType(aqb9);
        return isSpawnPositionOk(c2, aag, a, aqb9) && SpawnPlacements.checkSpawnRules(aqb9, aag, MobSpawnType.NATURAL, a, aag.random) && aag.noCollision(aqb9.getAABB(a.getX() + 0.5, a.getY(), a.getZ() + 0.5));
    }
    
    @Nullable
    private static Mob getMobForSpawn(final ServerLevel aag, final EntityType<?> aqb) {
        Mob aqk3;
        try {
            final Entity apx4 = (Entity)aqb.create(aag);
            if (!(apx4 instanceof Mob)) {
                throw new IllegalStateException(new StringBuilder().append("Trying to spawn a non-mob: ").append(Registry.ENTITY_TYPE.getKey(aqb)).toString());
            }
            aqk3 = (Mob)apx4;
        }
        catch (Exception exception4) {
            NaturalSpawner.LOGGER.warn("Failed to create mob", (Throwable)exception4);
            return null;
        }
        return aqk3;
    }
    
    private static boolean isValidPositionForMob(final ServerLevel aag, final Mob aqk, final double double3) {
        return (double3 <= aqk.getType().getCategory().getDespawnDistance() * aqk.getType().getCategory().getDespawnDistance() || !aqk.removeWhenFarAway(double3)) && aqk.checkSpawnRules(aag, MobSpawnType.NATURAL) && aqk.checkSpawnObstruction(aag);
    }
    
    @Nullable
    private static MobSpawnSettings.SpawnerData getRandomSpawnMobAt(final ServerLevel aag, final StructureFeatureManager bsk, final ChunkGenerator cfv, final MobCategory aql, final Random random, final BlockPos fx) {
        final Biome bss7 = aag.getBiome(fx);
        if (aql == MobCategory.WATER_AMBIENT && bss7.getBiomeCategory() == Biome.BiomeCategory.RIVER && random.nextFloat() < 0.98f) {
            return null;
        }
        final List<MobSpawnSettings.SpawnerData> list8 = mobsAt(aag, bsk, cfv, aql, fx, bss7);
        if (list8.isEmpty()) {
            return null;
        }
        return WeighedRandom.<MobSpawnSettings.SpawnerData>getRandomItem(random, list8);
    }
    
    private static boolean canSpawnMobAt(final ServerLevel aag, final StructureFeatureManager bsk, final ChunkGenerator cfv, final MobCategory aql, final MobSpawnSettings.SpawnerData c, final BlockPos fx) {
        return mobsAt(aag, bsk, cfv, aql, fx, null).contains(c);
    }
    
    private static List<MobSpawnSettings.SpawnerData> mobsAt(final ServerLevel aag, final StructureFeatureManager bsk, final ChunkGenerator cfv, final MobCategory aql, final BlockPos fx, @Nullable final Biome bss) {
        if (aql == MobCategory.MONSTER && aag.getBlockState(fx.below()).getBlock() == Blocks.NETHER_BRICKS && bsk.getStructureAt(fx, false, StructureFeature.NETHER_BRIDGE).isValid()) {
            return StructureFeature.NETHER_BRIDGE.getSpecialEnemies();
        }
        return cfv.getMobsAt((bss != null) ? bss : aag.getBiome(fx), bsk, aql, fx);
    }
    
    private static BlockPos getRandomPosWithin(final Level bru, final LevelChunk cge) {
        final ChunkPos bra3 = cge.getPos();
        final int integer4 = bra3.getMinBlockX() + bru.random.nextInt(16);
        final int integer5 = bra3.getMinBlockZ() + bru.random.nextInt(16);
        final int integer6 = cge.getHeight(Heightmap.Types.WORLD_SURFACE, integer4, integer5) + 1;
        final int integer7 = bru.random.nextInt(integer6 + 1);
        return new BlockPos(integer4, integer7, integer5);
    }
    
    public static boolean isValidEmptySpawnBlock(final BlockGetter bqz, final BlockPos fx, final BlockState cee, final FluidState cuu, final EntityType<?> aqb) {
        return !cee.isCollisionShapeFullBlock(bqz, fx) && !cee.isSignalSource() && cuu.isEmpty() && !cee.is(BlockTags.PREVENT_MOB_SPAWNING_INSIDE) && !aqb.isBlockDangerous(cee);
    }
    
    public static boolean isSpawnPositionOk(final SpawnPlacements.Type c, final LevelReader brw, final BlockPos fx, @Nullable final EntityType<?> aqb) {
        if (c == SpawnPlacements.Type.NO_RESTRICTIONS) {
            return true;
        }
        if (aqb == null || !brw.getWorldBorder().isWithinBounds(fx)) {
            return false;
        }
        final BlockState cee5 = brw.getBlockState(fx);
        final FluidState cuu6 = brw.getFluidState(fx);
        final BlockPos fx2 = fx.above();
        final BlockPos fx3 = fx.below();
        switch (c) {
            case IN_WATER: {
                return cuu6.is(FluidTags.WATER) && brw.getFluidState(fx3).is(FluidTags.WATER) && !brw.getBlockState(fx2).isRedstoneConductor(brw, fx2);
            }
            case IN_LAVA: {
                return cuu6.is(FluidTags.LAVA);
            }
            default: {
                final BlockState cee6 = brw.getBlockState(fx3);
                return cee6.isValidSpawn(brw, fx3, aqb) && isValidEmptySpawnBlock(brw, fx, cee5, cuu6, aqb) && isValidEmptySpawnBlock(brw, fx2, brw.getBlockState(fx2), brw.getFluidState(fx2), aqb);
            }
        }
    }
    
    public static void spawnMobsForChunkGeneration(final ServerLevelAccessor bsh, final Biome bss, final int integer3, final int integer4, final Random random) {
        final MobSpawnSettings btd6 = bss.getMobSettings();
        final List<MobSpawnSettings.SpawnerData> list7 = btd6.getMobs(MobCategory.CREATURE);
        if (list7.isEmpty()) {
            return;
        }
        final int integer5 = integer3 << 4;
        final int integer6 = integer4 << 4;
        while (random.nextFloat() < btd6.getCreatureProbability()) {
            final MobSpawnSettings.SpawnerData c10 = WeighedRandom.<MobSpawnSettings.SpawnerData>getRandomItem(random, list7);
            final int integer7 = c10.minCount + random.nextInt(1 + c10.maxCount - c10.minCount);
            SpawnGroupData aqz12 = null;
            int integer8 = integer5 + random.nextInt(16);
            int integer9 = integer6 + random.nextInt(16);
            final int integer10 = integer8;
            final int integer11 = integer9;
            for (int integer12 = 0; integer12 < integer7; ++integer12) {
                boolean boolean18 = false;
                for (int integer13 = 0; !boolean18 && integer13 < 4; ++integer13) {
                    final BlockPos fx20 = getTopNonCollidingPos(bsh, c10.type, integer8, integer9);
                    if (c10.type.canSummon() && isSpawnPositionOk(SpawnPlacements.getPlacementType(c10.type), bsh, fx20, c10.type)) {
                        final float float21 = c10.type.getWidth();
                        final double double22 = Mth.clamp(integer8, integer5 + (double)float21, integer5 + 16.0 - float21);
                        final double double23 = Mth.clamp(integer9, integer6 + (double)float21, integer6 + 16.0 - float21);
                        if (!bsh.noCollision(c10.type.getAABB(double22, fx20.getY(), double23))) {
                            continue;
                        }
                        if (!SpawnPlacements.<Entity>checkSpawnRules(c10.type, bsh, MobSpawnType.CHUNK_GENERATION, new BlockPos(double22, fx20.getY(), double23), bsh.getRandom())) {
                            continue;
                        }
                        Entity apx26;
                        try {
                            apx26 = (Entity)c10.type.create(bsh.getLevel());
                        }
                        catch (Exception exception27) {
                            NaturalSpawner.LOGGER.warn("Failed to create mob", (Throwable)exception27);
                            continue;
                        }
                        apx26.moveTo(double22, fx20.getY(), double23, random.nextFloat() * 360.0f, 0.0f);
                        if (apx26 instanceof Mob) {
                            final Mob aqk27 = (Mob)apx26;
                            if (aqk27.checkSpawnRules(bsh, MobSpawnType.CHUNK_GENERATION) && aqk27.checkSpawnObstruction(bsh)) {
                                aqz12 = aqk27.finalizeSpawn(bsh, bsh.getCurrentDifficultyAt(aqk27.blockPosition()), MobSpawnType.CHUNK_GENERATION, aqz12, null);
                                bsh.addFreshEntityWithPassengers(aqk27);
                                boolean18 = true;
                            }
                        }
                    }
                    for (integer8 += random.nextInt(5) - random.nextInt(5), integer9 += random.nextInt(5) - random.nextInt(5); integer8 < integer5 || integer8 >= integer5 + 16 || integer9 < integer6 || integer9 >= integer6 + 16; integer8 = integer10 + random.nextInt(5) - random.nextInt(5), integer9 = integer11 + random.nextInt(5) - random.nextInt(5)) {}
                }
            }
        }
    }
    
    private static BlockPos getTopNonCollidingPos(final LevelReader brw, final EntityType<?> aqb, final int integer3, final int integer4) {
        final int integer5 = brw.getHeight(SpawnPlacements.getHeightmapType(aqb), integer3, integer4);
        final BlockPos.MutableBlockPos a6 = new BlockPos.MutableBlockPos(integer3, integer5, integer4);
        if (brw.dimensionType().hasCeiling()) {
            do {
                a6.move(Direction.DOWN);
            } while (!brw.getBlockState(a6).isAir());
            do {
                a6.move(Direction.DOWN);
            } while (brw.getBlockState(a6).isAir() && a6.getY() > 0);
        }
        if (SpawnPlacements.getPlacementType(aqb) == SpawnPlacements.Type.ON_GROUND) {
            final BlockPos fx7 = a6.below();
            if (brw.getBlockState(fx7).isPathfindable(brw, fx7, PathComputationType.LAND)) {
                return fx7;
            }
        }
        return a6.immutable();
    }
    
    static {
        LOGGER = LogManager.getLogger();
        MAGIC_NUMBER = (int)Math.pow(17.0, 2.0);
        SPAWNING_CATEGORIES = (MobCategory[])Stream.of((Object[])MobCategory.values()).filter(aql -> aql != MobCategory.MISC).toArray(MobCategory[]::new);
    }
    
    public static class SpawnState {
        private final int spawnableChunkCount;
        private final Object2IntOpenHashMap<MobCategory> mobCategoryCounts;
        private final PotentialCalculator spawnPotential;
        private final Object2IntMap<MobCategory> unmodifiableMobCategoryCounts;
        @Nullable
        private BlockPos lastCheckedPos;
        @Nullable
        private EntityType<?> lastCheckedType;
        private double lastCharge;
        
        private SpawnState(final int integer, final Object2IntOpenHashMap<MobCategory> object2IntOpenHashMap, final PotentialCalculator bsg) {
            this.spawnableChunkCount = integer;
            this.mobCategoryCounts = object2IntOpenHashMap;
            this.spawnPotential = bsg;
            this.unmodifiableMobCategoryCounts = (Object2IntMap<MobCategory>)Object2IntMaps.unmodifiable((Object2IntMap)object2IntOpenHashMap);
        }
        
        private boolean canSpawn(final EntityType<?> aqb, final BlockPos fx, final ChunkAccess cft) {
            this.lastCheckedPos = fx;
            this.lastCheckedType = aqb;
            final MobSpawnSettings.MobSpawnCost b5 = getRoughBiome(fx, cft).getMobSettings().getMobSpawnCost(aqb);
            if (b5 == null) {
                this.lastCharge = 0.0;
                return true;
            }
            final double double6 = b5.getCharge();
            this.lastCharge = double6;
            final double double7 = this.spawnPotential.getPotentialEnergyChange(fx, double6);
            return double7 <= b5.getEnergyBudget();
        }
        
        private void afterSpawn(final Mob aqk, final ChunkAccess cft) {
            final EntityType<?> aqb4 = aqk.getType();
            final BlockPos fx7 = aqk.blockPosition();
            double double5;
            if (fx7.equals(this.lastCheckedPos) && aqb4 == this.lastCheckedType) {
                double5 = this.lastCharge;
            }
            else {
                final MobSpawnSettings.MobSpawnCost b8 = getRoughBiome(fx7, cft).getMobSettings().getMobSpawnCost(aqb4);
                if (b8 != null) {
                    double5 = b8.getCharge();
                }
                else {
                    double5 = 0.0;
                }
            }
            this.spawnPotential.addCharge(fx7, double5);
            this.mobCategoryCounts.addTo(aqb4.getCategory(), 1);
        }
        
        public int getSpawnableChunkCount() {
            return this.spawnableChunkCount;
        }
        
        public Object2IntMap<MobCategory> getMobCategoryCounts() {
            return this.unmodifiableMobCategoryCounts;
        }
        
        private boolean canSpawnForCategory(final MobCategory aql) {
            final int integer3 = aql.getMaxInstancesPerChunk() * this.spawnableChunkCount / NaturalSpawner.MAGIC_NUMBER;
            return this.mobCategoryCounts.getInt(aql) < integer3;
        }
    }
    
    @FunctionalInterface
    public interface ChunkGetter {
        void query(final long long1, final Consumer<LevelChunk> consumer);
    }
    
    @FunctionalInterface
    public interface AfterSpawnCallback {
        void run(final Mob aqk, final ChunkAccess cft);
    }
    
    @FunctionalInterface
    public interface SpawnPredicate {
        boolean test(final EntityType<?> aqb, final BlockPos fx, final ChunkAccess cft);
    }
}
