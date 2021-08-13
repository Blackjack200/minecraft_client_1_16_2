package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import java.util.stream.Collectors;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.EntityType;
import net.minecraft.server.level.ServerLevel;
import com.google.common.collect.ImmutableSet;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.Item;
import java.util.Set;
import net.minecraft.world.entity.npc.Villager;

public class TradeWithVillager extends Behavior<Villager> {
    private Set<Item> trades;
    
    public TradeWithVillager() {
        super((Map)ImmutableMap.of(MemoryModuleType.INTERACTION_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT));
        this.trades = (Set<Item>)ImmutableSet.of();
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final Villager bfg) {
        return BehaviorUtils.targetIsValid(bfg.getBrain(), MemoryModuleType.INTERACTION_TARGET, EntityType.VILLAGER);
    }
    
    @Override
    protected boolean canStillUse(final ServerLevel aag, final Villager bfg, final long long3) {
        return this.checkExtraStartConditions(aag, bfg);
    }
    
    @Override
    protected void start(final ServerLevel aag, final Villager bfg, final long long3) {
        final Villager bfg2 = (Villager)bfg.getBrain().<LivingEntity>getMemory(MemoryModuleType.INTERACTION_TARGET).get();
        BehaviorUtils.lockGazeAndWalkToEachOther(bfg, bfg2, 0.5f);
        this.trades = figureOutWhatIAmWillingToTrade(bfg, bfg2);
    }
    
    @Override
    protected void stop(final ServerLevel aag, final Villager bfg, final long long3) {
        final Villager bfg2 = (Villager)bfg.getBrain().<LivingEntity>getMemory(MemoryModuleType.INTERACTION_TARGET).get();
        if (bfg.distanceToSqr(bfg2) > 5.0) {
            return;
        }
        BehaviorUtils.lockGazeAndWalkToEachOther(bfg, bfg2, 0.5f);
        bfg.gossip(aag, bfg2, long3);
        if (bfg.hasExcessFood() && (bfg.getVillagerData().getProfession() == VillagerProfession.FARMER || bfg2.wantsMoreFood())) {
            throwHalfStack(bfg, (Set<Item>)Villager.FOOD_POINTS.keySet(), bfg2);
        }
        if (bfg2.getVillagerData().getProfession() == VillagerProfession.FARMER && bfg.getInventory().countItem(Items.WHEAT) > Items.WHEAT.getMaxStackSize() / 2) {
            throwHalfStack(bfg, (Set<Item>)ImmutableSet.of(Items.WHEAT), bfg2);
        }
        if (!this.trades.isEmpty() && bfg.getInventory().hasAnyOf(this.trades)) {
            throwHalfStack(bfg, this.trades, bfg2);
        }
    }
    
    @Override
    protected void stop(final ServerLevel aag, final Villager bfg, final long long3) {
        bfg.getBrain().<LivingEntity>eraseMemory(MemoryModuleType.INTERACTION_TARGET);
    }
    
    private static Set<Item> figureOutWhatIAmWillingToTrade(final Villager bfg1, final Villager bfg2) {
        final ImmutableSet<Item> immutableSet3 = bfg2.getVillagerData().getProfession().getRequestedItems();
        final ImmutableSet<Item> immutableSet4 = bfg1.getVillagerData().getProfession().getRequestedItems();
        return (Set<Item>)immutableSet3.stream().filter(blu -> !immutableSet4.contains(blu)).collect(Collectors.toSet());
    }
    
    private static void throwHalfStack(final Villager bfg, final Set<Item> set, final LivingEntity aqj) {
        final SimpleContainer aox4 = bfg.getInventory();
        ItemStack bly5 = ItemStack.EMPTY;
        for (int integer6 = 0; integer6 < aox4.getContainerSize(); ++integer6) {
            final ItemStack bly6 = aox4.getItem(integer6);
            if (!bly6.isEmpty()) {
                final Item blu8 = bly6.getItem();
                if (set.contains(blu8)) {
                    int integer7;
                    if (bly6.getCount() > bly6.getMaxStackSize() / 2) {
                        integer7 = bly6.getCount() / 2;
                    }
                    else {
                        if (bly6.getCount() <= 24) {
                            continue;
                        }
                        integer7 = bly6.getCount() - 24;
                    }
                    bly6.shrink(integer7);
                    bly5 = new ItemStack(blu8, integer7);
                    break;
                }
            }
        }
        if (!bly5.isEmpty()) {
            BehaviorUtils.throwItem(bfg, bly5, aqj.position());
        }
    }
}
