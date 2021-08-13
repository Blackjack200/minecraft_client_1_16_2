package net.minecraft.world.entity.ai.behavior;

import javax.annotation.Nullable;
import java.util.List;
import com.google.common.collect.Maps;
import java.util.Comparator;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.util.RandomPos;
import java.util.Optional;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.PathfinderMob;

public class PlayTagWithOtherKids extends Behavior<PathfinderMob> {
    public PlayTagWithOtherKids() {
        super((Map)ImmutableMap.of(MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryStatus.VALUE_PRESENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.INTERACTION_TARGET, MemoryStatus.REGISTERED));
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final PathfinderMob aqr) {
        return aag.getRandom().nextInt(10) == 0 && this.hasFriendsNearby(aqr);
    }
    
    @Override
    protected void start(final ServerLevel aag, final PathfinderMob aqr, final long long3) {
        final LivingEntity aqj6 = this.seeIfSomeoneIsChasingMe(aqr);
        if (aqj6 != null) {
            this.fleeFromChaser(aag, aqr, aqj6);
            return;
        }
        final Optional<LivingEntity> optional7 = this.findSomeoneBeingChased(aqr);
        if (optional7.isPresent()) {
            chaseKid(aqr, (LivingEntity)optional7.get());
            return;
        }
        this.findSomeoneToChase(aqr).ifPresent(aqj -> chaseKid(aqr, aqj));
    }
    
    private void fleeFromChaser(final ServerLevel aag, final PathfinderMob aqr, final LivingEntity aqj) {
        for (int integer5 = 0; integer5 < 10; ++integer5) {
            final Vec3 dck6 = RandomPos.getLandPos(aqr, 20, 8);
            if (dck6 != null && aag.isVillage(new BlockPos(dck6))) {
                aqr.getBrain().<WalkTarget>setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(dck6, 0.6f, 0));
                return;
            }
        }
    }
    
    private static void chaseKid(final PathfinderMob aqr, final LivingEntity aqj) {
        final Brain<?> arc3 = aqr.getBrain();
        arc3.<LivingEntity>setMemory(MemoryModuleType.INTERACTION_TARGET, aqj);
        arc3.<EntityTracker>setMemory((MemoryModuleType<EntityTracker>)MemoryModuleType.LOOK_TARGET, new EntityTracker(aqj, true));
        arc3.<WalkTarget>setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker(aqj, false), 0.6f, 1));
    }
    
    private Optional<LivingEntity> findSomeoneToChase(final PathfinderMob aqr) {
        return (Optional<LivingEntity>)this.getFriendsNearby(aqr).stream().findAny();
    }
    
    private Optional<LivingEntity> findSomeoneBeingChased(final PathfinderMob aqr) {
        final Map<LivingEntity, Integer> map3 = this.checkHowManyChasersEachFriendHas(aqr);
        return (Optional<LivingEntity>)map3.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getValue)).filter(entry -> (int)entry.getValue() > 0 && (int)entry.getValue() <= 5).map(Map.Entry::getKey).findFirst();
    }
    
    private Map<LivingEntity, Integer> checkHowManyChasersEachFriendHas(final PathfinderMob aqr) {
        final Map<LivingEntity, Integer> map3 = (Map<LivingEntity, Integer>)Maps.newHashMap();
        this.getFriendsNearby(aqr).stream().filter(this::isChasingSomeone).forEach(aqj -> {
            final Integer n = (Integer)map3.compute(this.whoAreYouChasing(aqj), (aqj, integer) -> (integer == null) ? 1 : (integer + 1));
        });
        return map3;
    }
    
    private List<LivingEntity> getFriendsNearby(final PathfinderMob aqr) {
        return (List<LivingEntity>)aqr.getBrain().<List<LivingEntity>>getMemory(MemoryModuleType.VISIBLE_VILLAGER_BABIES).get();
    }
    
    private LivingEntity whoAreYouChasing(final LivingEntity aqj) {
        return (LivingEntity)aqj.getBrain().<LivingEntity>getMemory(MemoryModuleType.INTERACTION_TARGET).get();
    }
    
    @Nullable
    private LivingEntity seeIfSomeoneIsChasingMe(final LivingEntity aqj) {
        return (LivingEntity)((List)aqj.getBrain().<List<LivingEntity>>getMemory(MemoryModuleType.VISIBLE_VILLAGER_BABIES).get()).stream().filter(aqj2 -> this.isFriendChasingMe(aqj, aqj2)).findAny().orElse(null);
    }
    
    private boolean isChasingSomeone(final LivingEntity aqj) {
        return aqj.getBrain().<LivingEntity>getMemory(MemoryModuleType.INTERACTION_TARGET).isPresent();
    }
    
    private boolean isFriendChasingMe(final LivingEntity aqj1, final LivingEntity aqj2) {
        return aqj2.getBrain().<LivingEntity>getMemory(MemoryModuleType.INTERACTION_TARGET).filter(aqj2 -> aqj2 == aqj1).isPresent();
    }
    
    private boolean hasFriendsNearby(final PathfinderMob aqr) {
        return aqr.getBrain().hasMemoryValue(MemoryModuleType.VISIBLE_VILLAGER_BABIES);
    }
}
