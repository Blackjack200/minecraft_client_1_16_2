package net.minecraft.world.level;

import java.util.UUID;
import com.google.common.collect.Lists;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import java.util.stream.Stream;
import java.util.Iterator;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.phys.AABB;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;

public interface EntityGetter {
    List<Entity> getEntities(@Nullable final Entity apx, final AABB dcf, @Nullable final Predicate<? super Entity> predicate);
    
     <T extends Entity> List<T> getEntitiesOfClass(final Class<? extends T> class1, final AABB dcf, @Nullable final Predicate<? super T> predicate);
    
    default <T extends Entity> List<T> getLoadedEntitiesOfClass(final Class<? extends T> class1, final AABB dcf, @Nullable final Predicate<? super T> predicate) {
        return this.getEntitiesOfClass((java.lang.Class<? extends Entity>)class1, dcf, (java.util.function.Predicate<? super Entity>)predicate);
    }
    
    List<? extends Player> players();
    
    default List<Entity> getEntities(@Nullable final Entity apx, final AABB dcf) {
        return this.getEntities(apx, dcf, EntitySelector.NO_SPECTATORS);
    }
    
    default boolean isUnobstructed(@Nullable final Entity apx, final VoxelShape dde) {
        if (dde.isEmpty()) {
            return true;
        }
        for (final Entity apx2 : this.getEntities(apx, dde.bounds())) {
            if (!apx2.removed && apx2.blocksBuilding && (apx == null || !apx2.isPassengerOfSameVehicle(apx)) && Shapes.joinIsNotEmpty(dde, Shapes.create(apx2.getBoundingBox()), BooleanOp.AND)) {
                return false;
            }
        }
        return true;
    }
    
    default <T extends Entity> List<T> getEntitiesOfClass(final Class<? extends T> class1, final AABB dcf) {
        return this.<T>getEntitiesOfClass(class1, dcf, (java.util.function.Predicate<? super T>)EntitySelector.NO_SPECTATORS);
    }
    
    default <T extends Entity> List<T> getLoadedEntitiesOfClass(final Class<? extends T> class1, final AABB dcf) {
        return this.<T>getLoadedEntitiesOfClass(class1, dcf, (java.util.function.Predicate<? super T>)EntitySelector.NO_SPECTATORS);
    }
    
    default Stream<VoxelShape> getEntityCollisions(@Nullable final Entity apx, final AABB dcf, final Predicate<Entity> predicate) {
        if (dcf.getSize() < 1.0E-7) {
            return (Stream<VoxelShape>)Stream.empty();
        }
        final AABB dcf2 = dcf.inflate(1.0E-7);
        return (Stream<VoxelShape>)this.getEntities(apx, dcf2, predicate.and(apx3 -> apx3.getBoundingBox().intersects(dcf2) && ((apx != null) ? apx.canCollideWith(apx3) : apx3.canBeCollidedWith()))).stream().map(Entity::getBoundingBox).map(Shapes::create);
    }
    
    @Nullable
    default Player getNearestPlayer(final double double1, final double double2, final double double3, final double double4, @Nullable final Predicate<Entity> predicate) {
        double double5 = -1.0;
        Player bft13 = null;
        for (final Player bft14 : this.players()) {
            if (predicate != null && !predicate.test(bft14)) {
                continue;
            }
            final double double6 = bft14.distanceToSqr(double1, double2, double3);
            if ((double4 >= 0.0 && double6 >= double4 * double4) || (double5 != -1.0 && double6 >= double5)) {
                continue;
            }
            double5 = double6;
            bft13 = bft14;
        }
        return bft13;
    }
    
    @Nullable
    default Player getNearestPlayer(final Entity apx, final double double2) {
        return this.getNearestPlayer(apx.getX(), apx.getY(), apx.getZ(), double2, false);
    }
    
    @Nullable
    default Player getNearestPlayer(final double double1, final double double2, final double double3, final double double4, final boolean boolean5) {
        final Predicate<Entity> predicate11 = boolean5 ? EntitySelector.NO_CREATIVE_OR_SPECTATOR : EntitySelector.NO_SPECTATORS;
        return this.getNearestPlayer(double1, double2, double3, double4, predicate11);
    }
    
