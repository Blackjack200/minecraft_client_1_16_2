package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import java.util.function.Consumer;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class TntBlock extends Block {
    public static final BooleanProperty UNSTABLE;
    
    public TntBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)TntBlock.UNSTABLE, false));
    }
    
    @Override
    public void onPlace(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (cee4.is(cee1.getBlock())) {
            return;
        }
        if (bru.hasNeighborSignal(fx)) {
            explode(bru, fx);
            bru.removeBlock(fx, false);
        }
    }
    
    @Override
    public void neighborChanged(final BlockState cee, final Level bru, final BlockPos fx3, final Block bul, final BlockPos fx5, final boolean boolean6) {
        if (bru.hasNeighborSignal(fx3)) {
            explode(bru, fx3);
            bru.removeBlock(fx3, false);
        }
    }
    
    @Override
    public void playerWillDestroy(final Level bru, final BlockPos fx, final BlockState cee, final Player bft) {
        if (!bru.isClientSide() && !bft.isCreative() && cee.<Boolean>getValue((Property<Boolean>)TntBlock.UNSTABLE)) {
            explode(bru, fx);
        }
        super.playerWillDestroy(bru, fx, cee, bft);
    }
    
    @Override
    public void wasExploded(final Level bru, final BlockPos fx, final Explosion brm) {
        if (bru.isClientSide) {
            return;
        }
        final PrimedTnt bct5 = new PrimedTnt(bru, fx.getX() + 0.5, fx.getY(), fx.getZ() + 0.5, brm.getSourceMob());
        bct5.setFuse((short)(bru.random.nextInt(bct5.getLife() / 4) + bct5.getLife() / 8));
        bru.addFreshEntity(bct5);
    }
    
    public static void explode(final Level bru, final BlockPos fx) {
        explode(bru, fx, null);
    }
    
    private static void explode(final Level bru, final BlockPos fx, @Nullable final LivingEntity aqj) {
        if (bru.isClientSide) {
            return;
        }
        final PrimedTnt bct4 = new PrimedTnt(bru, fx.getX() + 0.5, fx.getY(), fx.getZ() + 0.5, aqj);
        bru.addFreshEntity(bct4);
        bru.playSound(null, bct4.getX(), bct4.getY(), bct4.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0f, 1.0f);
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        final ItemStack bly8 = bft.getItemInHand(aoq);
        final Item blu9 = bly8.getItem();
        if (blu9 == Items.FLINT_AND_STEEL || blu9 == Items.FIRE_CHARGE) {
            explode(bru, fx, bft);
            bru.setBlock(fx, Blocks.AIR.defaultBlockState(), 11);
            if (!bft.isCreative()) {
                if (blu9 == Items.FLINT_AND_STEEL) {
                    bly8.<Player>hurtAndBreak(1, bft, (java.util.function.Consumer<Player>)(bft -> bft.broadcastBreakEvent(aoq)));
                }
                else {
                    bly8.shrink(1);
                }
            }
            return InteractionResult.sidedSuccess(bru.isClientSide);
        }
        return super.use(cee, bru, fx, bft, aoq, dcg);
    }
    
    @Override
    public void onProjectileHit(final Level bru, final BlockState cee, final BlockHitResult dcg, final Projectile bgj) {
        if (!bru.isClientSide) {
            final Entity apx6 = bgj.getOwner();
            if (bgj.isOnFire()) {
                final BlockPos fx7 = dcg.getBlockPos();
                explode(bru, fx7, (apx6 instanceof LivingEntity) ? ((LivingEntity)apx6) : null);
                bru.removeBlock(fx7, false);
            }
        }
    }
    
    @Override
    public boolean dropFromExplosion(final Explosion brm) {
        return false;
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(TntBlock.UNSTABLE);
    }
    
    static {
        UNSTABLE = BlockStateProperties.UNSTABLE;
    }
}
