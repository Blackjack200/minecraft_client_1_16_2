package net.minecraft.world.level.pathfinder;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.BaseRailBlock;
import java.util.Iterator;
import java.util.EnumSet;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.Vec3;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.PathNavigationRegion;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.world.phys.AABB;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;

public class WalkNodeEvaluator extends NodeEvaluator {
    protected float oldWaterCost;
    private final Long2ObjectMap<BlockPathTypes> pathTypesByPosCache;
    private final Object2BooleanMap<AABB> collisionCache;
    
    public WalkNodeEvaluator() {
        this.pathTypesByPosCache = (Long2ObjectMap<BlockPathTypes>)new Long2ObjectOpenHashMap();
        this.collisionCache = (Object2BooleanMap<AABB>)new Object2BooleanOpenHashMap();
    }
    
    @Override
    public void prepare(final PathNavigationRegion bsf, final Mob aqk) {
        super.prepare(bsf, aqk);
        this.oldWaterCost = aqk.getPathfindingMalus(BlockPathTypes.WATER);
    }
    
    @Override
    public void done() {
        this.mob.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
        this.pathTypesByPosCache.clear();
        this.collisionCache.clear();
        super.done();
    }
    
    @Override
    public Node getStart() {
        final BlockPos.MutableBlockPos a3 = new BlockPos.MutableBlockPos();
        int integer2 = Mth.floor(this.mob.getY());
        BlockState cee4 = this.level.getBlockState(a3.set(this.mob.getX(), integer2, this.mob.getZ()));
        if (this.mob.canStandOnFluid(cee4.getFluidState().getType())) {
            while (this.mob.canStandOnFluid(cee4.getFluidState().getType())) {
                ++integer2;
                cee4 = this.level.getBlockState(a3.set(this.mob.getX(), integer2, this.mob.getZ()));
            }
            --integer2;
        }
        else if (this.canFloat() && this.mob.isInWater()) {
            while (cee4.getBlock() == Blocks.WATER || cee4.getFluidState() == Fluids.WATER.getSource(false)) {
                ++integer2;
                cee4 = this.level.getBlockState(a3.set(this.mob.getX(), integer2, this.mob.getZ()));
            }
            --integer2;
        }
        else if (this.mob.isOnGround()) {
            integer2 = Mth.floor(this.mob.getY() + 0.5);
        }
        else {
            BlockPos fx5;
            for (fx5 = this.mob.blockPosition(); (this.level.getBlockState(fx5).isAir() || this.level.getBlockState(fx5).isPathfindable(this.level, fx5, PathComputationType.LAND)) && fx5.getY() > 0; fx5 = fx5.below()) {}
            integer2 = fx5.above().getY();
        }
        BlockPos fx5 = this.mob.blockPosition();
        final BlockPathTypes cww6 = this.getCachedBlockType(this.mob, fx5.getX(), integer2, fx5.getZ());
        if (this.mob.getPathfindingMalus(cww6) < 0.0f) {
            final AABB dcf7 = this.mob.getBoundingBox();
            if (this.hasPositiveMalus(a3.set(dcf7.minX, integer2, dcf7.minZ)) || this.hasPositiveMalus(a3.set(dcf7.minX, integer2, dcf7.maxZ)) || this.hasPositiveMalus(a3.set(dcf7.maxX, integer2, dcf7.minZ)) || this.hasPositiveMalus(a3.set(dcf7.maxX, integer2, dcf7.maxZ))) {
                final Node cwy8 = this.getNode(a3);
                cwy8.type = this.getBlockPathType(this.mob, cwy8.asBlockPos());
                cwy8.costMalus = this.mob.getPathfindingMalus(cwy8.type);
                return cwy8;
            }
        }
        final Node cwy9 = this.getNode(fx5.getX(), integer2, fx5.getZ());
        cwy9.type = this.getBlockPathType(this.mob, cwy9.asBlockPos());
        cwy9.costMalus = this.mob.getPathfindingMalus(cwy9.type);
        return cwy9;
    }
    
    private boolean hasPositiveMalus(final BlockPos fx) {
        final BlockPathTypes cww3 = this.getBlockPathType(this.mob, fx);
        return this.mob.getPathfindingMalus(cww3) >= 0.0f;
    }
    
