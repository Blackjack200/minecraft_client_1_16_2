package net.minecraft.world.entity;

import javax.annotation.Nullable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.Container;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import com.google.common.base.Predicates;
import net.minecraft.world.scores.Team;
import java.util.function.Predicate;

public final class EntitySelector {
    public static final Predicate<Entity> ENTITY_STILL_ALIVE;
    public static final Predicate<LivingEntity> LIVING_ENTITY_STILL_ALIVE;
    public static final Predicate<Entity> ENTITY_NOT_BEING_RIDDEN;
    public static final Predicate<Entity> CONTAINER_ENTITY_SELECTOR;
    public static final Predicate<Entity> NO_CREATIVE_OR_SPECTATOR;
    public static final Predicate<Entity> ATTACK_ALLOWED;
    public static final Predicate<Entity> NO_SPECTATORS;
    
    public static Predicate<Entity> withinDistance(final double double1, final double double2, final double double3, final double double4) {
        final double double5 = double4 * double4;
        return (Predicate<Entity>)(apx -> apx != null && apx.distanceToSqr(double1, double2, double3) <= double5);
    }
    
    public static Predicate<Entity> pushableBy(final Entity apx) {
        final Team ddm2 = apx.getTeam();
        final Team.CollisionRule a3 = (ddm2 == null) ? Team.CollisionRule.ALWAYS : ddm2.getCollisionRule();
        if (a3 == Team.CollisionRule.NEVER) {
            return (Predicate<Entity>)Predicates.alwaysFalse();
        }
        return (Predicate<Entity>)EntitySelector.NO_SPECTATORS.and(apx4 -> {
            if (!apx4.isPushable()) {
                return false;
            }
            if (apx.level.isClientSide && (!(apx4 instanceof Player) || !((Player)apx4).isLocalPlayer())) {
                return false;
            }
            final Team ddm2 = apx4.getTeam();
            final Team.CollisionRule a2 = (ddm2 == null) ? Team.CollisionRule.ALWAYS : ddm2.getCollisionRule();
            if (a2 == Team.CollisionRule.NEVER) {
                return false;
            }
            final boolean boolean7 = ddm2 != null && ddm2.isAlliedTo(ddm2);
            return ((a3 != Team.CollisionRule.PUSH_OWN_TEAM && a2 != Team.CollisionRule.PUSH_OWN_TEAM) || !boolean7) && ((a3 != Team.CollisionRule.PUSH_OTHER_TEAMS && a2 != Team.CollisionRule.PUSH_OTHER_TEAMS) || boolean7);
        });
    }
    
    public static Predicate<Entity> notRiding(final Entity apx) {
        return (Predicate<Entity>)(apx2 -> {
            while (apx2.isPassenger()) {
                apx2 = apx2.getVehicle();
                if (apx2 == apx) {
                    return false;
                }
            }
            return true;
        });
    }
    
    static {
        ENTITY_STILL_ALIVE = Entity::isAlive;
        LIVING_ENTITY_STILL_ALIVE = LivingEntity::isAlive;
        ENTITY_NOT_BEING_RIDDEN = (apx -> apx.isAlive() && !apx.isVehicle() && !apx.isPassenger());
        CONTAINER_ENTITY_SELECTOR = (apx -> apx instanceof Container && apx.isAlive());
        NO_CREATIVE_OR_SPECTATOR = (apx -> !(apx instanceof Player) || (!apx.isSpectator() && !((Player)apx).isCreative()));
        ATTACK_ALLOWED = (apx -> !(apx instanceof Player) || (!apx.isSpectator() && !((Player)apx).isCreative() && apx.level.getDifficulty() != Difficulty.PEACEFUL));
        NO_SPECTATORS = (apx -> !apx.isSpectator());
    }
    
    public static class MobCanWearArmorEntitySelector implements Predicate<Entity> {
        private final ItemStack itemStack;
        
        public MobCanWearArmorEntitySelector(final ItemStack bly) {
            this.itemStack = bly;
        }
        
        public boolean test(@Nullable final Entity apx) {
            if (!apx.isAlive()) {
                return false;
            }
            if (!(apx instanceof LivingEntity)) {
                return false;
            }
            final LivingEntity aqj3 = (LivingEntity)apx;
            return aqj3.canTakeItem(this.itemStack);
        }
    }
}
