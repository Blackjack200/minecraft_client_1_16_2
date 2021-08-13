package net.minecraft.client.multiplayer;

import org.apache.logging.log4j.LogManager;
import net.minecraft.network.protocol.game.ServerboundPickItemPacket;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.network.protocol.game.ServerboundContainerButtonClickPacket;
import net.minecraft.network.protocol.game.ServerboundPlaceRecipePacket;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.stats.StatsCounter;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.util.Mth;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.StructureBlock;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.world.level.GameType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;

public class MultiPlayerGameMode {
    private static final Logger LOGGER;
    private final Minecraft minecraft;
    private final ClientPacketListener connection;
    private BlockPos destroyBlockPos;
    private ItemStack destroyingItem;
    private float destroyProgress;
    private float destroyTicks;
    private int destroyDelay;
    private boolean isDestroying;
    private GameType localPlayerMode;
    private GameType previousLocalPlayerMode;
    private final Object2ObjectLinkedOpenHashMap<Pair<BlockPos, ServerboundPlayerActionPacket.Action>, Vec3> unAckedActions;
    private int carriedIndex;
    
    public MultiPlayerGameMode(final Minecraft djw, final ClientPacketListener dwm) {
        this.destroyBlockPos = new BlockPos(-1, -1, -1);
        this.destroyingItem = ItemStack.EMPTY;
        this.localPlayerMode = GameType.SURVIVAL;
        this.previousLocalPlayerMode = GameType.NOT_SET;
        this.unAckedActions = (Object2ObjectLinkedOpenHashMap<Pair<BlockPos, ServerboundPlayerActionPacket.Action>, Vec3>)new Object2ObjectLinkedOpenHashMap();
        this.minecraft = djw;
        this.connection = dwm;
    }
    
    public void adjustPlayer(final Player bft) {
        this.localPlayerMode.updatePlayerAbilities(bft.abilities);
    }
    
    public void setPreviousLocalMode(final GameType brr) {
        this.previousLocalPlayerMode = brr;
    }
    
    public void setLocalMode(final GameType brr) {
        if (brr != this.localPlayerMode) {
            this.previousLocalPlayerMode = this.localPlayerMode;
        }
        (this.localPlayerMode = brr).updatePlayerAbilities(this.minecraft.player.abilities);
    }
    
    public boolean canHurtPlayer() {
        return this.localPlayerMode.isSurvival();
    }
    
    public boolean destroyBlock(final BlockPos fx) {
        if (this.minecraft.player.blockActionRestricted(this.minecraft.level, fx, this.localPlayerMode)) {
            return false;
        }
        final Level bru3 = this.minecraft.level;
        final BlockState cee4 = bru3.getBlockState(fx);
        if (!this.minecraft.player.getMainHandItem().getItem().canAttackBlock(cee4, bru3, fx, this.minecraft.player)) {
            return false;
        }
        final Block bul5 = cee4.getBlock();
        if ((bul5 instanceof CommandBlock || bul5 instanceof StructureBlock || bul5 instanceof JigsawBlock) && !this.minecraft.player.canUseGameMasterBlocks()) {
            return false;
        }
        if (cee4.isAir()) {
            return false;
        }
        bul5.playerWillDestroy(bru3, fx, cee4, this.minecraft.player);
        final FluidState cuu6 = bru3.getFluidState(fx);
        final boolean boolean7 = bru3.setBlock(fx, cuu6.createLegacyBlock(), 11);
        if (boolean7) {
            bul5.destroy(bru3, fx, cee4);
        }
        return boolean7;
    }
    
    public boolean startDestroyBlock(final BlockPos fx, final Direction gc) {
        if (this.minecraft.player.blockActionRestricted(this.minecraft.level, fx, this.localPlayerMode)) {
            return false;
        }
        if (!this.minecraft.level.getWorldBorder().isWithinBounds(fx)) {
            return false;
        }
        if (this.localPlayerMode.isCreative()) {
            final BlockState cee4 = this.minecraft.level.getBlockState(fx);
            this.minecraft.getTutorial().onDestroyBlock(this.minecraft.level, fx, cee4, 1.0f);
            this.sendBlockAction(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, fx, gc);
            this.destroyBlock(fx);
            this.destroyDelay = 5;
        }
        else if (!this.isDestroying || !this.sameDestroyTarget(fx)) {
            if (this.isDestroying) {
                this.sendBlockAction(ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK, this.destroyBlockPos, gc);
            }
            final BlockState cee4 = this.minecraft.level.getBlockState(fx);
            this.minecraft.getTutorial().onDestroyBlock(this.minecraft.level, fx, cee4, 0.0f);
            this.sendBlockAction(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, fx, gc);
            final boolean boolean5 = !cee4.isAir();
            if (boolean5 && this.destroyProgress == 0.0f) {
                cee4.attack(this.minecraft.level, fx, this.minecraft.player);
            }
            if (boolean5 && cee4.getDestroyProgress(this.minecraft.player, this.minecraft.player.level, fx) >= 1.0f) {
                this.destroyBlock(fx);
            }
            else {
                this.isDestroying = true;
                this.destroyBlockPos = fx;
                this.destroyingItem = this.minecraft.player.getMainHandItem();
                this.destroyProgress = 0.0f;
                this.destroyTicks = 0.0f;
                this.minecraft.level.destroyBlockProgress(this.minecraft.player.getId(), this.destroyBlockPos, (int)(this.destroyProgress * 10.0f) - 1);
            }
        }
        return true;
    }
    