    @Override
    public Target getGoal(final double double1, final double double2, final double double3) {
        return new Target(this.getNode(Mth.floor(double1), Mth.floor(double2), Mth.floor(double3)));
    }
    
    @Override
    public int getNeighbors(final Node[] arr, final Node cwy) {
        int integer4 = 0;
        int integer5 = 0;
        final BlockPathTypes cww6 = this.getCachedBlockType(this.mob, cwy.x, cwy.y + 1, cwy.z);
        final BlockPathTypes cww7 = this.getCachedBlockType(this.mob, cwy.x, cwy.y, cwy.z);
        if (this.mob.getPathfindingMalus(cww6) >= 0.0f && cww7 != BlockPathTypes.STICKY_HONEY) {
            integer5 = Mth.floor(Math.max(1.0f, this.mob.maxUpStep));
        }
        final double double8 = getFloorLevel(this.level, new BlockPos(cwy.x, cwy.y, cwy.z));
        final Node cwy2 = this.getLandNode(cwy.x, cwy.y, cwy.z + 1, integer5, double8, Direction.SOUTH, cww7);
        if (this.isNeighborValid(cwy2, cwy)) {
            arr[integer4++] = cwy2;
        }
        final Node cwy3 = this.getLandNode(cwy.x - 1, cwy.y, cwy.z, integer5, double8, Direction.WEST, cww7);
        if (this.isNeighborValid(cwy3, cwy)) {
            arr[integer4++] = cwy3;
        }
        final Node cwy4 = this.getLandNode(cwy.x + 1, cwy.y, cwy.z, integer5, double8, Direction.EAST, cww7);
        if (this.isNeighborValid(cwy4, cwy)) {
            arr[integer4++] = cwy4;
        }
        final Node cwy5 = this.getLandNode(cwy.x, cwy.y, cwy.z - 1, integer5, double8, Direction.NORTH, cww7);
        if (this.isNeighborValid(cwy5, cwy)) {
            arr[integer4++] = cwy5;
        }
        final Node cwy6 = this.getLandNode(cwy.x - 1, cwy.y, cwy.z - 1, integer5, double8, Direction.NORTH, cww7);
        if (this.isDiagonalValid(cwy, cwy3, cwy5, cwy6)) {
            arr[integer4++] = cwy6;
        }
        final Node cwy7 = this.getLandNode(cwy.x + 1, cwy.y, cwy.z - 1, integer5, double8, Direction.NORTH, cww7);
        if (this.isDiagonalValid(cwy, cwy4, cwy5, cwy7)) {
            arr[integer4++] = cwy7;
        }
        final Node cwy8 = this.getLandNode(cwy.x - 1, cwy.y, cwy.z + 1, integer5, double8, Direction.SOUTH, cww7);
        if (this.isDiagonalValid(cwy, cwy3, cwy2, cwy8)) {
            arr[integer4++] = cwy8;
        }
        final Node cwy9 = this.getLandNode(cwy.x + 1, cwy.y, cwy.z + 1, integer5, double8, Direction.SOUTH, cww7);
        if (this.isDiagonalValid(cwy, cwy4, cwy2, cwy9)) {
            arr[integer4++] = cwy9;
        }
        return integer4;
    }
    
    private boolean isNeighborValid(final Node cwy1, final Node cwy2) {
        return cwy1 != null && !cwy1.closed && (cwy1.costMalus >= 0.0f || cwy2.costMalus < 0.0f);
    }
    
    private boolean isDiagonalValid(final Node cwy1, @Nullable final Node cwy2, @Nullable final Node cwy3, @Nullable final Node cwy4) {
        if (cwy4 == null || cwy3 == null || cwy2 == null) {
            return false;
        }
        if (cwy4.closed) {
            return false;
        }
        if (cwy3.y > cwy1.y || cwy2.y > cwy1.y) {
            return false;
        }
        if (cwy2.type == BlockPathTypes.WALKABLE_DOOR || cwy3.type == BlockPathTypes.WALKABLE_DOOR || cwy4.type == BlockPathTypes.WALKABLE_DOOR) {
            return false;
        }
        final boolean boolean6 = cwy3.type == BlockPathTypes.FENCE && cwy2.type == BlockPathTypes.FENCE && this.mob.getBbWidth() < 0.5;
        return cwy4.costMalus >= 0.0f && (cwy3.y < cwy1.y || cwy3.costMalus >= 0.0f || boolean6) && (cwy2.y < cwy1.y || cwy2.costMalus >= 0.0f || boolean6);
    }
    
