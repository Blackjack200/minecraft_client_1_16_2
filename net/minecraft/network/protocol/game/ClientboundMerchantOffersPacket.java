package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.network.protocol.Packet;

public class ClientboundMerchantOffersPacket implements Packet<ClientGamePacketListener> {
    private int containerId;
    private MerchantOffers offers;
    private int villagerLevel;
    private int villagerXp;
    private boolean showProgress;
    private boolean canRestock;
    
    public ClientboundMerchantOffersPacket() {
    }
    
    public ClientboundMerchantOffersPacket(final int integer1, final MerchantOffers bqt, final int integer3, final int integer4, final boolean boolean5, final boolean boolean6) {
        this.containerId = integer1;
        this.offers = bqt;
        this.villagerLevel = integer3;
        this.villagerXp = integer4;
        this.showProgress = boolean5;
        this.canRestock = boolean6;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.containerId = nf.readVarInt();
        this.offers = MerchantOffers.createFromStream(nf);
        this.villagerLevel = nf.readVarInt();
        this.villagerXp = nf.readVarInt();
        this.showProgress = nf.readBoolean();
        this.canRestock = nf.readBoolean();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.containerId);
        this.offers.writeToStream(nf);
        nf.writeVarInt(this.villagerLevel);
        nf.writeVarInt(this.villagerXp);
        nf.writeBoolean(this.showProgress);
        nf.writeBoolean(this.canRestock);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleMerchantOffers(this);
    }
    
    public int getContainerId() {
        return this.containerId;
    }
    
    public MerchantOffers getOffers() {
        return this.offers;
    }
    
    public int getVillagerLevel() {
        return this.villagerLevel;
    }
    
    public int getVillagerXp() {
        return this.villagerXp;
    }
    
    public boolean showProgress() {
        return this.showProgress;
    }
    
    public boolean canRestock() {
        return this.canRestock;
    }
}
