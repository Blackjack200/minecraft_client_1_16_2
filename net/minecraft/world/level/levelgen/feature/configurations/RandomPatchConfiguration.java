package net.minecraft.world.level.levelgen.feature.configurations;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Collection;
import com.google.common.collect.ImmutableSet;
import java.util.stream.Collectors;
import net.minecraft.world.level.block.state.BlockBehaviour;
import java.util.List;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import java.util.Set;
import net.minecraft.world.level.levelgen.feature.blockplacers.BlockPlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import com.mojang.serialization.Codec;

public class RandomPatchConfiguration implements FeatureConfiguration {
    public static final Codec<RandomPatchConfiguration> CODEC;
    public final BlockStateProvider stateProvider;
    public final BlockPlacer blockPlacer;
    public final Set<Block> whitelist;
    public final Set<BlockState> blacklist;
    public final int tries;
    public final int xspread;
    public final int yspread;
    public final int zspread;
    public final boolean canReplace;
    public final boolean project;
    public final boolean needWater;
    
    private RandomPatchConfiguration(final BlockStateProvider cnq, final BlockPlacer cli, final List<BlockState> list3, final List<BlockState> list4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final boolean boolean10, final boolean boolean11) {
        this(cnq, cli, (Set<Block>)list3.stream().map(BlockBehaviour.BlockStateBase::getBlock).collect(Collectors.toSet()), (Set<BlockState>)ImmutableSet.copyOf((Collection)list4), integer5, integer6, integer7, integer8, boolean9, boolean10, boolean11);
    }
    
    private RandomPatchConfiguration(final BlockStateProvider cnq, final BlockPlacer cli, final Set<Block> set3, final Set<BlockState> set4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final boolean boolean10, final boolean boolean11) {
        this.stateProvider = cnq;
        this.blockPlacer = cli;
        this.whitelist = set3;
        this.blacklist = set4;
        this.tries = integer5;
        this.xspread = integer6;
        this.yspread = integer7;
        this.zspread = integer8;
        this.canReplace = boolean9;
        this.project = boolean10;
        this.needWater = boolean11;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)BlockStateProvider.CODEC.fieldOf("state_provider").forGetter(cmk -> cmk.stateProvider), (App)BlockPlacer.CODEC.fieldOf("block_placer").forGetter(cmk -> cmk.blockPlacer), (App)BlockState.CODEC.listOf().fieldOf("whitelist").forGetter(cmk -> (List)cmk.whitelist.stream().map(Block::defaultBlockState).collect(Collectors.toList())), (App)BlockState.CODEC.listOf().fieldOf("blacklist").forGetter(cmk -> ImmutableList.copyOf((Collection)cmk.blacklist)), (App)Codec.INT.fieldOf("tries").orElse(128).forGetter(cmk -> cmk.tries), (App)Codec.INT.fieldOf("xspread").orElse(7).forGetter(cmk -> cmk.xspread), (App)Codec.INT.fieldOf("yspread").orElse(3).forGetter(cmk -> cmk.yspread), (App)Codec.INT.fieldOf("zspread").orElse(7).forGetter(cmk -> cmk.zspread), (App)Codec.BOOL.fieldOf("can_replace").orElse(false).forGetter(cmk -> cmk.canReplace), (App)Codec.BOOL.fieldOf("project").orElse(true).forGetter(cmk -> cmk.project), (App)Codec.BOOL.fieldOf("need_water").orElse(false).forGetter(cmk -> cmk.needWater)).apply((Applicative)instance, RandomPatchConfiguration::new));
    }
    
    public static class GrassConfigurationBuilder {
        private final BlockStateProvider stateProvider;
        private final BlockPlacer blockPlacer;
        private Set<Block> whitelist;
        private Set<BlockState> blacklist;
        private int tries;
        private int xspread;
        private int yspread;
        private int zspread;
        private boolean canReplace;
        private boolean project;
        private boolean needWater;
        
        public GrassConfigurationBuilder(final BlockStateProvider cnq, final BlockPlacer cli) {
            this.whitelist = (Set<Block>)ImmutableSet.of();
            this.blacklist = (Set<BlockState>)ImmutableSet.of();
            this.tries = 64;
            this.xspread = 7;
            this.yspread = 3;
            this.zspread = 7;
            this.project = true;
            this.needWater = false;
            this.stateProvider = cnq;
            this.blockPlacer = cli;
        }
        
        public GrassConfigurationBuilder whitelist(final Set<Block> set) {
            this.whitelist = set;
            return this;
        }
        
        public GrassConfigurationBuilder blacklist(final Set<BlockState> set) {
            this.blacklist = set;
            return this;
        }
        
        public GrassConfigurationBuilder tries(final int integer) {
            this.tries = integer;
            return this;
        }
        
        public GrassConfigurationBuilder xspread(final int integer) {
            this.xspread = integer;
            return this;
        }
        
        public GrassConfigurationBuilder yspread(final int integer) {
            this.yspread = integer;
            return this;
        }
        
        public GrassConfigurationBuilder zspread(final int integer) {
            this.zspread = integer;
            return this;
        }
        
        public GrassConfigurationBuilder canReplace() {
            this.canReplace = true;
            return this;
        }
        
        public GrassConfigurationBuilder noProjection() {
            this.project = false;
            return this;
        }
        
        public GrassConfigurationBuilder needWater() {
            this.needWater = true;
            return this;
        }
        
        public RandomPatchConfiguration build() {
            return new RandomPatchConfiguration(this.stateProvider, this.blockPlacer, this.whitelist, this.blacklist, this.tries, this.xspread, this.yspread, this.zspread, this.canReplace, this.project, this.needWater, null);
        }
    }
}
