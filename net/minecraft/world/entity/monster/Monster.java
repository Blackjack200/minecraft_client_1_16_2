package net.minecraft.world.entity.monster;

import java.util.function.Predicate;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LightLayer;
import java.util.Random;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;

public abstract class Monster extends PathfinderMob implements Enemy {
    protected Monster(final EntityType<? extends Monster> aqb, final Level bru) {
        super(aqb, bru);
        this.xpReward = 5;
    }
    
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }
    
    @Override
    public void aiStep() {
        this.updateSwingTime();
        this.updateNoActionTime();
        super.aiStep();
    }
    
    protected void updateNoActionTime() {
        final float float2 = this.getBrightness();
        if (float2 > 0.5f) {
            this.noActionTime += 2;
        }
    }
    
    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }
    
    protected SoundEvent getSwimSound() {
        return SoundEvents.HOSTILE_SWIM;
    }
    
    protected SoundEvent getSwimSplashSound() {
        return SoundEvents.HOSTILE_SPLASH;
    }
    
    public boolean hurt(final DamageSource aph, final float float2) {
        return !this.isInvulnerableTo(aph) && super.hurt(aph, float2);
    }
    
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.HOSTILE_HURT;
    }
    
    protected SoundEvent getDeathSound() {
        return SoundEvents.HOSTILE_DEATH;
    }
    
    protected SoundEvent getFallDamageSound(final int integer) {
        if (integer > 4) {
            return SoundEvents.HOSTILE_BIG_FALL;
        }
        return SoundEvents.HOSTILE_SMALL_FALL;
    }
    
    @Override
    public float getWalkTargetValue(final BlockPos fx, final LevelReader brw) {
        return 0.5f - brw.getBrightness(fx);
    }
    
    public static boolean isDarkEnoughToSpawn(final ServerLevelAccessor bsh, final BlockPos fx, final Random random) {
        if (bsh.getBrightness(LightLayer.SKY, fx) > random.nextInt(32)) {
            return false;
        }
        final int integer4 = bsh.getLevel().isThundering() ? bsh.getMaxLocalRawBrightness(fx, 10) : bsh.getMaxLocalRawBrightness(fx);
        return integer4 <= random.nextInt(8);
    }
    
    public static boolean checkMonsterSpawnRules(final EntityType<? extends Monster> aqb, final ServerLevelAccessor bsh, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        return bsh.getDifficulty() != Difficulty.PEACEFUL && isDarkEnoughToSpawn(bsh, fx, random) && Mob.checkMobSpawnRules(aqb, bsh, aqm, fx, random);
    }
    
    public static boolean checkAnyLightMonsterSpawnRules(final EntityType<? extends Monster> aqb, final LevelAccessor brv, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        return brv.getDifficulty() != Difficulty.PEACEFUL && Mob.checkMobSpawnRules(aqb, brv, aqm, fx, random);
    }
    
    public static AttributeSupplier.Builder createMonsterAttributes() {
        return Mob.createMobAttributes().add(Attributes.ATTACK_DAMAGE);
    }
    
    protected boolean shouldDropExperience() {
        return true;
    }
    
    protected boolean shouldDropLoot() {
        return true;
    }
    
    public boolean isPreventingPlayerRest(final Player bft) {
        return true;
    }
    
    public ItemStack getProjectile(final ItemStack bly) {
        if (bly.getItem() instanceof ProjectileWeaponItem) {
            final Predicate<ItemStack> predicate3 = ((ProjectileWeaponItem)bly.getItem()).getSupportedHeldProjectiles();
            final ItemStack bly2 = ProjectileWeaponItem.getHeldProjectile(this, predicate3);
            return bly2.isEmpty() ? new ItemStack(Items.ARROW) : bly2;
        }
        return ItemStack.EMPTY;
    }
}
