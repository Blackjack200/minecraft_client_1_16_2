package net.minecraft.world.entity.ai.behavior;

import java.util.stream.Stream;
import net.minecraft.world.entity.npc.Villager;
import java.util.UUID;
import java.util.Optional;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.entity.Mob;
import java.util.Comparator;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.Entity;
import java.util.function.Predicate;
import net.minecraft.world.entity.EntityType;
import java.util.List;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.LivingEntity;

public class BehaviorUtils {
    public static void lockGazeAndWalkToEachOther(final LivingEntity aqj1, final LivingEntity aqj2, final float float3) {
        lookAtEachOther(aqj1, aqj2);
        setWalkAndLookTargetMemoriesToEachOther(aqj1, aqj2, float3);
    }
    
    public static boolean entityIsVisible(final Brain<?> arc, final LivingEntity aqj) {
        return arc.<List<LivingEntity>>getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).filter(list -> list.contains(aqj)).isPresent();
    }
    
    public static boolean targetIsValid(final Brain<?> arc, final MemoryModuleType<? extends LivingEntity> aya, final EntityType<?> aqb) {
        return targetIsValid(arc, aya, (Predicate<LivingEntity>)(aqj -> aqj.getType() == aqb));
    }
    
    private static boolean targetIsValid(final Brain<?> arc, final MemoryModuleType<? extends LivingEntity> aya, final Predicate<LivingEntity> predicate) {
        return arc.getMemory(aya).filter((Predicate)predicate).filter(LivingEntity::isAlive).filter(aqj -> entityIsVisible(arc, aqj)).isPresent();
    }
    
    private static void lookAtEachOther(final LivingEntity aqj1, final LivingEntity aqj2) {
        lookAtEntity(aqj1, aqj2);
        lookAtEntity(aqj2, aqj1);
    }
    
    public static void lookAtEntity(final LivingEntity aqj1, final LivingEntity aqj2) {
        aqj1.getBrain().<EntityTracker>setMemory((MemoryModuleType<EntityTracker>)MemoryModuleType.LOOK_TARGET, new EntityTracker(aqj2, true));
    }
    
    private static void setWalkAndLookTargetMemoriesToEachOther(final LivingEntity aqj1, final LivingEntity aqj2, final float float3) {
        final int integer4 = 2;
        setWalkAndLookTargetMemories(aqj1, aqj2, float3, 2);
        setWalkAndLookTargetMemories(aqj2, aqj1, float3, 2);
    }
    
    public static void setWalkAndLookTargetMemories(final LivingEntity aqj, final Entity apx, final float float3, final int integer) {
        final WalkTarget ayc5 = new WalkTarget(new EntityTracker(apx, false), float3, integer);
        aqj.getBrain().<EntityTracker>setMemory((MemoryModuleType<EntityTracker>)MemoryModuleType.LOOK_TARGET, new EntityTracker(apx, true));
        aqj.getBrain().<WalkTarget>setMemory(MemoryModuleType.WALK_TARGET, ayc5);
    }
    
    public static void setWalkAndLookTargetMemories(final LivingEntity aqj, final BlockPos fx, final float float3, final int integer) {
        final WalkTarget ayc5 = new WalkTarget(new BlockPosTracker(fx), float3, integer);
        aqj.getBrain().<BlockPosTracker>setMemory((MemoryModuleType<BlockPosTracker>)MemoryModuleType.LOOK_TARGET, new BlockPosTracker(fx));
        aqj.getBrain().<WalkTarget>setMemory(MemoryModuleType.WALK_TARGET, ayc5);
    }
    
    public static void throwItem(final LivingEntity aqj, final ItemStack bly, final Vec3 dck) {
        final double double4 = aqj.getEyeY() - 0.30000001192092896;
        final ItemEntity bcs6 = new ItemEntity(aqj.level, aqj.getX(), double4, aqj.getZ(), bly);
        final float float7 = 0.3f;
        Vec3 dck2 = dck.subtract(aqj.position());
        dck2 = dck2.normalize().scale(0.30000001192092896);
        bcs6.setDeltaMovement(dck2);
        bcs6.setDefaultPickUpDelay();
        aqj.level.addFreshEntity(bcs6);
    }
    
    public static SectionPos findSectionClosestToVillage(final ServerLevel aag, final SectionPos gp, final int integer) {
        final int integer2 = aag.sectionsToVillage(gp);
        return (SectionPos)SectionPos.cube(gp, integer).filter(gp -> aag.sectionsToVillage(gp) < integer2).min(Comparator.comparingInt(aag::sectionsToVillage)).orElse(gp);
    }
    
    public static boolean isWithinAttackRange(final Mob aqk, final LivingEntity aqj, final int integer) {
        final Item blu4 = aqk.getMainHandItem().getItem();
        if (blu4 instanceof ProjectileWeaponItem && aqk.canFireProjectileWeapon((ProjectileWeaponItem)blu4)) {
            final int integer2 = ((ProjectileWeaponItem)blu4).getDefaultProjectileRange() - integer;
            return aqk.closerThan(aqj, integer2);
        }
        return isWithinMeleeAttackRange(aqk, aqj);
    }
    
    public static boolean isWithinMeleeAttackRange(final LivingEntity aqj1, final LivingEntity aqj2) {
        final double double3 = aqj1.distanceToSqr(aqj2.getX(), aqj2.getY(), aqj2.getZ());
        final double double4 = aqj1.getBbWidth() * 2.0f * (aqj1.getBbWidth() * 2.0f) + aqj2.getBbWidth();
        return double3 <= double4;
    }
    
    public static boolean isOtherTargetMuchFurtherAwayThanCurrentAttackTarget(final LivingEntity aqj1, final LivingEntity aqj2, final double double3) {
        final Optional<LivingEntity> optional5 = aqj1.getBrain().<LivingEntity>getMemory(MemoryModuleType.ATTACK_TARGET);
        if (!optional5.isPresent()) {
            return false;
        }
        final double double4 = aqj1.distanceToSqr(((LivingEntity)optional5.get()).position());
        final double double5 = aqj1.distanceToSqr(aqj2.position());
        return double5 > double4 + double3 * double3;
    }
    
    public static boolean canSee(final LivingEntity aqj1, final LivingEntity aqj2) {
        final Brain<?> arc3 = aqj1.getBrain();
        return arc3.hasMemoryValue(MemoryModuleType.VISIBLE_LIVING_ENTITIES) && ((List)arc3.<List<LivingEntity>>getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).get()).contains(aqj2);
    }
    
    public static LivingEntity getNearestTarget(final LivingEntity aqj1, final Optional<LivingEntity> optional, final LivingEntity aqj3) {
        if (!optional.isPresent()) {
            return aqj3;
        }
        return getTargetNearestMe(aqj1, (LivingEntity)optional.get(), aqj3);
    }
    
    public static LivingEntity getTargetNearestMe(final LivingEntity aqj1, final LivingEntity aqj2, final LivingEntity aqj3) {
        final Vec3 dck4 = aqj2.position();
        final Vec3 dck5 = aqj3.position();
        return (aqj1.distanceToSqr(dck4) < aqj1.distanceToSqr(dck5)) ? aqj2 : aqj3;
    }
    
    public static Optional<LivingEntity> getLivingEntityFromUUIDMemory(final LivingEntity aqj, final MemoryModuleType<UUID> aya) {
        final Optional<UUID> optional3 = aqj.getBrain().<UUID>getMemory(aya);
        return (Optional<LivingEntity>)optional3.map(uUID -> (LivingEntity)((ServerLevel)aqj.level).getEntity(uUID));
    }
    
    public static Stream<Villager> getNearbyVillagersWithCondition(final Villager bfg, final Predicate<Villager> predicate) {
        return (Stream<Villager>)bfg.getBrain().<List<LivingEntity>>getMemory(MemoryModuleType.LIVING_ENTITIES).map(list -> list.stream().filter(aqj -> aqj instanceof Villager && aqj != bfg).map(aqj -> (Villager)aqj).filter(LivingEntity::isAlive).filter(predicate)).orElseGet(Stream::empty);
    }
}
