package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.Mob;

public class Swim extends Behavior<Mob> {
    private final float chance;
    
    public Swim(final float float1) {
        super((Map)ImmutableMap.of());
        this.chance = float1;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final Mob aqk) {
        return (aqk.isInWater() && aqk.getFluidHeight(FluidTags.WATER) > aqk.getFluidJumpThreshold()) || aqk.isInLava();
    }
    
    @Override
    protected boolean canStillUse(final ServerLevel aag, final Mob aqk, final long long3) {
        return this.checkExtraStartConditions(aag, aqk);
    }
    
    @Override
    protected void tick(final ServerLevel aag, final Mob aqk, final long long3) {
        if (aqk.getRandom().nextFloat() < this.chance) {
            aqk.getJumpControl().jump();
        }
    }
}
