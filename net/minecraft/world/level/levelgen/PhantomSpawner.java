package net.minecraft.world.level.levelgen;

import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.core.BlockPos;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.entity.EntityType;
import net.minecraft.util.Mth;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.CustomSpawner;

public class PhantomSpawner implements CustomSpawner {
    private int nextTick;
    
    public int tick(final ServerLevel aag, final boolean boolean2, final boolean boolean3) {
        if (!boolean2) {
            return 0;
        }
        if (!aag.getGameRules().getBoolean(GameRules.RULE_DOINSOMNIA)) {
            return 0;
        }
        final Random random5 = aag.random;
        --this.nextTick;
        if (this.nextTick > 0) {
            return 0;
        }
        this.nextTick += (60 + random5.nextInt(60)) * 20;
        if (aag.getSkyDarken() < 5 && aag.dimensionType().hasSkyLight()) {
            return 0;
        }
        int integer6 = 0;
        for (final Player bft8 : aag.players()) {
            if (bft8.isSpectator()) {
                continue;
            }
            final BlockPos fx9 = bft8.blockPosition();
            if (aag.dimensionType().hasSkyLight()) {
                if (fx9.getY() < aag.getSeaLevel()) {
                    continue;
                }
                if (!aag.canSeeSky(fx9)) {
                    continue;
                }
            }
            final DifficultyInstance aop10 = aag.getCurrentDifficultyAt(fx9);
            if (!aop10.isHarderThan(random5.nextFloat() * 3.0f)) {
                continue;
            }
            final ServerStatsCounter adu11 = ((ServerPlayer)bft8).getStats();
            final int integer7 = Mth.clamp(adu11.getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_REST)), 1, Integer.MAX_VALUE);
            final int integer8 = 24000;
            if (random5.nextInt(integer7) < 72000) {
                continue;
            }
            final BlockPos fx10 = fx9.above(20 + random5.nextInt(15)).east(-10 + random5.nextInt(21)).south(-10 + random5.nextInt(21));
            final BlockState cee15 = aag.getBlockState(fx10);
            final FluidState cuu16 = aag.getFluidState(fx10);
            if (!NaturalSpawner.isValidEmptySpawnBlock(aag, fx10, cee15, cuu16, EntityType.PHANTOM)) {
                continue;
            }
            SpawnGroupData aqz17 = null;
            final int integer9 = 1 + random5.nextInt(aop10.getDifficulty().getId() + 1);
            for (int integer10 = 0; integer10 < integer9; ++integer10) {
                final Phantom bdp20 = EntityType.PHANTOM.create(aag);
                bdp20.moveTo(fx10, 0.0f, 0.0f);
                aqz17 = bdp20.finalizeSpawn(aag, aop10, MobSpawnType.NATURAL, aqz17, null);
                aag.addFreshEntityWithPassengers(bdp20);
            }
            integer6 += integer9;
        }
        return integer6;
    }
}
