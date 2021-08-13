package net.minecraft.world.level;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import net.minecraft.nbt.Tag;
import net.minecraft.util.WeighedRandom;
import net.minecraft.nbt.ListTag;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;
import java.util.function.Function;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.ResourceLocationException;
import net.minecraft.util.StringUtil;
import net.minecraft.resources.ResourceLocation;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import java.util.List;
import org.apache.logging.log4j.Logger;

public abstract class BaseSpawner {
    private static final Logger LOGGER;
    private int spawnDelay;
    private final List<SpawnData> spawnPotentials;
    private SpawnData nextSpawnData;
    private double spin;
    private double oSpin;
    private int minSpawnDelay;
    private int maxSpawnDelay;
    private int spawnCount;
    @Nullable
    private Entity displayEntity;
    private int maxNearbyEntities;
    private int requiredPlayerRange;
    private int spawnRange;
    
    public BaseSpawner() {
        this.spawnDelay = 20;
        this.spawnPotentials = (List<SpawnData>)Lists.newArrayList();
        this.nextSpawnData = new SpawnData();
        this.minSpawnDelay = 200;
        this.maxSpawnDelay = 800;
        this.spawnCount = 4;
        this.maxNearbyEntities = 6;
        this.requiredPlayerRange = 16;
        this.spawnRange = 4;
    }
    
    @Nullable
    private ResourceLocation getEntityId() {
        final String string2 = this.nextSpawnData.getTag().getString("id");
        try {
            return StringUtil.isNullOrEmpty(string2) ? null : new ResourceLocation(string2);
        }
        catch (ResourceLocationException v3) {
            final BlockPos fx4 = this.getPos();
            BaseSpawner.LOGGER.warn("Invalid entity id '{}' at spawner {}:[{},{},{}]", string2, this.getLevel().dimension().location(), fx4.getX(), fx4.getY(), fx4.getZ());
            return null;
        }
    }
    
    public void setEntityId(final EntityType<?> aqb) {
        this.nextSpawnData.getTag().putString("id", Registry.ENTITY_TYPE.getKey(aqb).toString());
    }
    
    private boolean isNearPlayer() {
        final BlockPos fx2 = this.getPos();
        return this.getLevel().hasNearbyAlivePlayer(fx2.getX() + 0.5, fx2.getY() + 0.5, fx2.getZ() + 0.5, this.requiredPlayerRange);
    }
    
    public void tick() {
        if (!this.isNearPlayer()) {
            this.oSpin = this.spin;
            return;
        }
        final Level bru2 = this.getLevel();
        final BlockPos fx3 = this.getPos();
        if (!(bru2 instanceof ServerLevel)) {
            final double double4 = fx3.getX() + bru2.random.nextDouble();
            final double double5 = fx3.getY() + bru2.random.nextDouble();
            final double double6 = fx3.getZ() + bru2.random.nextDouble();
            bru2.addParticle(ParticleTypes.SMOKE, double4, double5, double6, 0.0, 0.0, 0.0);
            bru2.addParticle(ParticleTypes.FLAME, double4, double5, double6, 0.0, 0.0, 0.0);
            if (this.spawnDelay > 0) {
                --this.spawnDelay;
            }
            this.oSpin = this.spin;
            this.spin = (this.spin + 1000.0f / (this.spawnDelay + 200.0f)) % 360.0;
        }
        else {
            if (this.spawnDelay == -1) {
                this.delay();
            }
            if (this.spawnDelay > 0) {
                --this.spawnDelay;
                return;
            }
            boolean boolean4 = false;
            for (int integer5 = 0; integer5 < this.spawnCount; ++integer5) {
                final CompoundTag md6 = this.nextSpawnData.getTag();
                final Optional<EntityType<?>> optional7 = EntityType.by(md6);
                if (!optional7.isPresent()) {
                    this.delay();
                    return;
                }
                final ListTag mj8 = md6.getList("Pos", 6);
                final int integer6 = mj8.size();
                final double double7 = (integer6 >= 1) ? mj8.getDouble(0) : (fx3.getX() + (bru2.random.nextDouble() - bru2.random.nextDouble()) * this.spawnRange + 0.5);
                final double double8 = (integer6 >= 2) ? mj8.getDouble(1) : (fx3.getY() + bru2.random.nextInt(3) - 1);
                final double double9 = (integer6 >= 3) ? mj8.getDouble(2) : (fx3.getZ() + (bru2.random.nextDouble() - bru2.random.nextDouble()) * this.spawnRange + 0.5);
                if (bru2.noCollision(((EntityType)optional7.get()).getAABB(double7, double8, double9))) {
                    final ServerLevel aag16 = (ServerLevel)bru2;
                    if (SpawnPlacements.<Entity>checkSpawnRules((EntityType<Entity>)optional7.get(), aag16, MobSpawnType.SPAWNER, new BlockPos(double7, double8, double9), bru2.getRandom())) {
                        final Entity apx17 = EntityType.loadEntityRecursive(md6, bru2, (Function<Entity, Entity>)(apx -> {
                            apx.moveTo(double7, double8, double9, apx.yRot, apx.xRot);
                            return apx;
                        }));
                        if (apx17 == null) {
                            this.delay();
                            return;
                        }
                        final int integer7 = bru2.<Entity>getEntitiesOfClass((java.lang.Class<? extends Entity>)apx17.getClass(), new AABB(fx3.getX(), fx3.getY(), fx3.getZ(), fx3.getX() + 1, fx3.getY() + 1, fx3.getZ() + 1).inflate(this.spawnRange)).size();
                        if (integer7 >= this.maxNearbyEntities) {
                            this.delay();
                            return;
                        }
                        apx17.moveTo(apx17.getX(), apx17.getY(), apx17.getZ(), bru2.random.nextFloat() * 360.0f, 0.0f);
                        if (apx17 instanceof Mob) {
                            final Mob aqk19 = (Mob)apx17;
                            if (!aqk19.checkSpawnRules(bru2, MobSpawnType.SPAWNER)) {
                                continue;
                            }
                            if (!aqk19.checkSpawnObstruction(bru2)) {
                                continue;
                            }
                            if (this.nextSpawnData.getTag().size() == 1 && this.nextSpawnData.getTag().contains("id", 8)) {
                                ((Mob)apx17).finalizeSpawn(aag16, bru2.getCurrentDifficultyAt(apx17.blockPosition()), MobSpawnType.SPAWNER, null, null);
                            }
                        }
                        if (!aag16.tryAddFreshEntityWithPassengers(apx17)) {
                            this.delay();
                            return;
                        }
                        bru2.levelEvent(2004, fx3, 0);
                        if (apx17 instanceof Mob) {
                            ((Mob)apx17).spawnAnim();
                        }
                        boolean4 = true;
                    }
                }
            }
            if (boolean4) {
                this.delay();
            }
        }
    }
    
