package net.minecraft.commands.synchronization.brigadier;

import com.mojang.brigadier.arguments.ArgumentType;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.minecraft.commands.synchronization.ArgumentSerializer;

public class DoubleArgumentSerializer implements ArgumentSerializer<DoubleArgumentType> {
    public void serializeToNetwork(final DoubleArgumentType doubleArgumentType, final FriendlyByteBuf nf) {
        final boolean boolean4 = doubleArgumentType.getMinimum() != -1.7976931348623157E308;
        final boolean boolean5 = doubleArgumentType.getMaximum() != Double.MAX_VALUE;
        nf.writeByte(BrigadierArgumentSerializers.createNumberFlags(boolean4, boolean5));
        if (boolean4) {
            nf.writeDouble(doubleArgumentType.getMinimum());
        }
        if (boolean5) {
            nf.writeDouble(doubleArgumentType.getMaximum());
        }
    }
    
    public DoubleArgumentType deserializeFromNetwork(final FriendlyByteBuf nf) {
        final byte byte3 = nf.readByte();
        final double double4 = BrigadierArgumentSerializers.numberHasMin(byte3) ? nf.readDouble() : -1.7976931348623157E308;
        final double double5 = BrigadierArgumentSerializers.numberHasMax(byte3) ? nf.readDouble() : Double.MAX_VALUE;
        return DoubleArgumentType.doubleArg(double4, double5);
    }
    
    public void serializeToJson(final DoubleArgumentType doubleArgumentType, final JsonObject jsonObject) {
        if (doubleArgumentType.getMinimum() != -1.7976931348623157E308) {
            jsonObject.addProperty("min", (Number)doubleArgumentType.getMinimum());
        }
        if (doubleArgumentType.getMaximum() != Double.MAX_VALUE) {
            jsonObject.addProperty("max", (Number)doubleArgumentType.getMaximum());
        }
    }
}