    private boolean canReachWithoutCollision(final Node cwy) {
        Vec3 dck3 = new Vec3(cwy.x - this.mob.getX(), cwy.y - this.mob.getY(), cwy.z - this.mob.getZ());
        AABB dcf4 = this.mob.getBoundingBox();
        final int integer5 = Mth.ceil(dck3.length() / dcf4.getSize());
        dck3 = dck3.scale(1.0f / integer5);
        for (int integer6 = 1; integer6 <= integer5; ++integer6) {
            dcf4 = dcf4.move(dck3);
            if (this.hasCollisions(dcf4)) {
                return false;
            }
        }
        return true;
    }
    
    public static double getFloorLevel(final BlockGetter bqz, final BlockPos fx) {
        final BlockPos fx2 = fx.below();
        final VoxelShape dde4 = bqz.getBlockState(fx2).getCollisionShape(bqz, fx2);
        return fx2.getY() + (dde4.isEmpty() ? 0.0 : dde4.max(Direction.Axis.Y));
    }
    
    @Nullable
    private Node getLandNode(final int integer1, int integer2, final int integer3, final int integer4, final double double5, final Direction gc, final BlockPathTypes cww) {
        Node cwy10 = null;
        final BlockPos.MutableBlockPos a11 = new BlockPos.MutableBlockPos();
        final double double6 = getFloorLevel(this.level, a11.set(integer1, integer2, integer3));
        if (double6 - double5 > 1.125) {
            return null;
        }
        BlockPathTypes cww2 = this.getCachedBlockType(this.mob, integer1, integer2, integer3);
        float float15 = this.mob.getPathfindingMalus(cww2);
        final double double7 = this.mob.getBbWidth() / 2.0;
        if (float15 >= 0.0f) {
            cwy10 = this.getNode(integer1, integer2, integer3);
            cwy10.type = cww2;
            cwy10.costMalus = Math.max(cwy10.costMalus, float15);
        }
        if (cww == BlockPathTypes.FENCE && cwy10 != null && cwy10.costMalus >= 0.0f && !this.canReachWithoutCollision(cwy10)) {
            cwy10 = null;
        }
        if (cww2 == BlockPathTypes.WALKABLE) {
            return cwy10;
        }
        if ((cwy10 == null || cwy10.costMalus < 0.0f) && integer4 > 0 && cww2 != BlockPathTypes.FENCE && cww2 != BlockPathTypes.UNPASSABLE_RAIL && cww2 != BlockPathTypes.TRAPDOOR) {
            cwy10 = this.getLandNode(integer1, integer2 + 1, integer3, integer4 - 1, double5, gc, cww);
            if (cwy10 != null && (cwy10.type == BlockPathTypes.OPEN || cwy10.type == BlockPathTypes.WALKABLE) && this.mob.getBbWidth() < 1.0f) {
                final double double8 = integer1 - gc.getStepX() + 0.5;
                final double double9 = integer3 - gc.getStepZ() + 0.5;
                final AABB dcf22 = new AABB(double8 - double7, getFloorLevel(this.level, a11.set(double8, integer2 + 1, double9)) + 0.001, double9 - double7, double8 + double7, this.mob.getBbHeight() + getFloorLevel(this.level, a11.set(cwy10.x, cwy10.y, (double)cwy10.z)) - 0.002, double9 + double7);
                if (this.hasCollisions(dcf22)) {
                    cwy10 = null;
                }
            }
        }
        if (cww2 == BlockPathTypes.WATER && !this.canFloat()) {
            if (this.getCachedBlockType(this.mob, integer1, integer2 - 1, integer3) != BlockPathTypes.WATER) {
                return cwy10;
            }
            while (integer2 > 0) {
                --integer2;
                cww2 = this.getCachedBlockType(this.mob, integer1, integer2, integer3);
                if (cww2 != BlockPathTypes.WATER) {
                    return cwy10;
                }
                cwy10 = this.getNode(integer1, integer2, integer3);
                cwy10.type = cww2;
                cwy10.costMalus = Math.max(cwy10.costMalus, this.mob.getPathfindingMalus(cww2));
            }
        }
        if (cww2 == BlockPathTypes.OPEN) {
            int integer5 = 0;
            final int integer6 = integer2;
            while (cww2 == BlockPathTypes.OPEN) {
                if (--integer2 < 0) {
                    final Node cwy11 = this.getNode(integer1, integer6, integer3);
                    cwy11.type = BlockPathTypes.BLOCKED;
                    cwy11.costMalus = -1.0f;
                    return cwy11;
                }
                if (integer5++ >= this.mob.getMaxFallDistance()) {
                    final Node cwy11 = this.getNode(integer1, integer2, integer3);
                    cwy11.type = BlockPathTypes.BLOCKED;
                    cwy11.costMalus = -1.0f;
                    return cwy11;
                }
                cww2 = this.getCachedBlockType(this.mob, integer1, integer2, integer3);
                float15 = this.mob.getPathfindingMalus(cww2);
                if (cww2 != BlockPathTypes.OPEN && float15 >= 0.0f) {
                    cwy10 = this.getNode(integer1, integer2, integer3);
                    cwy10.type = cww2;
                    cwy10.costMalus = Math.max(cwy10.costMalus, float15);
                    break;
                }
                if (float15 < 0.0f) {
                    final Node cwy11 = this.getNode(integer1, integer2, integer3);
                    cwy11.type = BlockPathTypes.BLOCKED;
                    cwy11.costMalus = -1.0f;
                    return cwy11;
                }
            }
        }
        if (cww2 == BlockPathTypes.FENCE) {
            cwy10 = this.getNode(integer1, integer2, integer3);
            cwy10.closed = true;
            cwy10.type = cww2;
            cwy10.costMalus = cww2.getMalus();
        }
        return cwy10;
    }
    
