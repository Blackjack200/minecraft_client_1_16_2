package net.minecraft.server.level;

import org.apache.logging.log4j.LogManager;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.StructureBlock;
import net.minecraft.world.level.block.CommandBlock;
import java.util.Objects;
import net.minecraft.world.level.Level;
import net.minecraft.network.protocol.game.ClientboundBlockBreakAckPacket;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.GameType;
import org.apache.logging.log4j.Logger;

public class ServerPlayerGameMode {
    private static final Logger LOGGER;
    public ServerLevel level;
    public ServerPlayer player;
    private GameType gameModeForPlayer;
    private GameType previousGameModeForPlayer;
    private boolean isDestroyingBlock;
    private int destroyProgressStart;
    private BlockPos destroyPos;
    private int gameTicks;
    private boolean hasDelayedDestroy;
    private BlockPos delayedDestroyPos;
    private int delayedTickStart;
    private int lastSentState;
    
    public ServerPlayerGameMode(final ServerLevel aag) {
        this.gameModeForPlayer = GameType.NOT_SET;
        this.previousGameModeForPlayer = GameType.NOT_SET;
        this.destroyPos = BlockPos.ZERO;
        this.delayedDestroyPos = BlockPos.ZERO;
        this.lastSentState = -1;
        this.level = aag;
    }
    
    public void setGameModeForPlayer(final GameType brr) {
        this.setGameModeForPlayer(brr, (brr != this.gameModeForPlayer) ? this.gameModeForPlayer : this.previousGameModeForPlayer);
    }
    
