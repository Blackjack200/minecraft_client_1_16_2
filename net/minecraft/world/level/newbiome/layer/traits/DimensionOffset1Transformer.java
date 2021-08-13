package net.minecraft.world.level.newbiome.layer.traits;

public interface DimensionOffset1Transformer extends DimensionTransformer {
    default int getParentX(final int integer) {
        return integer - 1;
    }
    
    default int getParentY(final int integer) {
        return integer - 1;
    }
}
