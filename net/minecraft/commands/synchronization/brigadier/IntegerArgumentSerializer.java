package net.minecraft.commands.synchronization.brigadier;

import com.mojang.brigadier.arguments.ArgumentType;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.synchronization.ArgumentSerializer;

public class IntegerArgumentSerializer implements ArgumentSerializer<IntegerArgumentType> {
    public void serializeToNetwork(final IntegerArgumentType integerArgumentType, final FriendlyByteBuf nf) {
        final boolean boolean4 = integerArgumentType.getMinimum() != Integer.MIN_VALUE;
        final boolean boolean5 = integerArgumentType.getMaximum() != Integer.MAX_VALUE;
        nf.writeByte(BrigadierArgumentSerializers.createNumberFlags(boolean4, boolean5));
        if (boolean4) {
            nf.writeInt(integerArgumentType.getMinimum());
        }
        if (boolean5) {
            nf.writeInt(integerArgumentType.getMaximum());
        }
    }
    
    public IntegerArgumentType deserializeFromNetwork(final FriendlyByteBuf nf) {
        final byte byte3 = nf.readByte();
        final int integer4 = BrigadierArgumentSerializers.numberHasMin(byte3) ? nf.readInt() : Integer.MIN_VALUE;
        final int integer5 = BrigadierArgumentSerializers.numberHasMax(byte3) ? nf.readInt() : Integer.MAX_VALUE;
        return IntegerArgumentType.integer(integer4, integer5);
    }
    
    public void serializeToJson(final IntegerArgumentType integerArgumentType, final JsonObject jsonObject) {
        if (integerArgumentType.getMinimum() != Integer.MIN_VALUE) {
            jsonObject.addProperty("min", (Number)integerArgumentType.getMinimum());
        }
        if (integerArgumentType.getMaximum() != Integer.MAX_VALUE) {
            jsonObject.addProperty("max", (Number)integerArgumentType.getMaximum());
        }
    }
}
