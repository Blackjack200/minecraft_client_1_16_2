package net.minecraft.network.protocol.game;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundGameEventPacket implements Packet<ClientGamePacketListener> {
    public static final Type NO_RESPAWN_BLOCK_AVAILABLE;
    public static final Type START_RAINING;
    public static final Type STOP_RAINING;
    public static final Type CHANGE_GAME_MODE;
    public static final Type WIN_GAME;
    public static final Type DEMO_EVENT;
    public static final Type ARROW_HIT_PLAYER;
    public static final Type RAIN_LEVEL_CHANGE;
    public static final Type THUNDER_LEVEL_CHANGE;
    public static final Type PUFFER_FISH_STING;
    public static final Type GUARDIAN_ELDER_EFFECT;
    public static final Type IMMEDIATE_RESPAWN;
    private Type event;
    private float param;
    
    public ClientboundGameEventPacket() {
    }
    
    public ClientboundGameEventPacket(final Type a, final float float2) {
        this.event = a;
        this.param = float2;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.event = (Type)Type.TYPES.get((int)nf.readUnsignedByte());
        this.param = nf.readFloat();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeByte(this.event.id);
        nf.writeFloat(this.param);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleGameEvent(this);
    }
    
    public Type getEvent() {
        return this.event;
    }
    
    public float getParam() {
        return this.param;
    }
    
    static {
        NO_RESPAWN_BLOCK_AVAILABLE = new Type(0);
        START_RAINING = new Type(1);
        STOP_RAINING = new Type(2);
        CHANGE_GAME_MODE = new Type(3);
        WIN_GAME = new Type(4);
        DEMO_EVENT = new Type(5);
        ARROW_HIT_PLAYER = new Type(6);
        RAIN_LEVEL_CHANGE = new Type(7);
        THUNDER_LEVEL_CHANGE = new Type(8);
        PUFFER_FISH_STING = new Type(9);
        GUARDIAN_ELDER_EFFECT = new Type(10);
        IMMEDIATE_RESPAWN = new Type(11);
    }
    
    public static class Type {
        private static final Int2ObjectMap<Type> TYPES;
        private final int id;
        
        public Type(final int integer) {
            this.id = integer;
            Type.TYPES.put(integer, this);
        }
        
        static {
            TYPES = (Int2ObjectMap)new Int2ObjectOpenHashMap();
        }
    }
}
