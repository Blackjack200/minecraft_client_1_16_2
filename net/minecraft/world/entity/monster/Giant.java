package net.minecraft.world.entity.monster;

import net.minecraft.world.level.LevelReader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class Giant extends Monster {
    public Giant(final EntityType<? extends Giant> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return 10.440001f;
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 100.0).add(Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.ATTACK_DAMAGE, 50.0);
    }
    
    @Override
    public float getWalkTargetValue(final BlockPos fx, final LevelReader brw) {
        return brw.getBrightness(fx) - 0.5f;
    }
}