    private void delay() {
        if (this.maxSpawnDelay <= this.minSpawnDelay) {
            this.spawnDelay = this.minSpawnDelay;
        }
        else {
            this.spawnDelay = this.minSpawnDelay + this.getLevel().random.nextInt(this.maxSpawnDelay - this.minSpawnDelay);
        }
        if (!this.spawnPotentials.isEmpty()) {
            this.setNextSpawnData(WeighedRandom.<SpawnData>getRandomItem(this.getLevel().random, this.spawnPotentials));
        }
        this.broadcastEvent(1);
    }
    
    public void load(final CompoundTag md) {
        this.spawnDelay = md.getShort("Delay");
        this.spawnPotentials.clear();
        if (md.contains("SpawnPotentials", 9)) {
            final ListTag mj3 = md.getList("SpawnPotentials", 10);
            for (int integer4 = 0; integer4 < mj3.size(); ++integer4) {
                this.spawnPotentials.add(new SpawnData(mj3.getCompound(integer4)));
            }
        }
        if (md.contains("SpawnData", 10)) {
            this.setNextSpawnData(new SpawnData(1, md.getCompound("SpawnData")));
        }
        else if (!this.spawnPotentials.isEmpty()) {
            this.setNextSpawnData(WeighedRandom.<SpawnData>getRandomItem(this.getLevel().random, this.spawnPotentials));
        }
        if (md.contains("MinSpawnDelay", 99)) {
            this.minSpawnDelay = md.getShort("MinSpawnDelay");
            this.maxSpawnDelay = md.getShort("MaxSpawnDelay");
            this.spawnCount = md.getShort("SpawnCount");
        }
        if (md.contains("MaxNearbyEntities", 99)) {
            this.maxNearbyEntities = md.getShort("MaxNearbyEntities");
            this.requiredPlayerRange = md.getShort("RequiredPlayerRange");
        }
        if (md.contains("SpawnRange", 99)) {
            this.spawnRange = md.getShort("SpawnRange");
        }
        if (this.getLevel() != null) {
            this.displayEntity = null;
        }
    }
    
    public CompoundTag save(final CompoundTag md) {
        final ResourceLocation vk3 = this.getEntityId();
        if (vk3 == null) {
            return md;
        }
        md.putShort("Delay", (short)this.spawnDelay);
        md.putShort("MinSpawnDelay", (short)this.minSpawnDelay);
        md.putShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
        md.putShort("SpawnCount", (short)this.spawnCount);
        md.putShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
        md.putShort("RequiredPlayerRange", (short)this.requiredPlayerRange);
        md.putShort("SpawnRange", (short)this.spawnRange);
        md.put("SpawnData", (Tag)this.nextSpawnData.getTag().copy());
        final ListTag mj4 = new ListTag();
        if (this.spawnPotentials.isEmpty()) {
            mj4.add(this.nextSpawnData.save());
        }
        else {
            for (final SpawnData bsj6 : this.spawnPotentials) {
                mj4.add(bsj6.save());
            }
        }
        md.put("SpawnPotentials", (Tag)mj4);
        return md;
    }
    
    @Nullable
    public Entity getOrCreateDisplayEntity() {
        if (this.displayEntity == null) {
            this.displayEntity = EntityType.loadEntityRecursive(this.nextSpawnData.getTag(), this.getLevel(), (Function<Entity, Entity>)Function.identity());
            if (this.nextSpawnData.getTag().size() != 1 || !this.nextSpawnData.getTag().contains("id", 8) || this.displayEntity instanceof Mob) {}
        }
        return this.displayEntity;
    }
    
    public boolean onEventTriggered(final int integer) {
        if (integer == 1 && this.getLevel().isClientSide) {
            this.spawnDelay = this.minSpawnDelay;
            return true;
        }
        return false;
    }
    
    public void setNextSpawnData(final SpawnData bsj) {
        this.nextSpawnData = bsj;
    }
    
    public abstract void broadcastEvent(final int integer);
    
    public abstract Level getLevel();
    
    public abstract BlockPos getPos();
    
    public double getSpin() {
        return this.spin;
    }
    
    public double getoSpin() {
        return this.oSpin;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
