package net.minecraft.world.level.levelgen.feature.trunkplacers;

import net.minecraft.world.level.block.state.StateHolder;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Objects;
import java.util.Iterator;
import com.google.common.collect.Lists;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import java.util.List;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import java.util.Set;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.LevelSimulatedRW;
import com.mojang.serialization.Codec;

public class FancyTrunkPlacer extends TrunkPlacer {
    public static final Codec<FancyTrunkPlacer> CODEC;
    
    public FancyTrunkPlacer(final int integer1, final int integer2, final int integer3) {
        super(integer1, integer2, integer3);
    }
    
    @Override
    protected TrunkPlacerType<?> type() {
        return TrunkPlacerType.FANCY_TRUNK_PLACER;
    }
    
    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(final LevelSimulatedRW bry, final Random random, final int integer, final BlockPos fx, final Set<BlockPos> set, final BoundingBox cqx, final TreeConfiguration cmw) {
        final int integer2 = 5;
        final int integer3 = integer + 2;
        final int integer4 = Mth.floor(integer3 * 0.618);
        if (!cmw.fromSapling) {
            TrunkPlacer.setDirtAt(bry, fx.below());
        }
        final double double12 = 1.0;
        final int integer5 = Math.min(1, Mth.floor(1.382 + Math.pow(1.0 * integer3 / 13.0, 2.0)));
        final int integer6 = fx.getY() + integer4;
        int integer7 = integer3 - 5;
        final List<FoliageCoords> list17 = (List<FoliageCoords>)Lists.newArrayList();
        list17.add(new FoliageCoords(fx.above(integer7), integer6));
        while (integer7 >= 0) {
            final float float18 = this.treeShape(integer3, integer7);
            if (float18 >= 0.0f) {
                for (int integer8 = 0; integer8 < integer5; ++integer8) {
                    final double double13 = 1.0;
                    final double double14 = 1.0 * float18 * (random.nextFloat() + 0.328);
                    final double double15 = random.nextFloat() * 2.0f * 3.141592653589793;
                    final double double16 = double14 * Math.sin(double15) + 0.5;
                    final double double17 = double14 * Math.cos(double15) + 0.5;
                    final BlockPos fx2 = fx.offset(double16, integer7 - 1, double17);
                    final BlockPos fx3 = fx2.above(5);
                    if (this.makeLimb(bry, random, fx2, fx3, false, set, cqx, cmw)) {
                        final int integer9 = fx.getX() - fx2.getX();
                        final int integer10 = fx.getZ() - fx2.getZ();
                        final double double18 = fx2.getY() - Math.sqrt((double)(integer9 * integer9 + integer10 * integer10)) * 0.381;
                        final int integer11 = (double18 > integer6) ? integer6 : ((int)double18);
                        final BlockPos fx4 = new BlockPos(fx.getX(), integer11, fx.getZ());
                        if (this.makeLimb(bry, random, fx4, fx2, false, set, cqx, cmw)) {
                            list17.add(new FoliageCoords(fx2, fx4.getY()));
                        }
                    }
                }
            }
            --integer7;
        }
        this.makeLimb(bry, random, fx, fx.above(integer4), true, set, cqx, cmw);
        this.makeBranches(bry, random, integer3, fx, list17, set, cqx, cmw);
        final List<FoliagePlacer.FoliageAttachment> list18 = (List<FoliagePlacer.FoliageAttachment>)Lists.newArrayList();
        for (final FoliageCoords a20 : list17) {
            if (this.trimBranches(integer3, a20.getBranchBase() - fx.getY())) {
                list18.add(a20.attachment);
            }
        }
        return list18;
    }
    
