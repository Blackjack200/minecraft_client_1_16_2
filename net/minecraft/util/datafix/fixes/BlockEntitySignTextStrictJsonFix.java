package net.minecraft.util.datafix.fixes;

import java.util.Iterator;
import com.google.gson.JsonArray;
import net.minecraft.network.chat.MutableComponent;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import com.google.gson.GsonBuilder;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.apache.commons.lang3.StringUtils;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.schemas.Schema;
import com.google.gson.Gson;

public class BlockEntitySignTextStrictJsonFix extends NamedEntityFix {
    public static final Gson GSON;
    
    public BlockEntitySignTextStrictJsonFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2, "BlockEntitySignTextStrictJsonFix", References.BLOCK_ENTITY, "Sign");
    }
    
    private Dynamic<?> updateLine(final Dynamic<?> dynamic, final String string) {
        final String string2 = dynamic.get(string).asString("");
        Component nr5 = null;
        if ("null".equals(string2) || StringUtils.isEmpty((CharSequence)string2)) {
            nr5 = TextComponent.EMPTY;
        }
        else {
            if (string2.charAt(0) != '\"' || string2.charAt(string2.length() - 1) != '\"') {
                if (string2.charAt(0) != '{' || string2.charAt(string2.length() - 1) != '}') {
                    nr5 = new TextComponent(string2);
                    return dynamic.set(string, dynamic.createString(Component.Serializer.toJson(nr5)));
                }
            }
            try {
                nr5 = GsonHelper.<Component>fromJson(BlockEntitySignTextStrictJsonFix.GSON, string2, Component.class, true);
                if (nr5 == null) {
                    nr5 = TextComponent.EMPTY;
                }
            }
            catch (JsonParseException ex) {}
            if (nr5 == null) {
                try {
                    nr5 = Component.Serializer.fromJson(string2);
                }
                catch (JsonParseException ex2) {}
            }
            if (nr5 == null) {
                try {
                    nr5 = Component.Serializer.fromJsonLenient(string2);
                }
                catch (JsonParseException ex3) {}
            }
            if (nr5 == null) {
                nr5 = new TextComponent(string2);
            }
        }
        return dynamic.set(string, dynamic.createString(Component.Serializer.toJson(nr5)));
    }
    
    @Override
    protected Typed<?> fix(final Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), dynamic -> {
            dynamic = this.updateLine(dynamic, "Text1");
            dynamic = this.updateLine(dynamic, "Text2");
            dynamic = this.updateLine(dynamic, "Text3");
            dynamic = this.updateLine(dynamic, "Text4");
            return dynamic;
        });
    }
    
    static {
        GSON = new GsonBuilder().registerTypeAdapter((Type)Component.class, new JsonDeserializer<Component>() {
            public MutableComponent deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                if (jsonElement.isJsonPrimitive()) {
                    return new TextComponent(jsonElement.getAsString());
                }
                if (jsonElement.isJsonArray()) {
                    final JsonArray jsonArray5 = jsonElement.getAsJsonArray();
                    MutableComponent nx6 = null;
                    for (final JsonElement jsonElement2 : jsonArray5) {
                        final MutableComponent nx7 = this.deserialize(jsonElement2, (Type)jsonElement2.getClass(), jsonDeserializationContext);
                        if (nx6 == null) {
                            nx6 = nx7;
                        }
                        else {
                            nx6.append(nx7);
                        }
                    }
                    return nx6;
                }
                throw new JsonParseException(new StringBuilder().append("Don't know how to turn ").append((Object)jsonElement).append(" into a Component").toString());
            }
        }).create();
    }
}
