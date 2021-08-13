package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.protocol.Packet;

public class ServerboundInteractPacket implements Packet<ServerGamePacketListener> {
    private int entityId;
    private Action action;
    private Vec3 location;
    private InteractionHand hand;
    private boolean usingSecondaryAction;
    
    public ServerboundInteractPacket() {
    }
    
    public ServerboundInteractPacket(final Entity apx, final boolean boolean2) {
        this.entityId = apx.getId();
        this.action = Action.ATTACK;
        this.usingSecondaryAction = boolean2;
    }
    
    public ServerboundInteractPacket(final Entity apx, final InteractionHand aoq, final boolean boolean3) {
        this.entityId = apx.getId();
        this.action = Action.INTERACT;
        this.hand = aoq;
        this.usingSecondaryAction = boolean3;
    }
    
    public ServerboundInteractPacket(final Entity apx, final InteractionHand aoq, final Vec3 dck, final boolean boolean4) {
        this.entityId = apx.getId();
        this.action = Action.INTERACT_AT;
        this.hand = aoq;
        this.location = dck;
        this.usingSecondaryAction = boolean4;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.entityId = nf.readVarInt();
        this.action = nf.<Action>readEnum(Action.class);
        if (this.action == Action.INTERACT_AT) {
            this.location = new Vec3(nf.readFloat(), nf.readFloat(), nf.readFloat());
        }
        if (this.action == Action.INTERACT || this.action == Action.INTERACT_AT) {
            this.hand = nf.<InteractionHand>readEnum(InteractionHand.class);
        }
        this.usingSecondaryAction = nf.readBoolean();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.entityId);
        nf.writeEnum(this.action);
        if (this.action == Action.INTERACT_AT) {
            nf.writeFloat((float)this.location.x);
            nf.writeFloat((float)this.location.y);
            nf.writeFloat((float)this.location.z);
        }
        if (this.action == Action.INTERACT || this.action == Action.INTERACT_AT) {
            nf.writeEnum(this.hand);
        }
        nf.writeBoolean(this.usingSecondaryAction);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleInteract(this);
    }
    
    @Nullable
    public Entity getTarget(final Level bru) {
        return bru.getEntity(this.entityId);
    }
    
    public Action getAction() {
        return this.action;
    }
    
    @Nullable
    public InteractionHand getHand() {
        return this.hand;
    }
    
    public Vec3 getLocation() {
        return this.location;
    }
    
    public boolean isUsingSecondaryAction() {
        return this.usingSecondaryAction;
    }
    
    public enum Action {
        INTERACT, 
        ATTACK, 
        INTERACT_AT;
    }
}
