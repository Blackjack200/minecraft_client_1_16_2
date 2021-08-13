package net.minecraft.world.level.levelgen.feature.configurations;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSize;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import java.util.List;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import com.mojang.serialization.Codec;

public class TreeConfiguration implements FeatureConfiguration {
    public static final Codec<TreeConfiguration> CODEC;
    public final BlockStateProvider trunkProvider;
    public final BlockStateProvider leavesProvider;
    public final List<TreeDecorator> decorators;
    public transient boolean fromSapling;
    public final FoliagePlacer foliagePlacer;
    public final TrunkPlacer trunkPlacer;
    public final FeatureSize minimumSize;
    public final int maxWaterDepth;
    public final boolean ignoreVines;
    public final Heightmap.Types heightmap;
    
    protected TreeConfiguration(final BlockStateProvider cnq1, final BlockStateProvider cnq2, final FoliagePlacer cni, final TrunkPlacer coy, final FeatureSize cmy, final List<TreeDecorator> list, final int integer, final boolean boolean8, final Heightmap.Types a) {
        this.trunkProvider = cnq1;
        this.leavesProvider = cnq2;
        this.decorators = list;
        this.foliagePlacer = cni;
        this.minimumSize = cmy;
        this.trunkPlacer = coy;
        this.maxWaterDepth = integer;
        this.ignoreVines = boolean8;
        this.heightmap = a;
    }
    
    public void setFromSapling() {
        this.fromSapling = true;
    }
    
    public TreeConfiguration withDecorators(final List<TreeDecorator> list) {
        return new TreeConfiguration(this.trunkProvider, this.leavesProvider, this.foliagePlacer, this.trunkPlacer, this.minimumSize, list, this.maxWaterDepth, this.ignoreVines, this.heightmap);
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter(cmw -> cmw.trunkProvider), (App)BlockStateProvider.CODEC.fieldOf("leaves_provider").forGetter(cmw -> cmw.leavesProvider), (App)FoliagePlacer.CODEC.fieldOf("foliage_placer").forGetter(cmw -> cmw.foliagePlacer), (App)TrunkPlacer.CODEC.fieldOf("trunk_placer").forGetter(cmw -> cmw.trunkPlacer), (App)FeatureSize.CODEC.fieldOf("minimum_size").forGetter(cmw -> cmw.minimumSize), (App)TreeDecorator.CODEC.listOf().fieldOf("decorators").forGetter(cmw -> cmw.decorators), (App)Codec.INT.fieldOf("max_water_depth").orElse(0).forGetter(cmw -> cmw.maxWaterDepth), (App)Codec.BOOL.fieldOf("ignore_vines").orElse(false).forGetter(cmw -> cmw.ignoreVines), (App)Heightmap.Types.CODEC.fieldOf("heightmap").forGetter(cmw -> cmw.heightmap)).apply((Applicative)instance, TreeConfiguration::new));
    }
    
    public static class TreeConfigurationBuilder {
        public final BlockStateProvider trunkProvider;
        public final BlockStateProvider leavesProvider;
        private final FoliagePlacer foliagePlacer;
        private final TrunkPlacer trunkPlacer;
        private final FeatureSize minimumSize;
        private List<TreeDecorator> decorators;
        private int maxWaterDepth;
        private boolean ignoreVines;
        private Heightmap.Types heightmap;
        
        public TreeConfigurationBuilder(final BlockStateProvider cnq1, final BlockStateProvider cnq2, final FoliagePlacer cni, final TrunkPlacer coy, final FeatureSize cmy) {
            this.decorators = (List<TreeDecorator>)ImmutableList.of();
            this.heightmap = Heightmap.Types.OCEAN_FLOOR;
            this.trunkProvider = cnq1;
            this.leavesProvider = cnq2;
            this.foliagePlacer = cni;
            this.trunkPlacer = coy;
            this.minimumSize = cmy;
        }
        
        public TreeConfigurationBuilder decorators(final List<TreeDecorator> list) {
            this.decorators = list;
            return this;
        }
        
        public TreeConfigurationBuilder maxWaterDepth(final int integer) {
            this.maxWaterDepth = integer;
            return this;
        }
        
        public TreeConfigurationBuilder ignoreVines() {
            this.ignoreVines = true;
            return this;
        }
        
        public TreeConfigurationBuilder heightmap(final Heightmap.Types a) {
            this.heightmap = a;
            return this;
        }
        
        public TreeConfiguration build() {
            return new TreeConfiguration(this.trunkProvider, this.leavesProvider, this.foliagePlacer, this.trunkPlacer, this.minimumSize, this.decorators, this.maxWaterDepth, this.ignoreVines, this.heightmap);
        }
    }
}
