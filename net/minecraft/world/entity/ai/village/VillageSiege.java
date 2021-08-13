package net.minecraft.world.entity.ai.village;

import org.apache.logging.log4j.LogManager;
import javax.annotation.Nullable;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.monster.Zombie;
import java.util.Iterator;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.level.CustomSpawner;

public class VillageSiege implements CustomSpawner {
    private static final Logger LOGGER;
    private boolean hasSetupSiege;
    private State siegeState;
    private int zombiesToSpawn;
    private int nextSpawnTime;
    private int spawnX;
    private int spawnY;
    private int spawnZ;
    
    public VillageSiege() {
        this.siegeState = State.SIEGE_DONE;
    }
    
    public int tick(final ServerLevel aag, final boolean boolean2, final boolean boolean3) {
        if (aag.isDay() || !boolean2) {
            this.siegeState = State.SIEGE_DONE;
            this.hasSetupSiege = false;
            return 0;
        }
        final float float5 = aag.getTimeOfDay(0.0f);
        if (float5 == 0.5) {
            this.siegeState = ((aag.random.nextInt(10) == 0) ? State.SIEGE_TONIGHT : State.SIEGE_DONE);
        }
        if (this.siegeState == State.SIEGE_DONE) {
            return 0;
        }
        if (!this.hasSetupSiege) {
            if (!this.tryToSetupSiege(aag)) {
                return 0;
            }
            this.hasSetupSiege = true;
        }
        if (this.nextSpawnTime > 0) {
            --this.nextSpawnTime;
            return 0;
        }
        this.nextSpawnTime = 2;
        if (this.zombiesToSpawn > 0) {
            this.trySpawn(aag);
            --this.zombiesToSpawn;
        }
        else {
            this.siegeState = State.SIEGE_DONE;
        }
        return 1;
    }
    
    private boolean tryToSetupSiege(final ServerLevel aag) {
        for (final Player bft4 : aag.players()) {
            if (!bft4.isSpectator()) {
                final BlockPos fx5 = bft4.blockPosition();
                if (!aag.isVillage(fx5)) {
                    continue;
                }
                if (aag.getBiome(fx5).getBiomeCategory() == Biome.BiomeCategory.MUSHROOM) {
                    continue;
                }
                for (int integer6 = 0; integer6 < 10; ++integer6) {
                    final float float7 = aag.random.nextFloat() * 6.2831855f;
                    this.spawnX = fx5.getX() + Mth.floor(Mth.cos(float7) * 32.0f);
                    this.spawnY = fx5.getY();
                    this.spawnZ = fx5.getZ() + Mth.floor(Mth.sin(float7) * 32.0f);
                    if (this.findRandomSpawnPos(aag, new BlockPos(this.spawnX, this.spawnY, this.spawnZ)) != null) {
                        this.nextSpawnTime = 0;
                        this.zombiesToSpawn = 20;
                        break;
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    private void trySpawn(final ServerLevel aag) {
        final Vec3 dck3 = this.findRandomSpawnPos(aag, new BlockPos(this.spawnX, this.spawnY, this.spawnZ));
        if (dck3 == null) {
            return;
        }
        Zombie beg4;
        try {
            beg4 = new Zombie(aag);
            beg4.finalizeSpawn(aag, aag.getCurrentDifficultyAt(beg4.blockPosition()), MobSpawnType.EVENT, null, null);
        }
        catch (Exception exception5) {
            VillageSiege.LOGGER.warn("Failed to create zombie for village siege at {}", dck3, exception5);
            return;
        }
        beg4.moveTo(dck3.x, dck3.y, dck3.z, aag.random.nextFloat() * 360.0f, 0.0f);
        aag.addFreshEntityWithPassengers(beg4);
    }
    
    @Nullable
    private Vec3 findRandomSpawnPos(final ServerLevel aag, final BlockPos fx) {
        for (int integer4 = 0; integer4 < 10; ++integer4) {
            final int integer5 = fx.getX() + aag.random.nextInt(16) - 8;
            final int integer6 = fx.getZ() + aag.random.nextInt(16) - 8;
            final int integer7 = aag.getHeight(Heightmap.Types.WORLD_SURFACE, integer5, integer6);
            final BlockPos fx2 = new BlockPos(integer5, integer7, integer6);
            if (aag.isVillage(fx2)) {
                if (Monster.checkMonsterSpawnRules(EntityType.ZOMBIE, aag, MobSpawnType.EVENT, fx2, aag.random)) {
                    return Vec3.atBottomCenterOf(fx2);
                }
            }
        }
        return null;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    enum State {
        SIEGE_CAN_ACTIVATE, 
        SIEGE_TONIGHT, 
        SIEGE_DONE;
    }
}
