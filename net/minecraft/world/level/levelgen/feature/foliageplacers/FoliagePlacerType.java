package net.minecraft.world.level.levelgen.feature.foliageplacers;

import net.minecraft.core.Registry;
import com.mojang.serialization.Codec;

public class FoliagePlacerType<P extends FoliagePlacer> {
    public static final FoliagePlacerType<BlobFoliagePlacer> BLOB_FOLIAGE_PLACER;
    public static final FoliagePlacerType<SpruceFoliagePlacer> SPRUCE_FOLIAGE_PLACER;
    public static final FoliagePlacerType<PineFoliagePlacer> PINE_FOLIAGE_PLACER;
    public static final FoliagePlacerType<AcaciaFoliagePlacer> ACACIA_FOLIAGE_PLACER;
    public static final FoliagePlacerType<BushFoliagePlacer> BUSH_FOLIAGE_PLACER;
    public static final FoliagePlacerType<FancyFoliagePlacer> FANCY_FOLIAGE_PLACER;
    public static final FoliagePlacerType<MegaJungleFoliagePlacer> MEGA_JUNGLE_FOLIAGE_PLACER;
    public static final FoliagePlacerType<MegaPineFoliagePlacer> MEGA_PINE_FOLIAGE_PLACER;
    public static final FoliagePlacerType<DarkOakFoliagePlacer> DARK_OAK_FOLIAGE_PLACER;
    private final Codec<P> codec;
    
    private static <P extends FoliagePlacer> FoliagePlacerType<P> register(final String string, final Codec<P> codec) {
        return Registry.<FoliagePlacerType<P>>register(Registry.FOLIAGE_PLACER_TYPES, string, new FoliagePlacerType<P>(codec));
    }
    
    private FoliagePlacerType(final Codec<P> codec) {
        this.codec = codec;
    }
    
    public Codec<P> codec() {
        return this.codec;
    }
    
    static {
        BLOB_FOLIAGE_PLACER = FoliagePlacerType.<BlobFoliagePlacer>register("blob_foliage_placer", BlobFoliagePlacer.CODEC);
        SPRUCE_FOLIAGE_PLACER = FoliagePlacerType.<SpruceFoliagePlacer>register("spruce_foliage_placer", SpruceFoliagePlacer.CODEC);
        PINE_FOLIAGE_PLACER = FoliagePlacerType.<PineFoliagePlacer>register("pine_foliage_placer", PineFoliagePlacer.CODEC);
        ACACIA_FOLIAGE_PLACER = FoliagePlacerType.<AcaciaFoliagePlacer>register("acacia_foliage_placer", AcaciaFoliagePlacer.CODEC);
        BUSH_FOLIAGE_PLACER = FoliagePlacerType.<BushFoliagePlacer>register("bush_foliage_placer", BushFoliagePlacer.CODEC);
        FANCY_FOLIAGE_PLACER = FoliagePlacerType.<FancyFoliagePlacer>register("fancy_foliage_placer", FancyFoliagePlacer.CODEC);
        MEGA_JUNGLE_FOLIAGE_PLACER = FoliagePlacerType.<MegaJungleFoliagePlacer>register("jungle_foliage_placer", MegaJungleFoliagePlacer.CODEC);
        MEGA_PINE_FOLIAGE_PLACER = FoliagePlacerType.<MegaPineFoliagePlacer>register("mega_pine_foliage_placer", MegaPineFoliagePlacer.CODEC);
        DARK_OAK_FOLIAGE_PLACER = FoliagePlacerType.<DarkOakFoliagePlacer>register("dark_oak_foliage_placer", DarkOakFoliagePlacer.CODEC);
    }
}
