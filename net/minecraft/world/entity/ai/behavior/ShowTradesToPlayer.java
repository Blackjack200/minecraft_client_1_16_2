package net.minecraft.world.entity.ai.behavior;

import java.util.Iterator;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import com.google.common.collect.Lists;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.npc.Villager;

public class ShowTradesToPlayer extends Behavior<Villager> {
    @Nullable
    private ItemStack playerItemStack;
    private final List<ItemStack> displayItems;
    private int cycleCounter;
    private int displayIndex;
    private int lookTime;
    
    public ShowTradesToPlayer(final int integer1, final int integer2) {
        super((Map)ImmutableMap.of(MemoryModuleType.INTERACTION_TARGET, MemoryStatus.VALUE_PRESENT), integer1, integer2);
        this.displayItems = (List<ItemStack>)Lists.newArrayList();
    }
    
    public boolean checkExtraStartConditions(final ServerLevel aag, final Villager bfg) {
        final Brain<?> arc4 = bfg.getBrain();
        if (!arc4.<LivingEntity>getMemory(MemoryModuleType.INTERACTION_TARGET).isPresent()) {
            return false;
        }
        final LivingEntity aqj5 = (LivingEntity)arc4.<LivingEntity>getMemory(MemoryModuleType.INTERACTION_TARGET).get();
        return aqj5.getType() == EntityType.PLAYER && bfg.isAlive() && aqj5.isAlive() && !bfg.isBaby() && bfg.distanceToSqr(aqj5) <= 17.0;
    }
    
    public boolean canStillUse(final ServerLevel aag, final Villager bfg, final long long3) {
        return this.checkExtraStartConditions(aag, bfg) && this.lookTime > 0 && bfg.getBrain().<LivingEntity>getMemory(MemoryModuleType.INTERACTION_TARGET).isPresent();
    }
    
    public void start(final ServerLevel aag, final Villager bfg, final long long3) {
        super.start(aag, bfg, long3);
        this.lookAtTarget(bfg);
        this.cycleCounter = 0;
        this.displayIndex = 0;
        this.lookTime = 40;
    }
    
    public void stop(final ServerLevel aag, final Villager bfg, final long long3) {
        final LivingEntity aqj6 = this.lookAtTarget(bfg);
        this.findItemsToDisplay(aqj6, bfg);
        if (!this.displayItems.isEmpty()) {
            this.displayCyclingItems(bfg);
        }
        else {
            bfg.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            this.lookTime = Math.min(this.lookTime, 40);
        }
        --this.lookTime;
    }
    
    public void stop(final ServerLevel aag, final Villager bfg, final long long3) {
        super.stop(aag, bfg, long3);
        bfg.getBrain().<LivingEntity>eraseMemory(MemoryModuleType.INTERACTION_TARGET);
        bfg.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        this.playerItemStack = null;
    }
    
    private void findItemsToDisplay(final LivingEntity aqj, final Villager bfg) {
        boolean boolean4 = false;
        final ItemStack bly5 = aqj.getMainHandItem();
        if (this.playerItemStack == null || !ItemStack.isSame(this.playerItemStack, bly5)) {
            this.playerItemStack = bly5;
            boolean4 = true;
            this.displayItems.clear();
        }
        if (boolean4 && !this.playerItemStack.isEmpty()) {
            this.updateDisplayItems(bfg);
            if (!this.displayItems.isEmpty()) {
                this.lookTime = 900;
                this.displayFirstItem(bfg);
            }
        }
    }
    
    private void displayFirstItem(final Villager bfg) {
        bfg.setItemSlot(EquipmentSlot.MAINHAND, (ItemStack)this.displayItems.get(0));
    }
    
    private void updateDisplayItems(final Villager bfg) {
        for (final MerchantOffer bqs4 : bfg.getOffers()) {
            if (!bqs4.isOutOfStock() && this.playerItemStackMatchesCostOfOffer(bqs4)) {
                this.displayItems.add(bqs4.getResult());
            }
        }
    }
    
    private boolean playerItemStackMatchesCostOfOffer(final MerchantOffer bqs) {
        return ItemStack.isSame(this.playerItemStack, bqs.getCostA()) || ItemStack.isSame(this.playerItemStack, bqs.getCostB());
    }
    
    private LivingEntity lookAtTarget(final Villager bfg) {
        final Brain<?> arc3 = bfg.getBrain();
        final LivingEntity aqj4 = (LivingEntity)arc3.<LivingEntity>getMemory(MemoryModuleType.INTERACTION_TARGET).get();
        arc3.<EntityTracker>setMemory((MemoryModuleType<EntityTracker>)MemoryModuleType.LOOK_TARGET, new EntityTracker(aqj4, true));
        return aqj4;
    }
    
    private void displayCyclingItems(final Villager bfg) {
        if (this.displayItems.size() >= 2 && ++this.cycleCounter >= 40) {
            ++this.displayIndex;
            this.cycleCounter = 0;
            if (this.displayIndex > this.displayItems.size() - 1) {
                this.displayIndex = 0;
            }
            bfg.setItemSlot(EquipmentSlot.MAINHAND, (ItemStack)this.displayItems.get(this.displayIndex));
        }
    }
}
