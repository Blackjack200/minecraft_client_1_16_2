package net.minecraft.world.level;

import java.util.function.BiPredicate;
import java.util.Spliterator;
import java.util.stream.StreamSupport;
import java.util.stream.Stream;
import java.util.function.Predicate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.entity.Entity;
import javax.annotation.Nullable;
import net.minecraft.world.level.border.WorldBorder;

public interface CollisionGetter extends BlockGetter {
    WorldBorder getWorldBorder();
    
    @Nullable
    BlockGetter getChunkForCollisions(final int integer1, final int integer2);
    
    default boolean isUnobstructed(@Nullable final Entity apx, final VoxelShape dde) {
        return true;
    }
    
    default boolean isUnobstructed(final BlockState cee, final BlockPos fx, final CollisionContext dcp) {
        final VoxelShape dde5 = cee.getCollisionShape(this, fx, dcp);
        return dde5.isEmpty() || this.isUnobstructed(null, dde5.move(fx.getX(), fx.getY(), fx.getZ()));
    }
    
    default boolean isUnobstructed(final Entity apx) {
        return this.isUnobstructed(apx, Shapes.create(apx.getBoundingBox()));
    }
    
    default boolean noCollision(final AABB dcf) {
        return this.noCollision(null, dcf, (Predicate<Entity>)(apx -> true));
    }
    
    default boolean noCollision(final Entity apx) {
        return this.noCollision(apx, apx.getBoundingBox(), (Predicate<Entity>)(apx -> true));
    }
    
    default boolean noCollision(final Entity apx, final AABB dcf) {
        return this.noCollision(apx, dcf, (Predicate<Entity>)(apx -> true));
    }
    
    default boolean noCollision(@Nullable final Entity apx, final AABB dcf, final Predicate<Entity> predicate) {
        return this.getCollisions(apx, dcf, predicate).allMatch(VoxelShape::isEmpty);
    }
    
    Stream<VoxelShape> getEntityCollisions(@Nullable final Entity apx, final AABB dcf, final Predicate<Entity> predicate);
    
    default Stream<VoxelShape> getCollisions(@Nullable final Entity apx, final AABB dcf, final Predicate<Entity> predicate) {
        return (Stream<VoxelShape>)Stream.concat((Stream)this.getBlockCollisions(apx, dcf), (Stream)this.getEntityCollisions(apx, dcf, predicate));
    }
    
    default Stream<VoxelShape> getBlockCollisions(@Nullable final Entity apx, final AABB dcf) {
        return (Stream<VoxelShape>)StreamSupport.stream((Spliterator)new CollisionSpliterator(this, apx, dcf), false);
    }
    
    default boolean noBlockCollision(@Nullable final Entity apx, final AABB dcf, final BiPredicate<BlockState, BlockPos> biPredicate) {
        return this.getBlockCollisions(apx, dcf, biPredicate).allMatch(VoxelShape::isEmpty);
    }
    
    default Stream<VoxelShape> getBlockCollisions(@Nullable final Entity apx, final AABB dcf, final BiPredicate<BlockState, BlockPos> biPredicate) {
        return (Stream<VoxelShape>)StreamSupport.stream((Spliterator)new CollisionSpliterator(this, apx, dcf, biPredicate), false);
    }
}
