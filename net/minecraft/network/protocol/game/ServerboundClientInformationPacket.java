package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.ChatVisiblity;
import net.minecraft.network.protocol.Packet;

public class ServerboundClientInformationPacket implements Packet<ServerGamePacketListener> {
    private String language;
    private int viewDistance;
    private ChatVisiblity chatVisibility;
    private boolean chatColors;
    private int modelCustomisation;
    private HumanoidArm mainHand;
    
    public ServerboundClientInformationPacket() {
    }
    
    public ServerboundClientInformationPacket(final String string, final int integer2, final ChatVisiblity bfr, final boolean boolean4, final int integer5, final HumanoidArm aqf) {
        this.language = string;
        this.viewDistance = integer2;
        this.chatVisibility = bfr;
        this.chatColors = boolean4;
        this.modelCustomisation = integer5;
        this.mainHand = aqf;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.language = nf.readUtf(16);
        this.viewDistance = nf.readByte();
        this.chatVisibility = nf.<ChatVisiblity>readEnum(ChatVisiblity.class);
        this.chatColors = nf.readBoolean();
        this.modelCustomisation = nf.readUnsignedByte();
        this.mainHand = nf.<HumanoidArm>readEnum(HumanoidArm.class);
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeUtf(this.language);
        nf.writeByte(this.viewDistance);
        nf.writeEnum(this.chatVisibility);
        nf.writeBoolean(this.chatColors);
        nf.writeByte(this.modelCustomisation);
        nf.writeEnum(this.mainHand);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleClientInformation(this);
    }
    
    public ChatVisiblity getChatVisibility() {
        return this.chatVisibility;
    }
    
    public boolean getChatColors() {
        return this.chatColors;
    }
    
    public int getModelCustomisation() {
        return this.modelCustomisation;
    }
    
    public HumanoidArm getMainHand() {
        return this.mainHand;
    }
}
