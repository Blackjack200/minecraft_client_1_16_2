package net.minecraft.world.entity.ai.sensing;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import java.util.List;
import net.minecraft.world.entity.ai.Brain;
import java.util.Optional;
import java.util.Comparator;
import java.util.function.Predicate;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.server.level.ServerLevel;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.Set;
import net.minecraft.world.entity.Mob;

public class NearestItemSensor extends Sensor<Mob> {
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);
    }
    
    @Override
    protected void doTick(final ServerLevel aag, final Mob aqk) {
        final Brain<?> arc4 = aqk.getBrain();
        final List<ItemEntity> list5 = aag.<ItemEntity>getEntitiesOfClass((java.lang.Class<? extends ItemEntity>)ItemEntity.class, aqk.getBoundingBox().inflate(8.0, 4.0, 8.0), (java.util.function.Predicate<? super ItemEntity>)(bcs -> true));
        list5.sort(Comparator.comparingDouble(aqk::distanceToSqr));
        final Optional<ItemEntity> optional6 = (Optional<ItemEntity>)list5.stream().filter(bcs -> aqk.wantsToPickUp(bcs.getItem())).filter(bcs -> bcs.closerThan(aqk, 9.0)).filter(aqk::canSee).findFirst();
        arc4.<ItemEntity>setMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, optional6);
    }
}
