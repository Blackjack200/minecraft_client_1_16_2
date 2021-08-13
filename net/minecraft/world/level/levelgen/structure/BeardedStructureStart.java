package net.minecraft.world.level.levelgen.structure;

import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public abstract class BeardedStructureStart<C extends FeatureConfiguration> extends StructureStart<C> {
    public BeardedStructureStart(final StructureFeature<C> ckx, final int integer2, final int integer3, final BoundingBox cqx, final int integer5, final long long6) {
        super(ckx, integer2, integer3, cqx, integer5, long6);
    }
    
    @Override
    protected void calculateBoundingBox() {
        super.calculateBoundingBox();
        final int integer2 = 12;
        final BoundingBox boundingBox = this.boundingBox;
        boundingBox.x0 -= 12;
        final BoundingBox boundingBox2 = this.boundingBox;
        boundingBox2.y0 -= 12;
        final BoundingBox boundingBox3 = this.boundingBox;
        boundingBox3.z0 -= 12;
        final BoundingBox boundingBox4 = this.boundingBox;
        boundingBox4.x1 += 12;
        final BoundingBox boundingBox5 = this.boundingBox;
        boundingBox5.y1 += 12;
        final BoundingBox boundingBox6 = this.boundingBox;
        boundingBox6.z1 += 12;
    }
}
