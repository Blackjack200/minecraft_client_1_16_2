package net.minecraft.world.level.block.entity;

import org.apache.logging.log4j.LogManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.Direction;
import net.minecraft.world.level.levelgen.feature.configurations.EndGatewayConfiguration;
import net.minecraft.world.level.levelgen.feature.Feature;
import java.util.Iterator;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.WorldGenLevel;
import java.util.Random;
import net.minecraft.data.worldgen.Features;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntitySelector;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import org.apache.logging.log4j.Logger;

public class TheEndGatewayBlockEntity extends TheEndPortalBlockEntity implements TickableBlockEntity {
    private static final Logger LOGGER;
    private long age;
    private int teleportCooldown;
    @Nullable
    private BlockPos exitPortal;
    private boolean exactTeleport;
    
    public TheEndGatewayBlockEntity() {
        super(BlockEntityType.END_GATEWAY);
    }
    
    @Override
    public CompoundTag save(final CompoundTag md) {
        super.save(md);
        md.putLong("Age", this.age);
        if (this.exitPortal != null) {
            md.put("ExitPortal", (Tag)NbtUtils.writeBlockPos(this.exitPortal));
        }
        if (this.exactTeleport) {
            md.putBoolean("ExactTeleport", this.exactTeleport);
        }
        return md;
    }
    
    @Override
    public void load(final BlockState cee, final CompoundTag md) {
        super.load(cee, md);
        this.age = md.getLong("Age");
        if (md.contains("ExitPortal", 10)) {
            this.exitPortal = NbtUtils.readBlockPos(md.getCompound("ExitPortal"));
        }
        this.exactTeleport = md.getBoolean("ExactTeleport");
    }
    
    @Override
    public double getViewDistance() {
        return 256.0;
    }
    
    @Override
    public void tick() {
        final boolean boolean2 = this.isSpawning();
        final boolean boolean3 = this.isCoolingDown();
        ++this.age;
        if (boolean3) {
            --this.teleportCooldown;
        }
        else if (!this.level.isClientSide) {
            final List<Entity> list4 = this.level.<Entity>getEntitiesOfClass((java.lang.Class<? extends Entity>)Entity.class, new AABB(this.getBlockPos()), (java.util.function.Predicate<? super Entity>)TheEndGatewayBlockEntity::canEntityTeleport);
            if (!list4.isEmpty()) {
                this.teleportEntity((Entity)list4.get(this.level.random.nextInt(list4.size())));
            }
            if (this.age % 2400L == 0L) {
                this.triggerCooldown();
            }
        }
        if (boolean2 != this.isSpawning() || boolean3 != this.isCoolingDown()) {
            this.setChanged();
        }
    }
    
    public static boolean canEntityTeleport(final Entity apx) {
        return EntitySelector.NO_SPECTATORS.test(apx) && !apx.getRootVehicle().isOnPortalCooldown();
    }
    
    public boolean isSpawning() {
        return this.age < 200L;
    }
    
    public boolean isCoolingDown() {
        return this.teleportCooldown > 0;
    }
    
    public float getSpawnPercent(final float float1) {
        return Mth.clamp((this.age + float1) / 200.0f, 0.0f, 1.0f);
    }
    
