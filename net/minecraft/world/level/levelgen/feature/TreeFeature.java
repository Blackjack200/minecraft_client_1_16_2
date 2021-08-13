package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import java.util.Iterator;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.LevelAccessor;
import java.util.Comparator;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import java.util.List;
import java.util.OptionalInt;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import java.util.Set;
import java.util.Random;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelSimulatedReader;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

public class TreeFeature extends Feature<TreeConfiguration> {
    public TreeFeature(final Codec<TreeConfiguration> codec) {
        super(codec);
    }
    
    public static boolean isFree(final LevelSimulatedReader brz, final BlockPos fx) {
        return validTreePos(brz, fx) || brz.isStateAtPosition(fx, (Predicate<BlockState>)(cee -> cee.is(BlockTags.LOGS)));
    }
    
    private static boolean isVine(final LevelSimulatedReader brz, final BlockPos fx) {
        return brz.isStateAtPosition(fx, (Predicate<BlockState>)(cee -> cee.is(Blocks.VINE)));
    }
    
    private static boolean isBlockWater(final LevelSimulatedReader brz, final BlockPos fx) {
        return brz.isStateAtPosition(fx, (Predicate<BlockState>)(cee -> cee.is(Blocks.WATER)));
    }
    
    public static boolean isAirOrLeaves(final LevelSimulatedReader brz, final BlockPos fx) {
        return brz.isStateAtPosition(fx, (Predicate<BlockState>)(cee -> cee.isAir() || cee.is(BlockTags.LEAVES)));
    }
    
    private static boolean isGrassOrDirtOrFarmland(final LevelSimulatedReader brz, final BlockPos fx) {
        return brz.isStateAtPosition(fx, (Predicate<BlockState>)(cee -> {
            final Block bul2 = cee.getBlock();
            return Feature.isDirt(bul2) || bul2 == Blocks.FARMLAND;
        }));
    }
    
    private static boolean isReplaceablePlant(final LevelSimulatedReader brz, final BlockPos fx) {
        return brz.isStateAtPosition(fx, (Predicate<BlockState>)(cee -> {
            final Material cux2 = cee.getMaterial();
            return cux2 == Material.REPLACEABLE_PLANT;
        }));
    }
    
    public static void setBlockKnownShape(final LevelWriter bsb, final BlockPos fx, final BlockState cee) {
        bsb.setBlock(fx, cee, 19);
    }
    
    public static boolean validTreePos(final LevelSimulatedReader brz, final BlockPos fx) {
        return isAirOrLeaves(brz, fx) || isReplaceablePlant(brz, fx) || isBlockWater(brz, fx);
    }
    
