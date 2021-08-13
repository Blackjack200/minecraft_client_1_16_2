package net.minecraft.server.network;

import net.minecraft.world.level.block.state.StateHolder;
import org.apache.logging.log4j.LogManager;
import net.minecraft.network.protocol.game.ClientboundCommandSuggestionsPacket;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.network.protocol.game.ServerboundLockDifficultyPacket;
import net.minecraft.network.protocol.game.ServerboundChangeDifficultyPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundClientInformationPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ServerboundKeepAlivePacket;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import net.minecraft.network.protocol.game.ServerboundContainerAckPacket;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.network.protocol.game.ServerboundContainerButtonClickPacket;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.network.protocol.game.ServerboundPlaceRecipePacket;
import net.minecraft.network.protocol.game.ClientboundContainerAckPacket;
import net.minecraft.world.inventory.Slot;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import java.util.Optional;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.CrashReportDetail;
import net.minecraft.CrashReport;
import net.minecraft.world.entity.player.ChatVisiblity;
import javax.annotation.Nullable;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.network.protocol.game.ServerboundPaddleBoatPacket;
import net.minecraft.network.protocol.game.ServerboundResourcePackPacket;
import java.util.Iterator;
import net.minecraft.network.protocol.game.ServerboundTeleportToEntityPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundChatPacket;
import net.minecraft.network.chat.ChatType;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import java.util.Set;
import java.util.Collections;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.stream.Stream;
import net.minecraft.world.phys.shapes.Shapes;
import java.util.function.Predicate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.GameRules;
import net.minecraft.network.protocol.game.ServerboundBlockEntityTagQuery;
import net.minecraft.network.protocol.game.ClientboundTagQueryPacket;
import net.minecraft.network.protocol.game.ServerboundEntityTagQuery;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.WritableBookItem;
import net.minecraft.network.protocol.game.ServerboundEditBookPacket;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.network.protocol.game.ServerboundSelectTradePacket;
import net.minecraft.network.protocol.game.ServerboundJigsawGeneratePacket;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import net.minecraft.network.protocol.game.ServerboundSetJigsawBlockPacket;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.network.protocol.game.ServerboundSetStructureBlockPacket;
import net.minecraft.world.inventory.BeaconMenu;
import net.minecraft.network.protocol.game.ServerboundSetBeaconPacket;
import net.minecraft.SharedConstants;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
import net.minecraft.network.protocol.game.ClientboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ServerboundPickItemPacket;
import net.minecraft.network.protocol.game.ServerboundSetCommandMinecartPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.util.StringUtil;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraft.network.protocol.game.ServerboundSetCommandBlockPacket;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.game.ServerboundSeenAdvancementsPacket;
import net.minecraft.network.protocol.game.ServerboundRecipeBookChangeSettingsPacket;
import net.minecraft.network.protocol.game.ServerboundRecipeBookSeenRecipePacket;
import net.minecraft.network.protocol.game.ServerboundAcceptTeleportationPacket;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MoverType;
import net.minecraft.network.protocol.game.ClientboundMoveVehiclePacket;
import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Doubles;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.protocol.game.ClientboundDisconnectPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundKeepAlivePacket;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.PacketListener;
import it.unimi.dsi.fastutil.ints.Int2ShortOpenHashMap;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import it.unimi.dsi.fastutil.ints.Int2ShortMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.Connection;
import org.apache.logging.log4j.Logger;
import net.minecraft.network.protocol.game.ServerGamePacketListener;

public class ServerGamePacketListenerImpl implements ServerGamePacketListener {
    private static final Logger LOGGER;
    public final Connection connection;
    private final MinecraftServer server;
    public ServerPlayer player;
    private int tickCount;
    private long keepAliveTime;
    private boolean keepAlivePending;
    private long keepAliveChallenge;
    private int chatSpamTickCount;
    private int dropSpamTickCount;
    private final Int2ShortMap expectedAcks;
    private double firstGoodX;
    private double firstGoodY;
    private double firstGoodZ;
    private double lastGoodX;
    private double lastGoodY;
    private double lastGoodZ;
    private Entity lastVehicle;
    private double vehicleFirstGoodX;
    private double vehicleFirstGoodY;
    private double vehicleFirstGoodZ;
    private double vehicleLastGoodX;
    private double vehicleLastGoodY;
    private double vehicleLastGoodZ;
    private Vec3 awaitingPositionFromClient;
    private int awaitingTeleport;
    private int awaitingTeleportTime;
    private boolean clientIsFloating;
    private int aboveGroundTickCount;
    private boolean clientVehicleIsFloating;
    private int aboveGroundVehicleTickCount;
    private int receivedMovePacketCount;
    private int knownMovePacketCount;
    
    public ServerGamePacketListenerImpl(final MinecraftServer minecraftServer, final Connection nd, final ServerPlayer aah) {
        this.expectedAcks = (Int2ShortMap)new Int2ShortOpenHashMap();
        this.server = minecraftServer;
        (this.connection = nd).setListener(this);
        this.player = aah;
        aah.connection = this;
    }
    
    public void tick() {
        this.resetPosition();
        this.player.xo = this.player.getX();
        this.player.yo = this.player.getY();
        this.player.zo = this.player.getZ();
        this.player.doTick();
        this.player.absMoveTo(this.firstGoodX, this.firstGoodY, this.firstGoodZ, this.player.yRot, this.player.xRot);
        ++this.tickCount;
        this.knownMovePacketCount = this.receivedMovePacketCount;
        if (this.clientIsFloating && !this.player.isSleeping()) {
            if (++this.aboveGroundTickCount > 80) {
                ServerGamePacketListenerImpl.LOGGER.warn("{} was kicked for floating too long!", this.player.getName().getString());
                this.disconnect(new TranslatableComponent("multiplayer.disconnect.flying"));
                return;
            }
        }
        else {
            this.clientIsFloating = false;
            this.aboveGroundTickCount = 0;
        }
        this.lastVehicle = this.player.getRootVehicle();
        if (this.lastVehicle == this.player || this.lastVehicle.getControllingPassenger() != this.player) {
            this.lastVehicle = null;
            this.clientVehicleIsFloating = false;
            this.aboveGroundVehicleTickCount = 0;
        }
        else {
            this.vehicleFirstGoodX = this.lastVehicle.getX();
            this.vehicleFirstGoodY = this.lastVehicle.getY();
            this.vehicleFirstGoodZ = this.lastVehicle.getZ();
            this.vehicleLastGoodX = this.lastVehicle.getX();
            this.vehicleLastGoodY = this.lastVehicle.getY();
            this.vehicleLastGoodZ = this.lastVehicle.getZ();
            if (this.clientVehicleIsFloating && this.player.getRootVehicle().getControllingPassenger() == this.player) {
                if (++this.aboveGroundVehicleTickCount > 80) {
                    ServerGamePacketListenerImpl.LOGGER.warn("{} was kicked for floating a vehicle too long!", this.player.getName().getString());
                    this.disconnect(new TranslatableComponent("multiplayer.disconnect.flying"));
                    return;
                }
            }
            else {
                this.clientVehicleIsFloating = false;
                this.aboveGroundVehicleTickCount = 0;
            }
        }
        this.server.getProfiler().push("keepAlive");
        final long long2 = Util.getMillis();
        if (long2 - this.keepAliveTime >= 15000L) {
            if (this.keepAlivePending) {
                this.disconnect(new TranslatableComponent("disconnect.timeout"));
            }
            else {
                this.keepAlivePending = true;
                this.keepAliveTime = long2;
                this.keepAliveChallenge = long2;
                this.send(new ClientboundKeepAlivePacket(this.keepAliveChallenge));
            }
        }
        this.server.getProfiler().pop();
        if (this.chatSpamTickCount > 0) {
            --this.chatSpamTickCount;
        }
        if (this.dropSpamTickCount > 0) {
            --this.dropSpamTickCount;
        }
        if (this.player.getLastActionTime() > 0L && this.server.getPlayerIdleTimeout() > 0 && Util.getMillis() - this.player.getLastActionTime() > this.server.getPlayerIdleTimeout() * 1000 * 60) {
            this.disconnect(new TranslatableComponent("multiplayer.disconnect.idling"));
        }
    }
    
