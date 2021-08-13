package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.block.state.StateHolder;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.phys.AABB;
import com.google.common.cache.CacheLoader;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Blocks;
import java.util.Iterator;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import java.util.Random;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import java.util.List;
import com.google.common.cache.LoadingCache;
import net.minecraft.world.level.levelgen.feature.configurations.SpikeConfiguration;

public class SpikeFeature extends Feature<SpikeConfiguration> {
    private static final LoadingCache<Long, List<EndSpike>> SPIKE_CACHE;
    
    public SpikeFeature(final Codec<SpikeConfiguration> codec) {
        super(codec);
    }
    
    public static List<EndSpike> getSpikesForLevel(final WorldGenLevel bso) {
        final Random random2 = new Random(bso.getSeed());
        final long long3 = random2.nextLong() & 0xFFFFL;
        return (List<EndSpike>)SpikeFeature.SPIKE_CACHE.getUnchecked(long3);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final SpikeConfiguration cms) {
        List<EndSpike> list7 = cms.getSpikes();
        if (list7.isEmpty()) {
            list7 = getSpikesForLevel(bso);
        }
        for (final EndSpike a9 : list7) {
            if (a9.isCenterWithinChunk(fx)) {
                this.placeSpike(bso, random, cms, a9);
            }
        }
        return true;
    }
    
    private void placeSpike(final ServerLevelAccessor bsh, final Random random, final SpikeConfiguration cms, final EndSpike a) {
        final int integer6 = a.getRadius();
        for (final BlockPos fx8 : BlockPos.betweenClosed(new BlockPos(a.getCenterX() - integer6, 0, a.getCenterZ() - integer6), new BlockPos(a.getCenterX() + integer6, a.getHeight() + 10, a.getCenterZ() + integer6))) {
            if (fx8.distSqr(a.getCenterX(), fx8.getY(), a.getCenterZ(), false) <= integer6 * integer6 + 1 && fx8.getY() < a.getHeight()) {
                this.setBlock(bsh, fx8, Blocks.OBSIDIAN.defaultBlockState());
            }
            else {
                if (fx8.getY() <= 65) {
                    continue;
                }
                this.setBlock(bsh, fx8, Blocks.AIR.defaultBlockState());
            }
        }
        if (a.isGuarded()) {
            final int integer7 = -2;
            final int integer8 = 2;
            final int integer9 = 3;
            final BlockPos.MutableBlockPos a2 = new BlockPos.MutableBlockPos();
            for (int integer10 = -2; integer10 <= 2; ++integer10) {
                for (int integer11 = -2; integer11 <= 2; ++integer11) {
                    for (int integer12 = 0; integer12 <= 3; ++integer12) {
                        final boolean boolean14 = Mth.abs(integer10) == 2;
                        final boolean boolean15 = Mth.abs(integer11) == 2;
                        final boolean boolean16 = integer12 == 3;
                        if (boolean14 || boolean15 || boolean16) {
                            final boolean boolean17 = integer10 == -2 || integer10 == 2 || boolean16;
                            final boolean boolean18 = integer11 == -2 || integer11 == 2 || boolean16;
                            final BlockState cee19 = (((((StateHolder<O, BlockState>)Blocks.IRON_BARS.defaultBlockState()).setValue((Property<Comparable>)IronBarsBlock.NORTH, boolean17 && integer11 != -2)).setValue((Property<Comparable>)IronBarsBlock.SOUTH, boolean17 && integer11 != 2)).setValue((Property<Comparable>)IronBarsBlock.WEST, boolean18 && integer10 != -2)).<Comparable, Boolean>setValue((Property<Comparable>)IronBarsBlock.EAST, boolean18 && integer10 != 2);
                            this.setBlock(bsh, a2.set(a.getCenterX() + integer10, a.getHeight() + integer12, a.getCenterZ() + integer11), cee19);
                        }
                    }
                }
            }
        }
        final EndCrystal bbn7 = EntityType.END_CRYSTAL.create(bsh.getLevel());
        bbn7.setBeamTarget(cms.getCrystalBeamTarget());
        bbn7.setInvulnerable(cms.isCrystalInvulnerable());
        bbn7.moveTo(a.getCenterX() + 0.5, a.getHeight() + 1, a.getCenterZ() + 0.5, random.nextFloat() * 360.0f, 0.0f);
        bsh.addFreshEntity(bbn7);
        this.setBlock(bsh, new BlockPos(a.getCenterX(), a.getHeight(), a.getCenterZ()), Blocks.BEDROCK.defaultBlockState());
    }
    
