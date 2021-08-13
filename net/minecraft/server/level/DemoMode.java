package net.minecraft.server.level;

import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;

public class DemoMode extends ServerPlayerGameMode {
    private boolean displayedIntro;
    private boolean demoHasEnded;
    private int demoEndedReminder;
    private int gameModeTicks;
    
    public DemoMode(final ServerLevel aag) {
        super(aag);
    }
    
    @Override
    public void tick() {
        super.tick();
        ++this.gameModeTicks;
        final long long2 = this.level.getGameTime();
        final long long3 = long2 / 24000L + 1L;
        if (!this.displayedIntro && this.gameModeTicks > 20) {
            this.displayedIntro = true;
            this.player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, 0.0f));
        }
        this.demoHasEnded = (long2 > 120500L);
        if (this.demoHasEnded) {
            ++this.demoEndedReminder;
        }
        if (long2 % 24000L == 500L) {
            if (long3 <= 6L) {
                if (long3 == 6L) {
                    this.player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, 104.0f));
                }
                else {
                    this.player.sendMessage(new TranslatableComponent(new StringBuilder().append("demo.day.").append(long3).toString()), Util.NIL_UUID);
                }
            }
        }
        else if (long3 == 1L) {
            if (long2 == 100L) {
                this.player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, 101.0f));
            }
            else if (long2 == 175L) {
                this.player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, 102.0f));
            }
            else if (long2 == 250L) {
                this.player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, 103.0f));
            }
        }
        else if (long3 == 5L && long2 % 24000L == 22000L) {
            this.player.sendMessage(new TranslatableComponent("demo.day.warning"), Util.NIL_UUID);
        }
    }
    
    private void outputDemoReminder() {
        if (this.demoEndedReminder > 100) {
            this.player.sendMessage(new TranslatableComponent("demo.reminder"), Util.NIL_UUID);
            this.demoEndedReminder = 0;
        }
    }
    
    @Override
    public void handleBlockBreakAction(final BlockPos fx, final ServerboundPlayerActionPacket.Action a, final Direction gc, final int integer) {
        if (this.demoHasEnded) {
            this.outputDemoReminder();
            return;
        }
        super.handleBlockBreakAction(fx, a, gc, integer);
    }
    
    @Override
    public InteractionResult useItem(final ServerPlayer aah, final Level bru, final ItemStack bly, final InteractionHand aoq) {
        if (this.demoHasEnded) {
            this.outputDemoReminder();
            return InteractionResult.PASS;
        }
        return super.useItem(aah, bru, bly, aoq);
    }
    
    @Override
    public InteractionResult useItemOn(final ServerPlayer aah, final Level bru, final ItemStack bly, final InteractionHand aoq, final BlockHitResult dcg) {
        if (this.demoHasEnded) {
            this.outputDemoReminder();
            return InteractionResult.PASS;
        }
        return super.useItemOn(aah, bru, bly, aoq, dcg);
    }
}
