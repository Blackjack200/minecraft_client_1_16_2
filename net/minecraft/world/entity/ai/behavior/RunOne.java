package net.minecraft.world.entity.ai.behavior;

import java.util.Set;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;

public class RunOne<E extends LivingEntity> extends GateBehavior<E> {
    public RunOne(final List<Pair<Behavior<? super E>, Integer>> list) {
        this((Map)ImmutableMap.of(), list);
    }
    
    public RunOne(final Map<MemoryModuleType<?>, MemoryStatus> map, final List<Pair<Behavior<? super E>, Integer>> list) {
        super(map, (Set)ImmutableSet.of(), OrderPolicy.SHUFFLED, RunningPolicy.RUN_ONE, list);
    }
}