    public void setGameModeForPlayer(final GameType brr1, final GameType brr2) {
        this.previousGameModeForPlayer = brr2;
        (this.gameModeForPlayer = brr1).updatePlayerAbilities(this.player.abilities);
        this.player.onUpdateAbilities();
        this.player.server.getPlayerList().broadcastAll(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.UPDATE_GAME_MODE, new ServerPlayer[] { this.player }));
        this.level.updateSleepingPlayerList();
    }
    
    public GameType getGameModeForPlayer() {
        return this.gameModeForPlayer;
    }
    
    public GameType getPreviousGameModeForPlayer() {
        return this.previousGameModeForPlayer;
    }
    
    public boolean isSurvival() {
        return this.gameModeForPlayer.isSurvival();
    }
    
    public boolean isCreative() {
        return this.gameModeForPlayer.isCreative();
    }
    
    public void updateGameMode(final GameType brr) {
        if (this.gameModeForPlayer == GameType.NOT_SET) {
            this.gameModeForPlayer = brr;
        }
        this.setGameModeForPlayer(this.gameModeForPlayer);
    }
    
    public void tick() {
        ++this.gameTicks;
        if (this.hasDelayedDestroy) {
            final BlockState cee2 = this.level.getBlockState(this.delayedDestroyPos);
            if (cee2.isAir()) {
                this.hasDelayedDestroy = false;
            }
            else {
                final float float3 = this.incrementDestroyProgress(cee2, this.delayedDestroyPos, this.delayedTickStart);
                if (float3 >= 1.0f) {
                    this.hasDelayedDestroy = false;
                    this.destroyBlock(this.delayedDestroyPos);
                }
            }
        }
        else if (this.isDestroyingBlock) {
            final BlockState cee2 = this.level.getBlockState(this.destroyPos);
            if (cee2.isAir()) {
                this.level.destroyBlockProgress(this.player.getId(), this.destroyPos, -1);
                this.lastSentState = -1;
                this.isDestroyingBlock = false;
            }
            else {
                this.incrementDestroyProgress(cee2, this.destroyPos, this.destroyProgressStart);
            }
        }
    }
    
    private float incrementDestroyProgress(final BlockState cee, final BlockPos fx, final int integer) {
        final int integer2 = this.gameTicks - integer;
        final float float6 = cee.getDestroyProgress(this.player, this.player.level, fx) * (integer2 + 1);
        final int integer3 = (int)(float6 * 10.0f);
        if (integer3 != this.lastSentState) {
            this.level.destroyBlockProgress(this.player.getId(), fx, integer3);
            this.lastSentState = integer3;
        }
        return float6;
    }
    
    public void handleBlockBreakAction(final BlockPos fx, final ServerboundPlayerActionPacket.Action a, final Direction gc, final int integer) {
        final double double6 = this.player.getX() - (fx.getX() + 0.5);
        final double double7 = this.player.getY() - (fx.getY() + 0.5) + 1.5;
        final double double8 = this.player.getZ() - (fx.getZ() + 0.5);
        final double double9 = double6 * double6 + double7 * double7 + double8 * double8;
        if (double9 > 36.0) {
            this.player.connection.send(new ClientboundBlockBreakAckPacket(fx, this.level.getBlockState(fx), a, false, "too far"));
            return;
        }
        if (fx.getY() >= integer) {
            this.player.connection.send(new ClientboundBlockBreakAckPacket(fx, this.level.getBlockState(fx), a, false, "too high"));
            return;
        }
        if (a == ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK) {
            if (!this.level.mayInteract(this.player, fx)) {
                this.player.connection.send(new ClientboundBlockBreakAckPacket(fx, this.level.getBlockState(fx), a, false, "may not interact"));
                return;
            }
            if (this.isCreative()) {
                this.destroyAndAck(fx, a, "creative destroy");
                return;
            }
            if (this.player.blockActionRestricted(this.level, fx, this.gameModeForPlayer)) {
                this.player.connection.send(new ClientboundBlockBreakAckPacket(fx, this.level.getBlockState(fx), a, false, "block action restricted"));
                return;
            }
            this.destroyProgressStart = this.gameTicks;
            float float14 = 1.0f;
            final BlockState cee15 = this.level.getBlockState(fx);
            if (!cee15.isAir()) {
                cee15.attack(this.level, fx, this.player);
                float14 = cee15.getDestroyProgress(this.player, this.player.level, fx);
            }
            if (!cee15.isAir() && float14 >= 1.0f) {
                this.destroyAndAck(fx, a, "insta mine");
            }
            else {
                if (this.isDestroyingBlock) {
                    this.player.connection.send(new ClientboundBlockBreakAckPacket(this.destroyPos, this.level.getBlockState(this.destroyPos), ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, false, "abort destroying since another started (client insta mine, server disagreed)"));
                }
                this.isDestroyingBlock = true;
                this.destroyPos = fx.immutable();
                final int integer2 = (int)(float14 * 10.0f);
                this.level.destroyBlockProgress(this.player.getId(), fx, integer2);
                this.player.connection.send(new ClientboundBlockBreakAckPacket(fx, this.level.getBlockState(fx), a, true, "actual start of destroying"));
                this.lastSentState = integer2;
            }
        }
        else if (a == ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK) {
            if (fx.equals(this.destroyPos)) {
                final int integer3 = this.gameTicks - this.destroyProgressStart;
                final BlockState cee15 = this.level.getBlockState(fx);
                if (!cee15.isAir()) {
                    final float float15 = cee15.getDestroyProgress(this.player, this.player.level, fx) * (integer3 + 1);
                    if (float15 >= 0.7f) {
                        this.isDestroyingBlock = false;
                        this.level.destroyBlockProgress(this.player.getId(), fx, -1);
                        this.destroyAndAck(fx, a, "destroyed");
                        return;
                    }
                    if (!this.hasDelayedDestroy) {
                        this.isDestroyingBlock = false;
                        this.hasDelayedDestroy = true;
                        this.delayedDestroyPos = fx;
                        this.delayedTickStart = this.destroyProgressStart;
                    }
                }
            }
            this.player.connection.send(new ClientboundBlockBreakAckPacket(fx, this.level.getBlockState(fx), a, true, "stopped destroying"));
        }
        else if (a == ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK) {
            this.isDestroyingBlock = false;
            if (!Objects.equals(this.destroyPos, fx)) {
                ServerPlayerGameMode.LOGGER.warn(new StringBuilder().append("Mismatch in destroy block pos: ").append(this.destroyPos).append(" ").append(fx).toString());
                this.level.destroyBlockProgress(this.player.getId(), this.destroyPos, -1);
                this.player.connection.send(new ClientboundBlockBreakAckPacket(this.destroyPos, this.level.getBlockState(this.destroyPos), a, true, "aborted mismatched destroying"));
            }
            this.level.destroyBlockProgress(this.player.getId(), fx, -1);
            this.player.connection.send(new ClientboundBlockBreakAckPacket(fx, this.level.getBlockState(fx), a, true, "aborted destroying"));
        }
    }
    
    public void destroyAndAck(final BlockPos fx, final ServerboundPlayerActionPacket.Action a, final String string) {
        if (this.destroyBlock(fx)) {
            this.player.connection.send(new ClientboundBlockBreakAckPacket(fx, this.level.getBlockState(fx), a, true, string));
        }
        else {
            this.player.connection.send(new ClientboundBlockBreakAckPacket(fx, this.level.getBlockState(fx), a, false, string));
        }
    }
    
    public boolean destroyBlock(final BlockPos fx) {
        final BlockState cee3 = this.level.getBlockState(fx);
        if (!this.player.getMainHandItem().getItem().canAttackBlock(cee3, this.level, fx, this.player)) {
            return false;
        }
        final BlockEntity ccg4 = this.level.getBlockEntity(fx);
        final Block bul5 = cee3.getBlock();
        if ((bul5 instanceof CommandBlock || bul5 instanceof StructureBlock || bul5 instanceof JigsawBlock) && !this.player.canUseGameMasterBlocks()) {
            this.level.sendBlockUpdated(fx, cee3, cee3, 3);
            return false;
        }
        if (this.player.blockActionRestricted(this.level, fx, this.gameModeForPlayer)) {
            return false;
        }
        bul5.playerWillDestroy(this.level, fx, cee3, this.player);
        final boolean boolean6 = this.level.removeBlock(fx, false);
        if (boolean6) {
            bul5.destroy(this.level, fx, cee3);
        }
        if (this.isCreative()) {
            return true;
        }
        final ItemStack bly7 = this.player.getMainHandItem();
        final ItemStack bly8 = bly7.copy();
        final boolean boolean7 = this.player.hasCorrectToolForDrops(cee3);
        bly7.mineBlock(this.level, cee3, fx, this.player);
        if (boolean6 && boolean7) {
            bul5.playerDestroy(this.level, this.player, fx, cee3, ccg4, bly8);
        }
        return true;
    }
    
    public InteractionResult useItem(final ServerPlayer aah, final Level bru, final ItemStack bly, final InteractionHand aoq) {
        if (this.gameModeForPlayer == GameType.SPECTATOR) {
            return InteractionResult.PASS;
        }
        if (aah.getCooldowns().isOnCooldown(bly.getItem())) {
            return InteractionResult.PASS;
        }
        final int integer6 = bly.getCount();
        final int integer7 = bly.getDamageValue();
        final InteractionResultHolder<ItemStack> aos8 = bly.use(bru, aah, aoq);
        final ItemStack bly2 = aos8.getObject();
        if (bly2 == bly && bly2.getCount() == integer6 && bly2.getUseDuration() <= 0 && bly2.getDamageValue() == integer7) {
            return aos8.getResult();
        }
        if (aos8.getResult() == InteractionResult.FAIL && bly2.getUseDuration() > 0 && !aah.isUsingItem()) {
            return aos8.getResult();
        }
        aah.setItemInHand(aoq, bly2);
        if (this.isCreative()) {
            bly2.setCount(integer6);
            if (bly2.isDamageableItem() && bly2.getDamageValue() != integer7) {
                bly2.setDamageValue(integer7);
            }
        }
        if (bly2.isEmpty()) {
            aah.setItemInHand(aoq, ItemStack.EMPTY);
        }
        if (!aah.isUsingItem()) {
            aah.refreshContainer(aah.inventoryMenu);
        }
        return aos8.getResult();
    }
    
    public InteractionResult useItemOn(final ServerPlayer aah, final Level bru, final ItemStack bly, final InteractionHand aoq, final BlockHitResult dcg) {
        final BlockPos fx7 = dcg.getBlockPos();
        final BlockState cee8 = bru.getBlockState(fx7);
        if (this.gameModeForPlayer == GameType.SPECTATOR) {
            final MenuProvider aou9 = cee8.getMenuProvider(bru, fx7);
            if (aou9 != null) {
                aah.openMenu(aou9);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }
        else {
            final boolean boolean9 = !aah.getMainHandItem().isEmpty() || !aah.getOffhandItem().isEmpty();
            final boolean boolean10 = aah.isSecondaryUseActive() && boolean9;
            final ItemStack bly2 = bly.copy();
            if (!boolean10) {
                final InteractionResult aor12 = cee8.use(bru, aah, aoq, dcg);
                if (aor12.consumesAction()) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(aah, fx7, bly2);
                    return aor12;
                }
            }
            if (bly.isEmpty() || aah.getCooldowns().isOnCooldown(bly.getItem())) {
                return InteractionResult.PASS;
            }
            final UseOnContext bnx12 = new UseOnContext(aah, aoq, dcg);
            InteractionResult aor13;
            if (this.isCreative()) {
                final int integer14 = bly.getCount();
                aor13 = bly.useOn(bnx12);
                bly.setCount(integer14);
            }
            else {
                aor13 = bly.useOn(bnx12);
            }
            if (aor13.consumesAction()) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(aah, fx7, bly2);
            }
            return aor13;
        }
    }
    
    public void setLevel(final ServerLevel aag) {
        this.level = aag;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