    private boolean doPlace(final LevelSimulatedRW bry, final Random random, final BlockPos fx, final Set<BlockPos> set4, final Set<BlockPos> set5, final BoundingBox cqx, final TreeConfiguration cmw) {
        final int integer9 = cmw.trunkPlacer.getTreeHeight(random);
        final int integer10 = cmw.foliagePlacer.foliageHeight(random, integer9, cmw);
        final int integer11 = integer9 - integer10;
        final int integer12 = cmw.foliagePlacer.foliageRadius(random, integer11);
        BlockPos fx2;
        if (!cmw.fromSapling) {
            final int integer13 = bry.getHeightmapPos(Heightmap.Types.OCEAN_FLOOR, fx).getY();
            final int integer14 = bry.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, fx).getY();
            if (integer14 - integer13 > cmw.maxWaterDepth) {
                return false;
            }
            int integer15;
            if (cmw.heightmap == Heightmap.Types.OCEAN_FLOOR) {
                integer15 = integer13;
            }
            else if (cmw.heightmap == Heightmap.Types.WORLD_SURFACE) {
                integer15 = integer14;
            }
            else {
                integer15 = bry.getHeightmapPos(cmw.heightmap, fx).getY();
            }
            fx2 = new BlockPos(fx.getX(), integer15, fx.getZ());
        }
        else {
            fx2 = fx;
        }
        if (fx2.getY() < 1 || fx2.getY() + integer9 + 1 > 256) {
            return false;
        }
        if (!isGrassOrDirtOrFarmland(bry, fx2.below())) {
            return false;
        }
        final OptionalInt optionalInt14 = cmw.minimumSize.minClippedHeight();
        final int integer14 = this.getMaxFreeTreeHeight(bry, integer9, fx2, cmw);
        if (integer14 < integer9 && (!optionalInt14.isPresent() || integer14 < optionalInt14.getAsInt())) {
            return false;
        }
        final List<FoliagePlacer.FoliageAttachment> list16 = cmw.trunkPlacer.placeTrunk(bry, random, integer14, fx2, set4, cqx, cmw);
        list16.forEach(b -> cmw.foliagePlacer.createFoliage(bry, random, cmw, integer14, b, integer10, integer12, set5, cqx));
        return true;
    }
    
    private int getMaxFreeTreeHeight(final LevelSimulatedReader brz, final int integer, final BlockPos fx, final TreeConfiguration cmw) {
        final BlockPos.MutableBlockPos a6 = new BlockPos.MutableBlockPos();
        for (int integer2 = 0; integer2 <= integer + 1; ++integer2) {
            for (int integer3 = cmw.minimumSize.getSizeAtHeight(integer, integer2), integer4 = -integer3; integer4 <= integer3; ++integer4) {
                for (int integer5 = -integer3; integer5 <= integer3; ++integer5) {
                    a6.setWithOffset(fx, integer4, integer2, integer5);
                    if (!isFree(brz, a6) || (!cmw.ignoreVines && isVine(brz, a6))) {
                        return integer2 - 2;
                    }
                }
            }
        }
        return integer;
    }
    
    @Override
    protected void setBlock(final LevelWriter bsb, final BlockPos fx, final BlockState cee) {
        setBlockKnownShape(bsb, fx, cee);
    }
    
    @Override
    public final boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final TreeConfiguration cmw) {
        final Set<BlockPos> set7 = (Set<BlockPos>)Sets.newHashSet();
        final Set<BlockPos> set8 = (Set<BlockPos>)Sets.newHashSet();
        final Set<BlockPos> set9 = (Set<BlockPos>)Sets.newHashSet();
        final BoundingBox cqx10 = BoundingBox.getUnknownBox();
        final boolean boolean11 = this.doPlace(bso, random, fx, set7, set8, cqx10, cmw);
        if (cqx10.x0 > cqx10.x1 || !boolean11 || set7.isEmpty()) {
            return false;
        }
        if (!cmw.decorators.isEmpty()) {
            final List<BlockPos> list12 = (List<BlockPos>)Lists.newArrayList((Iterable)set7);
            final List<BlockPos> list13 = (List<BlockPos>)Lists.newArrayList((Iterable)set8);
            list12.sort(Comparator.comparingInt(Vec3i::getY));
            list13.sort(Comparator.comparingInt(Vec3i::getY));
            cmw.decorators.forEach(coo -> coo.place(bso, random, list12, list13, set9, cqx10));
        }
        final DiscreteVoxelShape dct12 = this.updateLeaves(bso, cqx10, set7, set9);
        StructureTemplate.updateShapeAtEdge(bso, 3, dct12, cqx10.x0, cqx10.y0, cqx10.z0);
        return true;
    }
    
    private DiscreteVoxelShape updateLeaves(final LevelAccessor brv, final BoundingBox cqx, final Set<BlockPos> set3, final Set<BlockPos> set4) {
        final List<Set<BlockPos>> list6 = (List<Set<BlockPos>>)Lists.newArrayList();
        final DiscreteVoxelShape dct7 = new BitSetDiscreteVoxelShape(cqx.getXSpan(), cqx.getYSpan(), cqx.getZSpan());
        final int integer8 = 6;
        for (int integer9 = 0; integer9 < 6; ++integer9) {
            list6.add(Sets.newHashSet());
        }
        final BlockPos.MutableBlockPos a9 = new BlockPos.MutableBlockPos();
        for (final BlockPos fx11 : Lists.newArrayList((Iterable)set4)) {
            if (cqx.isInside(fx11)) {
                dct7.setFull(fx11.getX() - cqx.x0, fx11.getY() - cqx.y0, fx11.getZ() - cqx.z0, true, true);
            }
        }
        for (final BlockPos fx11 : Lists.newArrayList((Iterable)set3)) {
            if (cqx.isInside(fx11)) {
                dct7.setFull(fx11.getX() - cqx.x0, fx11.getY() - cqx.y0, fx11.getZ() - cqx.z0, true, true);
            }
            for (final Direction gc15 : Direction.values()) {
                a9.setWithOffset(fx11, gc15);
                if (!set3.contains(a9)) {
                    final BlockState cee16 = brv.getBlockState(a9);
                    if (cee16.<Comparable>hasProperty((Property<Comparable>)BlockStateProperties.DISTANCE)) {
                        ((Set)list6.get(0)).add(a9.immutable());
                        setBlockKnownShape(brv, a9, ((StateHolder<O, BlockState>)cee16).<Comparable, Integer>setValue((Property<Comparable>)BlockStateProperties.DISTANCE, 1));
                        if (cqx.isInside(a9)) {
                            dct7.setFull(a9.getX() - cqx.x0, a9.getY() - cqx.y0, a9.getZ() - cqx.z0, true, true);
                        }
                    }
                }
            }
        }
        for (int integer10 = 1; integer10 < 6; ++integer10) {
            final Set<BlockPos> set5 = (Set<BlockPos>)list6.get(integer10 - 1);
            final Set<BlockPos> set6 = (Set<BlockPos>)list6.get(integer10);
            for (final BlockPos fx12 : set5) {
                if (cqx.isInside(fx12)) {
                    dct7.setFull(fx12.getX() - cqx.x0, fx12.getY() - cqx.y0, fx12.getZ() - cqx.z0, true, true);
                }
                for (final Direction gc16 : Direction.values()) {
                    a9.setWithOffset(fx12, gc16);
                    if (!set5.contains(a9)) {
                        if (!set6.contains(a9)) {
                            final BlockState cee17 = brv.getBlockState(a9);
                            if (cee17.<Comparable>hasProperty((Property<Comparable>)BlockStateProperties.DISTANCE)) {
                                final int integer11 = cee17.<Integer>getValue((Property<Integer>)BlockStateProperties.DISTANCE);
                                if (integer11 > integer10 + 1) {
                                    final BlockState cee18 = ((StateHolder<O, BlockState>)cee17).<Comparable, Integer>setValue((Property<Comparable>)BlockStateProperties.DISTANCE, integer10 + 1);
                                    setBlockKnownShape(brv, a9, cee18);
                                    if (cqx.isInside(a9)) {
                                        dct7.setFull(a9.getX() - cqx.x0, a9.getY() - cqx.y0, a9.getZ() - cqx.z0, true, true);
                                    }
                                    set6.add(a9.immutable());
                                }
                            }
                        }
                    }
                }
            }
        }
        return dct7;
    }
}
