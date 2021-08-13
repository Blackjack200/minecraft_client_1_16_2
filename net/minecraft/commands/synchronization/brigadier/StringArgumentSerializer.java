package net.minecraft.commands.synchronization.brigadier;

import com.mojang.brigadier.arguments.ArgumentType;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.synchronization.ArgumentSerializer;

public class StringArgumentSerializer implements ArgumentSerializer<StringArgumentType> {
    public void serializeToNetwork(final StringArgumentType stringArgumentType, final FriendlyByteBuf nf) {
        nf.writeEnum(stringArgumentType.getType());
    }
    
    public StringArgumentType deserializeFromNetwork(final FriendlyByteBuf nf) {
        final StringArgumentType.StringType stringType3 = nf.<StringArgumentType.StringType>readEnum(StringArgumentType.StringType.class);
        switch (stringType3) {
            case SINGLE_WORD: {
                return StringArgumentType.word();
            }
            case QUOTABLE_PHRASE: {
                return StringArgumentType.string();
            }
            default: {
                return StringArgumentType.greedyString();
            }
        }
    }
    
    public void serializeToJson(final StringArgumentType stringArgumentType, final JsonObject jsonObject) {
        switch (stringArgumentType.getType()) {
            case SINGLE_WORD: {
                jsonObject.addProperty("type", "word");
                break;
            }
            case QUOTABLE_PHRASE: {
                jsonObject.addProperty("type", "phrase");
                break;
            }
            default: {
                jsonObject.addProperty("type", "greedy");
                break;
            }
        }
    }
}
