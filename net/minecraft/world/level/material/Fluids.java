package net.minecraft.world.level.material;

import com.google.common.collect.UnmodifiableIterator;
import java.util.Iterator;
import net.minecraft.core.Registry;

public class Fluids {
    public static final Fluid EMPTY;
    public static final FlowingFluid FLOWING_WATER;
    public static final FlowingFluid WATER;
    public static final FlowingFluid FLOWING_LAVA;
    public static final FlowingFluid LAVA;
    
    private static <T extends Fluid> T register(final String string, final T cut) {
        return Registry.<T>register(Registry.FLUID, string, cut);
    }
    
    static {
        EMPTY = Fluids.<EmptyFluid>register("empty", new EmptyFluid());
        FLOWING_WATER = Fluids.<WaterFluid.Flowing>register("flowing_water", new WaterFluid.Flowing());
        WATER = Fluids.<WaterFluid.Source>register("water", new WaterFluid.Source());
        FLOWING_LAVA = Fluids.<LavaFluid.Flowing>register("flowing_lava", new LavaFluid.Flowing());
        LAVA = Fluids.<LavaFluid.Source>register("lava", new LavaFluid.Source());
        for (final Fluid cut2 : Registry.FLUID) {
            for (final FluidState cuu4 : cut2.getStateDefinition().getPossibleStates()) {
                Fluid.FLUID_STATE_REGISTRY.add(cuu4);
            }
        }
    }
}
