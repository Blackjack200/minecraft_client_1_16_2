package net.minecraft.world.level;

import net.minecraft.world.level.biome.Biome;

public interface ColorResolver {
    int getColor(final Biome bss, final double double2, final double double3);
}
