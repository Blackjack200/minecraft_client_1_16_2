package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import net.minecraft.world.entity.decoration.Motive;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import java.util.UUID;
import net.minecraft.network.protocol.Packet;

public class ClientboundAddPaintingPacket implements Packet<ClientGamePacketListener> {
    private int id;
    private UUID uuid;
    private BlockPos pos;
    private Direction direction;
    private int motive;
    
    public ClientboundAddPaintingPacket() {
    }
    
    public ClientboundAddPaintingPacket(final Painting bcp) {
        this.id = bcp.getId();
        this.uuid = bcp.getUUID();
        this.pos = bcp.getPos();
        this.direction = bcp.getDirection();
        this.motive = Registry.MOTIVE.getId(bcp.motive);
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.id = nf.readVarInt();
        this.uuid = nf.readUUID();
        this.motive = nf.readVarInt();
        this.pos = nf.readBlockPos();
        this.direction = Direction.from2DDataValue(nf.readUnsignedByte());
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.id);
        nf.writeUUID(this.uuid);
        nf.writeVarInt(this.motive);
        nf.writeBlockPos(this.pos);
        nf.writeByte(this.direction.get2DDataValue());
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleAddPainting(this);
    }
    
    public int getId() {
        return this.id;
    }
    
    public UUID getUUID() {
        return this.uuid;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public Direction getDirection() {
        return this.direction;
    }
    
    public Motive getMotive() {
        return Registry.MOTIVE.byId(this.motive);
    }
}
