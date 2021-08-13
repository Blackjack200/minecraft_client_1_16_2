package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import java.util.Collection;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.network.protocol.Packet;

public class ClientboundMapItemDataPacket implements Packet<ClientGamePacketListener> {
    private int mapId;
    private byte scale;
    private boolean trackingPosition;
    private boolean locked;
    private MapDecoration[] decorations;
    private int startX;
    private int startY;
    private int width;
    private int height;
    private byte[] mapColors;
    
    public ClientboundMapItemDataPacket() {
    }
    
    public ClientboundMapItemDataPacket(final int integer1, final byte byte2, final boolean boolean3, final boolean boolean4, final Collection<MapDecoration> collection, final byte[] arr, final int integer7, final int integer8, final int integer9, final int integer10) {
        this.mapId = integer1;
        this.scale = byte2;
        this.trackingPosition = boolean3;
        this.locked = boolean4;
        this.decorations = (MapDecoration[])collection.toArray((Object[])new MapDecoration[collection.size()]);
        this.startX = integer7;
        this.startY = integer8;
        this.width = integer9;
        this.height = integer10;
        this.mapColors = new byte[integer9 * integer10];
        for (int integer11 = 0; integer11 < integer9; ++integer11) {
            for (int integer12 = 0; integer12 < integer10; ++integer12) {
                this.mapColors[integer11 + integer12 * integer9] = arr[integer7 + integer11 + (integer8 + integer12) * 128];
            }
        }
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.mapId = nf.readVarInt();
        this.scale = nf.readByte();
        this.trackingPosition = nf.readBoolean();
        this.locked = nf.readBoolean();
        this.decorations = new MapDecoration[nf.readVarInt()];
        for (int integer3 = 0; integer3 < this.decorations.length; ++integer3) {
            final MapDecoration.Type a4 = nf.<MapDecoration.Type>readEnum(MapDecoration.Type.class);
            this.decorations[integer3] = new MapDecoration(a4, nf.readByte(), nf.readByte(), (byte)(nf.readByte() & 0xF), nf.readBoolean() ? nf.readComponent() : null);
        }
        this.width = nf.readUnsignedByte();
        if (this.width > 0) {
            this.height = nf.readUnsignedByte();
            this.startX = nf.readUnsignedByte();
            this.startY = nf.readUnsignedByte();
            this.mapColors = nf.readByteArray();
        }
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.mapId);
        nf.writeByte(this.scale);
        nf.writeBoolean(this.trackingPosition);
        nf.writeBoolean(this.locked);
        nf.writeVarInt(this.decorations.length);
        for (final MapDecoration cxr6 : this.decorations) {
            nf.writeEnum(cxr6.getType());
            nf.writeByte(cxr6.getX());
            nf.writeByte(cxr6.getY());
            nf.writeByte(cxr6.getRot() & 0xF);
            if (cxr6.getName() != null) {
                nf.writeBoolean(true);
                nf.writeComponent(cxr6.getName());
            }
            else {
                nf.writeBoolean(false);
            }
        }
        nf.writeByte(this.width);
        if (this.width > 0) {
            nf.writeByte(this.height);
            nf.writeByte(this.startX);
            nf.writeByte(this.startY);
            nf.writeByteArray(this.mapColors);
        }
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleMapItemData(this);
    }
    
    public int getMapId() {
        return this.mapId;
    }
    
    public void applyToMap(final MapItemSavedData cxu) {
        cxu.scale = this.scale;
        cxu.trackingPosition = this.trackingPosition;
        cxu.locked = this.locked;
        cxu.decorations.clear();
        for (int integer3 = 0; integer3 < this.decorations.length; ++integer3) {
            final MapDecoration cxr4 = this.decorations[integer3];
            cxu.decorations.put(new StringBuilder().append("icon-").append(integer3).toString(), cxr4);
        }
        for (int integer3 = 0; integer3 < this.width; ++integer3) {
            for (int integer4 = 0; integer4 < this.height; ++integer4) {
                cxu.colors[this.startX + integer3 + (this.startY + integer4) * 128] = this.mapColors[integer3 + integer4 * this.width];
            }
        }
    }
}
