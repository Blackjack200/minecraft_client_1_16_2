package net.minecraft.client.gui.screens.inventory;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.Util;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.client.gui.Font;
import java.util.Random;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public class EnchantmentNames {
    private static final ResourceLocation ALT_FONT;
    private static final Style ROOT_STYLE;
    private static final EnchantmentNames INSTANCE;
    private final Random random;
    private final String[] words;
    
    private EnchantmentNames() {
        this.random = new Random();
        this.words = new String[] { "the", "elder", "scrolls", "klaatu", "berata", "niktu", "xyzzy", "bless", "curse", "light", "darkness", "fire", "air", "earth", "water", "hot", "dry", "cold", "wet", "ignite", "snuff", "embiggen", "twist", "shorten", "stretch", "fiddle", "destroy", "imbue", "galvanize", "enchant", "free", "limited", "range", "of", "towards", "inside", "sphere", "cube", "self", "other", "ball", "mental", "physical", "grow", "shrink", "demon", "elemental", "spirit", "animal", "creature", "beast", "humanoid", "undead", "fresh", "stale", "phnglui", "mglwnafh", "cthulhu", "rlyeh", "wgahnagl", "fhtagn", "baguette" };
    }
    
    public static EnchantmentNames getInstance() {
        return EnchantmentNames.INSTANCE;
    }
    
    public FormattedText getRandomName(final Font dkr, final int integer) {
        final StringBuilder stringBuilder4 = new StringBuilder();
        for (int integer2 = this.random.nextInt(2) + 3, integer3 = 0; integer3 < integer2; ++integer3) {
            if (integer3 != 0) {
                stringBuilder4.append(" ");
            }
            stringBuilder4.append((String)Util.<String>getRandom(this.words, this.random));
        }
        return dkr.getSplitter().headByWidth(new TextComponent(stringBuilder4.toString()).withStyle(EnchantmentNames.ROOT_STYLE), integer, Style.EMPTY);
    }
    
    public void initSeed(final long long1) {
        this.random.setSeed(long1);
    }
    
    static {
        ALT_FONT = new ResourceLocation("minecraft", "alt");
        ROOT_STYLE = Style.EMPTY.withFont(EnchantmentNames.ALT_FONT);
        INSTANCE = new EnchantmentNames();
    }
}
