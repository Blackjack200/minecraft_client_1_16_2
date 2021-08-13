package net.minecraft.server.level;

import org.apache.logging.log4j.LogManager;
import java.util.Set;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import java.util.Collection;
import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
import net.minecraft.world.entity.Mob;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.EquipmentSlot;
import com.google.common.collect.Lists;
import net.minecraft.network.protocol.game.ClientboundAddMobPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import java.util.Iterator;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.util.Mth;
import java.util.Collections;
import java.util.List;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.protocol.Packet;
import java.util.function.Consumer;
import net.minecraft.world.entity.Entity;
import org.apache.logging.log4j.Logger;

public class ServerEntity {
    private static final Logger LOGGER;
    private final ServerLevel level;
    private final Entity entity;
    private final int updateInterval;
    private final boolean trackDelta;
    private final Consumer<Packet<?>> broadcast;
    private long xp;
    private long yp;
    private long zp;
    private int yRotp;
    private int xRotp;
    private int yHeadRotp;
    private Vec3 ap;
    private int tickCount;
    private int teleportDelay;
    private List<Entity> lastPassengers;
    private boolean wasRiding;
    private boolean wasOnGround;
    
    public ServerEntity(final ServerLevel aag, final Entity apx, final int integer, final boolean boolean4, final Consumer<Packet<?>> consumer) {
        this.ap = Vec3.ZERO;
        this.lastPassengers = (List<Entity>)Collections.emptyList();
        this.level = aag;
        this.broadcast = consumer;
        this.entity = apx;
        this.updateInterval = integer;
        this.trackDelta = boolean4;
        this.updateSentPos();
        this.yRotp = Mth.floor(apx.yRot * 256.0f / 360.0f);
        this.xRotp = Mth.floor(apx.xRot * 256.0f / 360.0f);
        this.yHeadRotp = Mth.floor(apx.getYHeadRot() * 256.0f / 360.0f);
        this.wasOnGround = apx.isOnGround();
    }
    
