package net.minecraft.world.entity.ai.behavior;

import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.common.collect.Maps;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import java.util.HashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.effect.MobEffects;
import java.util.Optional;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootContext;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerProfession;
import java.util.Map;
import net.minecraft.world.entity.npc.Villager;

public class GiveGiftToHero extends Behavior<Villager> {
    private static final Map<VillagerProfession, ResourceLocation> gifts;
    private int timeUntilNextGift;
    private boolean giftGivenDuringThisRun;
    private long timeSinceStart;
    
    public GiveGiftToHero(final int integer) {
        super((Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.INTERACTION_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryStatus.VALUE_PRESENT), integer);
        this.timeUntilNextGift = 600;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final Villager bfg) {
        if (!this.isHeroVisible(bfg)) {
            return false;
        }
        if (this.timeUntilNextGift > 0) {
            --this.timeUntilNextGift;
            return false;
        }
        return true;
    }
    
    @Override
    protected void start(final ServerLevel aag, final Villager bfg, final long long3) {
        this.giftGivenDuringThisRun = false;
        this.timeSinceStart = long3;
        final Player bft6 = (Player)this.getNearestTargetableHero(bfg).get();
        bfg.getBrain().<LivingEntity>setMemory(MemoryModuleType.INTERACTION_TARGET, bft6);
        BehaviorUtils.lookAtEntity(bfg, bft6);
    }
    
    @Override
    protected boolean canStillUse(final ServerLevel aag, final Villager bfg, final long long3) {
        return this.isHeroVisible(bfg) && !this.giftGivenDuringThisRun;
    }
    
    @Override
    protected void stop(final ServerLevel aag, final Villager bfg, final long long3) {
        final Player bft6 = (Player)this.getNearestTargetableHero(bfg).get();
        BehaviorUtils.lookAtEntity(bfg, bft6);
        if (this.isWithinThrowingDistance(bfg, bft6)) {
            if (long3 - this.timeSinceStart > 20L) {
                this.throwGift(bfg, bft6);
                this.giftGivenDuringThisRun = true;
            }
        }
        else {
            BehaviorUtils.setWalkAndLookTargetMemories(bfg, bft6, 0.5f, 5);
        }
    }
    
    @Override
    protected void stop(final ServerLevel aag, final Villager bfg, final long long3) {
        this.timeUntilNextGift = calculateTimeUntilNextGift(aag);
        bfg.getBrain().<LivingEntity>eraseMemory(MemoryModuleType.INTERACTION_TARGET);
        bfg.getBrain().<WalkTarget>eraseMemory(MemoryModuleType.WALK_TARGET);
        bfg.getBrain().<PositionTracker>eraseMemory(MemoryModuleType.LOOK_TARGET);
    }
    
    private void throwGift(final Villager bfg, final LivingEntity aqj) {
        final List<ItemStack> list4 = this.getItemToThrow(bfg);
        for (final ItemStack bly6 : list4) {
            BehaviorUtils.throwItem(bfg, bly6, aqj.position());
        }
    }
    
    private List<ItemStack> getItemToThrow(final Villager bfg) {
        if (bfg.isBaby()) {
            return (List<ItemStack>)ImmutableList.of(new ItemStack(Items.POPPY));
        }
        final VillagerProfession bfj3 = bfg.getVillagerData().getProfession();
        if (GiveGiftToHero.gifts.containsKey(bfj3)) {
            final LootTable cyv4 = bfg.level.getServer().getLootTables().get((ResourceLocation)GiveGiftToHero.gifts.get(bfj3));
            final LootContext.Builder a5 = new LootContext.Builder((ServerLevel)bfg.level).<Vec3>withParameter(LootContextParams.ORIGIN, bfg.position()).<Entity>withParameter(LootContextParams.THIS_ENTITY, bfg).withRandom(bfg.getRandom());
            return cyv4.getRandomItems(a5.create(LootContextParamSets.GIFT));
        }
        return (List<ItemStack>)ImmutableList.of(new ItemStack(Items.WHEAT_SEEDS));
    }
    
    private boolean isHeroVisible(final Villager bfg) {
        return this.getNearestTargetableHero(bfg).isPresent();
    }
    
    private Optional<Player> getNearestTargetableHero(final Villager bfg) {
        return (Optional<Player>)bfg.getBrain().<Player>getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER).filter(this::isHero);
    }
    
    private boolean isHero(final Player bft) {
        return bft.hasEffect(MobEffects.HERO_OF_THE_VILLAGE);
    }
    
    private boolean isWithinThrowingDistance(final Villager bfg, final Player bft) {
        final BlockPos fx4 = bft.blockPosition();
        final BlockPos fx5 = bfg.blockPosition();
        return fx5.closerThan(fx4, 5.0);
    }
    
    private static int calculateTimeUntilNextGift(final ServerLevel aag) {
        return 600 + aag.random.nextInt(6001);
    }
    
    static {
        gifts = Util.<Map>make((Map)Maps.newHashMap(), (java.util.function.Consumer<Map>)(hashMap -> {
            hashMap.put(VillagerProfession.ARMORER, BuiltInLootTables.ARMORER_GIFT);
            hashMap.put(VillagerProfession.BUTCHER, BuiltInLootTables.BUTCHER_GIFT);
            hashMap.put(VillagerProfession.CARTOGRAPHER, BuiltInLootTables.CARTOGRAPHER_GIFT);
            hashMap.put(VillagerProfession.CLERIC, BuiltInLootTables.CLERIC_GIFT);
            hashMap.put(VillagerProfession.FARMER, BuiltInLootTables.FARMER_GIFT);
            hashMap.put(VillagerProfession.FISHERMAN, BuiltInLootTables.FISHERMAN_GIFT);
            hashMap.put(VillagerProfession.FLETCHER, BuiltInLootTables.FLETCHER_GIFT);
            hashMap.put(VillagerProfession.LEATHERWORKER, BuiltInLootTables.LEATHERWORKER_GIFT);
            hashMap.put(VillagerProfession.LIBRARIAN, BuiltInLootTables.LIBRARIAN_GIFT);
            hashMap.put(VillagerProfession.MASON, BuiltInLootTables.MASON_GIFT);
            hashMap.put(VillagerProfession.SHEPHERD, BuiltInLootTables.SHEPHERD_GIFT);
            hashMap.put(VillagerProfession.TOOLSMITH, BuiltInLootTables.TOOLSMITH_GIFT);
            hashMap.put(VillagerProfession.WEAPONSMITH, BuiltInLootTables.WEAPONSMITH_GIFT);
        }));
    }
}
