package net.minecraft.world.entity.ai.sensing;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import java.util.function.Predicate;
import java.util.Iterator;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import com.google.common.collect.ImmutableList;
import java.util.List;
import com.google.common.collect.Lists;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.Set;
import net.minecraft.world.entity.LivingEntity;

public class PiglinSpecificSensor extends Sensor<LivingEntity> {
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryModuleType.LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, (Object[])new MemoryModuleType[] { MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, MemoryModuleType.NEAREST_REPELLENT });
    }
    
    @Override
    protected void doTick(final ServerLevel aag, final LivingEntity aqj) {
        final Brain<?> arc4 = aqj.getBrain();
        arc4.<BlockPos>setMemory(MemoryModuleType.NEAREST_REPELLENT, findNearestRepellent(aag, aqj));
        Optional<Mob> optional5 = (Optional<Mob>)Optional.empty();
        Optional<Hoglin> optional6 = (Optional<Hoglin>)Optional.empty();
        Optional<Hoglin> optional7 = (Optional<Hoglin>)Optional.empty();
        Optional<Piglin> optional8 = (Optional<Piglin>)Optional.empty();
        Optional<LivingEntity> optional9 = (Optional<LivingEntity>)Optional.empty();
        Optional<Player> optional10 = (Optional<Player>)Optional.empty();
        Optional<Player> optional11 = (Optional<Player>)Optional.empty();
        int integer12 = 0;
        final List<AbstractPiglin> list13 = (List<AbstractPiglin>)Lists.newArrayList();
        final List<AbstractPiglin> list14 = (List<AbstractPiglin>)Lists.newArrayList();
        final List<LivingEntity> list15 = (List<LivingEntity>)arc4.<List<LivingEntity>>getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).orElse(ImmutableList.of());
        for (final LivingEntity aqj2 : list15) {
            if (aqj2 instanceof Hoglin) {
                final Hoglin bej18 = (Hoglin)aqj2;
                if (bej18.isBaby() && !optional7.isPresent()) {
                    optional7 = (Optional<Hoglin>)Optional.of(bej18);
                }
                else {
                    if (!bej18.isAdult()) {
                        continue;
                    }
                    ++integer12;
                    if (optional6.isPresent() || !bej18.canBeHunted()) {
                        continue;
                    }
                    optional6 = (Optional<Hoglin>)Optional.of(bej18);
                }
            }
            else if (aqj2 instanceof PiglinBrute) {
                list13.add(aqj2);
            }
            else if (aqj2 instanceof Piglin) {
                final Piglin bep18 = (Piglin)aqj2;
                if (bep18.isBaby() && !optional8.isPresent()) {
                    optional8 = (Optional<Piglin>)Optional.of(bep18);
                }
                else {
                    if (!bep18.isAdult()) {
                        continue;
                    }
                    list13.add(bep18);
                }
            }
            else if (aqj2 instanceof Player) {
                final Player bft18 = (Player)aqj2;
                if (!optional10.isPresent() && EntitySelector.ATTACK_ALLOWED.test(aqj2) && !PiglinAi.isWearingGold(bft18)) {
                    optional10 = (Optional<Player>)Optional.of(bft18);
                }
                if (optional11.isPresent() || bft18.isSpectator() || !PiglinAi.isPlayerHoldingLovedItem(bft18)) {
                    continue;
                }
                optional11 = (Optional<Player>)Optional.of(bft18);
            }
            else if (!optional5.isPresent() && (aqj2 instanceof WitherSkeleton || aqj2 instanceof WitherBoss)) {
                optional5 = (Optional<Mob>)Optional.of(aqj2);
            }
            else {
                if (optional9.isPresent() || !PiglinAi.isZombified(aqj2.getType())) {
                    continue;
                }
                optional9 = (Optional<LivingEntity>)Optional.of(aqj2);
            }
        }
        final List<LivingEntity> list16 = (List<LivingEntity>)arc4.<List<LivingEntity>>getMemory(MemoryModuleType.LIVING_ENTITIES).orElse(ImmutableList.of());
        for (final LivingEntity aqj3 : list16) {
            if (aqj3 instanceof AbstractPiglin && ((AbstractPiglin)aqj3).isAdult()) {
                list14.add(aqj3);
            }
        }
        arc4.<Mob>setMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, optional5);
        arc4.<Hoglin>setMemory(MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, optional6);
        arc4.<Hoglin>setMemory(MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, optional7);
        arc4.<LivingEntity>setMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, optional9);
        arc4.<Player>setMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, optional10);
        arc4.<Player>setMemory(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, optional11);
        arc4.<List<AbstractPiglin>>setMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS, list14);
        arc4.<List<AbstractPiglin>>setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, list13);
        arc4.<Integer>setMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, list13.size());
        arc4.<Integer>setMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, integer12);
    }
    
    private static Optional<BlockPos> findNearestRepellent(final ServerLevel aag, final LivingEntity aqj) {
        return BlockPos.findClosestMatch(aqj.blockPosition(), 8, 4, (Predicate<BlockPos>)(fx -> isValidRepellent(aag, fx)));
    }
    
    private static boolean isValidRepellent(final ServerLevel aag, final BlockPos fx) {
        final BlockState cee3 = aag.getBlockState(fx);
        final boolean boolean4 = cee3.is(BlockTags.PIGLIN_REPELLENTS);
        if (boolean4 && cee3.is(Blocks.SOUL_CAMPFIRE)) {
            return CampfireBlock.isLitCampfire(cee3);
        }
        return boolean4;
    }
}