    static {
        SPIKE_CACHE = CacheBuilder.newBuilder().expireAfterWrite(5L, TimeUnit.MINUTES).build((CacheLoader)new SpikeCacheLoader());
    }
    
    public static class EndSpike {
        public static final Codec<EndSpike> CODEC;
        private final int centerX;
        private final int centerZ;
        private final int radius;
        private final int height;
        private final boolean guarded;
        private final AABB topBoundingBox;
        
        public EndSpike(final int integer1, final int integer2, final int integer3, final int integer4, final boolean boolean5) {
            this.centerX = integer1;
            this.centerZ = integer2;
            this.radius = integer3;
            this.height = integer4;
            this.guarded = boolean5;
            this.topBoundingBox = new AABB(integer1 - integer3, 0.0, integer2 - integer3, integer1 + integer3, 256.0, integer2 + integer3);
        }
        
        public boolean isCenterWithinChunk(final BlockPos fx) {
            return fx.getX() >> 4 == this.centerX >> 4 && fx.getZ() >> 4 == this.centerZ >> 4;
        }
        
        public int getCenterX() {
            return this.centerX;
        }
        
        public int getCenterZ() {
            return this.centerZ;
        }
        
        public int getRadius() {
            return this.radius;
        }
        
        public int getHeight() {
            return this.height;
        }
        
        public boolean isGuarded() {
            return this.guarded;
        }
        
        public AABB getTopBoundingBox() {
            return this.topBoundingBox;
        }
        
        static {
            CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.INT.fieldOf("centerX").orElse(0).forGetter(a -> a.centerX), (App)Codec.INT.fieldOf("centerZ").orElse(0).forGetter(a -> a.centerZ), (App)Codec.INT.fieldOf("radius").orElse(0).forGetter(a -> a.radius), (App)Codec.INT.fieldOf("height").orElse(0).forGetter(a -> a.height), (App)Codec.BOOL.fieldOf("guarded").orElse(false).forGetter(a -> a.guarded)).apply((Applicative)instance, EndSpike::new));
        }
    }
    
    static class SpikeCacheLoader extends CacheLoader<Long, List<EndSpike>> {
        private SpikeCacheLoader() {
        }
        
        public List<EndSpike> load(final Long long1) {
            final List<Integer> list3 = (List<Integer>)IntStream.range(0, 10).boxed().collect(Collectors.toList());
            Collections.shuffle((List)list3, new Random((long)long1));
            final List<EndSpike> list4 = (List<EndSpike>)Lists.newArrayList();
            for (int integer5 = 0; integer5 < 10; ++integer5) {
                final int integer6 = Mth.floor(42.0 * Math.cos(2.0 * (-3.141592653589793 + 0.3141592653589793 * integer5)));
                final int integer7 = Mth.floor(42.0 * Math.sin(2.0 * (-3.141592653589793 + 0.3141592653589793 * integer5)));
                final int integer8 = (int)list3.get(integer5);
                final int integer9 = 2 + integer8 / 3;
                final int integer10 = 76 + integer8 * 3;
                final boolean boolean11 = integer8 == 1 || integer8 == 2;
                list4.add(new EndSpike(integer6, integer7, integer9, integer10, boolean11));
            }
            return list4;
        }
    }
}
