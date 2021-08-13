package net.minecraft.world.entity.monster;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class CaveSpider extends Spider {
    public CaveSpider(final EntityType<? extends CaveSpider> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    public static AttributeSupplier.Builder createCaveSpider() {
        return Spider.createAttributes().add(Attributes.MAX_HEALTH, 12.0);
    }
    
    @Override
    public boolean doHurtTarget(final Entity apx) {
        if (super.doHurtTarget(apx)) {
            if (apx instanceof LivingEntity) {
                int integer3 = 0;
                if (this.level.getDifficulty() == Difficulty.NORMAL) {
                    integer3 = 7;
                }
                else if (this.level.getDifficulty() == Difficulty.HARD) {
                    integer3 = 15;
                }
                if (integer3 > 0) {
                    ((LivingEntity)apx).addEffect(new MobEffectInstance(MobEffects.POISON, integer3 * 20, 0));
                }
            }
            return true;
        }
        return false;
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable final SpawnGroupData aqz, @Nullable final CompoundTag md) {
        return aqz;
    }
    
    @Override
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return 0.45f;
    }
}