    public void sendChanges() {
        final List<Entity> list2 = this.entity.getPassengers();
        if (!list2.equals(this.lastPassengers)) {
            this.lastPassengers = list2;
            this.broadcast.accept(new ClientboundSetPassengersPacket(this.entity));
        }
        if (this.entity instanceof ItemFrame && this.tickCount % 10 == 0) {
            final ItemFrame bcm3 = (ItemFrame)this.entity;
            final ItemStack bly4 = bcm3.getItem();
            if (bly4.getItem() instanceof MapItem) {
                final MapItemSavedData cxu5 = MapItem.getOrCreateSavedData(bly4, this.level);
                for (final ServerPlayer aah7 : this.level.players()) {
                    cxu5.tickCarriedBy(aah7, bly4);
                    final Packet<?> oj8 = ((MapItem)bly4.getItem()).getUpdatePacket(bly4, this.level, aah7);
                    if (oj8 != null) {
                        aah7.connection.send(oj8);
                    }
                }
            }
            this.sendDirtyEntityData();
        }
        if (this.tickCount % this.updateInterval == 0 || this.entity.hasImpulse || this.entity.getEntityData().isDirty()) {
            if (this.entity.isPassenger()) {
                final int integer3 = Mth.floor(this.entity.yRot * 256.0f / 360.0f);
                final int integer4 = Mth.floor(this.entity.xRot * 256.0f / 360.0f);
                final boolean boolean5 = Math.abs(integer3 - this.yRotp) >= 1 || Math.abs(integer4 - this.xRotp) >= 1;
                if (boolean5) {
                    this.broadcast.accept(new ClientboundMoveEntityPacket.Rot(this.entity.getId(), (byte)integer3, (byte)integer4, this.entity.isOnGround()));
                    this.yRotp = integer3;
                    this.xRotp = integer4;
                }
                this.updateSentPos();
                this.sendDirtyEntityData();
                this.wasRiding = true;
            }
            else {
                ++this.teleportDelay;
                final int integer3 = Mth.floor(this.entity.yRot * 256.0f / 360.0f);
                final int integer4 = Mth.floor(this.entity.xRot * 256.0f / 360.0f);
                final Vec3 dck5 = this.entity.position().subtract(ClientboundMoveEntityPacket.packetToEntity(this.xp, this.yp, this.zp));
                final boolean boolean6 = dck5.lengthSqr() >= 7.62939453125E-6;
                Packet<?> oj9 = null;
                final boolean boolean7 = boolean6 || this.tickCount % 60 == 0;
                final boolean boolean8 = Math.abs(integer3 - this.yRotp) >= 1 || Math.abs(integer4 - this.xRotp) >= 1;
                if (this.tickCount > 0 || this.entity instanceof AbstractArrow) {
                    final long long10 = ClientboundMoveEntityPacket.entityToPacket(dck5.x);
                    final long long11 = ClientboundMoveEntityPacket.entityToPacket(dck5.y);
                    final long long12 = ClientboundMoveEntityPacket.entityToPacket(dck5.z);
                    final boolean boolean9 = long10 < -32768L || long10 > 32767L || long11 < -32768L || long11 > 32767L || long12 < -32768L || long12 > 32767L;
                    if (boolean9 || this.teleportDelay > 400 || this.wasRiding || this.wasOnGround != this.entity.isOnGround()) {
                        this.wasOnGround = this.entity.isOnGround();
                        this.teleportDelay = 0;
                        oj9 = new ClientboundTeleportEntityPacket(this.entity);
                    }
                    else if ((boolean7 && boolean8) || this.entity instanceof AbstractArrow) {
                        oj9 = new ClientboundMoveEntityPacket.PosRot(this.entity.getId(), (short)long10, (short)long11, (short)long12, (byte)integer3, (byte)integer4, this.entity.isOnGround());
                    }
                    else if (boolean7) {
                        oj9 = new ClientboundMoveEntityPacket.Pos(this.entity.getId(), (short)long10, (short)long11, (short)long12, this.entity.isOnGround());
                    }
                    else if (boolean8) {
                        oj9 = new ClientboundMoveEntityPacket.Rot(this.entity.getId(), (byte)integer3, (byte)integer4, this.entity.isOnGround());
                    }
                }
                if ((this.trackDelta || this.entity.hasImpulse || (this.entity instanceof LivingEntity && ((LivingEntity)this.entity).isFallFlying())) && this.tickCount > 0) {
                    final Vec3 dck6 = this.entity.getDeltaMovement();
                    final double double11 = dck6.distanceToSqr(this.ap);
                    if (double11 > 1.0E-7 || (double11 > 0.0 && dck6.lengthSqr() == 0.0)) {
                        this.ap = dck6;
                        this.broadcast.accept(new ClientboundSetEntityMotionPacket(this.entity.getId(), this.ap));
                    }
                }
                if (oj9 != null) {
                    this.broadcast.accept(oj9);
                }
                this.sendDirtyEntityData();
                if (boolean7) {
                    this.updateSentPos();
                }
                if (boolean8) {
                    this.yRotp = integer3;
                    this.xRotp = integer4;
                }
                this.wasRiding = false;
            }
            final int integer3 = Mth.floor(this.entity.getYHeadRot() * 256.0f / 360.0f);
            if (Math.abs(integer3 - this.yHeadRotp) >= 1) {
                this.broadcast.accept(new ClientboundRotateHeadPacket(this.entity, (byte)integer3));
                this.yHeadRotp = integer3;
            }
            this.entity.hasImpulse = false;
        }
        ++this.tickCount;
        if (this.entity.hurtMarked) {
            this.broadcastAndSend(new ClientboundSetEntityMotionPacket(this.entity));
            this.entity.hurtMarked = false;
        }
    }
    
    public void removePairing(final ServerPlayer aah) {
        this.entity.stopSeenByPlayer(aah);
        aah.sendRemoveEntity(this.entity);
    }
    
    public void addPairing(final ServerPlayer aah) {
        this.sendPairingData((Consumer<Packet<?>>)aah.connection::send);
        this.entity.startSeenByPlayer(aah);
        aah.cancelRemoveEntity(this.entity);
    }
    
