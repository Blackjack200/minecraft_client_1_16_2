package net.minecraft.commands.synchronization;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import com.mojang.brigadier.arguments.ArgumentType;

public interface ArgumentSerializer<T extends ArgumentType<?>> {
    void serializeToNetwork(final T argumentType, final FriendlyByteBuf nf);
    
    T deserializeFromNetwork(final FriendlyByteBuf nf);
    
    void serializeToJson(final T argumentType, final JsonObject jsonObject);
}
