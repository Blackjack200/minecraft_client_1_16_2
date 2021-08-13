package net.minecraft.world.level.material;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.DoorBlock;
import it.unimi.dsi.fastutil.shorts.Short2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2BooleanMap;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Iterator;
import net.minecraft.core.Vec3i;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.StateDefinition;
import com.google.common.collect.Maps;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.Map;
import net.minecraft.world.level.block.Block;
import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public abstract class FlowingFluid extends Fluid {
    public static final BooleanProperty FALLING;
    public static final IntegerProperty LEVEL;
    private static final ThreadLocal<Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey>> OCCLUSION_CACHE;
    private final Map<FluidState, VoxelShape> shapes;
    
    public FlowingFluid() {
        this.shapes = (Map<FluidState, VoxelShape>)Maps.newIdentityHashMap();
    }
    
    @Override
    protected void createFluidStateDefinition(final StateDefinition.Builder<Fluid, FluidState> a) {
        a.add(FlowingFluid.FALLING);
    }
    
    public Vec3 getFlow(final BlockGetter bqz, final BlockPos fx, final FluidState cuu) {
        double double5 = 0.0;
        double double6 = 0.0;
        final BlockPos.MutableBlockPos a9 = new BlockPos.MutableBlockPos();
        for (final Direction gc11 : Direction.Plane.HORIZONTAL) {
            a9.setWithOffset(fx, gc11);
            final FluidState cuu2 = bqz.getFluidState(a9);
            if (!this.affectsFlow(cuu2)) {
                continue;
            }
            float float13 = cuu2.getOwnHeight();
            float float14 = 0.0f;
            if (float13 == 0.0f) {
                if (!bqz.getBlockState(a9).getMaterial().blocksMotion()) {
                    final BlockPos fx2 = a9.below();
                    final FluidState cuu3 = bqz.getFluidState(fx2);
                    if (this.affectsFlow(cuu3)) {
                        float13 = cuu3.getOwnHeight();
                        if (float13 > 0.0f) {
                            float14 = cuu.getOwnHeight() - (float13 - 0.8888889f);
                        }
                    }
                }
            }
            else if (float13 > 0.0f) {
                float14 = cuu.getOwnHeight() - float13;
            }
            if (float14 == 0.0f) {
                continue;
            }
            double5 += gc11.getStepX() * float14;
            double6 += gc11.getStepZ() * float14;
        }
        Vec3 dck10 = new Vec3(double5, 0.0, double6);
        if (cuu.<Boolean>getValue((Property<Boolean>)FlowingFluid.FALLING)) {
            for (final Direction gc12 : Direction.Plane.HORIZONTAL) {
                a9.setWithOffset(fx, gc12);
                if (this.isSolidFace(bqz, a9, gc12) || this.isSolidFace(bqz, a9.above(), gc12)) {
                    dck10 = dck10.normalize().add(0.0, -6.0, 0.0);
                    break;
                }
            }
        }
        return dck10.normalize();
    }
    
    private boolean affectsFlow(final FluidState cuu) {
        return cuu.isEmpty() || cuu.getType().isSame(this);
    }
    
    protected boolean isSolidFace(final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        final BlockState cee5 = bqz.getBlockState(fx);
        final FluidState cuu6 = bqz.getFluidState(fx);
        return !cuu6.getType().isSame(this) && (gc == Direction.UP || (cee5.getMaterial() != Material.ICE && cee5.isFaceSturdy(bqz, fx, gc)));
    }
    
    protected void spread(final LevelAccessor brv, final BlockPos fx, final FluidState cuu) {
        if (cuu.isEmpty()) {
            return;
        }
        final BlockState cee5 = brv.getBlockState(fx);
        final BlockPos fx2 = fx.below();
        final BlockState cee6 = brv.getBlockState(fx2);
        final FluidState cuu2 = this.getNewLiquid(brv, fx2, cee6);
        if (this.canSpreadTo(brv, fx, cee5, Direction.DOWN, fx2, cee6, brv.getFluidState(fx2), cuu2.getType())) {
            this.spreadTo(brv, fx2, cee6, Direction.DOWN, cuu2);
            if (this.sourceNeighborCount(brv, fx) >= 3) {
                this.spreadToSides(brv, fx, cuu, cee5);
            }
        }
        else if (cuu.isSource() || !this.isWaterHole(brv, cuu2.getType(), fx, cee5, fx2, cee6)) {
            this.spreadToSides(brv, fx, cuu, cee5);
        }
    }
    
    private void spreadToSides(final LevelAccessor brv, final BlockPos fx, final FluidState cuu, final BlockState cee) {
        int integer6 = cuu.getAmount() - this.getDropOff(brv);
        if (cuu.<Boolean>getValue((Property<Boolean>)FlowingFluid.FALLING)) {
            integer6 = 7;
        }
        if (integer6 <= 0) {
            return;
        }
        final Map<Direction, FluidState> map7 = this.getSpread(brv, fx, cee);
        for (final Map.Entry<Direction, FluidState> entry9 : map7.entrySet()) {
            final Direction gc10 = (Direction)entry9.getKey();
            final FluidState cuu2 = (FluidState)entry9.getValue();
            final BlockPos fx2 = fx.relative(gc10);
            final BlockState cee2 = brv.getBlockState(fx2);
            if (this.canSpreadTo(brv, fx, cee, gc10, fx2, cee2, brv.getFluidState(fx2), cuu2.getType())) {
                this.spreadTo(brv, fx2, cee2, gc10, cuu2);
            }
        }
    }
    
    protected FluidState getNewLiquid(final LevelReader brw, final BlockPos fx, final BlockState cee) {
        int integer5 = 0;
        int integer6 = 0;
        for (final Direction gc8 : Direction.Plane.HORIZONTAL) {
            final BlockPos fx2 = fx.relative(gc8);
            final BlockState cee2 = brw.getBlockState(fx2);
            final FluidState cuu11 = cee2.getFluidState();
            if (cuu11.getType().isSame(this) && this.canPassThroughWall(gc8, brw, fx, cee, fx2, cee2)) {
                if (cuu11.isSource()) {
                    ++integer6;
                }
                integer5 = Math.max(integer5, cuu11.getAmount());
            }
        }
        if (this.canConvertToSource() && integer6 >= 2) {
            final BlockState cee3 = brw.getBlockState(fx.below());
            final FluidState cuu12 = cee3.getFluidState();
            if (cee3.getMaterial().isSolid() || this.isSourceBlockOfThisType(cuu12)) {
                return this.getSource(false);
            }
        }
        final BlockPos fx3 = fx.above();
        final BlockState cee4 = brw.getBlockState(fx3);
        final FluidState cuu13 = cee4.getFluidState();
        if (!cuu13.isEmpty() && cuu13.getType().isSame(this) && this.canPassThroughWall(Direction.UP, brw, fx, cee, fx3, cee4)) {
            return this.getFlowing(8, true);
        }
        final int integer7 = integer5 - this.getDropOff(brw);
        if (integer7 <= 0) {
            return Fluids.EMPTY.defaultFluidState();
        }
        return this.getFlowing(integer7, false);
    }
    
    private boolean canPassThroughWall(final Direction gc, final BlockGetter bqz, final BlockPos fx3, final BlockState cee4, final BlockPos fx5, final BlockState cee6) {
        Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey> object2ByteLinkedOpenHashMap8;
        if (cee4.getBlock().hasDynamicShape() || cee6.getBlock().hasDynamicShape()) {
            object2ByteLinkedOpenHashMap8 = null;
        }
        else {
            object2ByteLinkedOpenHashMap8 = (Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey>)FlowingFluid.OCCLUSION_CACHE.get();
        }
        Block.BlockStatePairKey a9;
        if (object2ByteLinkedOpenHashMap8 != null) {
            a9 = new Block.BlockStatePairKey(cee4, cee6, gc);
            final byte byte10 = object2ByteLinkedOpenHashMap8.getAndMoveToFirst(a9);
            if (byte10 != 127) {
                return byte10 != 0;
            }
        }
        else {
            a9 = null;
        }
        final VoxelShape dde10 = cee4.getCollisionShape(bqz, fx3);
        final VoxelShape dde11 = cee6.getCollisionShape(bqz, fx5);
        final boolean boolean12 = !Shapes.mergedFaceOccludes(dde10, dde11, gc);
        if (object2ByteLinkedOpenHashMap8 != null) {
            if (object2ByteLinkedOpenHashMap8.size() == 200) {
                object2ByteLinkedOpenHashMap8.removeLastByte();
            }
            object2ByteLinkedOpenHashMap8.putAndMoveToFirst(a9, (byte)(byte)(boolean12 ? 1 : 0));
        }
        return boolean12;
    }
    
    public abstract Fluid getFlowing();
    
    public FluidState getFlowing(final int integer, final boolean boolean2) {
        return (((StateHolder<O, FluidState>)this.getFlowing().defaultFluidState()).setValue((Property<Comparable>)FlowingFluid.LEVEL, integer)).<Comparable, Boolean>setValue((Property<Comparable>)FlowingFluid.FALLING, boolean2);
    }
    
    public abstract Fluid getSource();
    
    public FluidState getSource(final boolean boolean1) {
        return ((StateHolder<O, FluidState>)this.getSource().defaultFluidState()).<Comparable, Boolean>setValue((Property<Comparable>)FlowingFluid.FALLING, boolean1);
    }
    
    protected abstract boolean canConvertToSource();
    
    protected void spreadTo(final LevelAccessor brv, final BlockPos fx, final BlockState cee, final Direction gc, final FluidState cuu) {
        if (cee.getBlock() instanceof LiquidBlockContainer) {
            ((LiquidBlockContainer)cee.getBlock()).placeLiquid(brv, fx, cee, cuu);
        }
        else {
            if (!cee.isAir()) {
                this.beforeDestroyingBlock(brv, fx, cee);
            }
            brv.setBlock(fx, cuu.createLegacyBlock(), 3);
        }
    }
    
    protected abstract void beforeDestroyingBlock(final LevelAccessor brv, final BlockPos fx, final BlockState cee);
    
    private static short getCacheKey(final BlockPos fx1, final BlockPos fx2) {
        final int integer3 = fx2.getX() - fx1.getX();
        final int integer4 = fx2.getZ() - fx1.getZ();
        return (short)((integer3 + 128 & 0xFF) << 8 | (integer4 + 128 & 0xFF));
    }
    
    protected int getSlopeDistance(final LevelReader brw, final BlockPos fx2, final int integer, final Direction gc, final BlockState cee, final BlockPos fx6, final Short2ObjectMap<Pair<BlockState, FluidState>> short2ObjectMap, final Short2BooleanMap short2BooleanMap) {
        int integer2 = 1000;
        for (final Direction gc2 : Direction.Plane.HORIZONTAL) {
            if (gc2 == gc) {
                continue;
            }
            final BlockPos fx7 = fx2.relative(gc2);
            final short short14 = getCacheKey(fx6, fx7);
            final Pair<BlockState, FluidState> pair15 = (Pair<BlockState, FluidState>)short2ObjectMap.computeIfAbsent(short14, integer -> {
                final BlockState cee4 = brw.getBlockState(fx7);
                return Pair.of(cee4, cee4.getFluidState());
            });
            final BlockState cee2 = (BlockState)pair15.getFirst();
            final FluidState cuu17 = (FluidState)pair15.getSecond();
            if (!this.canPassThrough(brw, this.getFlowing(), fx2, cee, gc2, fx7, cee2, cuu17)) {
                continue;
            }
            final boolean boolean18 = short2BooleanMap.computeIfAbsent(short14, integer -> {
                final BlockPos fx2 = fx7.below();
                final BlockState cee2 = brw.getBlockState(fx2);
                return this.isWaterHole(brw, this.getFlowing(), fx7, cee2, fx2, cee2);
            });
            if (boolean18) {
                return integer;
            }
            if (integer >= this.getSlopeFindDistance(brw)) {
                continue;
            }
            final int integer3 = this.getSlopeDistance(brw, fx7, integer + 1, gc2.getOpposite(), cee2, fx6, short2ObjectMap, short2BooleanMap);
            if (integer3 >= integer2) {
                continue;
            }
            integer2 = integer3;
        }
        return integer2;
    }
    
    private boolean isWaterHole(final BlockGetter bqz, final Fluid cut, final BlockPos fx3, final BlockState cee4, final BlockPos fx5, final BlockState cee6) {
        return this.canPassThroughWall(Direction.DOWN, bqz, fx3, cee4, fx5, cee6) && (cee6.getFluidState().getType().isSame(this) || this.canHoldFluid(bqz, fx5, cee6, cut));
    }
    
    private boolean canPassThrough(final BlockGetter bqz, final Fluid cut, final BlockPos fx3, final BlockState cee4, final Direction gc, final BlockPos fx6, final BlockState cee7, final FluidState cuu) {
        return !this.isSourceBlockOfThisType(cuu) && this.canPassThroughWall(gc, bqz, fx3, cee4, fx6, cee7) && this.canHoldFluid(bqz, fx6, cee7, cut);
    }
    
    private boolean isSourceBlockOfThisType(final FluidState cuu) {
        return cuu.getType().isSame(this) && cuu.isSource();
    }
    
    protected abstract int getSlopeFindDistance(final LevelReader brw);
    
    private int sourceNeighborCount(final LevelReader brw, final BlockPos fx) {
        int integer4 = 0;
        for (final Direction gc6 : Direction.Plane.HORIZONTAL) {
            final BlockPos fx2 = fx.relative(gc6);
            final FluidState cuu8 = brw.getFluidState(fx2);
            if (this.isSourceBlockOfThisType(cuu8)) {
                ++integer4;
            }
        }
        return integer4;
    }
    
    protected Map<Direction, FluidState> getSpread(final LevelReader brw, final BlockPos fx, final BlockState cee) {
        int integer5 = 1000;
        final Map<Direction, FluidState> map6 = (Map<Direction, FluidState>)Maps.newEnumMap((Class)Direction.class);
        final Short2ObjectMap<Pair<BlockState, FluidState>> short2ObjectMap7 = (Short2ObjectMap<Pair<BlockState, FluidState>>)new Short2ObjectOpenHashMap();
        final Short2BooleanMap short2BooleanMap8 = (Short2BooleanMap)new Short2BooleanOpenHashMap();
        for (final Direction gc10 : Direction.Plane.HORIZONTAL) {
            final BlockPos fx2 = fx.relative(gc10);
            final short short12 = getCacheKey(fx, fx2);
            final Pair<BlockState, FluidState> pair13 = (Pair<BlockState, FluidState>)short2ObjectMap7.computeIfAbsent(short12, integer -> {
                final BlockState cee4 = brw.getBlockState(fx2);
                return Pair.of(cee4, cee4.getFluidState());
            });
            final BlockState cee2 = (BlockState)pair13.getFirst();
            final FluidState cuu15 = (FluidState)pair13.getSecond();
            final FluidState cuu16 = this.getNewLiquid(brw, fx2, cee2);
            if (this.canPassThrough(brw, cuu16.getType(), fx, cee, gc10, fx2, cee2, cuu15)) {
                final BlockPos fx3 = fx2.below();
                final boolean boolean19 = short2BooleanMap8.computeIfAbsent(short12, integer -> {
                    final BlockState cee2 = brw.getBlockState(fx3);
                    return this.isWaterHole(brw, this.getFlowing(), fx2, cee2, fx3, cee2);
                });
                int integer6;
                if (boolean19) {
                    integer6 = 0;
                }
                else {
                    integer6 = this.getSlopeDistance(brw, fx2, 1, gc10.getOpposite(), cee2, fx, short2ObjectMap7, short2BooleanMap8);
                }
                if (integer6 < integer5) {
                    map6.clear();
                }
                if (integer6 > integer5) {
                    continue;
                }
                map6.put(gc10, cuu16);
                integer5 = integer6;
            }
        }
        return map6;
    }
    
    private boolean canHoldFluid(final BlockGetter bqz, final BlockPos fx, final BlockState cee, final Fluid cut) {
        final Block bul6 = cee.getBlock();
        if (bul6 instanceof LiquidBlockContainer) {
            return ((LiquidBlockContainer)bul6).canPlaceLiquid(bqz, fx, cee, cut);
        }
        if (bul6 instanceof DoorBlock || bul6.is(BlockTags.SIGNS) || bul6 == Blocks.LADDER || bul6 == Blocks.SUGAR_CANE || bul6 == Blocks.BUBBLE_COLUMN) {
            return false;
        }
        final Material cux7 = cee.getMaterial();
        return cux7 != Material.PORTAL && cux7 != Material.STRUCTURAL_AIR && cux7 != Material.WATER_PLANT && cux7 != Material.REPLACEABLE_WATER_PLANT && !cux7.blocksMotion();
    }
    
    protected boolean canSpreadTo(final BlockGetter bqz, final BlockPos fx2, final BlockState cee3, final Direction gc, final BlockPos fx5, final BlockState cee6, final FluidState cuu, final Fluid cut) {
        return cuu.canBeReplacedWith(bqz, fx5, cut, gc) && this.canPassThroughWall(gc, bqz, fx2, cee3, fx5, cee6) && this.canHoldFluid(bqz, fx5, cee6, cut);
    }
    
    protected abstract int getDropOff(final LevelReader brw);
    
    protected int getSpreadDelay(final Level bru, final BlockPos fx, final FluidState cuu3, final FluidState cuu4) {
        return this.getTickDelay(bru);
    }
    
    public void tick(final Level bru, final BlockPos fx, FluidState cuu) {
        if (!cuu.isSource()) {
            final FluidState cuu2 = this.getNewLiquid(bru, fx, bru.getBlockState(fx));
            final int integer6 = this.getSpreadDelay(bru, fx, cuu, cuu2);
            if (cuu2.isEmpty()) {
                cuu = cuu2;
                bru.setBlock(fx, Blocks.AIR.defaultBlockState(), 3);
            }
            else if (!cuu2.equals(cuu)) {
                cuu = cuu2;
                final BlockState cee7 = cuu.createLegacyBlock();
                bru.setBlock(fx, cee7, 2);
                bru.getLiquidTicks().scheduleTick(fx, cuu.getType(), integer6);
                bru.updateNeighborsAt(fx, cee7.getBlock());
            }
        }
        this.spread(bru, fx, cuu);
    }
    
    protected static int getLegacyLevel(final FluidState cuu) {
        if (cuu.isSource()) {
            return 0;
        }
        return 8 - Math.min(cuu.getAmount(), 8) + (cuu.<Boolean>getValue((Property<Boolean>)FlowingFluid.FALLING) ? 8 : 0);
    }
    
    private static boolean hasSameAbove(final FluidState cuu, final BlockGetter bqz, final BlockPos fx) {
        return cuu.getType().isSame(bqz.getFluidState(fx.above()).getType());
    }
    
    @Override
    public float getHeight(final FluidState cuu, final BlockGetter bqz, final BlockPos fx) {
        if (hasSameAbove(cuu, bqz, fx)) {
            return 1.0f;
        }
        return cuu.getOwnHeight();
    }
    
    @Override
    public float getOwnHeight(final FluidState cuu) {
        return cuu.getAmount() / 9.0f;
    }
    
    @Override
    public VoxelShape getShape(final FluidState cuu, final BlockGetter bqz, final BlockPos fx) {
        if (cuu.getAmount() == 9 && hasSameAbove(cuu, bqz, fx)) {
            return Shapes.block();
        }
        return (VoxelShape)this.shapes.computeIfAbsent(cuu, cuu -> Shapes.box(0.0, 0.0, 0.0, 1.0, cuu.getHeight(bqz, fx), 1.0));
    }
    
    static {
        FALLING = BlockStateProperties.FALLING;
        LEVEL = BlockStateProperties.LEVEL_FLOWING;
        OCCLUSION_CACHE = ThreadLocal.withInitial(() -> {
            final Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey> object2ByteLinkedOpenHashMap1 = new Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey>(200) {
                protected void rehash(final int integer) {
                }
            };
            object2ByteLinkedOpenHashMap1.defaultReturnValue((byte)127);
            return object2ByteLinkedOpenHashMap1;
        });
    }
}
