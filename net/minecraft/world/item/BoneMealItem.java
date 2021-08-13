package net.minecraft.world.item;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.LevelReader;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.BaseCoralWallFanBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;

public class BoneMealItem extends Item {
    public BoneMealItem(final Properties a) {
        super(a);
    }
    
    @Override
    public InteractionResult useOn(final UseOnContext bnx) {
        final Level bru3 = bnx.getLevel();
        final BlockPos fx4 = bnx.getClickedPos();
        final BlockPos fx5 = fx4.relative(bnx.getClickedFace());
        if (growCrop(bnx.getItemInHand(), bru3, fx4)) {
            if (!bru3.isClientSide) {
                bru3.levelEvent(2005, fx4, 0);
            }
            return InteractionResult.sidedSuccess(bru3.isClientSide);
        }
        final BlockState cee6 = bru3.getBlockState(fx4);
        final boolean boolean7 = cee6.isFaceSturdy(bru3, fx4, bnx.getClickedFace());
        if (boolean7 && growWaterPlant(bnx.getItemInHand(), bru3, fx5, bnx.getClickedFace())) {
            if (!bru3.isClientSide) {
                bru3.levelEvent(2005, fx5, 0);
            }
            return InteractionResult.sidedSuccess(bru3.isClientSide);
        }
        return InteractionResult.PASS;
    }
    
    public static boolean growCrop(final ItemStack bly, final Level bru, final BlockPos fx) {
        final BlockState cee4 = bru.getBlockState(fx);
        if (cee4.getBlock() instanceof BonemealableBlock) {
            final BonemealableBlock bun5 = (BonemealableBlock)cee4.getBlock();
            if (bun5.isValidBonemealTarget(bru, fx, cee4, bru.isClientSide)) {
                if (bru instanceof ServerLevel) {
                    if (bun5.isBonemealSuccess(bru, bru.random, fx, cee4)) {
                        bun5.performBonemeal((ServerLevel)bru, bru.random, fx, cee4);
                    }
                    bly.shrink(1);
                }
                return true;
            }
        }
        return false;
    }
    
    public static boolean growWaterPlant(final ItemStack bly, final Level bru, final BlockPos fx, @Nullable final Direction gc) {
        if (!bru.getBlockState(fx).is(Blocks.WATER) || bru.getFluidState(fx).getAmount() != 8) {
            return false;
        }
        if (!(bru instanceof ServerLevel)) {
            return true;
        }
        int integer5 = 0;
    Label_0041:
        while (integer5 < 128) {
            BlockPos fx2 = fx;
            BlockState cee7 = Blocks.SEAGRASS.defaultBlockState();
            while (true) {
                for (int integer6 = 0; integer6 < integer5 / 16; ++integer6) {
                    fx2 = fx2.offset(BoneMealItem.random.nextInt(3) - 1, (BoneMealItem.random.nextInt(3) - 1) * BoneMealItem.random.nextInt(3) / 2, BoneMealItem.random.nextInt(3) - 1);
                    if (bru.getBlockState(fx2).isCollisionShapeFullBlock(bru, fx2)) {
                        ++integer5;
                        continue Label_0041;
                    }
                }
                final Optional<ResourceKey<Biome>> optional8 = bru.getBiomeName(fx2);
                if (Objects.equals(optional8, Optional.of((Object)Biomes.WARM_OCEAN)) || Objects.equals(optional8, Optional.of((Object)Biomes.DEEP_WARM_OCEAN))) {
                    if (integer5 == 0 && gc != null && gc.getAxis().isHorizontal()) {
                        cee7 = ((StateHolder<O, BlockState>)BlockTags.WALL_CORALS.getRandomElement(bru.random).defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)BaseCoralWallFanBlock.FACING, gc);
                    }
                    else if (BoneMealItem.random.nextInt(4) == 0) {
                        cee7 = BlockTags.UNDERWATER_BONEMEALS.getRandomElement(BoneMealItem.random).defaultBlockState();
                    }
                }
                if (cee7.getBlock().is(BlockTags.WALL_CORALS)) {
                    for (int integer7 = 0; !cee7.canSurvive(bru, fx2) && integer7 < 4; cee7 = ((StateHolder<O, BlockState>)cee7).<Comparable, Direction>setValue((Property<Comparable>)BaseCoralWallFanBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(BoneMealItem.random)), ++integer7) {}
                }
                if (!cee7.canSurvive(bru, fx2)) {
                    continue;
                }
                final BlockState cee8 = bru.getBlockState(fx2);
                if (cee8.is(Blocks.WATER) && bru.getFluidState(fx2).getAmount() == 8) {
                    bru.setBlock(fx2, cee7, 3);
                    continue;
                }
                if (cee8.is(Blocks.SEAGRASS) && BoneMealItem.random.nextInt(10) == 0) {
                    ((BonemealableBlock)Blocks.SEAGRASS).performBonemeal((ServerLevel)bru, BoneMealItem.random, fx2, cee8);
                }
                continue;
            }
        }
        bly.shrink(1);
        return true;
    }
    
    public static void addGrowthParticles(final LevelAccessor brv, BlockPos fx, int integer) {
        if (integer == 0) {
            integer = 15;
        }
        final BlockState cee4 = brv.getBlockState(fx);
        if (cee4.isAir()) {
            return;
        }
        double double5 = 0.5;
        double double6;
        if (cee4.is(Blocks.WATER)) {
            integer *= 3;
            double6 = 1.0;
            double5 = 3.0;
        }
        else if (cee4.isSolidRender(brv, fx)) {
            fx = fx.above();
            integer *= 3;
            double5 = 3.0;
            double6 = 1.0;
        }
        else {
            double6 = cee4.getShape(brv, fx).max(Direction.Axis.Y);
        }
        brv.addParticle(ParticleTypes.HAPPY_VILLAGER, fx.getX() + 0.5, fx.getY() + 0.5, fx.getZ() + 0.5, 0.0, 0.0, 0.0);
        for (int integer2 = 0; integer2 < integer; ++integer2) {
            final double double7 = BoneMealItem.random.nextGaussian() * 0.02;
            final double double8 = BoneMealItem.random.nextGaussian() * 0.02;
            final double double9 = BoneMealItem.random.nextGaussian() * 0.02;
            final double double10 = 0.5 - double5;
            final double double11 = fx.getX() + double10 + BoneMealItem.random.nextDouble() * double5 * 2.0;
            final double double12 = fx.getY() + BoneMealItem.random.nextDouble() * double6;
            final double double13 = fx.getZ() + double10 + BoneMealItem.random.nextDouble() * double5 * 2.0;
            if (!brv.getBlockState(new BlockPos(double11, double12, double13).below()).isAir()) {
                brv.addParticle(ParticleTypes.HAPPY_VILLAGER, double11, double12, double13, double7, double8, double9);
            }
        }
    }
}
