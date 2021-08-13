package net.minecraft.world.level.levelgen.feature.trunkplacers;

import net.minecraft.core.Registry;
import com.mojang.serialization.Codec;

public class TrunkPlacerType<P extends TrunkPlacer> {
    public static final TrunkPlacerType<StraightTrunkPlacer> STRAIGHT_TRUNK_PLACER;
    public static final TrunkPlacerType<ForkingTrunkPlacer> FORKING_TRUNK_PLACER;
    public static final TrunkPlacerType<GiantTrunkPlacer> GIANT_TRUNK_PLACER;
    public static final TrunkPlacerType<MegaJungleTrunkPlacer> MEGA_JUNGLE_TRUNK_PLACER;
    public static final TrunkPlacerType<DarkOakTrunkPlacer> DARK_OAK_TRUNK_PLACER;
    public static final TrunkPlacerType<FancyTrunkPlacer> FANCY_TRUNK_PLACER;
    private final Codec<P> codec;
    
    private static <P extends TrunkPlacer> TrunkPlacerType<P> register(final String string, final Codec<P> codec) {
        return Registry.<TrunkPlacerType<P>>register(Registry.TRUNK_PLACER_TYPES, string, new TrunkPlacerType<P>(codec));
    }
    
    private TrunkPlacerType(final Codec<P> codec) {
        this.codec = codec;
    }
    
    public Codec<P> codec() {
        return this.codec;
    }
    
    static {
        STRAIGHT_TRUNK_PLACER = TrunkPlacerType.<StraightTrunkPlacer>register("straight_trunk_placer", StraightTrunkPlacer.CODEC);
        FORKING_TRUNK_PLACER = TrunkPlacerType.<ForkingTrunkPlacer>register("forking_trunk_placer", ForkingTrunkPlacer.CODEC);
        GIANT_TRUNK_PLACER = TrunkPlacerType.<GiantTrunkPlacer>register("giant_trunk_placer", GiantTrunkPlacer.CODEC);
        MEGA_JUNGLE_TRUNK_PLACER = TrunkPlacerType.<MegaJungleTrunkPlacer>register("mega_jungle_trunk_placer", MegaJungleTrunkPlacer.CODEC);
        DARK_OAK_TRUNK_PLACER = TrunkPlacerType.<DarkOakTrunkPlacer>register("dark_oak_trunk_placer", DarkOakTrunkPlacer.CODEC);
        FANCY_TRUNK_PLACER = TrunkPlacerType.<FancyTrunkPlacer>register("fancy_trunk_placer", FancyTrunkPlacer.CODEC);
    }
}
