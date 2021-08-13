package net.minecraft.world.level.newbiome.layer;

import org.apache.logging.log4j.LogManager;
import net.minecraft.resources.ResourceKey;
import net.minecraft.Util;
import net.minecraft.SharedConstants;
import net.minecraft.data.worldgen.biome.Biomes;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.Registry;
import net.minecraft.world.level.newbiome.area.AreaFactory;
import net.minecraft.world.level.newbiome.area.LazyArea;
import org.apache.logging.log4j.Logger;

public class Layer {
    private static final Logger LOGGER;
    private final LazyArea area;
    
    public Layer(final AreaFactory<LazyArea> cvd) {
        this.area = cvd.make();
    }
    
    public Biome get(final Registry<Biome> gm, final int integer2, final int integer3) {
        final int integer4 = this.area.get(integer2, integer3);
        final ResourceKey<Biome> vj6 = Biomes.byId(integer4);
        if (vj6 == null) {
            throw new IllegalStateException(new StringBuilder().append("Unknown biome id emitted by layers: ").append(integer4).toString());
        }
        final Biome bss7 = gm.get(vj6);
        if (bss7 != null) {
            return bss7;
        }
        if (SharedConstants.IS_RUNNING_IN_IDE) {
            throw Util.<IllegalStateException>pauseInIde(new IllegalStateException(new StringBuilder().append("Unknown biome id: ").append(integer4).toString()));
        }
        Layer.LOGGER.warn("Unknown biome id: ", integer4);
        return gm.get(Biomes.byId(0));
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
