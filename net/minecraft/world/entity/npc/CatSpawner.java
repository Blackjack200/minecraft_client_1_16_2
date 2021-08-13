package net.minecraft.world.entity.npc;

import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import java.util.List;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.GameRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.CustomSpawner;

public class CatSpawner implements CustomSpawner {
    private int nextTick;
    
    public int tick(final ServerLevel aag, final boolean boolean2, final boolean boolean3) {
        if (!boolean3 || !aag.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
            return 0;
        }
        --this.nextTick;
        if (this.nextTick > 0) {
            return 0;
        }
        this.nextTick = 1200;
        final Player bft5 = aag.getRandomPlayer();
        if (bft5 == null) {
            return 0;
        }
        final Random random6 = aag.random;
        final int integer7 = (8 + random6.nextInt(24)) * (random6.nextBoolean() ? -1 : 1);
        final int integer8 = (8 + random6.nextInt(24)) * (random6.nextBoolean() ? -1 : 1);
        final BlockPos fx9 = bft5.blockPosition().offset(integer7, 0, integer8);
        if (!aag.hasChunksAt(fx9.getX() - 10, fx9.getY() - 10, fx9.getZ() - 10, fx9.getX() + 10, fx9.getY() + 10, fx9.getZ() + 10)) {
            return 0;
        }
        if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, aag, fx9, EntityType.CAT)) {
            if (aag.isCloseToVillage(fx9, 2)) {
                return this.spawnInVillage(aag, fx9);
            }
            if (aag.structureFeatureManager().getStructureAt(fx9, true, StructureFeature.SWAMP_HUT).isValid()) {
                return this.spawnInHut(aag, fx9);
            }
        }
        return 0;
    }
    
    private int spawnInVillage(final ServerLevel aag, final BlockPos fx) {
        final int integer4 = 48;
        if (aag.getPoiManager().getCountInRange(PoiType.HOME.getPredicate(), fx, 48, PoiManager.Occupancy.IS_OCCUPIED) > 4L) {
            final List<Cat> list5 = aag.<Cat>getEntitiesOfClass((java.lang.Class<? extends Cat>)Cat.class, new AABB(fx).inflate(48.0, 8.0, 48.0));
            if (list5.size() < 5) {
                return this.spawnCat(fx, aag);
            }
        }
        return 0;
    }
    
    private int spawnInHut(final ServerLevel aag, final BlockPos fx) {
        final int integer4 = 16;
        final List<Cat> list5 = aag.<Cat>getEntitiesOfClass((java.lang.Class<? extends Cat>)Cat.class, new AABB(fx).inflate(16.0, 8.0, 16.0));
        if (list5.size() < 1) {
            return this.spawnCat(fx, aag);
        }
        return 0;
    }
    
    private int spawnCat(final BlockPos fx, final ServerLevel aag) {
        final Cat azy4 = EntityType.CAT.create(aag);
        if (azy4 == null) {
            return 0;
        }
        azy4.finalizeSpawn(aag, aag.getCurrentDifficultyAt(fx), MobSpawnType.NATURAL, null, null);
        azy4.moveTo(fx, 0.0f, 0.0f);
        aag.addFreshEntityWithPassengers(azy4);
        return 1;
    }
}
