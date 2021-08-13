package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import java.util.UUID;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.network.FriendlyByteBuf;
import java.util.Iterator;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import java.util.Collection;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.network.protocol.Packet;

public class ClientboundUpdateAttributesPacket implements Packet<ClientGamePacketListener> {
    private int entityId;
    private final List<AttributeSnapshot> attributes;
    
    public ClientboundUpdateAttributesPacket() {
        this.attributes = (List<AttributeSnapshot>)Lists.newArrayList();
    }
    
    public ClientboundUpdateAttributesPacket(final int integer, final Collection<AttributeInstance> collection) {
        this.attributes = (List<AttributeSnapshot>)Lists.newArrayList();
        this.entityId = integer;
        for (final AttributeInstance are5 : collection) {
            this.attributes.add(new AttributeSnapshot(are5.getAttribute(), are5.getBaseValue(), (Collection<AttributeModifier>)are5.getModifiers()));
        }
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.entityId = nf.readVarInt();
        for (int integer3 = nf.readInt(), integer4 = 0; integer4 < integer3; ++integer4) {
            final ResourceLocation vk5 = nf.readResourceLocation();
            final Attribute ard6 = Registry.ATTRIBUTE.get(vk5);
            final double double7 = nf.readDouble();
            final List<AttributeModifier> list9 = (List<AttributeModifier>)Lists.newArrayList();
            for (int integer5 = nf.readVarInt(), integer6 = 0; integer6 < integer5; ++integer6) {
                final UUID uUID12 = nf.readUUID();
                list9.add(new AttributeModifier(uUID12, "Unknown synced attribute modifier", nf.readDouble(), AttributeModifier.Operation.fromValue(nf.readByte())));
            }
            this.attributes.add(new AttributeSnapshot(ard6, double7, (Collection<AttributeModifier>)list9));
        }
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.entityId);
        nf.writeInt(this.attributes.size());
        for (final AttributeSnapshot a4 : this.attributes) {
            nf.writeResourceLocation(Registry.ATTRIBUTE.getKey(a4.getAttribute()));
            nf.writeDouble(a4.getBase());
            nf.writeVarInt(a4.getModifiers().size());
            for (final AttributeModifier arg6 : a4.getModifiers()) {
                nf.writeUUID(arg6.getId());
                nf.writeDouble(arg6.getAmount());
                nf.writeByte(arg6.getOperation().toValue());
            }
        }
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleUpdateAttributes(this);
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public List<AttributeSnapshot> getValues() {
        return this.attributes;
    }
    
    public class AttributeSnapshot {
        private final Attribute attribute;
        private final double base;
        private final Collection<AttributeModifier> modifiers;
        
        public AttributeSnapshot(final Attribute ard, final double double3, final Collection<AttributeModifier> collection) {
            this.attribute = ard;
            this.base = double3;
            this.modifiers = collection;
        }
        
        public Attribute getAttribute() {
            return this.attribute;
        }
        
        public double getBase() {
            return this.base;
        }
        
        public Collection<AttributeModifier> getModifiers() {
            return this.modifiers;
        }
    }
}
