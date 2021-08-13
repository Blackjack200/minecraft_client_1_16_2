package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.item.Items;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.LivingEntity;

public class CrossbowAttack<E extends Mob, T extends LivingEntity> extends Behavior<E> {
    private int attackDelay;
    private CrossbowState crossbowState;
    
    public CrossbowAttack() {
        super((Map)ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), 1200);
        this.crossbowState = CrossbowState.UNCHARGED;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final E aqk) {
        final LivingEntity aqj4 = getAttackTarget((LivingEntity)aqk);
        return ((LivingEntity)aqk).isHolding(Items.CROSSBOW) && BehaviorUtils.canSee((LivingEntity)aqk, aqj4) && BehaviorUtils.isWithinAttackRange((Mob)aqk, aqj4, 0);
    }
    
    @Override
    protected boolean canStillUse(final ServerLevel aag, final E aqk, final long long3) {
        return ((LivingEntity)aqk).getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && this.checkExtraStartConditions(aag, aqk);
    }
    
    @Override
    protected void tick(final ServerLevel aag, final E aqk, final long long3) {
        final LivingEntity aqj6 = getAttackTarget((LivingEntity)aqk);
        this.lookAtTarget((Mob)aqk, aqj6);
        this.crossbowAttack(aqk, aqj6);
    }
    
    @Override
    protected void stop(final ServerLevel aag, final E aqk, final long long3) {
        if (((LivingEntity)aqk).isUsingItem()) {
            ((LivingEntity)aqk).stopUsingItem();
        }
        if (((LivingEntity)aqk).isHolding(Items.CROSSBOW)) {
            ((CrossbowAttackMob)aqk).setChargingCrossbow(false);
            CrossbowItem.setCharged(((LivingEntity)aqk).getUseItem(), false);
        }
    }
    
    private void crossbowAttack(final E aqk, final LivingEntity aqj) {
        if (this.crossbowState == CrossbowState.UNCHARGED) {
            ((LivingEntity)aqk).startUsingItem(ProjectileUtil.getWeaponHoldingHand((LivingEntity)aqk, Items.CROSSBOW));
            this.crossbowState = CrossbowState.CHARGING;
            ((CrossbowAttackMob)aqk).setChargingCrossbow(true);
        }
        else if (this.crossbowState == CrossbowState.CHARGING) {
            if (!((LivingEntity)aqk).isUsingItem()) {
                this.crossbowState = CrossbowState.UNCHARGED;
            }
            final int integer4 = ((LivingEntity)aqk).getTicksUsingItem();
            final ItemStack bly5 = ((LivingEntity)aqk).getUseItem();
            if (integer4 >= CrossbowItem.getChargeDuration(bly5)) {
                ((LivingEntity)aqk).releaseUsingItem();
                this.crossbowState = CrossbowState.CHARGED;
                this.attackDelay = 20 + ((LivingEntity)aqk).getRandom().nextInt(20);
                ((CrossbowAttackMob)aqk).setChargingCrossbow(false);
            }
        }
        else if (this.crossbowState == CrossbowState.CHARGED) {
            --this.attackDelay;
            if (this.attackDelay == 0) {
                this.crossbowState = CrossbowState.READY_TO_ATTACK;
            }
        }
        else if (this.crossbowState == CrossbowState.READY_TO_ATTACK) {
            ((RangedAttackMob)aqk).performRangedAttack(aqj, 1.0f);
            final ItemStack bly6 = ((LivingEntity)aqk).getItemInHand(ProjectileUtil.getWeaponHoldingHand((LivingEntity)aqk, Items.CROSSBOW));
            CrossbowItem.setCharged(bly6, false);
            this.crossbowState = CrossbowState.UNCHARGED;
        }
    }
    
    private void lookAtTarget(final Mob aqk, final LivingEntity aqj) {
        aqk.getBrain().<EntityTracker>setMemory((MemoryModuleType<EntityTracker>)MemoryModuleType.LOOK_TARGET, new EntityTracker(aqj, true));
    }
    
    private static LivingEntity getAttackTarget(final LivingEntity aqj) {
        return (LivingEntity)aqj.getBrain().<LivingEntity>getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }
    
    enum CrossbowState {
        UNCHARGED, 
        CHARGING, 
        CHARGED, 
        READY_TO_ATTACK;
    }
}
