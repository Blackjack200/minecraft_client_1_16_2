package net.minecraft.world.level;

import net.minecraft.world.level.border.WorldBorder;
import java.util.Objects;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.level.block.Blocks;
import java.util.function.Consumer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.BiPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Cursor3D;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.AABB;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.Spliterators;

public class CollisionSpliterator extends Spliterators.AbstractSpliterator<VoxelShape> {
    @Nullable
    private final Entity source;
    private final AABB box;
    private final CollisionContext context;
    private final Cursor3D cursor;
    private final BlockPos.MutableBlockPos pos;
    private final VoxelShape entityShape;
    private final CollisionGetter collisionGetter;
    private boolean needsBorderCheck;
    private final BiPredicate<BlockState, BlockPos> predicate;
    
    public CollisionSpliterator(final CollisionGetter brd, @Nullable final Entity apx, final AABB dcf) {
        this(brd, apx, dcf, (BiPredicate<BlockState, BlockPos>)((cee, fx) -> true));
    }
    
    public CollisionSpliterator(final CollisionGetter brd, @Nullable final Entity apx, final AABB dcf, final BiPredicate<BlockState, BlockPos> biPredicate) {
        super(Long.MAX_VALUE, 1280);
        this.context = ((apx == null) ? CollisionContext.empty() : CollisionContext.of(apx));
        this.pos = new BlockPos.MutableBlockPos();
        this.entityShape = Shapes.create(dcf);
        this.collisionGetter = brd;
        this.needsBorderCheck = (apx != null);
        this.source = apx;
        this.box = dcf;
        this.predicate = biPredicate;
        final int integer6 = Mth.floor(dcf.minX - 1.0E-7) - 1;
        final int integer7 = Mth.floor(dcf.maxX + 1.0E-7) + 1;
        final int integer8 = Mth.floor(dcf.minY - 1.0E-7) - 1;
        final int integer9 = Mth.floor(dcf.maxY + 1.0E-7) + 1;
        final int integer10 = Mth.floor(dcf.minZ - 1.0E-7) - 1;
        final int integer11 = Mth.floor(dcf.maxZ + 1.0E-7) + 1;
        this.cursor = new Cursor3D(integer6, integer8, integer10, integer7, integer9, integer11);
    }
    
    public boolean tryAdvance(final Consumer<? super VoxelShape> consumer) {
        return (this.needsBorderCheck && this.worldBorderCheck(consumer)) || this.collisionCheck(consumer);
    }
    
    boolean collisionCheck(final Consumer<? super VoxelShape> consumer) {
        while (this.cursor.advance()) {
            final int integer3 = this.cursor.nextX();
            final int integer4 = this.cursor.nextY();
            final int integer5 = this.cursor.nextZ();
            final int integer6 = this.cursor.getNextType();
            if (integer6 == 3) {
                continue;
            }
            final BlockGetter bqz7 = this.getChunk(integer3, integer5);
            if (bqz7 == null) {
                continue;
            }
            this.pos.set(integer3, integer4, integer5);
            final BlockState cee8 = bqz7.getBlockState(this.pos);
            if (!this.predicate.test(cee8, this.pos)) {
                continue;
            }
            if (integer6 == 1 && !cee8.hasLargeCollisionShape()) {
                continue;
            }
            if (integer6 == 2 && !cee8.is(Blocks.MOVING_PISTON)) {
                continue;
            }
            final VoxelShape dde9 = cee8.getCollisionShape(this.collisionGetter, this.pos, this.context);
            if (dde9 == Shapes.block()) {
                if (this.box.intersects(integer3, integer4, integer5, integer3 + 1.0, integer4 + 1.0, integer5 + 1.0)) {
                    consumer.accept(dde9.move(integer3, integer4, integer5));
                    return true;
                }
                continue;
            }
            else {
                final VoxelShape dde10 = dde9.move(integer3, integer4, integer5);
                if (Shapes.joinIsNotEmpty(dde10, this.entityShape, BooleanOp.AND)) {
                    consumer.accept(dde10);
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    @Nullable
    private BlockGetter getChunk(final int integer1, final int integer2) {
        final int integer3 = integer1 >> 4;
        final int integer4 = integer2 >> 4;
        return this.collisionGetter.getChunkForCollisions(integer3, integer4);
    }
    
    boolean worldBorderCheck(final Consumer<? super VoxelShape> consumer) {
        Objects.requireNonNull(this.source);
        this.needsBorderCheck = false;
        final WorldBorder cfr3 = this.collisionGetter.getWorldBorder();
        final AABB dcf4 = this.source.getBoundingBox();
        if (!isBoxFullyWithinWorldBorder(cfr3, dcf4)) {
            final VoxelShape dde5 = cfr3.getCollisionShape();
            if (!isOutsideBorder(dde5, dcf4) && isCloseToBorder(dde5, dcf4)) {
                consumer.accept(dde5);
                return true;
            }
        }
        return false;
    }
    
    private static boolean isCloseToBorder(final VoxelShape dde, final AABB dcf) {
        return Shapes.joinIsNotEmpty(dde, Shapes.create(dcf.inflate(1.0E-7)), BooleanOp.AND);
    }
    
    private static boolean isOutsideBorder(final VoxelShape dde, final AABB dcf) {
        return Shapes.joinIsNotEmpty(dde, Shapes.create(dcf.deflate(1.0E-7)), BooleanOp.AND);
    }
    
    public static boolean isBoxFullyWithinWorldBorder(final WorldBorder cfr, final AABB dcf) {
        final double double3 = Mth.floor(cfr.getMinX());
        final double double4 = Mth.floor(cfr.getMinZ());
        final double double5 = Mth.ceil(cfr.getMaxX());
        final double double6 = Mth.ceil(cfr.getMaxZ());
        return dcf.minX > double3 && dcf.minX < double5 && dcf.minZ > double4 && dcf.minZ < double6 && dcf.maxX > double3 && dcf.maxX < double5 && dcf.maxZ > double4 && dcf.maxZ < double6;
    }
}