    private boolean hasCollisions(final AABB dcf) {
        return (boolean)this.collisionCache.computeIfAbsent(dcf, dcf2 -> !this.level.noCollision(this.mob, dcf));
    }
    
    @Override
    public BlockPathTypes getBlockPathType(final BlockGetter bqz, final int integer2, final int integer3, final int integer4, final Mob aqk, final int integer6, final int integer7, final int integer8, final boolean boolean9, final boolean boolean10) {
        final EnumSet<BlockPathTypes> enumSet12 = (EnumSet<BlockPathTypes>)EnumSet.noneOf((Class)BlockPathTypes.class);
        BlockPathTypes cww13 = BlockPathTypes.BLOCKED;
        final BlockPos fx14 = aqk.blockPosition();
        cww13 = this.getBlockPathTypes(bqz, integer2, integer3, integer4, integer6, integer7, integer8, boolean9, boolean10, enumSet12, cww13, fx14);
        if (enumSet12.contains(BlockPathTypes.FENCE)) {
            return BlockPathTypes.FENCE;
        }
        if (enumSet12.contains(BlockPathTypes.UNPASSABLE_RAIL)) {
            return BlockPathTypes.UNPASSABLE_RAIL;
        }
        BlockPathTypes cww14 = BlockPathTypes.BLOCKED;
        for (final BlockPathTypes cww15 : enumSet12) {
            if (aqk.getPathfindingMalus(cww15) < 0.0f) {
                return cww15;
            }
            if (aqk.getPathfindingMalus(cww15) < aqk.getPathfindingMalus(cww14)) {
                continue;
            }
            cww14 = cww15;
        }
        if (cww13 == BlockPathTypes.OPEN && aqk.getPathfindingMalus(cww14) == 0.0f && integer6 <= 1) {
            return BlockPathTypes.OPEN;
        }
        return cww14;
    }
    
    public BlockPathTypes getBlockPathTypes(final BlockGetter bqz, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final boolean boolean8, final boolean boolean9, final EnumSet<BlockPathTypes> enumSet, BlockPathTypes cww, final BlockPos fx) {
        for (int integer8 = 0; integer8 < integer5; ++integer8) {
            for (int integer9 = 0; integer9 < integer6; ++integer9) {
                for (int integer10 = 0; integer10 < integer7; ++integer10) {
                    final int integer11 = integer8 + integer2;
                    final int integer12 = integer9 + integer3;
                    final int integer13 = integer10 + integer4;
                    BlockPathTypes cww2 = this.getBlockPathType(bqz, integer11, integer12, integer13);
                    cww2 = this.evaluateBlockPathType(bqz, boolean8, boolean9, fx, cww2);
                    if (integer8 == 0 && integer9 == 0 && integer10 == 0) {
                        cww = cww2;
                    }
                    enumSet.add(cww2);
                }
            }
        }
        return cww;
    }
    
