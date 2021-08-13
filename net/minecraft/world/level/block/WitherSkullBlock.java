package net.minecraft.world.level.block;

import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.predicate.BlockMaterialPredicate;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import java.util.function.Predicate;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.item.Items;
import java.util.Iterator;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.pattern.BlockPattern;

public class WitherSkullBlock extends SkullBlock {
    @Nullable
    private static BlockPattern witherPatternFull;
    @Nullable
    private static BlockPattern witherPatternBase;
    
    protected WitherSkullBlock(final Properties c) {
        super(Types.WITHER_SKELETON, c);
    }
    
    @Override
    public void setPlacedBy(final Level bru, final BlockPos fx, final BlockState cee, @Nullable final LivingEntity aqj, final ItemStack bly) {
        super.setPlacedBy(bru, fx, cee, aqj, bly);
        final BlockEntity ccg7 = bru.getBlockEntity(fx);
        if (ccg7 instanceof SkullBlockEntity) {
            checkSpawn(bru, fx, (SkullBlockEntity)ccg7);
        }
    }
    
    public static void checkSpawn(final Level bru, final BlockPos fx, final SkullBlockEntity cdd) {
        if (bru.isClientSide) {
            return;
        }
        final BlockState cee4 = cdd.getBlockState();
        final boolean boolean5 = cee4.is(Blocks.WITHER_SKELETON_SKULL) || cee4.is(Blocks.WITHER_SKELETON_WALL_SKULL);
        if (!boolean5 || fx.getY() < 0 || bru.getDifficulty() == Difficulty.PEACEFUL) {
            return;
        }
        final BlockPattern cej6 = getOrCreateWitherFull();
        final BlockPattern.BlockPatternMatch b7 = cej6.find(bru, fx);
        if (b7 == null) {
            return;
        }
        for (int integer8 = 0; integer8 < cej6.getWidth(); ++integer8) {
            for (int integer9 = 0; integer9 < cej6.getHeight(); ++integer9) {
                final BlockInWorld cei10 = b7.getBlock(integer8, integer9, 0);
                bru.setBlock(cei10.getPos(), Blocks.AIR.defaultBlockState(), 2);
                bru.levelEvent(2001, cei10.getPos(), Block.getId(cei10.getState()));
            }
        }
        final WitherBoss bci8 = EntityType.WITHER.create(bru);
        final BlockPos fx2 = b7.getBlock(1, 2, 0).getPos();
        bci8.moveTo(fx2.getX() + 0.5, fx2.getY() + 0.55, fx2.getZ() + 0.5, (b7.getForwards().getAxis() == Direction.Axis.X) ? 0.0f : 90.0f, 0.0f);
        bci8.yBodyRot = ((b7.getForwards().getAxis() == Direction.Axis.X) ? 0.0f : 90.0f);
        bci8.makeInvulnerable();
        for (final ServerPlayer aah11 : bru.<Entity>getEntitiesOfClass((java.lang.Class<? extends Entity>)ServerPlayer.class, bci8.getBoundingBox().inflate(50.0))) {
            CriteriaTriggers.SUMMONED_ENTITY.trigger(aah11, bci8);
        }
        bru.addFreshEntity(bci8);
        for (int integer10 = 0; integer10 < cej6.getWidth(); ++integer10) {
            for (int integer11 = 0; integer11 < cej6.getHeight(); ++integer11) {
                bru.blockUpdated(b7.getBlock(integer10, integer11, 0).getPos(), Blocks.AIR);
            }
        }
    }
    
    public static boolean canSpawnMob(final Level bru, final BlockPos fx, final ItemStack bly) {
        return bly.getItem() == Items.WITHER_SKELETON_SKULL && fx.getY() >= 2 && bru.getDifficulty() != Difficulty.PEACEFUL && !bru.isClientSide && getOrCreateWitherBase().find(bru, fx) != null;
    }
    
    private static BlockPattern getOrCreateWitherFull() {
        if (WitherSkullBlock.witherPatternFull == null) {
            WitherSkullBlock.witherPatternFull = BlockPatternBuilder.start().aisle("^^^", "###", "~#~").where('#', (Predicate<BlockInWorld>)(cei -> cei.getState().is(BlockTags.WITHER_SUMMON_BASE_BLOCKS))).where('^', BlockInWorld.hasState((Predicate<BlockState>)BlockStatePredicate.forBlock(Blocks.WITHER_SKELETON_SKULL).or((Predicate)BlockStatePredicate.forBlock(Blocks.WITHER_SKELETON_WALL_SKULL)))).where('~', BlockInWorld.hasState((Predicate<BlockState>)BlockMaterialPredicate.forMaterial(Material.AIR))).build();
        }
        return WitherSkullBlock.witherPatternFull;
    }
    
    private static BlockPattern getOrCreateWitherBase() {
        if (WitherSkullBlock.witherPatternBase == null) {
            WitherSkullBlock.witherPatternBase = BlockPatternBuilder.start().aisle("   ", "###", "~#~").where('#', (Predicate<BlockInWorld>)(cei -> cei.getState().is(BlockTags.WITHER_SUMMON_BASE_BLOCKS))).where('~', BlockInWorld.hasState((Predicate<BlockState>)BlockMaterialPredicate.forMaterial(Material.AIR))).build();
        }
        return WitherSkullBlock.witherPatternBase;
    }
}
