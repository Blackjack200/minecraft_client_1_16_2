package net.minecraft.world.level.levelgen.surfacebuilders;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;

public class SurfaceBuilderBaseConfiguration implements SurfaceBuilderConfiguration {
    public static final Codec<SurfaceBuilderBaseConfiguration> CODEC;
    private final BlockState topMaterial;
    private final BlockState underMaterial;
    private final BlockState underwaterMaterial;
    
    public SurfaceBuilderBaseConfiguration(final BlockState cee1, final BlockState cee2, final BlockState cee3) {
        this.topMaterial = cee1;
        this.underMaterial = cee2;
        this.underwaterMaterial = cee3;
    }
    
    public BlockState getTopMaterial() {
        return this.topMaterial;
    }
    
    public BlockState getUnderMaterial() {
        return this.underMaterial;
    }
    
    public BlockState getUnderwaterMaterial() {
        return this.underwaterMaterial;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)BlockState.CODEC.fieldOf("top_material").forGetter(ctr -> ctr.topMaterial), (App)BlockState.CODEC.fieldOf("under_material").forGetter(ctr -> ctr.underMaterial), (App)BlockState.CODEC.fieldOf("underwater_material").forGetter(ctr -> ctr.underwaterMaterial)).apply((Applicative)instance, SurfaceBuilderBaseConfiguration::new));
    }
}