    protected BlockPathTypes evaluateBlockPathType(final BlockGetter bqz, final boolean boolean2, final boolean boolean3, final BlockPos fx, BlockPathTypes cww) {
        if (cww == BlockPathTypes.DOOR_WOOD_CLOSED && boolean2 && boolean3) {
            cww = BlockPathTypes.WALKABLE_DOOR;
        }
        if (cww == BlockPathTypes.DOOR_OPEN && !boolean3) {
            cww = BlockPathTypes.BLOCKED;
        }
        if (cww == BlockPathTypes.RAIL && !(bqz.getBlockState(fx).getBlock() instanceof BaseRailBlock) && !(bqz.getBlockState(fx.below()).getBlock() instanceof BaseRailBlock)) {
            cww = BlockPathTypes.UNPASSABLE_RAIL;
        }
        if (cww == BlockPathTypes.LEAVES) {
            cww = BlockPathTypes.BLOCKED;
        }
        return cww;
    }
    
    private BlockPathTypes getBlockPathType(final Mob aqk, final BlockPos fx) {
        return this.getCachedBlockType(aqk, fx.getX(), fx.getY(), fx.getZ());
    }
    
    private BlockPathTypes getCachedBlockType(final Mob aqk, final int integer2, final int integer3, final int integer4) {
        return (BlockPathTypes)this.pathTypesByPosCache.computeIfAbsent(BlockPos.asLong(integer2, integer3, integer4), long5 -> this.getBlockPathType(this.level, integer2, integer3, integer4, aqk, this.entityWidth, this.entityHeight, this.entityDepth, this.canOpenDoors(), this.canPassDoors()));
    }
    
    @Override
    public BlockPathTypes getBlockPathType(final BlockGetter bqz, final int integer2, final int integer3, final int integer4) {
        return getBlockPathTypeStatic(bqz, new BlockPos.MutableBlockPos(integer2, integer3, integer4));
    }
    
    public static BlockPathTypes getBlockPathTypeStatic(final BlockGetter bqz, final BlockPos.MutableBlockPos a) {
        final int integer3 = a.getX();
        final int integer4 = a.getY();
        final int integer5 = a.getZ();
        BlockPathTypes cww6 = getBlockPathTypeRaw(bqz, a);
        if (cww6 == BlockPathTypes.OPEN && integer4 >= 1) {
            final BlockPathTypes cww7 = getBlockPathTypeRaw(bqz, a.set(integer3, integer4 - 1, integer5));
            cww6 = ((cww7 == BlockPathTypes.WALKABLE || cww7 == BlockPathTypes.OPEN || cww7 == BlockPathTypes.WATER || cww7 == BlockPathTypes.LAVA) ? BlockPathTypes.OPEN : BlockPathTypes.WALKABLE);
            if (cww7 == BlockPathTypes.DAMAGE_FIRE) {
                cww6 = BlockPathTypes.DAMAGE_FIRE;
            }
            if (cww7 == BlockPathTypes.DAMAGE_CACTUS) {
                cww6 = BlockPathTypes.DAMAGE_CACTUS;
            }
            if (cww7 == BlockPathTypes.DAMAGE_OTHER) {
                cww6 = BlockPathTypes.DAMAGE_OTHER;
            }
            if (cww7 == BlockPathTypes.STICKY_HONEY) {
                cww6 = BlockPathTypes.STICKY_HONEY;
            }
        }
        if (cww6 == BlockPathTypes.WALKABLE) {
            cww6 = checkNeighbourBlocks(bqz, a.set(integer3, integer4, integer5), cww6);
        }
        return cww6;
    }
    
