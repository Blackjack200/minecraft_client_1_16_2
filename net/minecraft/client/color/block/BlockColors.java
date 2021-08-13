package net.minecraft.client.color.block;

import com.google.common.collect.ImmutableSet;
import javax.annotation.Nullable;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.DoublePlantBlock;
import com.google.common.collect.Maps;
import net.minecraft.world.level.block.state.properties.Property;
import java.util.Set;
import net.minecraft.world.level.block.Block;
import java.util.Map;
import net.minecraft.core.IdMapper;

public class BlockColors {
    private final IdMapper<BlockColor> blockColors;
    private final Map<Block, Set<Property<?>>> coloringStates;
    
    public BlockColors() {
        this.blockColors = new IdMapper<BlockColor>(32);
        this.coloringStates = (Map<Block, Set<Property<?>>>)Maps.newHashMap();
    }
    
    public static BlockColors createDefault() {
        final BlockColors dkl1 = new BlockColors();
        dkl1.register((cee, bqx, fx, integer) -> {
            if (bqx == null || fx == null) {
                return -1;
            }
            else {
                return BiomeColors.getAverageGrassColor(bqx, (cee.<DoubleBlockHalf>getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) ? fx.below() : fx);
            }
        }, Blocks.LARGE_FERN, Blocks.TALL_GRASS);
        dkl1.addColoringState(DoublePlantBlock.HALF, Blocks.LARGE_FERN, Blocks.TALL_GRASS);
        dkl1.register((cee, bqx, fx, integer) -> {
            if (bqx == null || fx == null) {
                return GrassColor.get(0.5, 1.0);
            }
            else {
                return BiomeColors.getAverageGrassColor(bqx, fx);
            }
        }, Blocks.GRASS_BLOCK, Blocks.FERN, Blocks.GRASS, Blocks.POTTED_FERN);
        dkl1.register((cee, bqx, fx, integer) -> FoliageColor.getEvergreenColor(), Blocks.SPRUCE_LEAVES);
        dkl1.register((cee, bqx, fx, integer) -> FoliageColor.getBirchColor(), Blocks.BIRCH_LEAVES);
        dkl1.register((cee, bqx, fx, integer) -> {
            if (bqx == null || fx == null) {
                return FoliageColor.getDefaultColor();
            }
            else {
                return BiomeColors.getAverageFoliageColor(bqx, fx);
            }
        }, Blocks.OAK_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.ACACIA_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.VINE);
        dkl1.register((cee, bqx, fx, integer) -> {
            if (bqx == null || fx == null) {
                return -1;
            }
            else {
                return BiomeColors.getAverageWaterColor(bqx, fx);
            }
        }, Blocks.WATER, Blocks.BUBBLE_COLUMN, Blocks.CAULDRON);
        dkl1.register((cee, bqx, fx, integer) -> RedStoneWireBlock.getColorForPower(cee.<Integer>getValue((Property<Integer>)RedStoneWireBlock.POWER)), Blocks.REDSTONE_WIRE);
        dkl1.addColoringState(RedStoneWireBlock.POWER, Blocks.REDSTONE_WIRE);
        dkl1.register((cee, bqx, fx, integer) -> {
            if (bqx == null || fx == null) {
                return -1;
            }
            else {
                return BiomeColors.getAverageGrassColor(bqx, fx);
            }
        }, Blocks.SUGAR_CANE);
        dkl1.register((cee, bqx, fx, integer) -> 14731036, Blocks.ATTACHED_MELON_STEM, Blocks.ATTACHED_PUMPKIN_STEM);
        final int integer5;
        final int integer6;
        final int integer7;
        final int integer8;
        dkl1.register((cee, bqx, fx, integer) -> {
            integer5 = cee.<Integer>getValue((Property<Integer>)StemBlock.AGE);
            integer6 = integer5 * 32;
            integer7 = 255 - integer5 * 8;
            integer8 = integer5 * 4;
            return integer6 << 16 | integer7 << 8 | integer8;
        }, Blocks.MELON_STEM, Blocks.PUMPKIN_STEM);
        dkl1.addColoringState(StemBlock.AGE, Blocks.MELON_STEM, Blocks.PUMPKIN_STEM);
        dkl1.register((cee, bqx, fx, integer) -> {
            if (bqx == null || fx == null) {
                return 7455580;
            }
            else {
                return 2129968;
            }
        }, Blocks.LILY_PAD);
        return dkl1;
    }
    
    public int getColor(final BlockState cee, final Level bru, final BlockPos fx) {
        final BlockColor dkk5 = this.blockColors.byId(Registry.BLOCK.getId(cee.getBlock()));
        if (dkk5 != null) {
            return dkk5.getColor(cee, null, null, 0);
        }
        final MaterialColor cuy6 = cee.getMapColor(bru, fx);
        return (cuy6 != null) ? cuy6.col : -1;
    }
    
    public int getColor(final BlockState cee, @Nullable final BlockAndTintGetter bqx, @Nullable final BlockPos fx, final int integer) {
        final BlockColor dkk6 = this.blockColors.byId(Registry.BLOCK.getId(cee.getBlock()));
        return (dkk6 == null) ? -1 : dkk6.getColor(cee, bqx, fx, integer);
    }
    
    public void register(final BlockColor dkk, final Block... arr) {
        for (final Block bul7 : arr) {
            this.blockColors.addMapping(dkk, Registry.BLOCK.getId(bul7));
        }
    }
    
    private void addColoringStates(final Set<Property<?>> set, final Block... arr) {
        for (final Block bul7 : arr) {
            this.coloringStates.put(bul7, set);
        }
    }
    
    private void addColoringState(final Property<?> cfg, final Block... arr) {
        this.addColoringStates((Set<Property<?>>)ImmutableSet.of(cfg), arr);
    }
    
    public Set<Property<?>> getColoringProperties(final Block bul) {
        return (Set<Property<?>>)this.coloringStates.getOrDefault(bul, ImmutableSet.of());
    }
}
