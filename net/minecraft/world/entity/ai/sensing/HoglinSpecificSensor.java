package net.minecraft.world.entity.ai.sensing;

import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import java.util.function.Predicate;
import java.util.Iterator;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.LivingEntity;
import java.util.List;
import com.google.common.collect.Lists;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.Set;
import net.minecraft.world.entity.monster.hoglin.Hoglin;

public class HoglinSpecificSensor extends Sensor<Hoglin> {
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_REPELLENT, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLIN, MemoryModuleType.NEAREST_VISIBLE_ADULT_HOGLINS, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, (Object[])new MemoryModuleType[0]);
    }
    
    @Override
    protected void doTick(final ServerLevel aag, final Hoglin bej) {
        final Brain<?> arc4 = bej.getBrain();
        arc4.<BlockPos>setMemory(MemoryModuleType.NEAREST_REPELLENT, this.findNearestRepellent(aag, bej));
        Optional<Piglin> optional5 = (Optional<Piglin>)Optional.empty();
        int integer6 = 0;
        final List<Hoglin> list7 = (List<Hoglin>)Lists.newArrayList();
        final List<LivingEntity> list8 = (List<LivingEntity>)arc4.<List<LivingEntity>>getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).orElse(Lists.newArrayList());
        for (final LivingEntity aqj10 : list8) {
            if (aqj10 instanceof Piglin && !aqj10.isBaby()) {
                ++integer6;
                if (!optional5.isPresent()) {
                    optional5 = (Optional<Piglin>)Optional.of(aqj10);
                }
            }
            if (aqj10 instanceof Hoglin && !aqj10.isBaby()) {
                list7.add(aqj10);
            }
        }
        arc4.<AbstractPiglin>setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLIN, optional5);
        arc4.<List<Hoglin>>setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_HOGLINS, list7);
        arc4.<Integer>setMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, integer6);
        arc4.<Integer>setMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, list7.size());
    }
    
    private Optional<BlockPos> findNearestRepellent(final ServerLevel aag, final Hoglin bej) {
        return BlockPos.findClosestMatch(bej.blockPosition(), 8, 4, (Predicate<BlockPos>)(fx -> aag.getBlockState(fx).is(BlockTags.HOGLIN_REPELLENTS)));
    }
}
