package net.minecraft.world.item;

import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.ScaffoldingBlock;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundChatPacket;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;

public class ScaffoldingBlockItem extends BlockItem {
    public ScaffoldingBlockItem(final Block bul, final Properties a) {
        super(bul, a);
    }
    
    @Nullable
    @Override
    public BlockPlaceContext updatePlacementContext(final BlockPlaceContext bnv) {
        final BlockPos fx3 = bnv.getClickedPos();
        final Level bru4 = bnv.getLevel();
        BlockState cee5 = bru4.getBlockState(fx3);
        final Block bul6 = this.getBlock();
        if (cee5.is(bul6)) {
            Direction gc7;
            if (bnv.isSecondaryUseActive()) {
                gc7 = (bnv.isInside() ? bnv.getClickedFace().getOpposite() : bnv.getClickedFace());
            }
            else {
                gc7 = ((bnv.getClickedFace() == Direction.UP) ? bnv.getHorizontalDirection() : Direction.UP);
            }
            int integer8 = 0;
            final BlockPos.MutableBlockPos a9 = fx3.mutable().move(gc7);
            while (integer8 < 7) {
                if (!bru4.isClientSide && !Level.isInWorldBounds(a9)) {
                    final Player bft10 = bnv.getPlayer();
                    final int integer9 = bru4.getMaxBuildHeight();
                    if (bft10 instanceof ServerPlayer && a9.getY() >= integer9) {
                        final ClientboundChatPacket pb12 = new ClientboundChatPacket(new TranslatableComponent("build.tooHigh", new Object[] { integer9 }).withStyle(ChatFormatting.RED), ChatType.GAME_INFO, Util.NIL_UUID);
                        ((ServerPlayer)bft10).connection.send(pb12);
                        break;
                    }
                    break;
                }
                else {
                    cee5 = bru4.getBlockState(a9);
                    if (!cee5.is(this.getBlock())) {
                        if (cee5.canBeReplaced(bnv)) {
                            return BlockPlaceContext.at(bnv, a9, gc7);
                        }
                        break;
                    }
                    else {
                        a9.move(gc7);
                        if (!gc7.getAxis().isHorizontal()) {
                            continue;
                        }
                        ++integer8;
                    }
                }
            }
            return null;
        }
        if (ScaffoldingBlock.getDistance(bru4, fx3) == 7) {
            return null;
        }
        return bnv;
    }
    
    @Override
    protected boolean mustSurvive() {
        return false;
    }
}
