package net.minecraft.world.item;

import net.minecraft.resources.ResourceLocation;

public class HorseArmorItem extends Item {
    private final int protection;
    private final String texture;
    
    public HorseArmorItem(final int integer, final String string, final Properties a) {
        super(a);
        this.protection = integer;
        this.texture = "textures/entity/horse/armor/horse_armor_" + string + ".png";
    }
    
    public ResourceLocation getTexture() {
        return new ResourceLocation(this.texture);
    }
    
    public int getProtection() {
        return this.protection;
    }
}
