package net.minecraft.client.gui.font.providers;

import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.common.collect.Maps;
import java.util.HashMap;
import com.google.gson.JsonObject;
import java.util.function.Function;
import java.util.Map;

public enum GlyphProviderBuilderType {
    BITMAP("bitmap", (Function<JsonObject, GlyphProviderBuilder>)BitmapProvider.Builder::fromJson), 
    TTF("ttf", (Function<JsonObject, GlyphProviderBuilder>)TrueTypeGlyphProviderBuilder::fromJson), 
    LEGACY_UNICODE("legacy_unicode", (Function<JsonObject, GlyphProviderBuilder>)LegacyUnicodeBitmapsProvider.Builder::fromJson);
    
    private static final Map<String, GlyphProviderBuilderType> BY_NAME;
    private final String name;
    private final Function<JsonObject, GlyphProviderBuilder> factory;
    
    private GlyphProviderBuilderType(final String string3, final Function<JsonObject, GlyphProviderBuilder> function) {
        this.name = string3;
        this.factory = function;
    }
    
    public static GlyphProviderBuilderType byName(final String string) {
        final GlyphProviderBuilderType dne2 = (GlyphProviderBuilderType)GlyphProviderBuilderType.BY_NAME.get(string);
        if (dne2 == null) {
            throw new IllegalArgumentException("Invalid type: " + string);
        }
        return dne2;
    }
    
    public GlyphProviderBuilder create(final JsonObject jsonObject) {
        return (GlyphProviderBuilder)this.factory.apply(jsonObject);
    }
    
    static {
        BY_NAME = Util.<Map>make((Map)Maps.newHashMap(), (java.util.function.Consumer<Map>)(hashMap -> {
            for (final GlyphProviderBuilderType dne5 : values()) {
                hashMap.put(dne5.name, dne5);
            }
        }));
    }
}