    public void resetPosition() {
        this.firstGoodX = this.player.getX();
        this.firstGoodY = this.player.getY();
        this.firstGoodZ = this.player.getZ();
        this.lastGoodX = this.player.getX();
        this.lastGoodY = this.player.getY();
        this.lastGoodZ = this.player.getZ();
    }
    
    public Connection getConnection() {
        return this.connection;
    }
    
    private boolean isSingleplayerOwner() {
        return this.server.isSingleplayerOwner(this.player.getGameProfile());
    }
    
    public void disconnect(final Component nr) {
        this.connection.send(new ClientboundDisconnectPacket(nr), (future -> this.connection.disconnect(nr)));
        this.connection.setReadOnly();
        this.server.executeBlocking(this.connection::handleDisconnection);
    }
    
    public void handlePlayerInput(final ServerboundPlayerInputPacket tb) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)tb, this, this.player.getLevel());
        this.player.setPlayerInput(tb.getXxa(), tb.getZza(), tb.isJumping(), tb.isShiftKeyDown());
    }
    
    private static boolean containsInvalidValues(final ServerboundMovePlayerPacket st) {
        return !Doubles.isFinite(st.getX(0.0)) || !Doubles.isFinite(st.getY(0.0)) || !Doubles.isFinite(st.getZ(0.0)) || !Floats.isFinite(st.getXRot(0.0f)) || !Floats.isFinite(st.getYRot(0.0f)) || (Math.abs(st.getX(0.0)) > 3.0E7 || Math.abs(st.getY(0.0)) > 3.0E7 || Math.abs(st.getZ(0.0)) > 3.0E7);
    }
    
    private static boolean containsInvalidValues(final ServerboundMoveVehiclePacket su) {
        return !Doubles.isFinite(su.getX()) || !Doubles.isFinite(su.getY()) || !Doubles.isFinite(su.getZ()) || !Floats.isFinite(su.getXRot()) || !Floats.isFinite(su.getYRot());
    }
    
    public void handleMoveVehicle(final ServerboundMoveVehiclePacket su) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)su, this, this.player.getLevel());
        if (containsInvalidValues(su)) {
            this.disconnect(new TranslatableComponent("multiplayer.disconnect.invalid_vehicle_movement"));
            return;
        }
        final Entity apx3 = this.player.getRootVehicle();
        if (apx3 != this.player && apx3.getControllingPassenger() == this.player && apx3 == this.lastVehicle) {
            final ServerLevel aag4 = this.player.getLevel();
            final double double5 = apx3.getX();
            final double double6 = apx3.getY();
            final double double7 = apx3.getZ();
            final double double8 = su.getX();
            final double double9 = su.getY();
            final double double10 = su.getZ();
            final float float17 = su.getYRot();
            final float float18 = su.getXRot();
            double double11 = double8 - this.vehicleFirstGoodX;
            double double12 = double9 - this.vehicleFirstGoodY;
            double double13 = double10 - this.vehicleFirstGoodZ;
            final double double14 = apx3.getDeltaMovement().lengthSqr();
            double double15 = double11 * double11 + double12 * double12 + double13 * double13;
            if (double15 - double14 > 100.0 && !this.isSingleplayerOwner()) {
                ServerGamePacketListenerImpl.LOGGER.warn("{} (vehicle of {}) moved too quickly! {},{},{}", apx3.getName().getString(), this.player.getName().getString(), double11, double12, double13);
                this.connection.send(new ClientboundMoveVehiclePacket(apx3));
                return;
            }
            final boolean boolean29 = aag4.noCollision(apx3, apx3.getBoundingBox().deflate(0.0625));
            double11 = double8 - this.vehicleLastGoodX;
            double12 = double9 - this.vehicleLastGoodY - 1.0E-6;
            double13 = double10 - this.vehicleLastGoodZ;
            apx3.move(MoverType.PLAYER, new Vec3(double11, double12, double13));
            final double double16 = double12;
            double11 = double8 - apx3.getX();
            double12 = double9 - apx3.getY();
            if (double12 > -0.5 || double12 < 0.5) {
                double12 = 0.0;
            }
            double13 = double10 - apx3.getZ();
            double15 = double11 * double11 + double12 * double12 + double13 * double13;
            boolean boolean30 = false;
            if (double15 > 0.0625) {
                boolean30 = true;
                ServerGamePacketListenerImpl.LOGGER.warn("{} (vehicle of {}) moved wrongly! {}", apx3.getName().getString(), this.player.getName().getString(), Math.sqrt(double15));
            }
            apx3.absMoveTo(double8, double9, double10, float17, float18);
            final boolean boolean31 = aag4.noCollision(apx3, apx3.getBoundingBox().deflate(0.0625));
            if (boolean29 && (boolean30 || !boolean31)) {
                apx3.absMoveTo(double5, double6, double7, float17, float18);
                this.connection.send(new ClientboundMoveVehiclePacket(apx3));
                return;
            }
            this.player.getLevel().getChunkSource().move(this.player);
            this.player.checkMovementStatistics(this.player.getX() - double5, this.player.getY() - double6, this.player.getZ() - double7);
            this.clientVehicleIsFloating = (double16 >= -0.03125 && !this.server.isFlightAllowed() && this.noBlocksAround(apx3));
            this.vehicleLastGoodX = apx3.getX();
            this.vehicleLastGoodY = apx3.getY();
            this.vehicleLastGoodZ = apx3.getZ();
        }
    }
    
    private boolean noBlocksAround(final Entity apx) {
        return apx.level.getBlockStates(apx.getBoundingBox().inflate(0.0625).expandTowards(0.0, -0.55, 0.0)).allMatch(BlockBehaviour.BlockStateBase::isAir);
    }
    
    public void handleAcceptTeleportPacket(final ServerboundAcceptTeleportationPacket sb) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)sb, this, this.player.getLevel());
        if (sb.getId() == this.awaitingTeleport) {
            this.player.absMoveTo(this.awaitingPositionFromClient.x, this.awaitingPositionFromClient.y, this.awaitingPositionFromClient.z, this.player.yRot, this.player.xRot);
            this.lastGoodX = this.awaitingPositionFromClient.x;
            this.lastGoodY = this.awaitingPositionFromClient.y;
            this.lastGoodZ = this.awaitingPositionFromClient.z;
            if (this.player.isChangingDimension()) {
                this.player.hasChangedDimension();
            }
            this.awaitingPositionFromClient = null;
        }
    }
    
    public void handleRecipeBookSeenRecipePacket(final ServerboundRecipeBookSeenRecipePacket td) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)td, this, this.player.getLevel());
        this.server.getRecipeManager().byKey(td.getRecipe()).ifPresent(this.player.getRecipeBook()::removeHighlight);
    }
    
    public void handleRecipeBookChangeSettingsPacket(final ServerboundRecipeBookChangeSettingsPacket tc) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)tc, this, this.player.getLevel());
        this.player.getRecipeBook().setBookSetting(tc.getBookType(), tc.isOpen(), tc.isFiltering());
    }
    
    public void handleSeenAdvancements(final ServerboundSeenAdvancementsPacket tg) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)tg, this, this.player.getLevel());
        if (tg.getAction() == ServerboundSeenAdvancementsPacket.Action.OPENED_TAB) {
            final ResourceLocation vk3 = tg.getTab();
            final Advancement y4 = this.server.getAdvancements().getAdvancement(vk3);
            if (y4 != null) {
                this.player.getAdvancements().setSelectedTab(y4);
            }
        }
    }
    
    public void handleCustomCommandSuggestions(final ServerboundCommandSuggestionPacket sh) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)sh, this, this.player.getLevel());
        final StringReader stringReader3 = new StringReader(sh.getCommand());
        if (stringReader3.canRead() && stringReader3.peek() == '/') {
            stringReader3.skip();
        }
        final ParseResults<CommandSourceStack> parseResults4 = (ParseResults<CommandSourceStack>)this.server.getCommands().getDispatcher().parse(stringReader3, this.player.createCommandSourceStack());
        this.server.getCommands().getDispatcher().getCompletionSuggestions((ParseResults)parseResults4).thenAccept(suggestions -> this.connection.send(new ClientboundCommandSuggestionsPacket(sh.getId(), suggestions)));
    }
    
    public void handleSetCommandBlock(final ServerboundSetCommandBlockPacket tk) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)tk, this, this.player.getLevel());
        if (!this.server.isCommandBlockEnabled()) {
            this.player.sendMessage(new TranslatableComponent("advMode.notEnabled"), Util.NIL_UUID);
            return;
        }
        if (!this.player.canUseGameMasterBlocks()) {
            this.player.sendMessage(new TranslatableComponent("advMode.notAllowed"), Util.NIL_UUID);
            return;
        }
        BaseCommandBlock bqv3 = null;
        CommandBlockEntity ccl4 = null;
        final BlockPos fx5 = tk.getPos();
        final BlockEntity ccg6 = this.player.level.getBlockEntity(fx5);
        if (ccg6 instanceof CommandBlockEntity) {
            ccl4 = (CommandBlockEntity)ccg6;
            bqv3 = ccl4.getCommandBlock();
        }
        final String string7 = tk.getCommand();
        final boolean boolean8 = tk.isTrackOutput();
        if (bqv3 != null) {
            final CommandBlockEntity.Mode a9 = ccl4.getMode();
            final Direction gc10 = this.player.level.getBlockState(fx5).<Direction>getValue((Property<Direction>)CommandBlock.FACING);
            switch (tk.getMode()) {
                case SEQUENCE: {
                    final BlockState cee11 = Blocks.CHAIN_COMMAND_BLOCK.defaultBlockState();
                    this.player.level.setBlock(fx5, (((StateHolder<O, BlockState>)cee11).setValue((Property<Comparable>)CommandBlock.FACING, gc10)).<Comparable, Boolean>setValue((Property<Comparable>)CommandBlock.CONDITIONAL, tk.isConditional()), 2);
                    break;
                }
                case AUTO: {
                    final BlockState cee11 = Blocks.REPEATING_COMMAND_BLOCK.defaultBlockState();
                    this.player.level.setBlock(fx5, (((StateHolder<O, BlockState>)cee11).setValue((Property<Comparable>)CommandBlock.FACING, gc10)).<Comparable, Boolean>setValue((Property<Comparable>)CommandBlock.CONDITIONAL, tk.isConditional()), 2);
                    break;
                }
                default: {
                    final BlockState cee11 = Blocks.COMMAND_BLOCK.defaultBlockState();
                    this.player.level.setBlock(fx5, (((StateHolder<O, BlockState>)cee11).setValue((Property<Comparable>)CommandBlock.FACING, gc10)).<Comparable, Boolean>setValue((Property<Comparable>)CommandBlock.CONDITIONAL, tk.isConditional()), 2);
                    break;
                }
            }
            ccg6.clearRemoved();
            this.player.level.setBlockEntity(fx5, ccg6);
            bqv3.setCommand(string7);
            bqv3.setTrackOutput(boolean8);
            if (!boolean8) {
                bqv3.setLastOutput(null);
            }
            ccl4.setAutomatic(tk.isAutomatic());
            if (a9 != tk.getMode()) {
                ccl4.onModeSwitch();
            }
            bqv3.onUpdated();
            if (!StringUtil.isNullOrEmpty(string7)) {
                this.player.sendMessage(new TranslatableComponent("advMode.setCommand.success", new Object[] { string7 }), Util.NIL_UUID);
            }
        }
    }
    
    public void handleSetCommandMinecart(final ServerboundSetCommandMinecartPacket tl) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)tl, this, this.player.getLevel());
        if (!this.server.isCommandBlockEnabled()) {
            this.player.sendMessage(new TranslatableComponent("advMode.notEnabled"), Util.NIL_UUID);
            return;
        }
        if (!this.player.canUseGameMasterBlocks()) {
            this.player.sendMessage(new TranslatableComponent("advMode.notAllowed"), Util.NIL_UUID);
            return;
        }
        final BaseCommandBlock bqv3 = tl.getCommandBlock(this.player.level);
        if (bqv3 != null) {
            bqv3.setCommand(tl.getCommand());
            bqv3.setTrackOutput(tl.isTrackOutput());
            if (!tl.isTrackOutput()) {
                bqv3.setLastOutput(null);
            }
            bqv3.onUpdated();
            this.player.sendMessage(new TranslatableComponent("advMode.setCommand.success", new Object[] { tl.getCommand() }), Util.NIL_UUID);
        }
    }
    
    public void handlePickItem(final ServerboundPickItemPacket sw) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)sw, this, this.player.getLevel());
        this.player.inventory.pickSlot(sw.getSlot());
        this.player.connection.send(new ClientboundContainerSetSlotPacket(-2, this.player.inventory.selected, this.player.inventory.getItem(this.player.inventory.selected)));
        this.player.connection.send(new ClientboundContainerSetSlotPacket(-2, sw.getSlot(), this.player.inventory.getItem(sw.getSlot())));
        this.player.connection.send(new ClientboundSetCarriedItemPacket(this.player.inventory.selected));
    }
    
    public void handleRenameItem(final ServerboundRenameItemPacket te) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)te, this, this.player.getLevel());
        if (this.player.containerMenu instanceof AnvilMenu) {
            final AnvilMenu bib3 = (AnvilMenu)this.player.containerMenu;
            final String string4 = SharedConstants.filterText(te.getName());
            if (string4.length() <= 35) {
                bib3.setItemName(string4);
            }
        }
    }
    
    public void handleSetBeaconPacket(final ServerboundSetBeaconPacket ti) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)ti, this, this.player.getLevel());
        if (this.player.containerMenu instanceof BeaconMenu) {
            ((BeaconMenu)this.player.containerMenu).updateEffects(ti.getPrimary(), ti.getSecondary());
        }
    }
    
    public void handleSetStructureBlock(final ServerboundSetStructureBlockPacket to) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)to, this, this.player.getLevel());
        if (!this.player.canUseGameMasterBlocks()) {
            return;
        }
        final BlockPos fx3 = to.getPos();
        final BlockState cee4 = this.player.level.getBlockState(fx3);
        final BlockEntity ccg5 = this.player.level.getBlockEntity(fx3);
        if (ccg5 instanceof StructureBlockEntity) {
            final StructureBlockEntity cdg6 = (StructureBlockEntity)ccg5;
            cdg6.setMode(to.getMode());
            cdg6.setStructureName(to.getName());
            cdg6.setStructurePos(to.getOffset());
            cdg6.setStructureSize(to.getSize());
            cdg6.setMirror(to.getMirror());
            cdg6.setRotation(to.getRotation());
            cdg6.setMetaData(to.getData());
            cdg6.setIgnoreEntities(to.isIgnoreEntities());
            cdg6.setShowAir(to.isShowAir());
            cdg6.setShowBoundingBox(to.isShowBoundingBox());
            cdg6.setIntegrity(to.getIntegrity());
            cdg6.setSeed(to.getSeed());
            if (cdg6.hasStructureName()) {
                final String string7 = cdg6.getStructureName();
                if (to.getUpdateType() == StructureBlockEntity.UpdateType.SAVE_AREA) {
                    if (cdg6.saveStructure()) {
                        this.player.displayClientMessage(new TranslatableComponent("structure_block.save_success", new Object[] { string7 }), false);
                    }
                    else {
                        this.player.displayClientMessage(new TranslatableComponent("structure_block.save_failure", new Object[] { string7 }), false);
                    }
                }
                else if (to.getUpdateType() == StructureBlockEntity.UpdateType.LOAD_AREA) {
                    if (!cdg6.isStructureLoadable()) {
                        this.player.displayClientMessage(new TranslatableComponent("structure_block.load_not_found", new Object[] { string7 }), false);
                    }
                    else if (cdg6.loadStructure(this.player.getLevel())) {
                        this.player.displayClientMessage(new TranslatableComponent("structure_block.load_success", new Object[] { string7 }), false);
                    }
                    else {
                        this.player.displayClientMessage(new TranslatableComponent("structure_block.load_prepare", new Object[] { string7 }), false);
                    }
                }
                else if (to.getUpdateType() == StructureBlockEntity.UpdateType.SCAN_AREA) {
                    if (cdg6.detectSize()) {
                        this.player.displayClientMessage(new TranslatableComponent("structure_block.size_success", new Object[] { string7 }), false);
                    }
                    else {
                        this.player.displayClientMessage(new TranslatableComponent("structure_block.size_failure"), false);
                    }
                }
            }
            else {
                this.player.displayClientMessage(new TranslatableComponent("structure_block.invalid_structure_name", new Object[] { to.getName() }), false);
            }
            cdg6.setChanged();
            this.player.level.sendBlockUpdated(fx3, cee4, cee4, 3);
        }
    }
    
    public void handleSetJigsawBlock(final ServerboundSetJigsawBlockPacket tn) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)tn, this, this.player.getLevel());
        if (!this.player.canUseGameMasterBlocks()) {
            return;
        }
        final BlockPos fx3 = tn.getPos();
        final BlockState cee4 = this.player.level.getBlockState(fx3);
        final BlockEntity ccg5 = this.player.level.getBlockEntity(fx3);
        if (ccg5 instanceof JigsawBlockEntity) {
            final JigsawBlockEntity ccw6 = (JigsawBlockEntity)ccg5;
            ccw6.setName(tn.getName());
            ccw6.setTarget(tn.getTarget());
            ccw6.setPool(tn.getPool());
            ccw6.setFinalState(tn.getFinalState());
            ccw6.setJoint(tn.getJoint());
            ccw6.setChanged();
            this.player.level.sendBlockUpdated(fx3, cee4, cee4, 3);
        }
    }
    
    public void handleJigsawGenerate(final ServerboundJigsawGeneratePacket sq) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)sq, this, this.player.getLevel());
        if (!this.player.canUseGameMasterBlocks()) {
            return;
        }
        final BlockPos fx3 = sq.getPos();
        final BlockEntity ccg4 = this.player.level.getBlockEntity(fx3);
        if (ccg4 instanceof JigsawBlockEntity) {
            final JigsawBlockEntity ccw5 = (JigsawBlockEntity)ccg4;
            ccw5.generate(this.player.getLevel(), sq.levels(), sq.keepJigsaws());
        }
    }
    
    public void handleSelectTrade(final ServerboundSelectTradePacket th) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)th, this, this.player.getLevel());
        final int integer3 = th.getItem();
        final AbstractContainerMenu bhz4 = this.player.containerMenu;
        if (bhz4 instanceof MerchantMenu) {
            final MerchantMenu bjd5 = (MerchantMenu)bhz4;
            bjd5.setSelectionHint(integer3);
            bjd5.tryMoveItems(integer3);
        }
    }
    
    public void handleEditBook(final ServerboundEditBookPacket sn) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)sn, this, this.player.getLevel());
        final ItemStack bly3 = sn.getBook();
        if (bly3.isEmpty()) {
            return;
        }
        if (!WritableBookItem.makeSureTagIsValid(bly3.getTag())) {
            return;
        }
        final ItemStack bly4 = this.player.getItemInHand(sn.getHand());
        if (bly3.getItem() == Items.WRITABLE_BOOK && bly4.getItem() == Items.WRITABLE_BOOK) {
            if (sn.isSigning()) {
                final ItemStack bly5 = new ItemStack(Items.WRITTEN_BOOK);
                final CompoundTag md6 = bly4.getTag();
                if (md6 != null) {
                    bly5.setTag(md6.copy());
                }
                bly5.addTagElement("author", (Tag)StringTag.valueOf(this.player.getName().getString()));
                bly5.addTagElement("title", (Tag)StringTag.valueOf(bly3.getTag().getString("title")));
                final ListTag mj7 = bly3.getTag().getList("pages", 8);
                for (int integer8 = 0; integer8 < mj7.size(); ++integer8) {
                    String string9 = mj7.getString(integer8);
                    final Component nr10 = new TextComponent(string9);
                    string9 = Component.Serializer.toJson(nr10);
                    mj7.set(integer8, StringTag.valueOf(string9));
                }
                bly5.addTagElement("pages", (Tag)mj7);
                this.player.setItemInHand(sn.getHand(), bly5);
            }
            else {
                bly4.addTagElement("pages", (Tag)bly3.getTag().getList("pages", 8));
            }
        }
    }
    
    public void handleEntityTagQuery(final ServerboundEntityTagQuery so) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)so, this, this.player.getLevel());
        if (!this.player.hasPermissions(2)) {
            return;
        }
        final Entity apx3 = this.player.getLevel().getEntity(so.getEntityId());
        if (apx3 != null) {
            final CompoundTag md4 = apx3.saveWithoutId(new CompoundTag());
            this.player.connection.send(new ClientboundTagQueryPacket(so.getTransactionId(), md4));
        }
    }
    
    public void handleBlockEntityTagQuery(final ServerboundBlockEntityTagQuery sc) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)sc, this, this.player.getLevel());
        if (!this.player.hasPermissions(2)) {
            return;
        }
        final BlockEntity ccg3 = this.player.getLevel().getBlockEntity(sc.getPos());
        final CompoundTag md4 = (ccg3 != null) ? ccg3.save(new CompoundTag()) : null;
        this.player.connection.send(new ClientboundTagQueryPacket(sc.getTransactionId(), md4));
    }
    
    public void handleMovePlayer(final ServerboundMovePlayerPacket st) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)st, this, this.player.getLevel());
        if (containsInvalidValues(st)) {
            this.disconnect(new TranslatableComponent("multiplayer.disconnect.invalid_player_movement"));
            return;
        }
        final ServerLevel aag3 = this.player.getLevel();
        if (this.player.wonGame) {
            return;
        }
        if (this.tickCount == 0) {
            this.resetPosition();
        }
        if (this.awaitingPositionFromClient != null) {
            if (this.tickCount - this.awaitingTeleportTime > 20) {
                this.awaitingTeleportTime = this.tickCount;
                this.teleport(this.awaitingPositionFromClient.x, this.awaitingPositionFromClient.y, this.awaitingPositionFromClient.z, this.player.yRot, this.player.xRot);
            }
            return;
        }
        this.awaitingTeleportTime = this.tickCount;
        if (this.player.isPassenger()) {
            this.player.absMoveTo(this.player.getX(), this.player.getY(), this.player.getZ(), st.getYRot(this.player.yRot), st.getXRot(this.player.xRot));
            this.player.getLevel().getChunkSource().move(this.player);
            return;
        }
        final double double4 = this.player.getX();
        final double double5 = this.player.getY();
        final double double6 = this.player.getZ();
        final double double7 = this.player.getY();
        final double double8 = st.getX(this.player.getX());
        final double double9 = st.getY(this.player.getY());
        final double double10 = st.getZ(this.player.getZ());
        final float float18 = st.getYRot(this.player.yRot);
        final float float19 = st.getXRot(this.player.xRot);
        double double11 = double8 - this.firstGoodX;
        double double12 = double9 - this.firstGoodY;
        double double13 = double10 - this.firstGoodZ;
        final double double14 = this.player.getDeltaMovement().lengthSqr();
        double double15 = double11 * double11 + double12 * double12 + double13 * double13;
        if (this.player.isSleeping()) {
            if (double15 > 1.0) {
                this.teleport(this.player.getX(), this.player.getY(), this.player.getZ(), st.getYRot(this.player.yRot), st.getXRot(this.player.xRot));
            }
            return;
        }
        ++this.receivedMovePacketCount;
        int integer30 = this.receivedMovePacketCount - this.knownMovePacketCount;
        if (integer30 > 5) {
            ServerGamePacketListenerImpl.LOGGER.debug("{} is sending move packets too frequently ({} packets since last tick)", this.player.getName().getString(), integer30);
            integer30 = 1;
        }
        if (!this.player.isChangingDimension()) {
            if (!this.player.getLevel().getGameRules().getBoolean(GameRules.RULE_DISABLE_ELYTRA_MOVEMENT_CHECK) || !this.player.isFallFlying()) {
                final float float20 = this.player.isFallFlying() ? 300.0f : 100.0f;
                if (double15 - double14 > float20 * integer30 && !this.isSingleplayerOwner()) {
                    ServerGamePacketListenerImpl.LOGGER.warn("{} moved too quickly! {},{},{}", this.player.getName().getString(), double11, double12, double13);
                    this.teleport(this.player.getX(), this.player.getY(), this.player.getZ(), this.player.yRot, this.player.xRot);
                    return;
                }
            }
        }
        final AABB dcf31 = this.player.getBoundingBox();
        double11 = double8 - this.lastGoodX;
        double12 = double9 - this.lastGoodY;
        double13 = double10 - this.lastGoodZ;
        final boolean boolean32 = double12 > 0.0;
        if (this.player.isOnGround() && !st.isOnGround() && boolean32) {
            this.player.jumpFromGround();
        }
        this.player.move(MoverType.PLAYER, new Vec3(double11, double12, double13));
        final double double16 = double12;
        double11 = double8 - this.player.getX();
        double12 = double9 - this.player.getY();
        if (double12 > -0.5 || double12 < 0.5) {
            double12 = 0.0;
        }
        double13 = double10 - this.player.getZ();
        double15 = double11 * double11 + double12 * double12 + double13 * double13;
        boolean boolean33 = false;
        if (!this.player.isChangingDimension() && double15 > 0.0625 && !this.player.isSleeping() && !this.player.gameMode.isCreative() && this.player.gameMode.getGameModeForPlayer() != GameType.SPECTATOR) {
            boolean33 = true;
            ServerGamePacketListenerImpl.LOGGER.warn("{} moved wrongly!", this.player.getName().getString());
        }
        this.player.absMoveTo(double8, double9, double10, float18, float19);
        if (!this.player.noPhysics && !this.player.isSleeping() && ((boolean33 && aag3.noCollision(this.player, dcf31)) || this.isPlayerCollidingWithAnythingNew(aag3, dcf31))) {
            this.teleport(double4, double5, double6, float18, float19);
            return;
        }
        this.clientIsFloating = (double16 >= -0.03125 && this.player.gameMode.getGameModeForPlayer() != GameType.SPECTATOR && !this.server.isFlightAllowed() && !this.player.abilities.mayfly && !this.player.hasEffect(MobEffects.LEVITATION) && !this.player.isFallFlying() && this.noBlocksAround(this.player));
        this.player.getLevel().getChunkSource().move(this.player);
        this.player.doCheckFallDamage(this.player.getY() - double7, st.isOnGround());
        this.player.setOnGround(st.isOnGround());
        if (boolean32) {
            this.player.fallDistance = 0.0f;
        }
        this.player.checkMovementStatistics(this.player.getX() - double4, this.player.getY() - double5, this.player.getZ() - double6);
        this.lastGoodX = this.player.getX();
        this.lastGoodY = this.player.getY();
        this.lastGoodZ = this.player.getZ();
    }
    
    private boolean isPlayerCollidingWithAnythingNew(final LevelReader brw, final AABB dcf) {
        final Stream<VoxelShape> stream4 = brw.getCollisions(this.player, this.player.getBoundingBox().deflate(9.999999747378752E-6), (Predicate<Entity>)(apx -> true));
        final VoxelShape dde5 = Shapes.create(dcf.deflate(9.999999747378752E-6));
        return stream4.anyMatch(dde2 -> !Shapes.joinIsNotEmpty(dde2, dde5, BooleanOp.AND));
    }
    
    public void teleport(final double double1, final double double2, final double double3, final float float4, final float float5) {
        this.teleport(double1, double2, double3, float4, float5, (Set<ClientboundPlayerPositionPacket.RelativeArgument>)Collections.emptySet());
    }
    
    public void teleport(final double double1, final double double2, final double double3, final float float4, final float float5, final Set<ClientboundPlayerPositionPacket.RelativeArgument> set) {
        final double double4 = set.contains(ClientboundPlayerPositionPacket.RelativeArgument.X) ? this.player.getX() : 0.0;
        final double double5 = set.contains(ClientboundPlayerPositionPacket.RelativeArgument.Y) ? this.player.getY() : 0.0;
        final double double6 = set.contains(ClientboundPlayerPositionPacket.RelativeArgument.Z) ? this.player.getZ() : 0.0;
        final float float6 = set.contains(ClientboundPlayerPositionPacket.RelativeArgument.Y_ROT) ? this.player.yRot : 0.0f;
        final float float7 = set.contains(ClientboundPlayerPositionPacket.RelativeArgument.X_ROT) ? this.player.xRot : 0.0f;
        this.awaitingPositionFromClient = new Vec3(double1, double2, double3);
        if (++this.awaitingTeleport == Integer.MAX_VALUE) {
            this.awaitingTeleport = 0;
        }
        this.awaitingTeleportTime = this.tickCount;
        this.player.absMoveTo(double1, double2, double3, float4, float5);
        this.player.connection.send(new ClientboundPlayerPositionPacket(double1 - double4, double2 - double5, double3 - double6, float4 - float6, float5 - float7, set, this.awaitingTeleport));
    }
    
    public void handlePlayerAction(final ServerboundPlayerActionPacket sz) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)sz, this, this.player.getLevel());
        final BlockPos fx3 = sz.getPos();
        this.player.resetLastActionTime();
        final ServerboundPlayerActionPacket.Action a4 = sz.getAction();
        switch (a4) {
            case SWAP_ITEM_WITH_OFFHAND: {
                if (!this.player.isSpectator()) {
                    final ItemStack bly5 = this.player.getItemInHand(InteractionHand.OFF_HAND);
                    this.player.setItemInHand(InteractionHand.OFF_HAND, this.player.getItemInHand(InteractionHand.MAIN_HAND));
                    this.player.setItemInHand(InteractionHand.MAIN_HAND, bly5);
                    this.player.stopUsingItem();
                }
            }
            case DROP_ITEM: {
                if (!this.player.isSpectator()) {
                    this.player.drop(false);
                }
            }
            case DROP_ALL_ITEMS: {
                if (!this.player.isSpectator()) {
                    this.player.drop(true);
                }
            }
            case RELEASE_USE_ITEM: {
                this.player.releaseUsingItem();
            }
            case START_DESTROY_BLOCK:
            case ABORT_DESTROY_BLOCK:
            case STOP_DESTROY_BLOCK: {
                this.player.gameMode.handleBlockBreakAction(fx3, a4, sz.getDirection(), this.server.getMaxBuildHeight());
            }
            default: {
                throw new IllegalArgumentException("Invalid player action");
            }
        }
    }
    
    private static boolean wasBlockPlacementAttempt(final ServerPlayer aah, final ItemStack bly) {
        if (bly.isEmpty()) {
            return false;
        }
        final Item blu3 = bly.getItem();
        return (blu3 instanceof BlockItem || blu3 instanceof BucketItem) && !aah.getCooldowns().isOnCooldown(blu3);
    }
    
    public void handleUseItemOn(final ServerboundUseItemOnPacket ts) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)ts, this, this.player.getLevel());
        final ServerLevel aag3 = this.player.getLevel();
        final InteractionHand aoq4 = ts.getHand();
        final ItemStack bly5 = this.player.getItemInHand(aoq4);
        final BlockHitResult dcg6 = ts.getHitResult();
        final BlockPos fx7 = dcg6.getBlockPos();
        final Direction gc8 = dcg6.getDirection();
        this.player.resetLastActionTime();
        if (fx7.getY() < this.server.getMaxBuildHeight()) {
            if (this.awaitingPositionFromClient == null && this.player.distanceToSqr(fx7.getX() + 0.5, fx7.getY() + 0.5, fx7.getZ() + 0.5) < 64.0 && aag3.mayInteract(this.player, fx7)) {
                final InteractionResult aor9 = this.player.gameMode.useItemOn(this.player, aag3, bly5, aoq4, dcg6);
                if (gc8 == Direction.UP && !aor9.consumesAction() && fx7.getY() >= this.server.getMaxBuildHeight() - 1 && wasBlockPlacementAttempt(this.player, bly5)) {
                    final Component nr10 = new TranslatableComponent("build.tooHigh", new Object[] { this.server.getMaxBuildHeight() }).withStyle(ChatFormatting.RED);
                    this.player.connection.send(new ClientboundChatPacket(nr10, ChatType.GAME_INFO, Util.NIL_UUID));
                }
                else if (aor9.shouldSwing()) {
                    this.player.swing(aoq4, true);
                }
            }
        }
        else {
            final Component nr11 = new TranslatableComponent("build.tooHigh", new Object[] { this.server.getMaxBuildHeight() }).withStyle(ChatFormatting.RED);
            this.player.connection.send(new ClientboundChatPacket(nr11, ChatType.GAME_INFO, Util.NIL_UUID));
        }
        this.player.connection.send(new ClientboundBlockUpdatePacket(aag3, fx7));
        this.player.connection.send(new ClientboundBlockUpdatePacket(aag3, fx7.relative(gc8)));
    }
    
    public void handleUseItem(final ServerboundUseItemPacket tt) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)tt, this, this.player.getLevel());
        final ServerLevel aag3 = this.player.getLevel();
        final InteractionHand aoq4 = tt.getHand();
        final ItemStack bly5 = this.player.getItemInHand(aoq4);
        this.player.resetLastActionTime();
        if (bly5.isEmpty()) {
            return;
        }
        final InteractionResult aor6 = this.player.gameMode.useItem(this.player, aag3, bly5, aoq4);
        if (aor6.shouldSwing()) {
            this.player.swing(aoq4, true);
        }
    }
    
    public void handleTeleportToEntityPacket(final ServerboundTeleportToEntityPacket tr) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)tr, this, this.player.getLevel());
        if (this.player.isSpectator()) {
            for (final ServerLevel aag4 : this.server.getAllLevels()) {
                final Entity apx5 = tr.getEntity(aag4);
                if (apx5 != null) {
                    this.player.teleportTo(aag4, apx5.getX(), apx5.getY(), apx5.getZ(), apx5.yRot, apx5.xRot);
                }
            }
        }
    }
    
    public void handleResourcePackResponse(final ServerboundResourcePackPacket tf) {
    }
    
    public void handlePaddleBoat(final ServerboundPaddleBoatPacket sv) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)sv, this, this.player.getLevel());
        final Entity apx3 = this.player.getVehicle();
        if (apx3 instanceof Boat) {
            ((Boat)apx3).setPaddleState(sv.getLeft(), sv.getRight());
        }
    }
    
    public void onDisconnect(final Component nr) {
        ServerGamePacketListenerImpl.LOGGER.info("{} lost connection: {}", this.player.getName().getString(), nr.getString());
        this.server.invalidateStatus();
        this.server.getPlayerList().broadcastMessage(new TranslatableComponent("multiplayer.player.left", new Object[] { this.player.getDisplayName() }).withStyle(ChatFormatting.YELLOW), ChatType.SYSTEM, Util.NIL_UUID);
        this.player.disconnect();
        this.server.getPlayerList().remove(this.player);
        if (this.isSingleplayerOwner()) {
            ServerGamePacketListenerImpl.LOGGER.info("Stopping singleplayer server as player logged out");
            this.server.halt(false);
        }
    }
    
    public void send(final Packet<?> oj) {
        this.send(oj, null);
    }
    
    public void send(final Packet<?> oj, @Nullable final GenericFutureListener<? extends Future<? super Void>> genericFutureListener) {
        if (oj instanceof ClientboundChatPacket) {
            final ClientboundChatPacket pb4 = (ClientboundChatPacket)oj;
            final ChatVisiblity bfr5 = this.player.getChatVisibility();
            if (bfr5 == ChatVisiblity.HIDDEN && pb4.getType() != ChatType.GAME_INFO) {
                return;
            }
            if (bfr5 == ChatVisiblity.SYSTEM && !pb4.isSystem()) {
                return;
            }
        }
        try {
            this.connection.send(oj, genericFutureListener);
        }
        catch (Throwable throwable4) {
            final CrashReport l5 = CrashReport.forThrowable(throwable4, "Sending packet");
            final CrashReportCategory m6 = l5.addCategory("Packet being sent");
            m6.setDetail("Packet class", (CrashReportDetail<String>)(() -> oj.getClass().getCanonicalName()));
            throw new ReportedException(l5);
        }
    }
    
    public void handleSetCarriedItem(final ServerboundSetCarriedItemPacket tj) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)tj, this, this.player.getLevel());
        if (tj.getSlot() < 0 || tj.getSlot() >= Inventory.getSelectionSize()) {
            ServerGamePacketListenerImpl.LOGGER.warn("{} tried to set an invalid carried item", this.player.getName().getString());
            return;
        }
        if (this.player.inventory.selected != tj.getSlot() && this.player.getUsedItemHand() == InteractionHand.MAIN_HAND) {
            this.player.stopUsingItem();
        }
        this.player.inventory.selected = tj.getSlot();
        this.player.resetLastActionTime();
    }
    
    public void handleChat(final ServerboundChatPacket se) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)se, this, this.player.getLevel());
        if (this.player.getChatVisibility() == ChatVisiblity.HIDDEN) {
            this.send(new ClientboundChatPacket(new TranslatableComponent("chat.cannotSend").withStyle(ChatFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID));
            return;
        }
        this.player.resetLastActionTime();
        final String string3 = StringUtils.normalizeSpace(se.getMessage());
        for (int integer4 = 0; integer4 < string3.length(); ++integer4) {
            if (!SharedConstants.isAllowedChatCharacter(string3.charAt(integer4))) {
                this.disconnect(new TranslatableComponent("multiplayer.disconnect.illegal_characters"));
                return;
            }
        }
        if (string3.startsWith("/")) {
            this.handleCommand(string3);
        }
        else {
            final Component nr4 = new TranslatableComponent("chat.type.text", new Object[] { this.player.getDisplayName(), string3 });
            this.server.getPlayerList().broadcastMessage(nr4, ChatType.CHAT, this.player.getUUID());
        }
        this.chatSpamTickCount += 20;
        if (this.chatSpamTickCount > 200 && !this.server.getPlayerList().isOp(this.player.getGameProfile())) {
            this.disconnect(new TranslatableComponent("disconnect.spam"));
        }
    }
    
    private void handleCommand(final String string) {
        this.server.getCommands().performCommand(this.player.createCommandSourceStack(), string);
    }
    
    public void handleAnimate(final ServerboundSwingPacket tq) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)tq, this, this.player.getLevel());
        this.player.resetLastActionTime();
        this.player.swing(tq.getHand());
    }
    
    public void handlePlayerCommand(final ServerboundPlayerCommandPacket ta) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)ta, this, this.player.getLevel());
        this.player.resetLastActionTime();
        switch (ta.getAction()) {
            case PRESS_SHIFT_KEY: {
                this.player.setShiftKeyDown(true);
                break;
            }
            case RELEASE_SHIFT_KEY: {
                this.player.setShiftKeyDown(false);
                break;
            }
            case START_SPRINTING: {
                this.player.setSprinting(true);
                break;
            }
            case STOP_SPRINTING: {
                this.player.setSprinting(false);
                break;
            }
            case STOP_SLEEPING: {
                if (this.player.isSleeping()) {
                    this.player.stopSleepInBed(false, true);
                    this.awaitingPositionFromClient = this.player.position();
                    break;
                }
                break;
            }
            case START_RIDING_JUMP: {
                if (this.player.getVehicle() instanceof PlayerRideableJumping) {
                    final PlayerRideableJumping aqt3 = (PlayerRideableJumping)this.player.getVehicle();
                    final int integer4 = ta.getData();
                    if (aqt3.canJump() && integer4 > 0) {
                        aqt3.handleStartJump(integer4);
                    }
                    break;
                }
                break;
            }
            case STOP_RIDING_JUMP: {
                if (this.player.getVehicle() instanceof PlayerRideableJumping) {
                    final PlayerRideableJumping aqt3 = (PlayerRideableJumping)this.player.getVehicle();
                    aqt3.handleStopJump();
                    break;
                }
                break;
            }
            case OPEN_INVENTORY: {
                if (this.player.getVehicle() instanceof AbstractHorse) {
                    ((AbstractHorse)this.player.getVehicle()).openInventory(this.player);
                    break;
                }
                break;
            }
            case START_FALL_FLYING: {
                if (!this.player.tryToStartFallFlying()) {
                    this.player.stopFallFlying();
                    break;
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid client command!");
            }
        }
    }
    
    public void handleInteract(final ServerboundInteractPacket sp) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)sp, this, this.player.getLevel());
        final ServerLevel aag3 = this.player.getLevel();
        final Entity apx4 = sp.getTarget(aag3);
        this.player.resetLastActionTime();
        this.player.setShiftKeyDown(sp.isUsingSecondaryAction());
        if (apx4 != null) {
            final double double5 = 36.0;
            if (this.player.distanceToSqr(apx4) < 36.0) {
                final InteractionHand aoq7 = sp.getHand();
                final ItemStack bly8 = (aoq7 != null) ? this.player.getItemInHand(aoq7).copy() : ItemStack.EMPTY;
                Optional<InteractionResult> optional9 = (Optional<InteractionResult>)Optional.empty();
                if (sp.getAction() == ServerboundInteractPacket.Action.INTERACT) {
                    optional9 = (Optional<InteractionResult>)Optional.of(this.player.interactOn(apx4, aoq7));
                }
                else if (sp.getAction() == ServerboundInteractPacket.Action.INTERACT_AT) {
                    optional9 = (Optional<InteractionResult>)Optional.of(apx4.interactAt(this.player, sp.getLocation(), aoq7));
                }
                else if (sp.getAction() == ServerboundInteractPacket.Action.ATTACK) {
                    if (apx4 instanceof ItemEntity || apx4 instanceof ExperienceOrb || apx4 instanceof AbstractArrow || apx4 == this.player) {
                        this.disconnect(new TranslatableComponent("multiplayer.disconnect.invalid_entity_attacked"));
                        ServerGamePacketListenerImpl.LOGGER.warn("Player {} tried to attack an invalid entity", this.player.getName().getString());
                        return;
                    }
                    this.player.attack(apx4);
                }
                if (optional9.isPresent() && ((InteractionResult)optional9.get()).consumesAction()) {
                    CriteriaTriggers.PLAYER_INTERACTED_WITH_ENTITY.trigger(this.player, bly8, apx4);
                    if (((InteractionResult)optional9.get()).shouldSwing()) {
                        this.player.swing(aoq7, true);
                    }
                }
            }
        }
    }
    
    public void handleClientCommand(final ServerboundClientCommandPacket sf) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)sf, this, this.player.getLevel());
        this.player.resetLastActionTime();
        final ServerboundClientCommandPacket.Action a3 = sf.getAction();
        switch (a3) {
            case PERFORM_RESPAWN: {
                if (this.player.wonGame) {
                    this.player.wonGame = false;
                    this.player = this.server.getPlayerList().respawn(this.player, true);
                    CriteriaTriggers.CHANGED_DIMENSION.trigger(this.player, Level.END, Level.OVERWORLD);
                    break;
                }
                if (this.player.getHealth() > 0.0f) {
                    return;
                }
                this.player = this.server.getPlayerList().respawn(this.player, false);
                if (this.server.isHardcore()) {
                    this.player.setGameMode(GameType.SPECTATOR);
                    this.player.getLevel().getGameRules().<GameRules.BooleanValue>getRule(GameRules.RULE_SPECTATORSGENERATECHUNKS).set(false, this.server);
                    break;
                }
                break;
            }
            case REQUEST_STATS: {
                this.player.getStats().sendStats(this.player);
                break;
            }
        }
    }
    
    public void handleContainerClose(final ServerboundContainerClosePacket sl) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)sl, this, this.player.getLevel());
        this.player.doCloseContainer();
    }
    
    public void handleContainerClick(final ServerboundContainerClickPacket sk) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)sk, this, this.player.getLevel());
        this.player.resetLastActionTime();
        if (this.player.containerMenu.containerId == sk.getContainerId() && this.player.containerMenu.isSynched(this.player)) {
            if (this.player.isSpectator()) {
                final NonNullList<ItemStack> gj3 = NonNullList.<ItemStack>create();
                for (int integer4 = 0; integer4 < this.player.containerMenu.slots.size(); ++integer4) {
                    gj3.add(((Slot)this.player.containerMenu.slots.get(integer4)).getItem());
                }
                this.player.refreshContainer(this.player.containerMenu, gj3);
            }
            else {
                final ItemStack bly3 = this.player.containerMenu.clicked(sk.getSlotNum(), sk.getButtonNum(), sk.getClickType(), this.player);
                if (ItemStack.matches(sk.getItem(), bly3)) {
                    this.player.connection.send(new ClientboundContainerAckPacket(sk.getContainerId(), sk.getUid(), true));
                    this.player.ignoreSlotUpdateHack = true;
                    this.player.containerMenu.broadcastChanges();
                    this.player.broadcastCarriedItem();
                    this.player.ignoreSlotUpdateHack = false;
                }
                else {
                    this.expectedAcks.put(this.player.containerMenu.containerId, sk.getUid());
                    this.player.connection.send(new ClientboundContainerAckPacket(sk.getContainerId(), sk.getUid(), false));
                    this.player.containerMenu.setSynched(this.player, false);
                    final NonNullList<ItemStack> gj4 = NonNullList.<ItemStack>create();
                    for (int integer5 = 0; integer5 < this.player.containerMenu.slots.size(); ++integer5) {
                        final ItemStack bly4 = ((Slot)this.player.containerMenu.slots.get(integer5)).getItem();
                        gj4.add((bly4.isEmpty() ? ItemStack.EMPTY : bly4));
                    }
                    this.player.refreshContainer(this.player.containerMenu, gj4);
                }
            }
        }
    }
    
    public void handlePlaceRecipe(final ServerboundPlaceRecipePacket sx) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)sx, this, this.player.getLevel());
        this.player.resetLastActionTime();
        if (this.player.isSpectator() || this.player.containerMenu.containerId != sx.getContainerId() || !this.player.containerMenu.isSynched(this.player) || !(this.player.containerMenu instanceof RecipeBookMenu)) {
            return;
        }
        this.server.getRecipeManager().byKey(sx.getRecipe()).ifPresent(bon -> ((RecipeBookMenu)this.player.containerMenu).handlePlacement(sx.isShiftDown(), bon, this.player));
    }
    
    public void handleContainerButtonClick(final ServerboundContainerButtonClickPacket sj) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)sj, this, this.player.getLevel());
        this.player.resetLastActionTime();
        if (this.player.containerMenu.containerId == sj.getContainerId() && this.player.containerMenu.isSynched(this.player) && !this.player.isSpectator()) {
            this.player.containerMenu.clickMenuButton(this.player, sj.getButtonId());
            this.player.containerMenu.broadcastChanges();
        }
    }
    
    public void handleSetCreativeModeSlot(final ServerboundSetCreativeModeSlotPacket tm) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)tm, this, this.player.getLevel());
        if (this.player.gameMode.isCreative()) {
            final boolean boolean3 = tm.getSlotNum() < 0;
            final ItemStack bly4 = tm.getItem();
            final CompoundTag md5 = bly4.getTagElement("BlockEntityTag");
            if (!bly4.isEmpty() && md5 != null && md5.contains("x") && md5.contains("y") && md5.contains("z")) {
                final BlockPos fx6 = new BlockPos(md5.getInt("x"), md5.getInt("y"), md5.getInt("z"));
                final BlockEntity ccg7 = this.player.level.getBlockEntity(fx6);
                if (ccg7 != null) {
                    final CompoundTag md6 = ccg7.save(new CompoundTag());
                    md6.remove("x");
                    md6.remove("y");
                    md6.remove("z");
                    bly4.addTagElement("BlockEntityTag", (Tag)md6);
                }
            }
            final boolean boolean4 = tm.getSlotNum() >= 1 && tm.getSlotNum() <= 45;
            final boolean boolean5 = bly4.isEmpty() || (bly4.getDamageValue() >= 0 && bly4.getCount() <= 64 && !bly4.isEmpty());
            if (boolean4 && boolean5) {
                if (bly4.isEmpty()) {
                    this.player.inventoryMenu.setItem(tm.getSlotNum(), ItemStack.EMPTY);
                }
                else {
                    this.player.inventoryMenu.setItem(tm.getSlotNum(), bly4);
                }
                this.player.inventoryMenu.setSynched(this.player, true);
                this.player.inventoryMenu.broadcastChanges();
            }
            else if (boolean3 && boolean5 && this.dropSpamTickCount < 200) {
                this.dropSpamTickCount += 20;
                this.player.drop(bly4, true);
            }
        }
    }
    
    public void handleContainerAck(final ServerboundContainerAckPacket si) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)si, this, this.player.getLevel());
        final int integer3 = this.player.containerMenu.containerId;
        if (integer3 == si.getContainerId() && this.expectedAcks.getOrDefault(integer3, (short)(si.getUid() + 1)) == si.getUid() && !this.player.containerMenu.isSynched(this.player) && !this.player.isSpectator()) {
            this.player.containerMenu.setSynched(this.player, true);
        }
    }
    
    public void handleSignUpdate(final ServerboundSignUpdatePacket tp) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)tp, this, this.player.getLevel());
        this.player.resetLastActionTime();
        final ServerLevel aag3 = this.player.getLevel();
        final BlockPos fx4 = tp.getPos();
        if (aag3.hasChunkAt(fx4)) {
            final BlockState cee5 = aag3.getBlockState(fx4);
            final BlockEntity ccg6 = aag3.getBlockEntity(fx4);
            if (!(ccg6 instanceof SignBlockEntity)) {
                return;
            }
            final SignBlockEntity cdc7 = (SignBlockEntity)ccg6;
            if (!cdc7.isEditable() || cdc7.getPlayerWhoMayEdit() != this.player) {
                ServerGamePacketListenerImpl.LOGGER.warn("Player {} just tried to change non-editable sign", this.player.getName().getString());
                return;
            }
            final String[] arr8 = tp.getLines();
            for (int integer9 = 0; integer9 < arr8.length; ++integer9) {
                cdc7.setMessage(integer9, new TextComponent(ChatFormatting.stripFormatting(arr8[integer9])));
            }
            cdc7.setChanged();
            aag3.sendBlockUpdated(fx4, cee5, cee5, 3);
        }
    }
    
    public void handleKeepAlive(final ServerboundKeepAlivePacket sr) {
        if (this.keepAlivePending && sr.getId() == this.keepAliveChallenge) {
            final int integer3 = (int)(Util.getMillis() - this.keepAliveTime);
            this.player.latency = (this.player.latency * 3 + integer3) / 4;
            this.keepAlivePending = false;
        }
        else if (!this.isSingleplayerOwner()) {
            this.disconnect(new TranslatableComponent("disconnect.timeout"));
        }
    }
    
    public void handlePlayerAbilities(final ServerboundPlayerAbilitiesPacket sy) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)sy, this, this.player.getLevel());
        this.player.abilities.flying = (sy.isFlying() && this.player.abilities.mayfly);
    }
    
    public void handleClientInformation(final ServerboundClientInformationPacket sg) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)sg, this, this.player.getLevel());
        this.player.updateOptions(sg);
    }
    
    public void handleCustomPayload(final ServerboundCustomPayloadPacket sm) {
    }
    
    public void handleChangeDifficulty(final ServerboundChangeDifficultyPacket sd) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)sd, this, this.player.getLevel());
        if (!this.player.hasPermissions(2) && !this.isSingleplayerOwner()) {
            return;
        }
        this.server.setDifficulty(sd.getDifficulty(), false);
    }
    
    public void handleLockDifficulty(final ServerboundLockDifficultyPacket ss) {
        PacketUtils.<ServerGamePacketListenerImpl>ensureRunningOnSameThread((Packet<ServerGamePacketListenerImpl>)ss, this, this.player.getLevel());
        if (!this.player.hasPermissions(2) && !this.isSingleplayerOwner()) {
            return;
        }
        this.server.setDifficultyLocked(ss.isLocked());
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
