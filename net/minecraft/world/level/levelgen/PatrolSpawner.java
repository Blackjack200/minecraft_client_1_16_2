package net.minecraft.world.level.levelgen;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.entity.EntityType;
import java.util.Random;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.CustomSpawner;

public class PatrolSpawner implements CustomSpawner {
    private int nextTick;
    
    public int tick(final ServerLevel aag, final boolean boolean2, final boolean boolean3) {
        if (!boolean2) {
            return 0;
        }
        if (!aag.getGameRules().getBoolean(GameRules.RULE_DO_PATROL_SPAWNING)) {
            return 0;
        }
        final Random random5 = aag.random;
        --this.nextTick;
        if (this.nextTick > 0) {
            return 0;
        }
        this.nextTick += 12000 + random5.nextInt(1200);
        final long long6 = aag.getDayTime() / 24000L;
        if (long6 < 5L || !aag.isDay()) {
            return 0;
        }
        if (random5.nextInt(5) != 0) {
            return 0;
        }
        final int integer8 = aag.players().size();
        if (integer8 < 1) {
            return 0;
        }
        final Player bft9 = (Player)aag.players().get(random5.nextInt(integer8));
        if (bft9.isSpectator()) {
            return 0;
        }
        if (aag.isCloseToVillage(bft9.blockPosition(), 2)) {
            return 0;
        }
        final int integer9 = (24 + random5.nextInt(24)) * (random5.nextBoolean() ? -1 : 1);
        final int integer10 = (24 + random5.nextInt(24)) * (random5.nextBoolean() ? -1 : 1);
        final BlockPos.MutableBlockPos a12 = bft9.blockPosition().mutable().move(integer9, 0, integer10);
        if (!aag.hasChunksAt(a12.getX() - 10, a12.getY() - 10, a12.getZ() - 10, a12.getX() + 10, a12.getY() + 10, a12.getZ() + 10)) {
            return 0;
        }
        final Biome bss13 = aag.getBiome(a12);
        final Biome.BiomeCategory b14 = bss13.getBiomeCategory();
        if (b14 == Biome.BiomeCategory.MUSHROOM) {
            return 0;
        }
        int integer11 = 0;
        for (int integer12 = (int)Math.ceil((double)aag.getCurrentDifficultyAt(a12).getEffectiveDifficulty()) + 1, integer13 = 0; integer13 < integer12; ++integer13) {
            ++integer11;
            a12.setY(aag.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, a12).getY());
            if (integer13 == 0) {
                if (!this.spawnPatrolMember(aag, a12, random5, true)) {
                    break;
                }
            }
            else {
                this.spawnPatrolMember(aag, a12, random5, false);
            }
            a12.setX(a12.getX() + random5.nextInt(5) - random5.nextInt(5));
            a12.setZ(a12.getZ() + random5.nextInt(5) - random5.nextInt(5));
        }
        return integer11;
    }
    
    private boolean spawnPatrolMember(final ServerLevel aag, final BlockPos fx, final Random random, final boolean boolean4) {
        final BlockState cee6 = aag.getBlockState(fx);
        if (!NaturalSpawner.isValidEmptySpawnBlock(aag, fx, cee6, cee6.getFluidState(), EntityType.PILLAGER)) {
            return false;
        }
        if (!PatrollingMonster.checkPatrollingMonsterSpawnRules(EntityType.PILLAGER, aag, MobSpawnType.PATROL, fx, random)) {
            return false;
        }
        final PatrollingMonster bdo7 = EntityType.PILLAGER.create(aag);
        if (bdo7 != null) {
            if (boolean4) {
                bdo7.setPatrolLeader(true);
                bdo7.findPatrolTarget();
            }
            bdo7.setPos(fx.getX(), fx.getY(), fx.getZ());
            bdo7.finalizeSpawn(aag, aag.getCurrentDifficultyAt(fx), MobSpawnType.PATROL, null, null);
            aag.addFreshEntityWithPassengers(bdo7);
            return true;
        }
        return false;
    }
}
