package net.minecraft.world.level.block.entity;

import net.minecraft.world.phys.Vec2;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import javax.annotation.Nullable;
import java.util.function.Function;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;

public class SignBlockEntity extends BlockEntity {
    private final Component[] messages;
    private boolean isEditable;
    private Player playerWhoMayEdit;
    private final FormattedCharSequence[] renderMessages;
    private DyeColor color;
    
    public SignBlockEntity() {
        super(BlockEntityType.SIGN);
        this.messages = new Component[] { TextComponent.EMPTY, TextComponent.EMPTY, TextComponent.EMPTY, TextComponent.EMPTY };
        this.isEditable = true;
        this.renderMessages = new FormattedCharSequence[4];
        this.color = DyeColor.BLACK;
    }
    
    @Override
    public CompoundTag save(final CompoundTag md) {
        super.save(md);
        for (int integer3 = 0; integer3 < 4; ++integer3) {
            final String string4 = Component.Serializer.toJson(this.messages[integer3]);
            md.putString(new StringBuilder().append("Text").append(integer3 + 1).toString(), string4);
        }
        md.putString("Color", this.color.getName());
        return md;
    }
    
    @Override
    public void load(final BlockState cee, final CompoundTag md) {
        this.isEditable = false;
        super.load(cee, md);
        this.color = DyeColor.byName(md.getString("Color"), DyeColor.BLACK);
        for (int integer4 = 0; integer4 < 4; ++integer4) {
            final String string5 = md.getString(new StringBuilder().append("Text").append(integer4 + 1).toString());
            final Component nr6 = Component.Serializer.fromJson(string5.isEmpty() ? "\"\"" : string5);
            if (this.level instanceof ServerLevel) {
                try {
                    this.messages[integer4] = ComponentUtils.updateForEntity(this.createCommandSourceStack(null), nr6, null, 0);
                }
                catch (CommandSyntaxException commandSyntaxException7) {
                    this.messages[integer4] = nr6;
                }
            }
            else {
                this.messages[integer4] = nr6;
            }
            this.renderMessages[integer4] = null;
        }
    }
    
    public Component getMessage(final int integer) {
        return this.messages[integer];
    }
    
    public void setMessage(final int integer, final Component nr) {
        this.messages[integer] = nr;
        this.renderMessages[integer] = null;
    }
    
    @Nullable
    public FormattedCharSequence getRenderMessage(final int integer, final Function<Component, FormattedCharSequence> function) {
        if (this.renderMessages[integer] == null && this.messages[integer] != null) {
            this.renderMessages[integer] = (FormattedCharSequence)function.apply(this.messages[integer]);
        }
        return this.renderMessages[integer];
    }
    
    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 9, this.getUpdateTag());
    }
    
    @Override
    public CompoundTag getUpdateTag() {
        return this.save(new CompoundTag());
    }
    
    @Override
    public boolean onlyOpCanSetNbt() {
        return true;
    }
    
    public boolean isEditable() {
        return this.isEditable;
    }
    
    public void setEditable(final boolean boolean1) {
        if (!(this.isEditable = boolean1)) {
            this.playerWhoMayEdit = null;
        }
    }
    
    public void setAllowedPlayerEditor(final Player bft) {
        this.playerWhoMayEdit = bft;
    }
    
    public Player getPlayerWhoMayEdit() {
        return this.playerWhoMayEdit;
    }
    
    public boolean executeClickCommands(final Player bft) {
        for (final Component nr6 : this.messages) {
            final Style ob7 = (nr6 == null) ? null : nr6.getStyle();
            if (ob7 != null) {
                if (ob7.getClickEvent() != null) {
                    final ClickEvent np8 = ob7.getClickEvent();
                    if (np8.getAction() == ClickEvent.Action.RUN_COMMAND) {
                        bft.getServer().getCommands().performCommand(this.createCommandSourceStack((ServerPlayer)bft), np8.getValue());
                    }
                }
            }
        }
        return true;
    }
    
    public CommandSourceStack createCommandSourceStack(@Nullable final ServerPlayer aah) {
        final String string3 = (aah == null) ? "Sign" : aah.getName().getString();
        final Component nr4 = (aah == null) ? new TextComponent("Sign") : aah.getDisplayName();
        return new CommandSourceStack(CommandSource.NULL, Vec3.atCenterOf(this.worldPosition), Vec2.ZERO, (ServerLevel)this.level, 2, string3, nr4, this.level.getServer(), aah);
    }
    
    public DyeColor getColor() {
        return this.color;
    }
    
    public boolean setColor(final DyeColor bku) {
        if (bku != this.getColor()) {
            this.color = bku;
            this.setChanged();
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
            return true;
        }
        return false;
    }
}
