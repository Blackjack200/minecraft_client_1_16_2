package net.minecraft.client.gui.font.providers;

import org.apache.logging.log4j.LogManager;
import javax.annotation.Nullable;
import net.minecraft.server.packs.resources.Resource;
import java.nio.ByteBuffer;
import java.nio.Buffer;
import org.lwjgl.system.MemoryUtil;
import com.mojang.blaze3d.font.TrueTypeGlyphProvider;
import java.io.IOException;
import org.lwjgl.stb.STBTruetype;
import com.mojang.blaze3d.platform.TextureUtil;
import org.lwjgl.stb.STBTTFontinfo;
import com.mojang.blaze3d.font.GlyphProvider;
import net.minecraft.server.packs.resources.ResourceManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class TrueTypeGlyphProviderBuilder implements GlyphProviderBuilder {
    private static final Logger LOGGER;
    private final ResourceLocation location;
    private final float size;
    private final float oversample;
    private final float shiftX;
    private final float shiftY;
    private final String skip;
    
    public TrueTypeGlyphProviderBuilder(final ResourceLocation vk, final float float2, final float float3, final float float4, final float float5, final String string) {
        this.location = vk;
        this.size = float2;
        this.oversample = float3;
        this.shiftX = float4;
        this.shiftY = float5;
        this.skip = string;
    }
    
    public static GlyphProviderBuilder fromJson(final JsonObject jsonObject) {
        float float2 = 0.0f;
        float float3 = 0.0f;
        if (jsonObject.has("shift")) {
            final JsonArray jsonArray4 = jsonObject.getAsJsonArray("shift");
            if (jsonArray4.size() != 2) {
                throw new JsonParseException(new StringBuilder().append("Expected 2 elements in 'shift', found ").append(jsonArray4.size()).toString());
            }
            float2 = GsonHelper.convertToFloat(jsonArray4.get(0), "shift[0]");
            float3 = GsonHelper.convertToFloat(jsonArray4.get(1), "shift[1]");
        }
        final StringBuilder stringBuilder4 = new StringBuilder();
        if (jsonObject.has("skip")) {
            final JsonElement jsonElement5 = jsonObject.get("skip");
            if (jsonElement5.isJsonArray()) {
                final JsonArray jsonArray5 = GsonHelper.convertToJsonArray(jsonElement5, "skip");
                for (int integer7 = 0; integer7 < jsonArray5.size(); ++integer7) {
                    stringBuilder4.append(GsonHelper.convertToString(jsonArray5.get(integer7), new StringBuilder().append("skip[").append(integer7).append("]").toString()));
                }
            }
            else {
                stringBuilder4.append(GsonHelper.convertToString(jsonElement5, "skip"));
            }
        }
        return new TrueTypeGlyphProviderBuilder(new ResourceLocation(GsonHelper.getAsString(jsonObject, "file")), GsonHelper.getAsFloat(jsonObject, "size", 11.0f), GsonHelper.getAsFloat(jsonObject, "oversample", 1.0f), float2, float3, stringBuilder4.toString());
    }
    
    @Nullable
    public GlyphProvider create(final ResourceManager acf) {
        STBTTFontinfo sTBTTFontinfo3 = null;
        ByteBuffer byteBuffer4 = null;
        try (final Resource ace5 = acf.getResource(new ResourceLocation(this.location.getNamespace(), "font/" + this.location.getPath()))) {
            TrueTypeGlyphProviderBuilder.LOGGER.debug("Loading font {}", this.location);
            sTBTTFontinfo3 = STBTTFontinfo.malloc();
            byteBuffer4 = TextureUtil.readResource(ace5.getInputStream());
            byteBuffer4.flip();
            TrueTypeGlyphProviderBuilder.LOGGER.debug("Reading font {}", this.location);
            if (!STBTruetype.stbtt_InitFont(sTBTTFontinfo3, byteBuffer4)) {
                throw new IOException("Invalid ttf");
            }
            return new TrueTypeGlyphProvider(byteBuffer4, sTBTTFontinfo3, this.size, this.oversample, this.shiftX, this.shiftY, this.skip);
        }
        catch (Exception exception5) {
            TrueTypeGlyphProviderBuilder.LOGGER.error("Couldn't load truetype font {}", this.location, exception5);
            if (sTBTTFontinfo3 != null) {
                sTBTTFontinfo3.free();
            }
            MemoryUtil.memFree((Buffer)byteBuffer4);
            return null;
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
