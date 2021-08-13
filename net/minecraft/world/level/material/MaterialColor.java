package net.minecraft.world.level.material;

public class MaterialColor {
    public static final MaterialColor[] MATERIAL_COLORS;
    public static final MaterialColor NONE;
    public static final MaterialColor GRASS;
    public static final MaterialColor SAND;
    public static final MaterialColor WOOL;
    public static final MaterialColor FIRE;
    public static final MaterialColor ICE;
    public static final MaterialColor METAL;
    public static final MaterialColor PLANT;
    public static final MaterialColor SNOW;
    public static final MaterialColor CLAY;
    public static final MaterialColor DIRT;
    public static final MaterialColor STONE;
    public static final MaterialColor WATER;
    public static final MaterialColor WOOD;
    public static final MaterialColor QUARTZ;
    public static final MaterialColor COLOR_ORANGE;
    public static final MaterialColor COLOR_MAGENTA;
    public static final MaterialColor COLOR_LIGHT_BLUE;
    public static final MaterialColor COLOR_YELLOW;
    public static final MaterialColor COLOR_LIGHT_GREEN;
    public static final MaterialColor COLOR_PINK;
    public static final MaterialColor COLOR_GRAY;
    public static final MaterialColor COLOR_LIGHT_GRAY;
    public static final MaterialColor COLOR_CYAN;
    public static final MaterialColor COLOR_PURPLE;
    public static final MaterialColor COLOR_BLUE;
    public static final MaterialColor COLOR_BROWN;
    public static final MaterialColor COLOR_GREEN;
    public static final MaterialColor COLOR_RED;
    public static final MaterialColor COLOR_BLACK;
    public static final MaterialColor GOLD;
    public static final MaterialColor DIAMOND;
    public static final MaterialColor LAPIS;
    public static final MaterialColor EMERALD;
    public static final MaterialColor PODZOL;
    public static final MaterialColor NETHER;
    public static final MaterialColor TERRACOTTA_WHITE;
    public static final MaterialColor TERRACOTTA_ORANGE;
    public static final MaterialColor TERRACOTTA_MAGENTA;
    public static final MaterialColor TERRACOTTA_LIGHT_BLUE;
    public static final MaterialColor TERRACOTTA_YELLOW;
    public static final MaterialColor TERRACOTTA_LIGHT_GREEN;
    public static final MaterialColor TERRACOTTA_PINK;
    public static final MaterialColor TERRACOTTA_GRAY;
    public static final MaterialColor TERRACOTTA_LIGHT_GRAY;
    public static final MaterialColor TERRACOTTA_CYAN;
    public static final MaterialColor TERRACOTTA_PURPLE;
    public static final MaterialColor TERRACOTTA_BLUE;
    public static final MaterialColor TERRACOTTA_BROWN;
    public static final MaterialColor TERRACOTTA_GREEN;
    public static final MaterialColor TERRACOTTA_RED;
    public static final MaterialColor TERRACOTTA_BLACK;
    public static final MaterialColor CRIMSON_NYLIUM;
    public static final MaterialColor CRIMSON_STEM;
    public static final MaterialColor CRIMSON_HYPHAE;
    public static final MaterialColor WARPED_NYLIUM;
    public static final MaterialColor WARPED_STEM;
    public static final MaterialColor WARPED_HYPHAE;
    public static final MaterialColor WARPED_WART_BLOCK;
    public final int col;
    public final int id;
    
    private MaterialColor(final int integer1, final int integer2) {
        if (integer1 < 0 || integer1 > 63) {
            throw new IndexOutOfBoundsException("Map colour ID must be between 0 and 63 (inclusive)");
        }
        this.id = integer1;
        this.col = integer2;
        MaterialColor.MATERIAL_COLORS[integer1] = this;
    }
    
    public int calculateRGBColor(final int integer) {
        int integer2 = 220;
        if (integer == 3) {
            integer2 = 135;
        }
        if (integer == 2) {
            integer2 = 255;
        }
        if (integer == 1) {
            integer2 = 220;
        }
        if (integer == 0) {
            integer2 = 180;
        }
        final int integer3 = (this.col >> 16 & 0xFF) * integer2 / 255;
        final int integer4 = (this.col >> 8 & 0xFF) * integer2 / 255;
        final int integer5 = (this.col & 0xFF) * integer2 / 255;
        return 0xFF000000 | integer5 << 16 | integer4 << 8 | integer3;
    }
    
