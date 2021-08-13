package net.minecraft.world.level.block.entity;

import java.util.Iterator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import com.google.common.collect.Lists;
import net.minecraft.world.item.DyeColor;
import org.apache.commons.lang3.tuple.Pair;
import java.util.List;
import java.util.Arrays;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;

public enum BannerPattern {
    BASE("base", "b", false), 
    SQUARE_BOTTOM_LEFT("square_bottom_left", "bl"), 
    SQUARE_BOTTOM_RIGHT("square_bottom_right", "br"), 
    SQUARE_TOP_LEFT("square_top_left", "tl"), 
    SQUARE_TOP_RIGHT("square_top_right", "tr"), 
    STRIPE_BOTTOM("stripe_bottom", "bs"), 
    STRIPE_TOP("stripe_top", "ts"), 
    STRIPE_LEFT("stripe_left", "ls"), 
    STRIPE_RIGHT("stripe_right", "rs"), 
    STRIPE_CENTER("stripe_center", "cs"), 
    STRIPE_MIDDLE("stripe_middle", "ms"), 
    STRIPE_DOWNRIGHT("stripe_downright", "drs"), 
    STRIPE_DOWNLEFT("stripe_downleft", "dls"), 
    STRIPE_SMALL("small_stripes", "ss"), 
    CROSS("cross", "cr"), 
    STRAIGHT_CROSS("straight_cross", "sc"), 
    TRIANGLE_BOTTOM("triangle_bottom", "bt"), 
    TRIANGLE_TOP("triangle_top", "tt"), 
    TRIANGLES_BOTTOM("triangles_bottom", "bts"), 
    TRIANGLES_TOP("triangles_top", "tts"), 
    DIAGONAL_LEFT("diagonal_left", "ld"), 
    DIAGONAL_RIGHT("diagonal_up_right", "rd"), 
    DIAGONAL_LEFT_MIRROR("diagonal_up_left", "lud"), 
    DIAGONAL_RIGHT_MIRROR("diagonal_right", "rud"), 
    CIRCLE_MIDDLE("circle", "mc"), 
    RHOMBUS_MIDDLE("rhombus", "mr"), 
    HALF_VERTICAL("half_vertical", "vh"), 
    HALF_HORIZONTAL("half_horizontal", "hh"), 
    HALF_VERTICAL_MIRROR("half_vertical_right", "vhr"), 
    HALF_HORIZONTAL_MIRROR("half_horizontal_bottom", "hhb"), 
    BORDER("border", "bo"), 
    CURLY_BORDER("curly_border", "cbo"), 
    GRADIENT("gradient", "gra"), 
    GRADIENT_UP("gradient_up", "gru"), 
    BRICKS("bricks", "bri"), 
    GLOBE("globe", "glb", true), 
    CREEPER("creeper", "cre", true), 
    SKULL("skull", "sku", true), 
    FLOWER("flower", "flo", true), 
    MOJANG("mojang", "moj", true), 
    PIGLIN("piglin", "pig", true);
    
    private static final BannerPattern[] VALUES;
    public static final int COUNT;
    public static final int PATTERN_ITEM_COUNT;
    public static final int AVAILABLE_PATTERNS;
    private final boolean hasPatternItem;
    private final String filename;
    private final String hashname;
    
    private BannerPattern(final String string3, final String string4) {
        this(string3, string4, false);
    }
    
    private BannerPattern(final String string3, final String string4, final boolean boolean5) {
        this.filename = string3;
        this.hashname = string4;
        this.hasPatternItem = boolean5;
    }
    
    public ResourceLocation location(final boolean boolean1) {
        final String string3 = boolean1 ? "banner" : "shield";
        return new ResourceLocation("entity/" + string3 + "/" + this.getFilename());
    }
    
    public String getFilename() {
        return this.filename;
    }
    
    public String getHashname() {
        return this.hashname;
    }
    
    @Nullable
    public static BannerPattern byHash(final String string) {
        for (final BannerPattern cby5 : values()) {
            if (cby5.hashname.equals(string)) {
                return cby5;
            }
        }
        return null;
    }
    
    static {
        VALUES = values();
        COUNT = BannerPattern.VALUES.length;
        PATTERN_ITEM_COUNT = (int)Arrays.stream((Object[])BannerPattern.VALUES).filter(cby -> cby.hasPatternItem).count();
        AVAILABLE_PATTERNS = BannerPattern.COUNT - BannerPattern.PATTERN_ITEM_COUNT - 1;
    }
    
    public static class Builder {
        private final List<Pair<BannerPattern, DyeColor>> patterns;
        
        public Builder() {
            this.patterns = (List<Pair<BannerPattern, DyeColor>>)Lists.newArrayList();
        }
        
        public Builder addPattern(final BannerPattern cby, final DyeColor bku) {
            this.patterns.add(Pair.of((Object)cby, (Object)bku));
            return this;
        }
        
        public ListTag toListTag() {
            final ListTag mj2 = new ListTag();
            for (final Pair<BannerPattern, DyeColor> pair4 : this.patterns) {
                final CompoundTag md5 = new CompoundTag();
                md5.putString("Pattern", ((BannerPattern)pair4.getLeft()).hashname);
                md5.putInt("Color", ((DyeColor)pair4.getRight()).getId());
                mj2.add(md5);
            }
            return mj2;
        }
    }
}
