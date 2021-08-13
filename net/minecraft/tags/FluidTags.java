package net.minecraft.tags;

import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import java.util.List;
import net.minecraft.world.level.material.Fluid;

public final class FluidTags {
    protected static final StaticTagHelper<Fluid> HELPER;
    public static final Tag.Named<Fluid> WATER;
    public static final Tag.Named<Fluid> LAVA;
    
    private static Tag.Named<Fluid> bind(final String string) {
        return FluidTags.HELPER.bind(string);
    }
    
    public static List<? extends Tag.Named<Fluid>> getWrappers() {
        return FluidTags.HELPER.getWrappers();
    }
    
    static {
        HELPER = StaticTags.<Fluid>create(new ResourceLocation("fluid"), (java.util.function.Function<TagContainer, TagCollection<Fluid>>)TagContainer::getFluids);
        WATER = bind("water");
        LAVA = bind("lava");
    }
}