    public void stopDestroyBlock() {
        if (this.isDestroying) {
            final BlockState cee2 = this.minecraft.level.getBlockState(this.destroyBlockPos);
            this.minecraft.getTutorial().onDestroyBlock(this.minecraft.level, this.destroyBlockPos, cee2, -1.0f);
            this.sendBlockAction(ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK, this.destroyBlockPos, Direction.DOWN);
            this.isDestroying = false;
            this.destroyProgress = 0.0f;
            this.minecraft.level.destroyBlockProgress(this.minecraft.player.getId(), this.destroyBlockPos, -1);
            this.minecraft.player.resetAttackStrengthTicker();
        }
    }
    
    public boolean continueDestroyBlock(final BlockPos fx, final Direction gc) {
        this.ensureHasSentCarriedItem();
        if (this.destroyDelay > 0) {
            --this.destroyDelay;
            return true;
        }
        if (this.localPlayerMode.isCreative() && this.minecraft.level.getWorldBorder().isWithinBounds(fx)) {
            this.destroyDelay = 5;
            final BlockState cee4 = this.minecraft.level.getBlockState(fx);
            this.minecraft.getTutorial().onDestroyBlock(this.minecraft.level, fx, cee4, 1.0f);
            this.sendBlockAction(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, fx, gc);
            this.destroyBlock(fx);
            return true;
        }
        if (!this.sameDestroyTarget(fx)) {
            return this.startDestroyBlock(fx, gc);
        }
        final BlockState cee4 = this.minecraft.level.getBlockState(fx);
        if (cee4.isAir()) {
            return this.isDestroying = false;
        }
        this.destroyProgress += cee4.getDestroyProgress(this.minecraft.player, this.minecraft.player.level, fx);
        if (this.destroyTicks % 4.0f == 0.0f) {
            final SoundType cab5 = cee4.getSoundType();
            this.minecraft.getSoundManager().play(new SimpleSoundInstance(cab5.getHitSound(), SoundSource.BLOCKS, (cab5.getVolume() + 1.0f) / 8.0f, cab5.getPitch() * 0.5f, fx));
        }
        ++this.destroyTicks;
        this.minecraft.getTutorial().onDestroyBlock(this.minecraft.level, fx, cee4, Mth.clamp(this.destroyProgress, 0.0f, 1.0f));
        if (this.destroyProgress >= 1.0f) {
            this.isDestroying = false;
            this.sendBlockAction(ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK, fx, gc);
            this.destroyBlock(fx);
            this.destroyProgress = 0.0f;
            this.destroyTicks = 0.0f;
            this.destroyDelay = 5;
        }
        this.minecraft.level.destroyBlockProgress(this.minecraft.player.getId(), this.destroyBlockPos, (int)(this.destroyProgress * 10.0f) - 1);
        return true;
    }
    
    public float getPickRange() {
        if (this.localPlayerMode.isCreative()) {
            return 5.0f;
        }
        return 4.5f;
    }
    
    public void tick() {
        this.ensureHasSentCarriedItem();
        if (this.connection.getConnection().isConnected()) {
            this.connection.getConnection().tick();
        }
        else {
            this.connection.getConnection().handleDisconnection();
        }
    }
    
    private boolean sameDestroyTarget(final BlockPos fx) {
        final ItemStack bly3 = this.minecraft.player.getMainHandItem();
        boolean boolean4 = this.destroyingItem.isEmpty() && bly3.isEmpty();
        if (!this.destroyingItem.isEmpty() && !bly3.isEmpty()) {
            boolean4 = (bly3.getItem() == this.destroyingItem.getItem() && ItemStack.tagMatches(bly3, this.destroyingItem) && (bly3.isDamageableItem() || bly3.getDamageValue() == this.destroyingItem.getDamageValue()));
        }
        return fx.equals(this.destroyBlockPos) && boolean4;
    }
    
