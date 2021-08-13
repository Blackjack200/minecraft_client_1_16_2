package net.minecraft.data.tags;

import java.nio.file.Path;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.material.Fluid;

public class FluidTagsProvider extends TagsProvider<Fluid> {
    public FluidTagsProvider(final DataGenerator hl) {
        super(hl, Registry.FLUID);
    }
    
    @Override
    protected void addTags() {
        this.tag(FluidTags.WATER).add(Fluids.WATER, Fluids.FLOWING_WATER);
        this.tag(FluidTags.LAVA).add(Fluids.LAVA, Fluids.FLOWING_LAVA);
    }
    
    @Override
    protected Path getPath(final ResourceLocation vk) {
        return this.generator.getOutputFolder().resolve("data/" + vk.getNamespace() + "/tags/fluids/" + vk.getPath() + ".json");
    }
    
    public String getName() {
        return "Fluid Tags";
    }
}
