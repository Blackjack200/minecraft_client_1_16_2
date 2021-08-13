package net.minecraft.world.entity.npc;

import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.EntityType;
import java.util.Optional;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.BlockPos;
import java.util.function.Predicate;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.GameRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ServerLevelData;
import java.util.Random;
import net.minecraft.world.level.CustomSpawner;

public class WanderingTraderSpawner implements CustomSpawner {
    private final Random random;
    private final ServerLevelData serverLevelData;
    private int tickDelay;
    private int spawnDelay;
    private int spawnChance;
    
    public WanderingTraderSpawner(final ServerLevelData cyj) {
        this.random = new Random();
        this.serverLevelData = cyj;
        this.tickDelay = 1200;
        this.spawnDelay = cyj.getWanderingTraderSpawnDelay();
        this.spawnChance = cyj.getWanderingTraderSpawnChance();
        if (this.spawnDelay == 0 && this.spawnChance == 0) {
            cyj.setWanderingTraderSpawnDelay(this.spawnDelay = 24000);
            cyj.setWanderingTraderSpawnChance(this.spawnChance = 25);
        }
    }
    
    public int tick(final ServerLevel aag, final boolean boolean2, final boolean boolean3) {
        if (!aag.getGameRules().getBoolean(GameRules.RULE_DO_TRADER_SPAWNING)) {
            return 0;
        }
        if (--this.tickDelay > 0) {
            return 0;
        }
        this.tickDelay = 1200;
        this.spawnDelay -= 1200;
        this.serverLevelData.setWanderingTraderSpawnDelay(this.spawnDelay);
        if (this.spawnDelay > 0) {
            return 0;
        }
        this.spawnDelay = 24000;
        if (!aag.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
            return 0;
        }
        final int integer5 = this.spawnChance;
        this.spawnChance = Mth.clamp(this.spawnChance + 25, 25, 75);
        this.serverLevelData.setWanderingTraderSpawnChance(this.spawnChance);
        if (this.random.nextInt(100) > integer5) {
            return 0;
        }
        if (this.spawn(aag)) {
            this.spawnChance = 25;
            return 1;
        }
        return 0;
    }
    
    private boolean spawn(final ServerLevel aag) {
        final Player bft3 = aag.getRandomPlayer();
        if (bft3 == null) {
            return true;
        }
        if (this.random.nextInt(10) != 0) {
            return false;
        }
        final BlockPos fx4 = bft3.blockPosition();
        final int integer5 = 48;
        final PoiManager azl6 = aag.getPoiManager();
        final Optional<BlockPos> optional7 = azl6.find(PoiType.MEETING.getPredicate(), (Predicate<BlockPos>)(fx -> true), fx4, 48, PoiManager.Occupancy.ANY);
        final BlockPos fx5 = (BlockPos)optional7.orElse(fx4);
        final BlockPos fx6 = this.findSpawnPositionNear(aag, fx5, 48);
        if (fx6 != null && this.hasEnoughSpace(aag, fx6)) {
            if (aag.getBiomeName(fx6).equals(Optional.of((Object)Biomes.THE_VOID))) {
                return false;
            }
            final WanderingTrader bfm10 = EntityType.WANDERING_TRADER.spawn(aag, null, null, null, fx6, MobSpawnType.EVENT, false, false);
            if (bfm10 != null) {
                for (int integer6 = 0; integer6 < 2; ++integer6) {
                    this.tryToSpawnLlamaFor(aag, bfm10, 4);
                }
                this.serverLevelData.setWanderingTraderId(bfm10.getUUID());
                bfm10.setDespawnDelay(48000);
                bfm10.setWanderTarget(fx5);
                bfm10.restrictTo(fx5, 16);
                return true;
            }
        }
        return false;
    }
    
    private void tryToSpawnLlamaFor(final ServerLevel aag, final WanderingTrader bfm, final int integer) {
        final BlockPos fx5 = this.findSpawnPositionNear(aag, bfm.blockPosition(), integer);
        if (fx5 == null) {
            return;
        }
        final TraderLlama bbg6 = EntityType.TRADER_LLAMA.spawn(aag, null, null, null, fx5, MobSpawnType.EVENT, false, false);
        if (bbg6 == null) {
            return;
        }
        bbg6.setLeashedTo(bfm, true);
    }
    
    @Nullable
    private BlockPos findSpawnPositionNear(final LevelReader brw, final BlockPos fx, final int integer) {
        BlockPos fx2 = null;
        for (int integer2 = 0; integer2 < 10; ++integer2) {
            final int integer3 = fx.getX() + this.random.nextInt(integer * 2) - integer;
            final int integer4 = fx.getZ() + this.random.nextInt(integer * 2) - integer;
            final int integer5 = brw.getHeight(Heightmap.Types.WORLD_SURFACE, integer3, integer4);
            final BlockPos fx3 = new BlockPos(integer3, integer5, integer4);
            if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, brw, fx3, EntityType.WANDERING_TRADER)) {
                fx2 = fx3;
                break;
            }
        }
        return fx2;
    }
    
    private boolean hasEnoughSpace(final BlockGetter bqz, final BlockPos fx) {
        for (final BlockPos fx2 : BlockPos.betweenClosed(fx, fx.offset(1, 2, 1))) {
            if (!bqz.getBlockState(fx2).getCollisionShape(bqz, fx2).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