    public float getCooldownPercent(final float float1) {
        return 1.0f - Mth.clamp((this.teleportCooldown - float1) / 40.0f, 0.0f, 1.0f);
    }
    
    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 8, this.getUpdateTag());
    }
    
    @Override
    public CompoundTag getUpdateTag() {
        return this.save(new CompoundTag());
    }
    
    public void triggerCooldown() {
        if (!this.level.isClientSide) {
            this.teleportCooldown = 40;
            this.level.blockEvent(this.getBlockPos(), this.getBlockState().getBlock(), 1, 0);
            this.setChanged();
        }
    }
    
    @Override
    public boolean triggerEvent(final int integer1, final int integer2) {
        if (integer1 == 1) {
            this.teleportCooldown = 40;
            return true;
        }
        return super.triggerEvent(integer1, integer2);
    }
    
    public void teleportEntity(final Entity apx) {
        if (!(this.level instanceof ServerLevel) || this.isCoolingDown()) {
            return;
        }
        this.teleportCooldown = 100;
        if (this.exitPortal == null && this.level.dimension() == Level.END) {
            this.findExitPortal((ServerLevel)this.level);
        }
        if (this.exitPortal != null) {
            final BlockPos fx3 = this.exactTeleport ? this.exitPortal : this.findExitPosition();
            Entity apx3;
            if (apx instanceof ThrownEnderpearl) {
                final Entity apx2 = ((ThrownEnderpearl)apx).getOwner();
                if (apx2 instanceof ServerPlayer) {
                    CriteriaTriggers.ENTER_BLOCK.trigger((ServerPlayer)apx2, this.level.getBlockState(this.getBlockPos()));
                }
                if (apx2 != null) {
                    apx3 = apx2;
                    apx.remove();
                }
                else {
                    apx3 = apx;
                }
            }
            else {
                apx3 = apx.getRootVehicle();
            }
            apx3.setPortalCooldown();
            apx3.teleportToWithTicket(fx3.getX() + 0.5, fx3.getY(), fx3.getZ() + 0.5);
        }
        this.triggerCooldown();
    }
    
    private BlockPos findExitPosition() {
        final BlockPos fx2 = findTallestBlock(this.level, this.exitPortal.offset(0, 2, 0), 5, false);
        TheEndGatewayBlockEntity.LOGGER.debug("Best exit position for portal at {} is {}", this.exitPortal, fx2);
        return fx2.above();
    }
    
    private void findExitPortal(final ServerLevel aag) {
        final Vec3 dck3 = new Vec3(this.getBlockPos().getX(), 0.0, this.getBlockPos().getZ()).normalize();
        Vec3 dck4 = dck3.scale(1024.0);
        for (int integer5 = 16; getChunk(aag, dck4).getHighestSectionPosition() > 0 && integer5-- > 0; dck4 = dck4.add(dck3.scale(-16.0))) {
            TheEndGatewayBlockEntity.LOGGER.debug("Skipping backwards past nonempty chunk at {}", dck4);
        }
        for (int integer5 = 16; getChunk(aag, dck4).getHighestSectionPosition() == 0 && integer5-- > 0; dck4 = dck4.add(dck3.scale(16.0))) {
            TheEndGatewayBlockEntity.LOGGER.debug("Skipping forward past empty chunk at {}", dck4);
        }
        TheEndGatewayBlockEntity.LOGGER.debug("Found chunk at {}", dck4);
        final LevelChunk cge6 = getChunk(aag, dck4);
        this.exitPortal = findValidSpawnInChunk(cge6);
        if (this.exitPortal == null) {
            this.exitPortal = new BlockPos(dck4.x + 0.5, 75.0, dck4.z + 0.5);
            TheEndGatewayBlockEntity.LOGGER.debug("Failed to find suitable block, settling on {}", this.exitPortal);
            Features.END_ISLAND.place(aag, aag.getChunkSource().getGenerator(), new Random(this.exitPortal.asLong()), this.exitPortal);
        }
        else {
            TheEndGatewayBlockEntity.LOGGER.debug("Found block at {}", this.exitPortal);
        }
        this.exitPortal = findTallestBlock(aag, this.exitPortal, 16, true);
        TheEndGatewayBlockEntity.LOGGER.debug("Creating portal at {}", this.exitPortal);
        this.createExitPortal(aag, this.exitPortal = this.exitPortal.above(10));
        this.setChanged();
    }
    
    private static BlockPos findTallestBlock(final BlockGetter bqz, final BlockPos fx, final int integer, final boolean boolean4) {
        BlockPos fx2 = null;
        for (int integer2 = -integer; integer2 <= integer; ++integer2) {
            for (int integer3 = -integer; integer3 <= integer; ++integer3) {
                if (integer2 != 0 || integer3 != 0 || boolean4) {
                    for (int integer4 = 255; integer4 > ((fx2 == null) ? 0 : fx2.getY()); --integer4) {
                        final BlockPos fx3 = new BlockPos(fx.getX() + integer2, integer4, fx.getZ() + integer3);
                        final BlockState cee10 = bqz.getBlockState(fx3);
                        if (cee10.isCollisionShapeFullBlock(bqz, fx3) && (boolean4 || !cee10.is(Blocks.BEDROCK))) {
                            fx2 = fx3;
                            break;
                        }
                    }
                }
            }
        }
        return (fx2 == null) ? fx : fx2;
    }
    
    private static LevelChunk getChunk(final Level bru, final Vec3 dck) {
        return bru.getChunk(Mth.floor(dck.x / 16.0), Mth.floor(dck.z / 16.0));
    }
    
    @Nullable
    private static BlockPos findValidSpawnInChunk(final LevelChunk cge) {
        final ChunkPos bra2 = cge.getPos();
        final BlockPos fx3 = new BlockPos(bra2.getMinBlockX(), 30, bra2.getMinBlockZ());
        final int integer4 = cge.getHighestSectionPosition() + 16 - 1;
        final BlockPos fx4 = new BlockPos(bra2.getMaxBlockX(), integer4, bra2.getMaxBlockZ());
        BlockPos fx5 = null;
        double double7 = 0.0;
        for (final BlockPos fx6 : BlockPos.betweenClosed(fx3, fx4)) {
            final BlockState cee11 = cge.getBlockState(fx6);
            final BlockPos fx7 = fx6.above();
            final BlockPos fx8 = fx6.above(2);
            if (cee11.is(Blocks.END_STONE) && !cge.getBlockState(fx7).isCollisionShapeFullBlock(cge, fx7) && !cge.getBlockState(fx8).isCollisionShapeFullBlock(cge, fx8)) {
                final double double8 = fx6.distSqr(0.0, 0.0, 0.0, true);
                if (fx5 != null && double8 >= double7) {
                    continue;
                }
                fx5 = fx6;
                double7 = double8;
            }
        }
        return fx5;
    }
    
    private void createExitPortal(final ServerLevel aag, final BlockPos fx) {
        Feature.END_GATEWAY.configured(EndGatewayConfiguration.knownExit(this.getBlockPos(), false)).place(aag, aag.getChunkSource().getGenerator(), new Random(), fx);
    }
    
    @Override
    public boolean shouldRenderFace(final Direction gc) {
        return Block.shouldRenderFace(this.getBlockState(), this.level, this.getBlockPos(), gc);
    }
    
    public int getParticleAmount() {
        int integer2 = 0;
        for (final Direction gc6 : Direction.values()) {
            integer2 += (this.shouldRenderFace(gc6) ? 1 : 0);
        }
        return integer2;
    }
    
    public void setExitPosition(final BlockPos fx, final boolean boolean2) {
        this.exactTeleport = boolean2;
        this.exitPortal = fx;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