    public void sendPairingData(final Consumer<Packet<?>> consumer) {
        if (this.entity.removed) {
            ServerEntity.LOGGER.warn(new StringBuilder().append("Fetching packet for removed entity ").append(this.entity).toString());
        }
        final Packet<?> oj3 = this.entity.getAddEntityPacket();
        this.yHeadRotp = Mth.floor(this.entity.getYHeadRot() * 256.0f / 360.0f);
        consumer.accept(oj3);
        if (!this.entity.getEntityData().isEmpty()) {
            consumer.accept(new ClientboundSetEntityDataPacket(this.entity.getId(), this.entity.getEntityData(), true));
        }
        boolean boolean4 = this.trackDelta;
        if (this.entity instanceof LivingEntity) {
            final Collection<AttributeInstance> collection5 = ((LivingEntity)this.entity).getAttributes().getSyncableAttributes();
            if (!collection5.isEmpty()) {
                consumer.accept(new ClientboundUpdateAttributesPacket(this.entity.getId(), collection5));
            }
            if (((LivingEntity)this.entity).isFallFlying()) {
                boolean4 = true;
            }
        }
        this.ap = this.entity.getDeltaMovement();
        if (boolean4 && !(oj3 instanceof ClientboundAddMobPacket)) {
            consumer.accept(new ClientboundSetEntityMotionPacket(this.entity.getId(), this.ap));
        }
        if (this.entity instanceof LivingEntity) {
            final List<Pair<EquipmentSlot, ItemStack>> list5 = (List<Pair<EquipmentSlot, ItemStack>>)Lists.newArrayList();
            for (final EquipmentSlot aqc9 : EquipmentSlot.values()) {
                final ItemStack bly10 = ((LivingEntity)this.entity).getItemBySlot(aqc9);
                if (!bly10.isEmpty()) {
                    list5.add(Pair.of((Object)aqc9, (Object)bly10.copy()));
                }
            }
            if (!list5.isEmpty()) {
                consumer.accept(new ClientboundSetEquipmentPacket(this.entity.getId(), list5));
            }
        }
        if (this.entity instanceof LivingEntity) {
            final LivingEntity aqj5 = (LivingEntity)this.entity;
            for (final MobEffectInstance apr7 : aqj5.getActiveEffects()) {
                consumer.accept(new ClientboundUpdateMobEffectPacket(this.entity.getId(), apr7));
            }
        }
        if (!this.entity.getPassengers().isEmpty()) {
            consumer.accept(new ClientboundSetPassengersPacket(this.entity));
        }
        if (this.entity.isPassenger()) {
            consumer.accept(new ClientboundSetPassengersPacket(this.entity.getVehicle()));
        }
        if (this.entity instanceof Mob) {
            final Mob aqk5 = (Mob)this.entity;
            if (aqk5.isLeashed()) {
                consumer.accept(new ClientboundSetEntityLinkPacket(aqk5, aqk5.getLeashHolder()));
            }
        }
    }
    
    private void sendDirtyEntityData() {
        final SynchedEntityData uv2 = this.entity.getEntityData();
        if (uv2.isDirty()) {
            this.broadcastAndSend(new ClientboundSetEntityDataPacket(this.entity.getId(), uv2, false));
        }
        if (this.entity instanceof LivingEntity) {
            final Set<AttributeInstance> set3 = ((LivingEntity)this.entity).getAttributes().getDirtyAttributes();
            if (!set3.isEmpty()) {
                this.broadcastAndSend(new ClientboundUpdateAttributesPacket(this.entity.getId(), (Collection<AttributeInstance>)set3));
            }
            set3.clear();
        }
    }
    
    private void updateSentPos() {
        this.xp = ClientboundMoveEntityPacket.entityToPacket(this.entity.getX());
        this.yp = ClientboundMoveEntityPacket.entityToPacket(this.entity.getY());
        this.zp = ClientboundMoveEntityPacket.entityToPacket(this.entity.getZ());
    }
    
    public Vec3 sentPos() {
        return ClientboundMoveEntityPacket.packetToEntity(this.xp, this.yp, this.zp);
    }
    
    private void broadcastAndSend(final Packet<?> oj) {
        this.broadcast.accept(oj);
        if (this.entity instanceof ServerPlayer) {
            ((ServerPlayer)this.entity).connection.send(oj);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
