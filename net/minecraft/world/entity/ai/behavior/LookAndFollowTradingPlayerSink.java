package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;

public class LookAndFollowTradingPlayerSink extends Behavior<Villager> {
    private final float speedModifier;
    
    public LookAndFollowTradingPlayerSink(final float float1) {
        super((Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED), Integer.MAX_VALUE);
        this.speedModifier = float1;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final Villager bfg) {
        final Player bft4 = bfg.getTradingPlayer();
        return bfg.isAlive() && bft4 != null && !bfg.isInWater() && !bfg.hurtMarked && bfg.distanceToSqr(bft4) <= 16.0 && bft4.containerMenu != null;
    }
    
    @Override
    protected boolean canStillUse(final ServerLevel aag, final Villager bfg, final long long3) {
        return this.checkExtraStartConditions(aag, bfg);
    }
    
    @Override
    protected void start(final ServerLevel aag, final Villager bfg, final long long3) {
        this.followPlayer(bfg);
    }
    
    @Override
    protected void stop(final ServerLevel aag, final Villager bfg, final long long3) {
        final Brain<?> arc6 = bfg.getBrain();
        arc6.<WalkTarget>eraseMemory(MemoryModuleType.WALK_TARGET);
        arc6.<PositionTracker>eraseMemory(MemoryModuleType.LOOK_TARGET);
    }
    
    @Override
    protected void tick(final ServerLevel aag, final Villager bfg, final long long3) {
        this.followPlayer(bfg);
    }
    
    @Override
    protected boolean timedOut(final long long1) {
        return false;
    }
    
    private void followPlayer(final Villager bfg) {
        final Brain<?> arc3 = bfg.getBrain();
        arc3.<WalkTarget>setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker(bfg.getTradingPlayer(), false), this.speedModifier, 2));
        arc3.<EntityTracker>setMemory((MemoryModuleType<EntityTracker>)MemoryModuleType.LOOK_TARGET, new EntityTracker(bfg.getTradingPlayer(), true));
    }
}
