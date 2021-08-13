package net.minecraft.world.level.newbiome.layer.traits;

public interface DimensionOffset0Transformer extends DimensionTransformer {
    default int getParentX(final int integer) {
        return integer;
    }
    
    default int getParentY(final int integer) {
        return integer;
    }
}
