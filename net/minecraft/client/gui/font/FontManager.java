package net.minecraft.client.gui.font;

import org.apache.logging.log4j.LogManager;
import java.util.function.Function;
import net.minecraft.client.gui.Font;
import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntSet;
import com.google.gson.JsonArray;
import java.io.InputStream;
import java.util.Iterator;
import com.google.gson.Gson;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import java.io.IOException;
import net.minecraft.client.gui.font.providers.GlyphProviderBuilderType;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonObject;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import net.minecraft.server.packs.resources.Resource;
import java.util.function.Supplier;
import java.util.function.Predicate;
import com.google.gson.GsonBuilder;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.server.packs.resources.ResourceManager;
import com.mojang.blaze3d.font.GlyphProvider;
import java.util.List;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.client.renderer.texture.TextureManager;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class FontManager implements AutoCloseable {
    private static final Logger LOGGER;
    public static final ResourceLocation MISSING_FONT;
    private final FontSet missingFontSet;
    private final Map<ResourceLocation, FontSet> fontSets;
    private final TextureManager textureManager;
    private Map<ResourceLocation, ResourceLocation> renames;
    private final PreparableReloadListener reloadListener;
    
    public FontManager(final TextureManager ejv) {
        this.fontSets = (Map<ResourceLocation, FontSet>)Maps.newHashMap();
        this.renames = (Map<ResourceLocation, ResourceLocation>)ImmutableMap.of();
        this.reloadListener = new SimplePreparableReloadListener<Map<ResourceLocation, List<GlyphProvider>>>() {
            @Override
            protected Map<ResourceLocation, List<GlyphProvider>> prepare(final ResourceManager acf, final ProfilerFiller ant) {
                ant.startTick();
                final Gson gson4 = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
                final Map<ResourceLocation, List<GlyphProvider>> map5 = (Map<ResourceLocation, List<GlyphProvider>>)Maps.newHashMap();
                for (final ResourceLocation vk7 : acf.listResources("font", (Predicate<String>)(string -> string.endsWith(".json")))) {
                    final String string8 = vk7.getPath();
                    final ResourceLocation vk8 = new ResourceLocation(vk7.getNamespace(), string8.substring("font/".length(), string8.length() - ".json".length()));
                    final List<GlyphProvider> list10 = (List<GlyphProvider>)map5.computeIfAbsent(vk8, vk -> Lists.newArrayList((Object[])new GlyphProvider[] { new AllMissingGlyphProvider() }));
                    ant.push((Supplier<String>)vk8::toString);
                    try {
                        for (final Resource ace12 : acf.getResources(vk7)) {
                            ant.push((Supplier<String>)ace12::getSourceName);
                            try (final InputStream inputStream13 = ace12.getInputStream();
                                 final Reader reader15 = (Reader)new BufferedReader((Reader)new InputStreamReader(inputStream13, StandardCharsets.UTF_8))) {
                                ant.push("reading");
                                final JsonArray jsonArray17 = GsonHelper.getAsJsonArray((JsonObject)GsonHelper.<JsonObject>fromJson(gson4, reader15, JsonObject.class), "providers");
                                ant.popPush("parsing");
                                for (int integer18 = jsonArray17.size() - 1; integer18 >= 0; --integer18) {
                                    final JsonObject jsonObject19 = GsonHelper.convertToJsonObject(jsonArray17.get(integer18), new StringBuilder().append("providers[").append(integer18).append("]").toString());
                                    try {
                                        final String string9 = GsonHelper.getAsString(jsonObject19, "type");
                                        final GlyphProviderBuilderType dne21 = GlyphProviderBuilderType.byName(string9);
                                        ant.push(string9);
                                        final GlyphProvider ddy22 = dne21.create(jsonObject19).create(acf);
                                        if (ddy22 != null) {
                                            list10.add(ddy22);
                                        }
                                        ant.pop();
                                    }
                                    catch (RuntimeException runtimeException20) {
                                        FontManager.LOGGER.warn("Unable to read definition '{}' in fonts.json in resourcepack: '{}': {}", vk8, ace12.getSourceName(), runtimeException20.getMessage());
                                    }
                                }
                                ant.pop();
                            }
                            catch (RuntimeException runtimeException21) {
                                FontManager.LOGGER.warn("Unable to load font '{}' in fonts.json in resourcepack: '{}': {}", vk8, ace12.getSourceName(), runtimeException21.getMessage());
                            }
                            ant.pop();
                        }
                    }
                    catch (IOException iOException11) {
                        FontManager.LOGGER.warn("Unable to load font '{}' in fonts.json: {}", vk8, iOException11.getMessage());
                    }
                    ant.push("caching");
                    final IntSet intSet11 = (IntSet)new IntOpenHashSet();
                    for (final GlyphProvider ddy23 : list10) {
                        intSet11.addAll((IntCollection)ddy23.getSupportedGlyphs());
                    }
                    intSet11.forEach(integer -> {
                        if (integer == 32) {
                            return;
                        }
                        for (final GlyphProvider ddy4 : Lists.reverse(list10)) {
                            if (ddy4.getGlyph(integer) != null) {
                                break;
                            }
                        }
                    });
                    ant.pop();
                    ant.pop();
                }
                ant.endTick();
                return map5;
            }
            
            @Override
            protected void apply(final Map<ResourceLocation, List<GlyphProvider>> map, final ResourceManager acf, final ProfilerFiller ant) {
                ant.startTick();
                ant.push("closing");
                FontManager.this.fontSets.values().forEach(FontSet::close);
                FontManager.this.fontSets.clear();
                ant.popPush("reloading");
                map.forEach((vk, list) -> {
                    final FontSet dmt4 = new FontSet(FontManager.this.textureManager, vk);
                    dmt4.reload((List<GlyphProvider>)Lists.reverse(list));
                    FontManager.this.fontSets.put(vk, dmt4);
                });
                ant.pop();
                ant.endTick();
            }
            
            public String getName() {
                return "FontManager";
            }
        };
        this.textureManager = ejv;
        this.missingFontSet = Util.<FontSet>make(new FontSet(ejv, FontManager.MISSING_FONT), (java.util.function.Consumer<FontSet>)(dmt -> dmt.reload((List<GlyphProvider>)Lists.newArrayList((Object[])new GlyphProvider[] { new AllMissingGlyphProvider() }))));
    }
    
    public void setRenames(final Map<ResourceLocation, ResourceLocation> map) {
        this.renames = map;
    }
    
    public Font createFont() {
        return new Font((Function<ResourceLocation, FontSet>)(vk -> (FontSet)this.fontSets.getOrDefault(this.renames.getOrDefault(vk, vk), this.missingFontSet)));
    }
    
    public PreparableReloadListener getReloadListener() {
        return this.reloadListener;
    }
    
    public void close() {
        this.fontSets.values().forEach(FontSet::close);
        this.missingFontSet.close();
    }
    
    static {
        LOGGER = LogManager.getLogger();
        MISSING_FONT = new ResourceLocation("minecraft", "missing");
    }
}
