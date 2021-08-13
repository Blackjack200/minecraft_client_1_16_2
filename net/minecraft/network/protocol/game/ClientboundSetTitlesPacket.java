package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetTitlesPacket implements Packet<ClientGamePacketListener> {
    private Type type;
    private Component text;
    private int fadeInTime;
    private int stayTime;
    private int fadeOutTime;
    
    public ClientboundSetTitlesPacket() {
    }
    
    public ClientboundSetTitlesPacket(final Type a, final Component nr) {
        this(a, nr, -1, -1, -1);
    }
    
    public ClientboundSetTitlesPacket(final int integer1, final int integer2, final int integer3) {
        this(Type.TIMES, null, integer1, integer2, integer3);
    }
    
    public ClientboundSetTitlesPacket(final Type a, @Nullable final Component nr, final int integer3, final int integer4, final int integer5) {
        this.type = a;
        this.text = nr;
        this.fadeInTime = integer3;
        this.stayTime = integer4;
        this.fadeOutTime = integer5;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.type = nf.<Type>readEnum(Type.class);
        if (this.type == Type.TITLE || this.type == Type.SUBTITLE || this.type == Type.ACTIONBAR) {
            this.text = nf.readComponent();
        }
        if (this.type == Type.TIMES) {
            this.fadeInTime = nf.readInt();
            this.stayTime = nf.readInt();
            this.fadeOutTime = nf.readInt();
        }
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeEnum(this.type);
        if (this.type == Type.TITLE || this.type == Type.SUBTITLE || this.type == Type.ACTIONBAR) {
            nf.writeComponent(this.text);
        }
        if (this.type == Type.TIMES) {
            nf.writeInt(this.fadeInTime);
            nf.writeInt(this.stayTime);
            nf.writeInt(this.fadeOutTime);
        }
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleSetTitles(this);
    }
    
    public Type getType() {
        return this.type;
    }
    
    public Component getText() {
        return this.text;
    }
    
    public int getFadeInTime() {
        return this.fadeInTime;
    }
    
    public int getStayTime() {
        return this.stayTime;
    }
    
    public int getFadeOutTime() {
        return this.fadeOutTime;
    }
    
    public enum Type {
        TITLE, 
        SUBTITLE, 
        ACTIONBAR, 
        TIMES, 
        CLEAR, 
        RESET;
    }
}
