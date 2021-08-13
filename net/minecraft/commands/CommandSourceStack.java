package net.minecraft.commands;

import com.mojang.brigadier.Message;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.Set;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.stream.Stream;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import com.google.common.collect.Lists;
import java.util.Collection;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.network.chat.TextComponent;
import java.util.Iterator;
import net.minecraft.world.level.GameRules;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.Util;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.level.dimension.DimensionType;
import java.util.function.BinaryOperator;
import net.minecraft.world.phys.Vec2;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import com.mojang.brigadier.ResultConsumer;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class CommandSourceStack implements SharedSuggestionProvider {
    public static final SimpleCommandExceptionType ERROR_NOT_PLAYER;
    public static final SimpleCommandExceptionType ERROR_NOT_ENTITY;
    private final CommandSource source;
    private final Vec3 worldPosition;
    private final ServerLevel level;
    private final int permissionLevel;
    private final String textName;
    private final Component displayName;
    private final MinecraftServer server;
    private final boolean silent;
    @Nullable
    private final Entity entity;
    private final ResultConsumer<CommandSourceStack> consumer;
    private final EntityAnchorArgument.Anchor anchor;
    private final Vec2 rotation;
    
    public CommandSourceStack(final CommandSource da, final Vec3 dck, final Vec2 dcj, final ServerLevel aag, final int integer, final String string, final Component nr, final MinecraftServer minecraftServer, @Nullable final Entity apx) {
        this(da, dck, dcj, aag, integer, string, nr, minecraftServer, apx, false, (ResultConsumer<CommandSourceStack>)((commandContext, boolean2, integer) -> {}), EntityAnchorArgument.Anchor.FEET);
    }
    
    protected CommandSourceStack(final CommandSource da, final Vec3 dck, final Vec2 dcj, final ServerLevel aag, final int integer, final String string, final Component nr, final MinecraftServer minecraftServer, @Nullable final Entity apx, final boolean boolean10, final ResultConsumer<CommandSourceStack> resultConsumer, final EntityAnchorArgument.Anchor a) {
        this.source = da;
        this.worldPosition = dck;
        this.level = aag;
        this.silent = boolean10;
        this.entity = apx;
        this.permissionLevel = integer;
        this.textName = string;
        this.displayName = nr;
        this.server = minecraftServer;
        this.consumer = resultConsumer;
        this.anchor = a;
        this.rotation = dcj;
    }
    
    public CommandSourceStack withEntity(final Entity apx) {
        if (this.entity == apx) {
            return this;
        }
        return new CommandSourceStack(this.source, this.worldPosition, this.rotation, this.level, this.permissionLevel, apx.getName().getString(), apx.getDisplayName(), this.server, apx, this.silent, this.consumer, this.anchor);
    }
    
    public CommandSourceStack withPosition(final Vec3 dck) {
        if (this.worldPosition.equals(dck)) {
            return this;
        }
        return new CommandSourceStack(this.source, dck, this.rotation, this.level, this.permissionLevel, this.textName, this.displayName, this.server, this.entity, this.silent, this.consumer, this.anchor);
    }
    
    public CommandSourceStack withRotation(final Vec2 dcj) {
        if (this.rotation.equals(dcj)) {
            return this;
        }
        return new CommandSourceStack(this.source, this.worldPosition, dcj, this.level, this.permissionLevel, this.textName, this.displayName, this.server, this.entity, this.silent, this.consumer, this.anchor);
    }
    
    public CommandSourceStack withCallback(final ResultConsumer<CommandSourceStack> resultConsumer) {
        if (this.consumer.equals(resultConsumer)) {
            return this;
        }
        return new CommandSourceStack(this.source, this.worldPosition, this.rotation, this.level, this.permissionLevel, this.textName, this.displayName, this.server, this.entity, this.silent, resultConsumer, this.anchor);
    }
    
    public CommandSourceStack withCallback(final ResultConsumer<CommandSourceStack> resultConsumer, final BinaryOperator<ResultConsumer<CommandSourceStack>> binaryOperator) {
        final ResultConsumer<CommandSourceStack> resultConsumer2 = (ResultConsumer<CommandSourceStack>)binaryOperator.apply(this.consumer, resultConsumer);
        return this.withCallback(resultConsumer2);
    }
    
    public CommandSourceStack withSuppressedOutput() {
        if (this.silent) {
            return this;
        }
        return new CommandSourceStack(this.source, this.worldPosition, this.rotation, this.level, this.permissionLevel, this.textName, this.displayName, this.server, this.entity, true, this.consumer, this.anchor);
    }
    
    public CommandSourceStack withPermission(final int integer) {
        if (integer == this.permissionLevel) {
            return this;
        }
        return new CommandSourceStack(this.source, this.worldPosition, this.rotation, this.level, integer, this.textName, this.displayName, this.server, this.entity, this.silent, this.consumer, this.anchor);
    }
    
    public CommandSourceStack withMaximumPermission(final int integer) {
        if (integer <= this.permissionLevel) {
            return this;
        }
        return new CommandSourceStack(this.source, this.worldPosition, this.rotation, this.level, integer, this.textName, this.displayName, this.server, this.entity, this.silent, this.consumer, this.anchor);
    }
    
    public CommandSourceStack withAnchor(final EntityAnchorArgument.Anchor a) {
        if (a == this.anchor) {
            return this;
        }
        return new CommandSourceStack(this.source, this.worldPosition, this.rotation, this.level, this.permissionLevel, this.textName, this.displayName, this.server, this.entity, this.silent, this.consumer, a);
    }
    
    public CommandSourceStack withLevel(final ServerLevel aag) {
        if (aag == this.level) {
            return this;
        }
        final double double3 = DimensionType.getTeleportationScale(this.level.dimensionType(), aag.dimensionType());
        final Vec3 dck5 = new Vec3(this.worldPosition.x * double3, this.worldPosition.y, this.worldPosition.z * double3);
        return new CommandSourceStack(this.source, dck5, this.rotation, aag, this.permissionLevel, this.textName, this.displayName, this.server, this.entity, this.silent, this.consumer, this.anchor);
    }
    
    public CommandSourceStack facing(final Entity apx, final EntityAnchorArgument.Anchor a) throws CommandSyntaxException {
        return this.facing(a.apply(apx));
    }
    
    public CommandSourceStack facing(final Vec3 dck) throws CommandSyntaxException {
        final Vec3 dck2 = this.anchor.apply(this);
        final double double4 = dck.x - dck2.x;
        final double double5 = dck.y - dck2.y;
        final double double6 = dck.z - dck2.z;
        final double double7 = Mth.sqrt(double4 * double4 + double6 * double6);
        final float float12 = Mth.wrapDegrees((float)(-(Mth.atan2(double5, double7) * 57.2957763671875)));
        final float float13 = Mth.wrapDegrees((float)(Mth.atan2(double6, double4) * 57.2957763671875) - 90.0f);
        return this.withRotation(new Vec2(float12, float13));
    }
    
    public Component getDisplayName() {
        return this.displayName;
    }
    
    public String getTextName() {
        return this.textName;
    }
    
    public boolean hasPermission(final int integer) {
        return this.permissionLevel >= integer;
    }
    
    public Vec3 getPosition() {
        return this.worldPosition;
    }
    
    public ServerLevel getLevel() {
        return this.level;
    }
    
    @Nullable
    public Entity getEntity() {
        return this.entity;
    }
    
    public Entity getEntityOrException() throws CommandSyntaxException {
        if (this.entity == null) {
            throw CommandSourceStack.ERROR_NOT_ENTITY.create();
        }
        return this.entity;
    }
    
    public ServerPlayer getPlayerOrException() throws CommandSyntaxException {
        if (!(this.entity instanceof ServerPlayer)) {
            throw CommandSourceStack.ERROR_NOT_PLAYER.create();
        }
        return (ServerPlayer)this.entity;
    }
    
    public Vec2 getRotation() {
        return this.rotation;
    }
    
    public MinecraftServer getServer() {
        return this.server;
    }
    
    public EntityAnchorArgument.Anchor getAnchor() {
        return this.anchor;
    }
    
    public void sendSuccess(final Component nr, final boolean boolean2) {
        if (this.source.acceptsSuccess() && !this.silent) {
            this.source.sendMessage(nr, Util.NIL_UUID);
        }
        if (boolean2 && this.source.shouldInformAdmins() && !this.silent) {
            this.broadcastToAdmins(nr);
        }
    }
    
    private void broadcastToAdmins(final Component nr) {
        final Component nr2 = new TranslatableComponent("chat.type.admin", new Object[] { this.getDisplayName(), nr }).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
        if (this.server.getGameRules().getBoolean(GameRules.RULE_SENDCOMMANDFEEDBACK)) {
            for (final ServerPlayer aah5 : this.server.getPlayerList().getPlayers()) {
                if (aah5 != this.source && this.server.getPlayerList().isOp(aah5.getGameProfile())) {
                    aah5.sendMessage(nr2, Util.NIL_UUID);
                }
            }
        }
        if (this.source != this.server && this.server.getGameRules().getBoolean(GameRules.RULE_LOGADMINCOMMANDS)) {
            this.server.sendMessage(nr2, Util.NIL_UUID);
        }
    }
    
    public void sendFailure(final Component nr) {
        if (this.source.acceptsFailure() && !this.silent) {
            this.source.sendMessage(new TextComponent("").append(nr).withStyle(ChatFormatting.RED), Util.NIL_UUID);
        }
    }
    
    public void onCommandComplete(final CommandContext<CommandSourceStack> commandContext, final boolean boolean2, final int integer) {
        if (this.consumer != null) {
            this.consumer.onCommandComplete((CommandContext)commandContext, boolean2, integer);
        }
    }
    
    public Collection<String> getOnlinePlayerNames() {
        return (Collection<String>)Lists.newArrayList((Object[])this.server.getPlayerNames());
    }
    
    public Collection<String> getAllTeams() {
        return this.server.getScoreboard().getTeamNames();
    }
    
    public Collection<ResourceLocation> getAvailableSoundEvents() {
        return (Collection<ResourceLocation>)Registry.SOUND_EVENT.keySet();
    }
    
    public Stream<ResourceLocation> getRecipeNames() {
        return this.server.getRecipeManager().getRecipeIds();
    }
    
    public CompletableFuture<Suggestions> customSuggestion(final CommandContext<SharedSuggestionProvider> commandContext, final SuggestionsBuilder suggestionsBuilder) {
        return null;
    }
    
    public Set<ResourceKey<Level>> levels() {
        return this.server.levelKeys();
    }
    
    public RegistryAccess registryAccess() {
        return this.server.registryAccess();
    }
    
    static {
        ERROR_NOT_PLAYER = new SimpleCommandExceptionType((Message)new TranslatableComponent("permissions.requires.player"));
        ERROR_NOT_ENTITY = new SimpleCommandExceptionType((Message)new TranslatableComponent("permissions.requires.entity"));
    }
}
