package net.minecraft.world.level.levelgen.structure;

import net.minecraft.world.level.block.state.StateHolder;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockGetter;
import java.util.Iterator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import java.util.Random;
import java.util.List;
import net.minecraft.nbt.Tag;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import java.util.Set;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public abstract class StructurePiece {
    protected static final BlockState CAVE_AIR;
    protected BoundingBox boundingBox;
    @Nullable
    private Direction orientation;
    private Mirror mirror;
    private Rotation rotation;
    protected int genDepth;
    private final StructurePieceType type;
    private static final Set<Block> SHAPE_CHECK_BLOCKS;
    
    protected StructurePiece(final StructurePieceType cky, final int integer) {
        this.type = cky;
        this.genDepth = integer;
    }
    
    public StructurePiece(final StructurePieceType cky, final CompoundTag md) {
        this(cky, md.getInt("GD"));
        if (md.contains("BB")) {
            this.boundingBox = new BoundingBox(md.getIntArray("BB"));
        }
        final int integer4 = md.getInt("O");
        this.setOrientation((integer4 == -1) ? null : Direction.from2DDataValue(integer4));
    }
    
    public final CompoundTag createTag() {
        final CompoundTag md2 = new CompoundTag();
        md2.putString("id", Registry.STRUCTURE_PIECE.getKey(this.getType()).toString());
        md2.put("BB", (Tag)this.boundingBox.createTag());
        final Direction gc3 = this.getOrientation();
        md2.putInt("O", (gc3 == null) ? -1 : gc3.get2DDataValue());
        md2.putInt("GD", this.genDepth);
        this.addAdditionalSaveData(md2);
        return md2;
    }
    
    protected abstract void addAdditionalSaveData(final CompoundTag md);
    
    public void addChildren(final StructurePiece crr, final List<StructurePiece> list, final Random random) {
    }
    
    public abstract boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx);
    
    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }
    
    public int getGenDepth() {
        return this.genDepth;
    }
    
    public boolean isCloseToChunk(final ChunkPos bra, final int integer) {
        final int integer2 = bra.x << 4;
        final int integer3 = bra.z << 4;
        return this.boundingBox.intersects(integer2 - integer, integer3 - integer, integer2 + 15 + integer, integer3 + 15 + integer);
    }
    
    public static StructurePiece findCollisionPiece(final List<StructurePiece> list, final BoundingBox cqx) {
        for (final StructurePiece crr4 : list) {
            if (crr4.getBoundingBox() != null && crr4.getBoundingBox().intersects(cqx)) {
                return crr4;
            }
        }
        return null;
    }
    
    protected boolean edgesLiquid(final BlockGetter bqz, final BoundingBox cqx) {
        final int integer4 = Math.max(this.boundingBox.x0 - 1, cqx.x0);
        final int integer5 = Math.max(this.boundingBox.y0 - 1, cqx.y0);
        final int integer6 = Math.max(this.boundingBox.z0 - 1, cqx.z0);
        final int integer7 = Math.min(this.boundingBox.x1 + 1, cqx.x1);
        final int integer8 = Math.min(this.boundingBox.y1 + 1, cqx.y1);
        final int integer9 = Math.min(this.boundingBox.z1 + 1, cqx.z1);
        final BlockPos.MutableBlockPos a10 = new BlockPos.MutableBlockPos();
        for (int integer10 = integer4; integer10 <= integer7; ++integer10) {
            for (int integer11 = integer6; integer11 <= integer9; ++integer11) {
                if (bqz.getBlockState(a10.set(integer10, integer5, integer11)).getMaterial().isLiquid()) {
                    return true;
                }
                if (bqz.getBlockState(a10.set(integer10, integer8, integer11)).getMaterial().isLiquid()) {
                    return true;
                }
            }
        }
        for (int integer10 = integer4; integer10 <= integer7; ++integer10) {
            for (int integer11 = integer5; integer11 <= integer8; ++integer11) {
                if (bqz.getBlockState(a10.set(integer10, integer11, integer6)).getMaterial().isLiquid()) {
                    return true;
                }
                if (bqz.getBlockState(a10.set(integer10, integer11, integer9)).getMaterial().isLiquid()) {
                    return true;
                }
            }
        }
        for (int integer10 = integer6; integer10 <= integer9; ++integer10) {
            for (int integer11 = integer5; integer11 <= integer8; ++integer11) {
                if (bqz.getBlockState(a10.set(integer4, integer11, integer10)).getMaterial().isLiquid()) {
                    return true;
                }
                if (bqz.getBlockState(a10.set(integer7, integer11, integer10)).getMaterial().isLiquid()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    protected int getWorldX(final int integer1, final int integer2) {
        final Direction gc4 = this.getOrientation();
        if (gc4 == null) {
            return integer1;
        }
        switch (gc4) {
            case NORTH:
            case SOUTH: {
                return this.boundingBox.x0 + integer1;
            }
            case WEST: {
                return this.boundingBox.x1 - integer2;
            }
            case EAST: {
                return this.boundingBox.x0 + integer2;
            }
            default: {
                return integer1;
            }
        }
    }
    
    protected int getWorldY(final int integer) {
        if (this.getOrientation() == null) {
            return integer;
        }
        return integer + this.boundingBox.y0;
    }
    
    protected int getWorldZ(final int integer1, final int integer2) {
        final Direction gc4 = this.getOrientation();
        if (gc4 == null) {
            return integer2;
        }
        switch (gc4) {
            case NORTH: {
                return this.boundingBox.z1 - integer2;
            }
            case SOUTH: {
                return this.boundingBox.z0 + integer2;
            }
            case WEST:
            case EAST: {
                return this.boundingBox.z0 + integer1;
            }
            default: {
                return integer2;
            }
        }
    }
    
    protected void placeBlock(final WorldGenLevel bso, BlockState cee, final int integer3, final int integer4, final int integer5, final BoundingBox cqx) {
        final BlockPos fx8 = new BlockPos(this.getWorldX(integer3, integer5), this.getWorldY(integer4), this.getWorldZ(integer3, integer5));
        if (!cqx.isInside(fx8)) {
            return;
        }
        if (this.mirror != Mirror.NONE) {
            cee = cee.mirror(this.mirror);
        }
        if (this.rotation != Rotation.NONE) {
            cee = cee.rotate(this.rotation);
        }
        bso.setBlock(fx8, cee, 2);
        final FluidState cuu9 = bso.getFluidState(fx8);
        if (!cuu9.isEmpty()) {
            bso.getLiquidTicks().scheduleTick(fx8, cuu9.getType(), 0);
        }
        if (StructurePiece.SHAPE_CHECK_BLOCKS.contains(cee.getBlock())) {
            bso.getChunk(fx8).markPosForPostprocessing(fx8);
        }
    }
    
    protected BlockState getBlock(final BlockGetter bqz, final int integer2, final int integer3, final int integer4, final BoundingBox cqx) {
        final int integer5 = this.getWorldX(integer2, integer4);
        final int integer6 = this.getWorldY(integer3);
        final int integer7 = this.getWorldZ(integer2, integer4);
        final BlockPos fx10 = new BlockPos(integer5, integer6, integer7);
        if (!cqx.isInside(fx10)) {
            return Blocks.AIR.defaultBlockState();
        }
        return bqz.getBlockState(fx10);
    }
    
    protected boolean isInterior(final LevelReader brw, final int integer2, final int integer3, final int integer4, final BoundingBox cqx) {
        final int integer5 = this.getWorldX(integer2, integer4);
        final int integer6 = this.getWorldY(integer3 + 1);
        final int integer7 = this.getWorldZ(integer2, integer4);
        final BlockPos fx10 = new BlockPos(integer5, integer6, integer7);
        return cqx.isInside(fx10) && integer6 < brw.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, integer5, integer7);
    }
    
    protected void generateAirBox(final WorldGenLevel bso, final BoundingBox cqx, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8) {
        for (int integer9 = integer4; integer9 <= integer7; ++integer9) {
            for (int integer10 = integer3; integer10 <= integer6; ++integer10) {
                for (int integer11 = integer5; integer11 <= integer8; ++integer11) {
                    this.placeBlock(bso, Blocks.AIR.defaultBlockState(), integer10, integer9, integer11, cqx);
                }
            }
        }
    }
    
    protected void generateBox(final WorldGenLevel bso, final BoundingBox cqx, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final BlockState cee9, final BlockState cee10, final boolean boolean11) {
        for (int integer9 = integer4; integer9 <= integer7; ++integer9) {
            for (int integer10 = integer3; integer10 <= integer6; ++integer10) {
                for (int integer11 = integer5; integer11 <= integer8; ++integer11) {
                    if (!boolean11 || !this.getBlock(bso, integer10, integer9, integer11, cqx).isAir()) {
                        if (integer9 == integer4 || integer9 == integer7 || integer10 == integer3 || integer10 == integer6 || integer11 == integer5 || integer11 == integer8) {
                            this.placeBlock(bso, cee9, integer10, integer9, integer11, cqx);
                        }
                        else {
                            this.placeBlock(bso, cee10, integer10, integer9, integer11, cqx);
                        }
                    }
                }
            }
        }
    }
    
    protected void generateBox(final WorldGenLevel bso, final BoundingBox cqx, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final Random random, final BlockSelector a) {
        for (int integer9 = integer4; integer9 <= integer7; ++integer9) {
            for (int integer10 = integer3; integer10 <= integer6; ++integer10) {
                for (int integer11 = integer5; integer11 <= integer8; ++integer11) {
                    if (!boolean9 || !this.getBlock(bso, integer10, integer9, integer11, cqx).isAir()) {
                        a.next(random, integer10, integer9, integer11, integer9 == integer4 || integer9 == integer7 || integer10 == integer3 || integer10 == integer6 || integer11 == integer5 || integer11 == integer8);
                        this.placeBlock(bso, a.getNext(), integer10, integer9, integer11, cqx);
                    }
                }
            }
        }
    }
    
    protected void generateMaybeBox(final WorldGenLevel bso, final BoundingBox cqx, final Random random, final float float4, final int integer5, final int integer6, final int integer7, final int integer8, final int integer9, final int integer10, final BlockState cee11, final BlockState cee12, final boolean boolean13, final boolean boolean14) {
        for (int integer11 = integer6; integer11 <= integer9; ++integer11) {
            for (int integer12 = integer5; integer12 <= integer8; ++integer12) {
                for (int integer13 = integer7; integer13 <= integer10; ++integer13) {
                    if (random.nextFloat() <= float4) {
                        if (!boolean13 || !this.getBlock(bso, integer12, integer11, integer13, cqx).isAir()) {
                            if (!boolean14 || this.isInterior(bso, integer12, integer11, integer13, cqx)) {
                                if (integer11 == integer6 || integer11 == integer9 || integer12 == integer5 || integer12 == integer8 || integer13 == integer7 || integer13 == integer10) {
                                    this.placeBlock(bso, cee11, integer12, integer11, integer13, cqx);
                                }
                                else {
                                    this.placeBlock(bso, cee12, integer12, integer11, integer13, cqx);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    protected void maybeGenerateBlock(final WorldGenLevel bso, final BoundingBox cqx, final Random random, final float float4, final int integer5, final int integer6, final int integer7, final BlockState cee) {
        if (random.nextFloat() < float4) {
            this.placeBlock(bso, cee, integer5, integer6, integer7, cqx);
        }
    }
    
    protected void generateUpperHalfSphere(final WorldGenLevel bso, final BoundingBox cqx, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final BlockState cee, final boolean boolean10) {
        final float float12 = (float)(integer6 - integer3 + 1);
        final float float13 = (float)(integer7 - integer4 + 1);
        final float float14 = (float)(integer8 - integer5 + 1);
        final float float15 = integer3 + float12 / 2.0f;
        final float float16 = integer5 + float14 / 2.0f;
        for (int integer9 = integer4; integer9 <= integer7; ++integer9) {
            final float float17 = (integer9 - integer4) / float13;
            for (int integer10 = integer3; integer10 <= integer6; ++integer10) {
                final float float18 = (integer10 - float15) / (float12 * 0.5f);
                for (int integer11 = integer5; integer11 <= integer8; ++integer11) {
                    final float float19 = (integer11 - float16) / (float14 * 0.5f);
                    if (!boolean10 || !this.getBlock(bso, integer10, integer9, integer11, cqx).isAir()) {
                        final float float20 = float18 * float18 + float17 * float17 + float19 * float19;
                        if (float20 <= 1.05f) {
                            this.placeBlock(bso, cee, integer10, integer9, integer11, cqx);
                        }
                    }
                }
            }
        }
    }
    
    protected void fillColumnDown(final WorldGenLevel bso, final BlockState cee, final int integer3, final int integer4, final int integer5, final BoundingBox cqx) {
        final int integer6 = this.getWorldX(integer3, integer5);
        int integer7 = this.getWorldY(integer4);
        final int integer8 = this.getWorldZ(integer3, integer5);
        if (!cqx.isInside(new BlockPos(integer6, integer7, integer8))) {
            return;
        }
        while ((bso.isEmptyBlock(new BlockPos(integer6, integer7, integer8)) || bso.getBlockState(new BlockPos(integer6, integer7, integer8)).getMaterial().isLiquid()) && integer7 > 1) {
            bso.setBlock(new BlockPos(integer6, integer7, integer8), cee, 2);
            --integer7;
        }
    }
    
    protected boolean createChest(final WorldGenLevel bso, final BoundingBox cqx, final Random random, final int integer4, final int integer5, final int integer6, final ResourceLocation vk) {
        final BlockPos fx9 = new BlockPos(this.getWorldX(integer4, integer6), this.getWorldY(integer5), this.getWorldZ(integer4, integer6));
        return this.createChest(bso, cqx, random, fx9, vk, null);
    }
    
    public static BlockState reorient(final BlockGetter bqz, final BlockPos fx, final BlockState cee) {
        Direction gc4 = null;
        for (final Direction gc5 : Direction.Plane.HORIZONTAL) {
            final BlockPos fx2 = fx.relative(gc5);
            final BlockState cee2 = bqz.getBlockState(fx2);
            if (cee2.is(Blocks.CHEST)) {
                return cee;
            }
            if (!cee2.isSolidRender(bqz, fx2)) {
                continue;
            }
            if (gc4 != null) {
                gc4 = null;
                break;
            }
            gc4 = gc5;
        }
        if (gc4 != null) {
            return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)HorizontalDirectionalBlock.FACING, gc4.getOpposite());
        }
        Direction gc6 = cee.<Direction>getValue((Property<Direction>)HorizontalDirectionalBlock.FACING);
        BlockPos fx3 = fx.relative(gc6);
        if (bqz.getBlockState(fx3).isSolidRender(bqz, fx3)) {
            gc6 = gc6.getOpposite();
            fx3 = fx.relative(gc6);
        }
        if (bqz.getBlockState(fx3).isSolidRender(bqz, fx3)) {
            gc6 = gc6.getClockWise();
            fx3 = fx.relative(gc6);
        }
        if (bqz.getBlockState(fx3).isSolidRender(bqz, fx3)) {
            gc6 = gc6.getOpposite();
            fx3 = fx.relative(gc6);
        }
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)HorizontalDirectionalBlock.FACING, gc6);
    }
    
    protected boolean createChest(final ServerLevelAccessor bsh, final BoundingBox cqx, final Random random, final BlockPos fx, final ResourceLocation vk, @Nullable BlockState cee) {
        if (!cqx.isInside(fx) || bsh.getBlockState(fx).is(Blocks.CHEST)) {
            return false;
        }
        if (cee == null) {
            cee = reorient(bsh, fx, Blocks.CHEST.defaultBlockState());
        }
        bsh.setBlock(fx, cee, 2);
        final BlockEntity ccg8 = bsh.getBlockEntity(fx);
        if (ccg8 instanceof ChestBlockEntity) {
            ((ChestBlockEntity)ccg8).setLootTable(vk, random.nextLong());
        }
        return true;
    }
    
    protected boolean createDispenser(final WorldGenLevel bso, final BoundingBox cqx, final Random random, final int integer4, final int integer5, final int integer6, final Direction gc, final ResourceLocation vk) {
        final BlockPos fx10 = new BlockPos(this.getWorldX(integer4, integer6), this.getWorldY(integer5), this.getWorldZ(integer4, integer6));
        if (cqx.isInside(fx10) && !bso.getBlockState(fx10).is(Blocks.DISPENSER)) {
            this.placeBlock(bso, ((StateHolder<O, BlockState>)Blocks.DISPENSER.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)DispenserBlock.FACING, gc), integer4, integer5, integer6, cqx);
            final BlockEntity ccg11 = bso.getBlockEntity(fx10);
            if (ccg11 instanceof DispenserBlockEntity) {
                ((DispenserBlockEntity)ccg11).setLootTable(vk, random.nextLong());
            }
            return true;
        }
        return false;
    }
    
    public void move(final int integer1, final int integer2, final int integer3) {
        this.boundingBox.move(integer1, integer2, integer3);
    }
    
    @Nullable
    public Direction getOrientation() {
        return this.orientation;
    }
    
    public void setOrientation(@Nullable final Direction gc) {
        this.orientation = gc;
        if (gc == null) {
            this.rotation = Rotation.NONE;
            this.mirror = Mirror.NONE;
        }
        else {
            switch (gc) {
                case SOUTH: {
                    this.mirror = Mirror.LEFT_RIGHT;
                    this.rotation = Rotation.NONE;
                    break;
                }
                case WEST: {
                    this.mirror = Mirror.LEFT_RIGHT;
                    this.rotation = Rotation.CLOCKWISE_90;
                    break;
                }
                case EAST: {
                    this.mirror = Mirror.NONE;
                    this.rotation = Rotation.CLOCKWISE_90;
                    break;
                }
                default: {
                    this.mirror = Mirror.NONE;
                    this.rotation = Rotation.NONE;
                    break;
                }
            }
        }
    }
    
    public Rotation getRotation() {
        return this.rotation;
    }
    
    public StructurePieceType getType() {
        return this.type;
    }
    
    static {
        CAVE_AIR = Blocks.CAVE_AIR.defaultBlockState();
        SHAPE_CHECK_BLOCKS = (Set)ImmutableSet.builder().add(Blocks.NETHER_BRICK_FENCE).add(Blocks.TORCH).add(Blocks.WALL_TORCH).add(Blocks.OAK_FENCE).add(Blocks.SPRUCE_FENCE).add(Blocks.DARK_OAK_FENCE).add(Blocks.ACACIA_FENCE).add(Blocks.BIRCH_FENCE).add(Blocks.JUNGLE_FENCE).add(Blocks.LADDER).add(Blocks.IRON_BARS).build();
    }
    
    public abstract static class BlockSelector {
        protected BlockState next;
        
        protected BlockSelector() {
            this.next = Blocks.AIR.defaultBlockState();
        }
        
        public abstract void next(final Random random, final int integer2, final int integer3, final int integer4, final boolean boolean5);
        
        public BlockState getNext() {
            return this.next;
        }
    }
}