    private void ensureHasSentCarriedItem() {
        final int integer2 = this.minecraft.player.inventory.selected;
        if (integer2 != this.carriedIndex) {
            this.carriedIndex = integer2;
            this.connection.send(new ServerboundSetCarriedItemPacket(this.carriedIndex));
        }
    }
    
    public InteractionResult useItemOn(final LocalPlayer dze, final ClientLevel dwl, final InteractionHand aoq, final BlockHitResult dcg) {
        this.ensureHasSentCarriedItem();
        final BlockPos fx6 = dcg.getBlockPos();
        if (!this.minecraft.level.getWorldBorder().isWithinBounds(fx6)) {
            return InteractionResult.FAIL;
        }
        final ItemStack bly7 = dze.getItemInHand(aoq);
        if (this.localPlayerMode == GameType.SPECTATOR) {
            this.connection.send(new ServerboundUseItemOnPacket(aoq, dcg));
            return InteractionResult.SUCCESS;
        }
        final boolean boolean8 = !dze.getMainHandItem().isEmpty() || !dze.getOffhandItem().isEmpty();
        final boolean boolean9 = dze.isSecondaryUseActive() && boolean8;
        if (!boolean9) {
            final InteractionResult aor10 = dwl.getBlockState(fx6).use(dwl, dze, aoq, dcg);
            if (aor10.consumesAction()) {
                this.connection.send(new ServerboundUseItemOnPacket(aoq, dcg));
                return aor10;
            }
        }
        this.connection.send(new ServerboundUseItemOnPacket(aoq, dcg));
        if (bly7.isEmpty() || dze.getCooldowns().isOnCooldown(bly7.getItem())) {
            return InteractionResult.PASS;
        }
        final UseOnContext bnx11 = new UseOnContext(dze, aoq, dcg);
        InteractionResult aor10;
        if (this.localPlayerMode.isCreative()) {
            final int integer12 = bly7.getCount();
            aor10 = bly7.useOn(bnx11);
            bly7.setCount(integer12);
        }
        else {
            aor10 = bly7.useOn(bnx11);
        }
        return aor10;
    }
    
    public InteractionResult useItem(final Player bft, final Level bru, final InteractionHand aoq) {
        if (this.localPlayerMode == GameType.SPECTATOR) {
            return InteractionResult.PASS;
        }
        this.ensureHasSentCarriedItem();
        this.connection.send(new ServerboundUseItemPacket(aoq));
        final ItemStack bly5 = bft.getItemInHand(aoq);
        if (bft.getCooldowns().isOnCooldown(bly5.getItem())) {
            return InteractionResult.PASS;
        }
        final int integer6 = bly5.getCount();
        final InteractionResultHolder<ItemStack> aos7 = bly5.use(bru, bft, aoq);
        final ItemStack bly6 = aos7.getObject();
        if (bly6 != bly5) {
            bft.setItemInHand(aoq, bly6);
        }
        return aos7.getResult();
    }
    
    public LocalPlayer createPlayer(final ClientLevel dwl, final StatsCounter adz, final ClientRecipeBook djj) {
        return this.createPlayer(dwl, adz, djj, false, false);
    }
    
    public LocalPlayer createPlayer(final ClientLevel dwl, final StatsCounter adz, final ClientRecipeBook djj, final boolean boolean4, final boolean boolean5) {
        return new LocalPlayer(this.minecraft, dwl, this.connection, adz, djj, boolean4, boolean5);
    }
    
    public void attack(final Player bft, final Entity apx) {
        this.ensureHasSentCarriedItem();
        this.connection.send(new ServerboundInteractPacket(apx, bft.isShiftKeyDown()));
        if (this.localPlayerMode != GameType.SPECTATOR) {
            bft.attack(apx);
            bft.resetAttackStrengthTicker();
        }
    }
    
    public InteractionResult interact(final Player bft, final Entity apx, final InteractionHand aoq) {
        this.ensureHasSentCarriedItem();
        this.connection.send(new ServerboundInteractPacket(apx, aoq, bft.isShiftKeyDown()));
        if (this.localPlayerMode == GameType.SPECTATOR) {
            return InteractionResult.PASS;
        }
        return bft.interactOn(apx, aoq);
    }
    