    static {
        MATERIAL_COLORS = new MaterialColor[64];
        NONE = new MaterialColor(0, 0);
        GRASS = new MaterialColor(1, 8368696);
        SAND = new MaterialColor(2, 16247203);
        WOOL = new MaterialColor(3, 13092807);
        FIRE = new MaterialColor(4, 16711680);
        ICE = new MaterialColor(5, 10526975);
        METAL = new MaterialColor(6, 10987431);
        PLANT = new MaterialColor(7, 31744);
        SNOW = new MaterialColor(8, 16777215);
        CLAY = new MaterialColor(9, 10791096);
        DIRT = new MaterialColor(10, 9923917);
        STONE = new MaterialColor(11, 7368816);
        WATER = new MaterialColor(12, 4210943);
        WOOD = new MaterialColor(13, 9402184);
        QUARTZ = new MaterialColor(14, 16776437);
        COLOR_ORANGE = new MaterialColor(15, 14188339);
        COLOR_MAGENTA = new MaterialColor(16, 11685080);
        COLOR_LIGHT_BLUE = new MaterialColor(17, 6724056);
        COLOR_YELLOW = new MaterialColor(18, 15066419);
        COLOR_LIGHT_GREEN = new MaterialColor(19, 8375321);
        COLOR_PINK = new MaterialColor(20, 15892389);
        COLOR_GRAY = new MaterialColor(21, 5000268);
        COLOR_LIGHT_GRAY = new MaterialColor(22, 10066329);
        COLOR_CYAN = new MaterialColor(23, 5013401);
        COLOR_PURPLE = new MaterialColor(24, 8339378);
        COLOR_BLUE = new MaterialColor(25, 3361970);
        COLOR_BROWN = new MaterialColor(26, 6704179);
        COLOR_GREEN = new MaterialColor(27, 6717235);
        COLOR_RED = new MaterialColor(28, 10040115);
        COLOR_BLACK = new MaterialColor(29, 1644825);
        GOLD = new MaterialColor(30, 16445005);
        DIAMOND = new MaterialColor(31, 6085589);
        LAPIS = new MaterialColor(32, 4882687);
        EMERALD = new MaterialColor(33, 55610);
        PODZOL = new MaterialColor(34, 8476209);
        NETHER = new MaterialColor(35, 7340544);
        TERRACOTTA_WHITE = new MaterialColor(36, 13742497);
        TERRACOTTA_ORANGE = new MaterialColor(37, 10441252);
        TERRACOTTA_MAGENTA = new MaterialColor(38, 9787244);
        TERRACOTTA_LIGHT_BLUE = new MaterialColor(39, 7367818);
        TERRACOTTA_YELLOW = new MaterialColor(40, 12223780);
        TERRACOTTA_LIGHT_GREEN = new MaterialColor(41, 6780213);
        TERRACOTTA_PINK = new MaterialColor(42, 10505550);
        TERRACOTTA_GRAY = new MaterialColor(43, 3746083);
        TERRACOTTA_LIGHT_GRAY = new MaterialColor(44, 8874850);
        TERRACOTTA_CYAN = new MaterialColor(45, 5725276);
        TERRACOTTA_PURPLE = new MaterialColor(46, 8014168);
        TERRACOTTA_BLUE = new MaterialColor(47, 4996700);
        TERRACOTTA_BROWN = new MaterialColor(48, 4993571);
        TERRACOTTA_GREEN = new MaterialColor(49, 5001770);
        TERRACOTTA_RED = new MaterialColor(50, 9321518);
        TERRACOTTA_BLACK = new MaterialColor(51, 2430480);
        CRIMSON_NYLIUM = new MaterialColor(52, 12398641);
        CRIMSON_STEM = new MaterialColor(53, 9715553);
        CRIMSON_HYPHAE = new MaterialColor(54, 6035741);
        WARPED_NYLIUM = new MaterialColor(55, 1474182);
        WARPED_STEM = new MaterialColor(56, 3837580);
        WARPED_HYPHAE = new MaterialColor(57, 5647422);
        WARPED_WART_BLOCK = new MaterialColor(58, 1356933);
    }
}