    default boolean hasNearbyAlivePlayer(final double double1, final double double2, final double double3, final double double4) {
        for (final Player bft11 : this.players()) {
            if (EntitySelector.NO_SPECTATORS.test(bft11)) {
                if (!EntitySelector.LIVING_ENTITY_STILL_ALIVE.test(bft11)) {
                    continue;
                }
                final double double5 = bft11.distanceToSqr(double1, double2, double3);
                if (double4 < 0.0 || double5 < double4 * double4) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    @Nullable
    default Player getNearestPlayer(final TargetingConditions azd, final LivingEntity aqj) {
        return this.<Player>getNearestEntity(this.players(), azd, aqj, aqj.getX(), aqj.getY(), aqj.getZ());
    }
    
    @Nullable
    default Player getNearestPlayer(final TargetingConditions azd, final LivingEntity aqj, final double double3, final double double4, final double double5) {
        return this.<Player>getNearestEntity(this.players(), azd, aqj, double3, double4, double5);
    }
    
    @Nullable
    default Player getNearestPlayer(final TargetingConditions azd, final double double2, final double double3, final double double4) {
        return this.<Player>getNearestEntity(this.players(), azd, (LivingEntity)null, double2, double3, double4);
    }
    
    @Nullable
    default <T extends LivingEntity> T getNearestEntity(final Class<? extends T> class1, final TargetingConditions azd, @Nullable final LivingEntity aqj, final double double4, final double double5, final double double6, final AABB dcf) {
        return this.<T>getNearestEntity(this.getEntitiesOfClass(class1, dcf, (java.util.function.Predicate<? super T>)null), azd, aqj, double4, double5, double6);
    }
    
    @Nullable
    default <T extends LivingEntity> T getNearestLoadedEntity(final Class<? extends T> class1, final TargetingConditions azd, @Nullable final LivingEntity aqj, final double double4, final double double5, final double double6, final AABB dcf) {
        return this.<T>getNearestEntity(this.getLoadedEntitiesOfClass(class1, dcf, (java.util.function.Predicate<? super T>)null), azd, aqj, double4, double5, double6);
    }
    
    @Nullable
    default <T extends LivingEntity> T getNearestEntity(final List<? extends T> list, final TargetingConditions azd, @Nullable final LivingEntity aqj, final double double4, final double double5, final double double6) {
        double double7 = -1.0;
        T aqj2 = null;
        for (final T aqj3 : list) {
            if (!azd.test(aqj, aqj3)) {
                continue;
            }
            final double double8 = aqj3.distanceToSqr(double4, double5, double6);
            if (double7 != -1.0 && double8 >= double7) {
                continue;
            }
            double7 = double8;
            aqj2 = aqj3;
        }
        return aqj2;
    }
    
    default List<Player> getNearbyPlayers(final TargetingConditions azd, final LivingEntity aqj, final AABB dcf) {
        final List<Player> list5 = (List<Player>)Lists.newArrayList();
        for (final Player bft7 : this.players()) {
            if (dcf.contains(bft7.getX(), bft7.getY(), bft7.getZ()) && azd.test(aqj, bft7)) {
                list5.add(bft7);
            }
        }
        return list5;
    }
    
    default <T extends LivingEntity> List<T> getNearbyEntities(final Class<? extends T> class1, final TargetingConditions azd, final LivingEntity aqj, final AABB dcf) {
        final List<T> list6 = this.<T>getEntitiesOfClass(class1, dcf, (java.util.function.Predicate<? super T>)null);
        final List<T> list7 = (List<T>)Lists.newArrayList();
        for (final T aqj2 : list6) {
            if (azd.test(aqj, aqj2)) {
                list7.add(aqj2);
            }
        }
        return list7;
    }
    
    @Nullable
    default Player getPlayerByUUID(final UUID uUID) {
        for (int integer3 = 0; integer3 < this.players().size(); ++integer3) {
            final Player bft4 = (Player)this.players().get(integer3);
            if (uUID.equals(bft4.getUUID())) {
                return bft4;
            }
        }
        return null;
    }
}
