package net.minecraft.world.entity.monster.piglin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.behavior.Behavior;

public class StopHoldingItemIfNoLongerAdmiring<E extends Piglin> extends Behavior<E> {
    public StopHoldingItemIfNoLongerAdmiring() {
        super((Map)ImmutableMap.of(MemoryModuleType.ADMIRING_ITEM, MemoryStatus.VALUE_ABSENT));
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final E bep) {
        return !bep.getOffhandItem().isEmpty() && bep.getOffhandItem().getItem() != Items.SHIELD;
    }
    
    @Override
    protected void start(final ServerLevel aag, final E bep, final long long3) {
        PiglinAi.stopHoldingOffHandItem(bep, true);
    }
}
