package net.minecraft.world.entity.animal.horse;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import javax.annotation.Nullable;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class ZombieHorse extends AbstractHorse {
    public ZombieHorse(final EntityType<? extends ZombieHorse> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return AbstractHorse.createBaseHorseAttributes().add(Attributes.MAX_HEALTH, 15.0).add(Attributes.MOVEMENT_SPEED, 0.20000000298023224);
    }
    
    @Override
    protected void randomizeAttributes() {
        this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(this.generateRandomJumpStrength());
    }
    
    public MobType getMobType() {
        return MobType.UNDEAD;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        super.getAmbientSound();
        return SoundEvents.ZOMBIE_HORSE_AMBIENT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        super.getDeathSound();
        return SoundEvents.ZOMBIE_HORSE_DEATH;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        super.getHurtSound(aph);
        return SoundEvents.ZOMBIE_HORSE_HURT;
    }
    
    @Nullable
    @Override
    public AgableMob getBreedOffspring(final ServerLevel aag, final AgableMob apv) {
        return EntityType.ZOMBIE_HORSE.create(aag);
    }
    
    @Override
    public InteractionResult mobInteract(final Player bft, final InteractionHand aoq) {
        final ItemStack bly4 = bft.getItemInHand(aoq);
        if (!this.isTamed()) {
            return InteractionResult.PASS;
        }
        if (this.isBaby()) {
            return super.mobInteract(bft, aoq);
        }
        if (bft.isSecondaryUseActive()) {
            this.openInventory(bft);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        if (this.isVehicle()) {
            return super.mobInteract(bft, aoq);
        }
        if (!bly4.isEmpty()) {
            if (bly4.getItem() == Items.SADDLE && !this.isSaddled()) {
                this.openInventory(bft);
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
            final InteractionResult aor5 = bly4.interactLivingEntity(bft, this, aoq);
            if (aor5.consumesAction()) {
                return aor5;
            }
        }
        this.doPlayerRide(bft);
        return InteractionResult.sidedSuccess(this.level.isClientSide);
    }
    
    @Override
    protected void addBehaviourGoals() {
    }
}
