package net.minecraft.client.gui.font;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.texture.AbstractTexture;
import java.util.Set;
import java.util.Iterator;
import it.unimi.dsi.fastutil.ints.IntSet;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.client.gui.font.glyphs.WhiteGlyph;
import com.mojang.blaze3d.font.RawGlyph;
import net.minecraft.client.gui.font.glyphs.MissingGlyph;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import com.mojang.blaze3d.font.GlyphProvider;
import java.util.List;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.TextureManager;
import java.util.Random;
import com.mojang.blaze3d.font.GlyphInfo;
import net.minecraft.client.gui.font.glyphs.EmptyGlyph;

public class FontSet implements AutoCloseable {
    private static final EmptyGlyph SPACE_GLYPH;
    private static final GlyphInfo SPACE_INFO;
    private static final Random RANDOM;
    private final TextureManager textureManager;
    private final ResourceLocation name;
    private BakedGlyph missingGlyph;
    private BakedGlyph whiteGlyph;
    private final List<GlyphProvider> providers;
    private final Int2ObjectMap<BakedGlyph> glyphs;
    private final Int2ObjectMap<GlyphInfo> glyphInfos;
    private final Int2ObjectMap<IntList> glyphsByWidth;
    private final List<FontTexture> textures;
    
    public FontSet(final TextureManager ejv, final ResourceLocation vk) {
        this.providers = (List<GlyphProvider>)Lists.newArrayList();
        this.glyphs = (Int2ObjectMap<BakedGlyph>)new Int2ObjectOpenHashMap();
        this.glyphInfos = (Int2ObjectMap<GlyphInfo>)new Int2ObjectOpenHashMap();
        this.glyphsByWidth = (Int2ObjectMap<IntList>)new Int2ObjectOpenHashMap();
        this.textures = (List<FontTexture>)Lists.newArrayList();
        this.textureManager = ejv;
        this.name = vk;
    }
    
    public void reload(final List<GlyphProvider> list) {
        this.closeProviders();
        this.closeTextures();
        this.glyphs.clear();
        this.glyphInfos.clear();
        this.glyphsByWidth.clear();
        this.missingGlyph = this.stitch(MissingGlyph.INSTANCE);
        this.whiteGlyph = this.stitch(WhiteGlyph.INSTANCE);
        final IntSet intSet3 = (IntSet)new IntOpenHashSet();
        for (final GlyphProvider ddy5 : list) {
            intSet3.addAll((IntCollection)ddy5.getSupportedGlyphs());
        }
        final Set<GlyphProvider> set4 = (Set<GlyphProvider>)Sets.newHashSet();
        intSet3.forEach(integer -> {
            for (final GlyphProvider ddy6 : list) {
                final GlyphInfo ddx7 = (integer == 32) ? FontSet.SPACE_INFO : ddy6.getGlyph(integer);
                if (ddx7 != null) {
                    set4.add(ddy6);
                    if (ddx7 != MissingGlyph.INSTANCE) {
                        ((IntList)this.glyphsByWidth.computeIfAbsent(Mth.ceil(ddx7.getAdvance(false)), integer -> new IntArrayList())).add(integer);
                        break;
                    }
                    break;
                }
            }
        });
        list.stream().filter(set4::contains).forEach(this.providers::add);
    }
    
    public void close() {
        this.closeProviders();
        this.closeTextures();
    }
    
    private void closeProviders() {
        for (final GlyphProvider ddy3 : this.providers) {
            ddy3.close();
        }
        this.providers.clear();
    }
    
    private void closeTextures() {
        for (final FontTexture dmu3 : this.textures) {
            dmu3.close();
        }
        this.textures.clear();
    }
    
    public GlyphInfo getGlyphInfo(final int integer) {
        return (GlyphInfo)this.glyphInfos.computeIfAbsent(integer, integer -> (integer == 32) ? FontSet.SPACE_INFO : this.getRaw(integer));
    }
    
    private RawGlyph getRaw(final int integer) {
        for (final GlyphProvider ddy4 : this.providers) {
            final RawGlyph ddz5 = ddy4.getGlyph(integer);
            if (ddz5 != null) {
                return ddz5;
            }
        }
        return MissingGlyph.INSTANCE;
    }
    
    public BakedGlyph getGlyph(final int integer) {
        return (BakedGlyph)this.glyphs.computeIfAbsent(integer, integer -> (integer == 32) ? FontSet.SPACE_GLYPH : this.stitch(this.getRaw(integer)));
    }
    
    private BakedGlyph stitch(final RawGlyph ddz) {
        for (final FontTexture dmu4 : this.textures) {
            final BakedGlyph dmw5 = dmu4.add(ddz);
            if (dmw5 != null) {
                return dmw5;
            }
        }
        final FontTexture dmu5 = new FontTexture(new ResourceLocation(this.name.getNamespace(), this.name.getPath() + "/" + this.textures.size()), ddz.isColored());
        this.textures.add(dmu5);
        this.textureManager.register(dmu5.getName(), dmu5);
        final BakedGlyph dmw6 = dmu5.add(ddz);
        return (dmw6 == null) ? this.missingGlyph : dmw6;
    }
    
    public BakedGlyph getRandomGlyph(final GlyphInfo ddx) {
        final IntList intList3 = (IntList)this.glyphsByWidth.get(Mth.ceil(ddx.getAdvance(false)));
        if (intList3 != null && !intList3.isEmpty()) {
            return this.getGlyph(intList3.getInt(FontSet.RANDOM.nextInt(intList3.size())));
        }
        return this.missingGlyph;
    }
    
    public BakedGlyph whiteGlyph() {
        return this.whiteGlyph;
    }
    
    static {
        SPACE_GLYPH = new EmptyGlyph();
        SPACE_INFO = (() -> 4.0f);
        RANDOM = new Random();
    }
}