    public InteractionResult interactAt(final Player bft, final Entity apx, final EntityHitResult dch, final InteractionHand aoq) {
        this.ensureHasSentCarriedItem();
        final Vec3 dck6 = dch.getLocation().subtract(apx.getX(), apx.getY(), apx.getZ());
        this.connection.send(new ServerboundInteractPacket(apx, aoq, dck6, bft.isShiftKeyDown()));
        if (this.localPlayerMode == GameType.SPECTATOR) {
            return InteractionResult.PASS;
        }
        return apx.interactAt(bft, dck6, aoq);
    }
    
    public ItemStack handleInventoryMouseClick(final int integer1, final int integer2, final int integer3, final ClickType bih, final Player bft) {
        final short short7 = bft.containerMenu.backup(bft.inventory);
        final ItemStack bly8 = bft.containerMenu.clicked(integer2, integer3, bih, bft);
        this.connection.send(new ServerboundContainerClickPacket(integer1, integer2, integer3, bih, bly8, short7));
        return bly8;
    }
    
    public void handlePlaceRecipe(final int integer, final Recipe<?> bon, final boolean boolean3) {
        this.connection.send(new ServerboundPlaceRecipePacket(integer, bon, boolean3));
    }
    
    public void handleInventoryButtonClick(final int integer1, final int integer2) {
        this.connection.send(new ServerboundContainerButtonClickPacket(integer1, integer2));
    }
    
    public void handleCreativeModeItemAdd(final ItemStack bly, final int integer) {
        if (this.localPlayerMode.isCreative()) {
            this.connection.send(new ServerboundSetCreativeModeSlotPacket(integer, bly));
        }
    }
    
    public void handleCreativeModeItemDrop(final ItemStack bly) {
        if (this.localPlayerMode.isCreative() && !bly.isEmpty()) {
            this.connection.send(new ServerboundSetCreativeModeSlotPacket(-1, bly));
        }
    }
    
    public void releaseUsingItem(final Player bft) {
        this.ensureHasSentCarriedItem();
        this.connection.send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM, BlockPos.ZERO, Direction.DOWN));
        bft.releaseUsingItem();
    }
    
    public boolean hasExperience() {
        return this.localPlayerMode.isSurvival();
    }
    
    public boolean hasMissTime() {
        return !this.localPlayerMode.isCreative();
    }
    
    public boolean hasInfiniteItems() {
        return this.localPlayerMode.isCreative();
    }
    
    public boolean hasFarPickRange() {
        return this.localPlayerMode.isCreative();
    }
    
    public boolean isServerControlledInventory() {
        return this.minecraft.player.isPassenger() && this.minecraft.player.getVehicle() instanceof AbstractHorse;
    }
    
    public boolean isAlwaysFlying() {
        return this.localPlayerMode == GameType.SPECTATOR;
    }
    
    public GameType getPreviousPlayerMode() {
        return this.previousLocalPlayerMode;
    }
    
    public GameType getPlayerMode() {
        return this.localPlayerMode;
    }
    
    public boolean isDestroying() {
        return this.isDestroying;
    }
    
    public void handlePickItem(final int integer) {
        this.connection.send(new ServerboundPickItemPacket(integer));
    }
    
    private void sendBlockAction(final ServerboundPlayerActionPacket.Action a, final BlockPos fx, final Direction gc) {
        final LocalPlayer dze5 = this.minecraft.player;
        this.unAckedActions.put(Pair.of((Object)fx, (Object)a), dze5.position());
        this.connection.send(new ServerboundPlayerActionPacket(a, fx, gc));
    }
    
    public void handleBlockBreakAck(final ClientLevel dwl, final BlockPos fx, final BlockState cee, final ServerboundPlayerActionPacket.Action a, final boolean boolean5) {
        final Vec3 dck7 = (Vec3)this.unAckedActions.remove(Pair.of((Object)fx, (Object)a));
        final BlockState cee2 = dwl.getBlockState(fx);
        if ((dck7 == null || !boolean5 || (a != ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK && cee2 != cee)) && cee2 != cee) {
            dwl.setKnownState(fx, cee);
            final Player bft9 = this.minecraft.player;
            if (dck7 != null && dwl == bft9.level && bft9.isColliding(fx, cee)) {
                bft9.absMoveTo(dck7.x, dck7.y, dck7.z);
            }
        }
        while (this.unAckedActions.size() >= 50) {
            final Pair<BlockPos, ServerboundPlayerActionPacket.Action> pair9 = (Pair<BlockPos, ServerboundPlayerActionPacket.Action>)this.unAckedActions.firstKey();
            this.unAckedActions.removeFirst();
            MultiPlayerGameMode.LOGGER.error(new StringBuilder().append("Too many unacked block actions, dropping ").append(pair9).toString());
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
