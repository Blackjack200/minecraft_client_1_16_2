package net.minecraft.world.item;

import java.util.stream.Collectors;
import java.util.Map;
import java.util.Comparator;
import java.util.Arrays;
import javax.annotation.Nullable;
import net.minecraft.world.level.material.MaterialColor;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.util.StringRepresentable;

public enum DyeColor implements StringRepresentable {
    WHITE(0, "white", 16383998, MaterialColor.SNOW, 15790320, 16777215), 
    ORANGE(1, "orange", 16351261, MaterialColor.COLOR_ORANGE, 15435844, 16738335), 
    MAGENTA(2, "magenta", 13061821, MaterialColor.COLOR_MAGENTA, 12801229, 16711935), 
    LIGHT_BLUE(3, "light_blue", 3847130, MaterialColor.COLOR_LIGHT_BLUE, 6719955, 10141901), 
    YELLOW(4, "yellow", 16701501, MaterialColor.COLOR_YELLOW, 14602026, 16776960), 
    LIME(5, "lime", 8439583, MaterialColor.COLOR_LIGHT_GREEN, 4312372, 12582656), 
    PINK(6, "pink", 15961002, MaterialColor.COLOR_PINK, 14188952, 16738740), 
    GRAY(7, "gray", 4673362, MaterialColor.COLOR_GRAY, 4408131, 8421504), 
    LIGHT_GRAY(8, "light_gray", 10329495, MaterialColor.COLOR_LIGHT_GRAY, 11250603, 13882323), 
    CYAN(9, "cyan", 1481884, MaterialColor.COLOR_CYAN, 2651799, 65535), 
    PURPLE(10, "purple", 8991416, MaterialColor.COLOR_PURPLE, 8073150, 10494192), 
    BLUE(11, "blue", 3949738, MaterialColor.COLOR_BLUE, 2437522, 255), 
    BROWN(12, "brown", 8606770, MaterialColor.COLOR_BROWN, 5320730, 9127187), 
    GREEN(13, "green", 6192150, MaterialColor.COLOR_GREEN, 3887386, 65280), 
    RED(14, "red", 11546150, MaterialColor.COLOR_RED, 11743532, 16711680), 
    BLACK(15, "black", 1908001, MaterialColor.COLOR_BLACK, 1973019, 0);
    
    private static final DyeColor[] BY_ID;
    private static final Int2ObjectOpenHashMap<DyeColor> BY_FIREWORK_COLOR;
    private final int id;
    private final String name;
    private final MaterialColor color;
    private final int textureDiffuseColor;
    private final int textureDiffuseColorBGR;
    private final float[] textureDiffuseColors;
    private final int fireworkColor;
    private final int textColor;
    
    private DyeColor(final int integer3, final String string4, final int integer5, final MaterialColor cuy, final int integer7, final int integer8) {
        this.id = integer3;
        this.name = string4;
        this.textureDiffuseColor = integer5;
        this.color = cuy;
        this.textColor = integer8;
        final int integer9 = (integer5 & 0xFF0000) >> 16;
        final int integer10 = (integer5 & 0xFF00) >> 8;
        final int integer11 = (integer5 & 0xFF) >> 0;
        this.textureDiffuseColorBGR = (integer11 << 16 | integer10 << 8 | integer9 << 0);
        this.textureDiffuseColors = new float[] { integer9 / 255.0f, integer10 / 255.0f, integer11 / 255.0f };
        this.fireworkColor = integer7;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public float[] getTextureDiffuseColors() {
        return this.textureDiffuseColors;
    }
    
    public MaterialColor getMaterialColor() {
        return this.color;
    }
    
    public int getFireworkColor() {
        return this.fireworkColor;
    }
    
    public int getTextColor() {
        return this.textColor;
    }
    
    public static DyeColor byId(int integer) {
        if (integer < 0 || integer >= DyeColor.BY_ID.length) {
            integer = 0;
        }
        return DyeColor.BY_ID[integer];
    }
    
    public static DyeColor byName(final String string, final DyeColor bku) {
        for (final DyeColor bku2 : values()) {
            if (bku2.name.equals(string)) {
                return bku2;
            }
        }
        return bku;
    }
    
    @Nullable
    public static DyeColor byFireworkColor(final int integer) {
        return (DyeColor)DyeColor.BY_FIREWORK_COLOR.get(integer);
    }
    
    public String toString() {
        return this.name;
    }
    
    public String getSerializedName() {
        return this.name;
    }
    
    static {
        BY_ID = (DyeColor[])Arrays.stream((Object[])values()).sorted(Comparator.comparingInt(DyeColor::getId)).toArray(DyeColor[]::new);
        BY_FIREWORK_COLOR = new Int2ObjectOpenHashMap((Map)Arrays.stream((Object[])values()).collect(Collectors.toMap(bku -> bku.fireworkColor, bku -> bku)));
    }
}
