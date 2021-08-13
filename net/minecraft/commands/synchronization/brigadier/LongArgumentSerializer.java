package net.minecraft.commands.synchronization.brigadier;

import com.mojang.brigadier.arguments.ArgumentType;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import com.mojang.brigadier.arguments.LongArgumentType;
import net.minecraft.commands.synchronization.ArgumentSerializer;

public class LongArgumentSerializer implements ArgumentSerializer<LongArgumentType> {
    public void serializeToNetwork(final LongArgumentType longArgumentType, final FriendlyByteBuf nf) {
        final boolean boolean4 = longArgumentType.getMinimum() != Long.MIN_VALUE;
        final boolean boolean5 = longArgumentType.getMaximum() != Long.MAX_VALUE;
        nf.writeByte(BrigadierArgumentSerializers.createNumberFlags(boolean4, boolean5));
        if (boolean4) {
            nf.writeLong(longArgumentType.getMinimum());
        }
        if (boolean5) {
            nf.writeLong(longArgumentType.getMaximum());
        }
    }
    
    public LongArgumentType deserializeFromNetwork(final FriendlyByteBuf nf) {
        final byte byte3 = nf.readByte();
        final long long4 = BrigadierArgumentSerializers.numberHasMin(byte3) ? nf.readLong() : Long.MIN_VALUE;
        final long long5 = BrigadierArgumentSerializers.numberHasMax(byte3) ? nf.readLong() : Long.MAX_VALUE;
        return LongArgumentType.longArg(long4, long5);
    }
    
    public void serializeToJson(final LongArgumentType longArgumentType, final JsonObject jsonObject) {
        if (longArgumentType.getMinimum() != Long.MIN_VALUE) {
            jsonObject.addProperty("min", (Number)longArgumentType.getMinimum());
        }
        if (longArgumentType.getMaximum() != Long.MAX_VALUE) {
            jsonObject.addProperty("max", (Number)longArgumentType.getMaximum());
        }
    }
}
