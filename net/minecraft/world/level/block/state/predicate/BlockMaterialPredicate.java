package net.minecraft.world.level.block.state.predicate;

import javax.annotation.Nullable;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Predicate;

public class BlockMaterialPredicate implements Predicate<BlockState> {
    private static final BlockMaterialPredicate AIR;
    private final Material material;
    
    private BlockMaterialPredicate(final Material cux) {
        this.material = cux;
    }
    
    public static BlockMaterialPredicate forMaterial(final Material cux) {
        return (cux == Material.AIR) ? BlockMaterialPredicate.AIR : new BlockMaterialPredicate(cux);
    }
    
    public boolean test(@Nullable final BlockState cee) {
        return cee != null && cee.getMaterial() == this.material;
    }
    
    static {
        AIR = new BlockMaterialPredicate(Material.AIR) {
            @Override
            public boolean test(@Nullable final BlockState cee) {
                return cee != null && cee.isAir();
            }
        };
    }
}
