package net.minecraft.world.level.storage.loot;

import java.util.Random;
import net.minecraft.resources.ResourceLocation;

public interface RandomIntGenerator {
    public static final ResourceLocation CONSTANT = new ResourceLocation("constant");
    public static final ResourceLocation UNIFORM = new ResourceLocation("uniform");
    public static final ResourceLocation BINOMIAL = new ResourceLocation("binomial");
    
    int getInt(final Random random);
    
    ResourceLocation getType();
}
