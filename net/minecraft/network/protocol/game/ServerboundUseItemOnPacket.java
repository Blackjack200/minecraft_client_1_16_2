package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.network.protocol.Packet;

public class ServerboundUseItemOnPacket implements Packet<ServerGamePacketListener> {
    private BlockHitResult blockHit;
    private InteractionHand hand;
    
    public ServerboundUseItemOnPacket() {
    }
    
    public ServerboundUseItemOnPacket(final InteractionHand aoq, final BlockHitResult dcg) {
        this.hand = aoq;
        this.blockHit = dcg;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.hand = nf.<InteractionHand>readEnum(InteractionHand.class);
        this.blockHit = nf.readBlockHitResult();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeEnum(this.hand);
        nf.writeBlockHitResult(this.blockHit);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleUseItemOn(this);
    }
    
    public InteractionHand getHand() {
        return this.hand;
    }
    
    public BlockHitResult getHitResult() {
        return this.blockHit;
    }
}