    private boolean makeLimb(final LevelSimulatedRW bry, final Random random, final BlockPos fx3, final BlockPos fx4, final boolean boolean5, final Set<BlockPos> set, final BoundingBox cqx, final TreeConfiguration cmw) {
        if (!boolean5 && Objects.equals(fx3, fx4)) {
            return true;
        }
        final BlockPos fx5 = fx4.offset(-fx3.getX(), -fx3.getY(), -fx3.getZ());
        final int integer11 = this.getSteps(fx5);
        final float float12 = fx5.getX() / (float)integer11;
        final float float13 = fx5.getY() / (float)integer11;
        final float float14 = fx5.getZ() / (float)integer11;
        for (int integer12 = 0; integer12 <= integer11; ++integer12) {
            final BlockPos fx6 = fx3.offset(0.5f + integer12 * float12, 0.5f + integer12 * float13, 0.5f + integer12 * float14);
            if (boolean5) {
                TrunkPlacer.setBlock(bry, fx6, ((StateHolder<O, BlockState>)cmw.trunkProvider.getState(random, fx6)).<Direction.Axis, Direction.Axis>setValue(RotatedPillarBlock.AXIS, this.getLogAxis(fx3, fx6)), cqx);
                set.add(fx6.immutable());
            }
            else if (!TreeFeature.isFree(bry, fx6)) {
                return false;
            }
        }
        return true;
    }
    
    private int getSteps(final BlockPos fx) {
        final int integer3 = Mth.abs(fx.getX());
        final int integer4 = Mth.abs(fx.getY());
        final int integer5 = Mth.abs(fx.getZ());
        return Math.max(integer3, Math.max(integer4, integer5));
    }
    
    private Direction.Axis getLogAxis(final BlockPos fx1, final BlockPos fx2) {
        Direction.Axis a4 = Direction.Axis.Y;
        final int integer5 = Math.abs(fx2.getX() - fx1.getX());
        final int integer6 = Math.abs(fx2.getZ() - fx1.getZ());
        final int integer7 = Math.max(integer5, integer6);
        if (integer7 > 0) {
            if (integer5 == integer7) {
                a4 = Direction.Axis.X;
            }
            else {
                a4 = Direction.Axis.Z;
            }
        }
        return a4;
    }
    
    private boolean trimBranches(final int integer1, final int integer2) {
        return integer2 >= integer1 * 0.2;
    }
    
    private void makeBranches(final LevelSimulatedRW bry, final Random random, final int integer, final BlockPos fx, final List<FoliageCoords> list, final Set<BlockPos> set, final BoundingBox cqx, final TreeConfiguration cmw) {
        for (final FoliageCoords a11 : list) {
            final int integer2 = a11.getBranchBase();
            final BlockPos fx2 = new BlockPos(fx.getX(), integer2, fx.getZ());
            if (!fx2.equals(a11.attachment.foliagePos()) && this.trimBranches(integer, integer2 - fx.getY())) {
                this.makeLimb(bry, random, fx2, a11.attachment.foliagePos(), true, set, cqx, cmw);
            }
        }
    }
    
    private float treeShape(final int integer1, final int integer2) {
        if (integer2 < integer1 * 0.3f) {
            return -1.0f;
        }
        final float float4 = integer1 / 2.0f;
        final float float5 = float4 - integer2;
        float float6 = Mth.sqrt(float4 * float4 - float5 * float5);
        if (float5 == 0.0f) {
            float6 = float4;
        }
        else if (Math.abs(float5) >= float4) {
            return 0.0f;
        }
        return float6 * 0.5f;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> TrunkPlacer.<TrunkPlacer>trunkPlacerParts((RecordCodecBuilder.Instance<TrunkPlacer>)instance).apply((Applicative)instance, FancyTrunkPlacer::new));
    }
    
    static class FoliageCoords {
        private final FoliagePlacer.FoliageAttachment attachment;
        private final int branchBase;
        
        public FoliageCoords(final BlockPos fx, final int integer) {
            this.attachment = new FoliagePlacer.FoliageAttachment(fx, 0, false);
            this.branchBase = integer;
        }
        
        public int getBranchBase() {
            return this.branchBase;
        }
    }
}
