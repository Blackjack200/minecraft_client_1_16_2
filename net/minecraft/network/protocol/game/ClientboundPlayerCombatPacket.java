package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;

public class ClientboundPlayerCombatPacket implements Packet<ClientGamePacketListener> {
    public Event event;
    public int playerId;
    public int killerId;
    public int duration;
    public Component message;
    
    public ClientboundPlayerCombatPacket() {
    }
    
    public ClientboundPlayerCombatPacket(final CombatTracker apg, final Event a) {
        this(apg, a, TextComponent.EMPTY);
    }
    
    public ClientboundPlayerCombatPacket(final CombatTracker apg, final Event a, final Component nr) {
        this.event = a;
        final LivingEntity aqj5 = apg.getKiller();
        switch (a) {
            case END_COMBAT: {
                this.duration = apg.getCombatDuration();
                this.killerId = ((aqj5 == null) ? -1 : aqj5.getId());
                break;
            }
            case ENTITY_DIED: {
                this.playerId = apg.getMob().getId();
                this.killerId = ((aqj5 == null) ? -1 : aqj5.getId());
                this.message = nr;
                break;
            }
        }
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.event = nf.<Event>readEnum(Event.class);
        if (this.event == Event.END_COMBAT) {
            this.duration = nf.readVarInt();
            this.killerId = nf.readInt();
        }
        else if (this.event == Event.ENTITY_DIED) {
            this.playerId = nf.readVarInt();
            this.killerId = nf.readInt();
            this.message = nf.readComponent();
        }
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeEnum(this.event);
        if (this.event == Event.END_COMBAT) {
            nf.writeVarInt(this.duration);
            nf.writeInt(this.killerId);
        }
        else if (this.event == Event.ENTITY_DIED) {
            nf.writeVarInt(this.playerId);
            nf.writeInt(this.killerId);
            nf.writeComponent(this.message);
        }
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handlePlayerCombat(this);
    }
    
    public boolean isSkippable() {
        return this.event == Event.ENTITY_DIED;
    }
    
    public enum Event {
        ENTER_COMBAT, 
        END_COMBAT, 
        ENTITY_DIED;
    }
}
