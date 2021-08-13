package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.apache.logging.log4j.LogManager;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.util.StringUtil;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.apache.logging.log4j.Logger;

public class CommandBlock extends BaseEntityBlock {
    private static final Logger LOGGER;
    public static final DirectionProperty FACING;
    public static final BooleanProperty CONDITIONAL;
    
    public CommandBlock(final Properties c) {
        super(c);
        this.registerDefaultState((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)CommandBlock.FACING, Direction.NORTH)).<Comparable, Boolean>setValue((Property<Comparable>)CommandBlock.CONDITIONAL, false));
    }
    
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        final CommandBlockEntity ccl3 = new CommandBlockEntity();
        ccl3.setAutomatic(this == Blocks.CHAIN_COMMAND_BLOCK);
        return ccl3;
    }
    
    @Override
    public void neighborChanged(final BlockState cee, final Level bru, final BlockPos fx3, final Block bul, final BlockPos fx5, final boolean boolean6) {
        if (bru.isClientSide) {
            return;
        }
        final BlockEntity ccg8 = bru.getBlockEntity(fx3);
        if (!(ccg8 instanceof CommandBlockEntity)) {
            return;
        }
        final CommandBlockEntity ccl9 = (CommandBlockEntity)ccg8;
        final boolean boolean7 = bru.hasNeighborSignal(fx3);
        final boolean boolean8 = ccl9.isPowered();
        ccl9.setPowered(boolean7);
        if (boolean8 || ccl9.isAutomatic() || ccl9.getMode() == CommandBlockEntity.Mode.SEQUENCE) {
            return;
        }
        if (boolean7) {
            ccl9.markConditionMet();
            bru.getBlockTicks().scheduleTick(fx3, this, 1);
        }
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        final BlockEntity ccg6 = aag.getBlockEntity(fx);
        if (ccg6 instanceof CommandBlockEntity) {
            final CommandBlockEntity ccl7 = (CommandBlockEntity)ccg6;
            final BaseCommandBlock bqv8 = ccl7.getCommandBlock();
            final boolean boolean9 = !StringUtil.isNullOrEmpty(bqv8.getCommand());
            final CommandBlockEntity.Mode a10 = ccl7.getMode();
            final boolean boolean10 = ccl7.wasConditionMet();
            if (a10 == CommandBlockEntity.Mode.AUTO) {
                ccl7.markConditionMet();
                if (boolean10) {
                    this.execute(cee, aag, fx, bqv8, boolean9);
                }
                else if (ccl7.isConditional()) {
                    bqv8.setSuccessCount(0);
                }
                if (ccl7.isPowered() || ccl7.isAutomatic()) {
                    aag.getBlockTicks().scheduleTick(fx, this, 1);
                }
            }
            else if (a10 == CommandBlockEntity.Mode.REDSTONE) {
                if (boolean10) {
                    this.execute(cee, aag, fx, bqv8, boolean9);
                }
                else if (ccl7.isConditional()) {
                    bqv8.setSuccessCount(0);
                }
            }
            aag.updateNeighbourForOutputSignal(fx, this);
        }
    }
    
    private void execute(final BlockState cee, final Level bru, final BlockPos fx, final BaseCommandBlock bqv, final boolean boolean5) {
        if (boolean5) {
            bqv.performCommand(bru);
        }
        else {
            bqv.setSuccessCount(0);
        }
        executeChain(bru, fx, cee.<Direction>getValue((Property<Direction>)CommandBlock.FACING));
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        final BlockEntity ccg8 = bru.getBlockEntity(fx);
        if (ccg8 instanceof CommandBlockEntity && bft.canUseGameMasterBlocks()) {
            bft.openCommandBlock((CommandBlockEntity)ccg8);
            return InteractionResult.sidedSuccess(bru.isClientSide);
        }
        return InteractionResult.PASS;
    }
    
    @Override
    public boolean hasAnalogOutputSignal(final BlockState cee) {
        return true;
    }
    
    @Override
    public int getAnalogOutputSignal(final BlockState cee, final Level bru, final BlockPos fx) {
        final BlockEntity ccg5 = bru.getBlockEntity(fx);
        if (ccg5 instanceof CommandBlockEntity) {
            return ((CommandBlockEntity)ccg5).getCommandBlock().getSuccessCount();
        }
        return 0;
    }
    
    @Override
    public void setPlacedBy(final Level bru, final BlockPos fx, final BlockState cee, final LivingEntity aqj, final ItemStack bly) {
        final BlockEntity ccg7 = bru.getBlockEntity(fx);
        if (!(ccg7 instanceof CommandBlockEntity)) {
            return;
        }
        final CommandBlockEntity ccl8 = (CommandBlockEntity)ccg7;
        final BaseCommandBlock bqv9 = ccl8.getCommandBlock();
        if (bly.hasCustomHoverName()) {
            bqv9.setName(bly.getHoverName());
        }
        if (!bru.isClientSide) {
            if (bly.getTagElement("BlockEntityTag") == null) {
                bqv9.setTrackOutput(bru.getGameRules().getBoolean(GameRules.RULE_SENDCOMMANDFEEDBACK));
                ccl8.setAutomatic(this == Blocks.CHAIN_COMMAND_BLOCK);
            }
            if (ccl8.getMode() == CommandBlockEntity.Mode.SEQUENCE) {
                final boolean boolean10 = bru.hasNeighborSignal(fx);
                ccl8.setPowered(boolean10);
            }
        }
    }
    
    @Override
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.MODEL;
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)CommandBlock.FACING, bzj.rotate(cee.<Direction>getValue((Property<Direction>)CommandBlock.FACING)));
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return cee.rotate(byd.getRotation(cee.<Direction>getValue((Property<Direction>)CommandBlock.FACING)));
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(CommandBlock.FACING, CommandBlock.CONDITIONAL);
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        return ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)CommandBlock.FACING, bnv.getNearestLookingDirection().getOpposite());
    }
    
    private static void executeChain(final Level bru, final BlockPos fx, Direction gc) {
        final BlockPos.MutableBlockPos a4 = fx.mutable();
        final GameRules brq5 = bru.getGameRules();
        int integer6 = brq5.getInt(GameRules.RULE_MAX_COMMAND_CHAIN_LENGTH);
        while (integer6-- > 0) {
            a4.move(gc);
            final BlockState cee7 = bru.getBlockState(a4);
            final Block bul8 = cee7.getBlock();
            if (!cee7.is(Blocks.CHAIN_COMMAND_BLOCK)) {
                break;
            }
            final BlockEntity ccg9 = bru.getBlockEntity(a4);
            if (!(ccg9 instanceof CommandBlockEntity)) {
                break;
            }
            final CommandBlockEntity ccl10 = (CommandBlockEntity)ccg9;
            if (ccl10.getMode() != CommandBlockEntity.Mode.SEQUENCE) {
                break;
            }
            if (ccl10.isPowered() || ccl10.isAutomatic()) {
                final BaseCommandBlock bqv11 = ccl10.getCommandBlock();
                if (ccl10.markConditionMet()) {
                    if (!bqv11.performCommand(bru)) {
                        break;
                    }
                    bru.updateNeighbourForOutputSignal(a4, bul8);
                }
                else if (ccl10.isConditional()) {
                    bqv11.setSuccessCount(0);
                }
            }
            gc = cee7.<Direction>getValue((Property<Direction>)CommandBlock.FACING);
        }
        if (integer6 <= 0) {
            final int integer7 = Math.max(brq5.getInt(GameRules.RULE_MAX_COMMAND_CHAIN_LENGTH), 0);
            CommandBlock.LOGGER.warn("Command Block chain tried to execute more than {} steps!", integer7);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        FACING = DirectionalBlock.FACING;
        CONDITIONAL = BlockStateProperties.CONDITIONAL;
    }
}