    public static BlockPathTypes checkNeighbourBlocks(final BlockGetter bqz, final BlockPos.MutableBlockPos a, final BlockPathTypes cww) {
        final int integer4 = a.getX();
        final int integer5 = a.getY();
        final int integer6 = a.getZ();
        for (int integer7 = -1; integer7 <= 1; ++integer7) {
            for (int integer8 = -1; integer8 <= 1; ++integer8) {
                for (int integer9 = -1; integer9 <= 1; ++integer9) {
                    if (integer7 != 0 || integer9 != 0) {
                        a.set(integer4 + integer7, integer5 + integer8, integer6 + integer9);
                        final BlockState cee10 = bqz.getBlockState(a);
                        if (cee10.is(Blocks.CACTUS)) {
                            return BlockPathTypes.DANGER_CACTUS;
                        }
                        if (cee10.is(Blocks.SWEET_BERRY_BUSH)) {
                            return BlockPathTypes.DANGER_OTHER;
                        }
                        if (isBurningBlock(cee10)) {
                            return BlockPathTypes.DANGER_FIRE;
                        }
                        if (bqz.getFluidState(a).is(FluidTags.WATER)) {
                            return BlockPathTypes.WATER_BORDER;
                        }
                    }
                }
            }
        }
        return cww;
    }
    
    protected static BlockPathTypes getBlockPathTypeRaw(final BlockGetter bqz, final BlockPos fx) {
        final BlockState cee3 = bqz.getBlockState(fx);
        final Block bul4 = cee3.getBlock();
        final Material cux5 = cee3.getMaterial();
        if (cee3.isAir()) {
            return BlockPathTypes.OPEN;
        }
        if (cee3.is(BlockTags.TRAPDOORS) || cee3.is(Blocks.LILY_PAD)) {
            return BlockPathTypes.TRAPDOOR;
        }
        if (cee3.is(Blocks.CACTUS)) {
            return BlockPathTypes.DAMAGE_CACTUS;
        }
        if (cee3.is(Blocks.SWEET_BERRY_BUSH)) {
            return BlockPathTypes.DAMAGE_OTHER;
        }
        if (cee3.is(Blocks.HONEY_BLOCK)) {
            return BlockPathTypes.STICKY_HONEY;
        }
        if (cee3.is(Blocks.COCOA)) {
            return BlockPathTypes.COCOA;
        }
        final FluidState cuu6 = bqz.getFluidState(fx);
        if (cuu6.is(FluidTags.WATER)) {
            return BlockPathTypes.WATER;
        }
        if (cuu6.is(FluidTags.LAVA)) {
            return BlockPathTypes.LAVA;
        }
        if (isBurningBlock(cee3)) {
            return BlockPathTypes.DAMAGE_FIRE;
        }
        if (DoorBlock.isWoodenDoor(cee3) && !cee3.<Boolean>getValue((Property<Boolean>)DoorBlock.OPEN)) {
            return BlockPathTypes.DOOR_WOOD_CLOSED;
        }
        if (bul4 instanceof DoorBlock && cux5 == Material.METAL && !cee3.<Boolean>getValue((Property<Boolean>)DoorBlock.OPEN)) {
            return BlockPathTypes.DOOR_IRON_CLOSED;
        }
        if (bul4 instanceof DoorBlock && cee3.<Boolean>getValue((Property<Boolean>)DoorBlock.OPEN)) {
            return BlockPathTypes.DOOR_OPEN;
        }
        if (bul4 instanceof BaseRailBlock) {
            return BlockPathTypes.RAIL;
        }
        if (bul4 instanceof LeavesBlock) {
            return BlockPathTypes.LEAVES;
        }
        if (bul4.is(BlockTags.FENCES) || bul4.is(BlockTags.WALLS) || (bul4 instanceof FenceGateBlock && !cee3.<Boolean>getValue((Property<Boolean>)FenceGateBlock.OPEN))) {
            return BlockPathTypes.FENCE;
        }
        if (!cee3.isPathfindable(bqz, fx, PathComputationType.LAND)) {
            return BlockPathTypes.BLOCKED;
        }
        return BlockPathTypes.OPEN;
    }
    
    private static boolean isBurningBlock(final BlockState cee) {
        return cee.is(BlockTags.FIRE) || cee.is(Blocks.LAVA) || cee.is(Blocks.MAGMA_BLOCK) || CampfireBlock.isLitCampfire(cee);
    }
}
