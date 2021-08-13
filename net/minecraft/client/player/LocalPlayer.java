package net.minecraft.client.player;

import java.util.stream.Stream;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.client.resources.sounds.UnderwaterAmbientSoundInstances;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.Vec2;
import java.util.function.Predicate;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.effect.MobEffectInstance;
import javax.annotation.Nullable;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Pose;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.Item;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.world.item.Items;
import net.minecraft.client.gui.screens.inventory.JigsawBlockEditScreen;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import net.minecraft.client.gui.screens.inventory.StructureBlockEditScreen;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.client.gui.screens.inventory.CommandBlockEditScreen;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraft.client.gui.screens.inventory.MinecartCommandBlockEditScreen;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.client.resources.sounds.ElytraOnPlayerSoundInstance;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import java.util.UUID;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.BiPredicate;
import net.minecraft.world.phys.AABB;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundRecipeBookSeenRecipePacket;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.util.Mth;
import net.minecraft.network.protocol.game.ServerboundPlayerAbilitiesPacket;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import java.util.Iterator;
import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.resources.sounds.RidingMinecartSoundInstance;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.client.resources.sounds.BiomeAmbientSoundsHandler;
import net.minecraft.client.resources.sounds.BubbleColumnAmbientSoundHandler;
import net.minecraft.client.resources.sounds.UnderwaterAmbientSoundHandler;
import com.google.common.collect.Lists;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AmbientSoundHandler;
import java.util.List;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.stats.StatsCounter;
import net.minecraft.client.multiplayer.ClientPacketListener;

public class LocalPlayer extends AbstractClientPlayer {
    public final ClientPacketListener connection;
    private final StatsCounter stats;
    private final ClientRecipeBook recipeBook;
    private final List<AmbientSoundHandler> ambientSoundHandlers;
    private int permissionLevel;
    private double xLast;
    private double yLast1;
    private double zLast;
    private float yRotLast;
    private float xRotLast;
    private boolean lastOnGround;
    private boolean crouching;
    private boolean wasShiftKeyDown;
    private boolean wasSprinting;
    private int positionReminder;
    private boolean flashOnSetHealth;
    private String serverBrand;
    public Input input;
    protected final Minecraft minecraft;
    protected int sprintTriggerTime;
    public int sprintTime;
    public float yBob;
    public float xBob;
    public float yBobO;
    public float xBobO;
    private int jumpRidingTicks;
    private float jumpRidingScale;
    public float portalTime;
    public float oPortalTime;
    private boolean startedUsingItem;
    private InteractionHand usingItemHand;
    private boolean handsBusy;
    private boolean autoJumpEnabled;
    private int autoJumpTime;
    private boolean wasFallFlying;
    private int waterVisionTime;
    private boolean showDeathScreen;
    
    public LocalPlayer(final Minecraft djw, final ClientLevel dwl, final ClientPacketListener dwm, final StatsCounter adz, final ClientRecipeBook djj, final boolean boolean6, final boolean boolean7) {
        super(dwl, dwm.getLocalGameProfile());
        this.ambientSoundHandlers = (List<AmbientSoundHandler>)Lists.newArrayList();
        this.permissionLevel = 0;
        this.autoJumpEnabled = true;
        this.showDeathScreen = true;
        this.minecraft = djw;
        this.connection = dwm;
        this.stats = adz;
        this.recipeBook = djj;
        this.wasShiftKeyDown = boolean6;
        this.wasSprinting = boolean7;
        this.ambientSoundHandlers.add(new UnderwaterAmbientSoundHandler(this, djw.getSoundManager()));
        this.ambientSoundHandlers.add(new BubbleColumnAmbientSoundHandler(this));
        this.ambientSoundHandlers.add(new BiomeAmbientSoundsHandler(this, djw.getSoundManager(), dwl.getBiomeManager()));
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        return false;
    }
    
    @Override
    public void heal(final float float1) {
    }
    
    @Override
    public boolean startRiding(final Entity apx, final boolean boolean2) {
        if (!super.startRiding(apx, boolean2)) {
            return false;
        }
        if (apx instanceof AbstractMinecart) {
            this.minecraft.getSoundManager().play(new RidingMinecartSoundInstance(this, (AbstractMinecart)apx));
        }
        if (apx instanceof Boat) {
            this.yRotO = apx.yRot;
            this.yRot = apx.yRot;
            this.setYHeadRot(apx.yRot);
        }
        return true;
    }
    
    @Override
    public void removeVehicle() {
        super.removeVehicle();
        this.handsBusy = false;
    }
    
    @Override
    public float getViewXRot(final float float1) {
        return this.xRot;
    }
    
    @Override
    public float getViewYRot(final float float1) {
        if (this.isPassenger()) {
            return super.getViewYRot(float1);
        }
        return this.yRot;
    }
    
    @Override
    public void tick() {
        if (!this.level.hasChunkAt(new BlockPos(this.getX(), 0.0, this.getZ()))) {
            return;
        }
        super.tick();
        if (this.isPassenger()) {
            this.connection.send(new ServerboundMovePlayerPacket.Rot(this.yRot, this.xRot, this.onGround));
            this.connection.send(new ServerboundPlayerInputPacket(this.xxa, this.zza, this.input.jumping, this.input.shiftKeyDown));
            final Entity apx2 = this.getRootVehicle();
            if (apx2 != this && apx2.isControlledByLocalInstance()) {
                this.connection.send(new ServerboundMoveVehiclePacket(apx2));
            }
        }
        else {
            this.sendPosition();
        }
        for (final AmbientSoundHandler elw3 : this.ambientSoundHandlers) {
            elw3.tick();
        }
    }
    
    public float getCurrentMood() {
        for (final AmbientSoundHandler elw3 : this.ambientSoundHandlers) {
            if (elw3 instanceof BiomeAmbientSoundsHandler) {
                return ((BiomeAmbientSoundsHandler)elw3).getMoodiness();
            }
        }
        return 0.0f;
    }
    
    private void sendPosition() {
        final boolean boolean2 = this.isSprinting();
        if (boolean2 != this.wasSprinting) {
            final ServerboundPlayerCommandPacket.Action a3 = boolean2 ? ServerboundPlayerCommandPacket.Action.START_SPRINTING : ServerboundPlayerCommandPacket.Action.STOP_SPRINTING;
            this.connection.send(new ServerboundPlayerCommandPacket(this, a3));
            this.wasSprinting = boolean2;
        }
        final boolean boolean3 = this.isShiftKeyDown();
        if (boolean3 != this.wasShiftKeyDown) {
            final ServerboundPlayerCommandPacket.Action a4 = boolean3 ? ServerboundPlayerCommandPacket.Action.PRESS_SHIFT_KEY : ServerboundPlayerCommandPacket.Action.RELEASE_SHIFT_KEY;
            this.connection.send(new ServerboundPlayerCommandPacket(this, a4));
            this.wasShiftKeyDown = boolean3;
        }
        if (this.isControlledCamera()) {
            final double double4 = this.getX() - this.xLast;
            final double double5 = this.getY() - this.yLast1;
            final double double6 = this.getZ() - this.zLast;
            final double double7 = this.yRot - this.yRotLast;
            final double double8 = this.xRot - this.xRotLast;
            ++this.positionReminder;
            boolean boolean4 = double4 * double4 + double5 * double5 + double6 * double6 > 9.0E-4 || this.positionReminder >= 20;
            final boolean boolean5 = double7 != 0.0 || double8 != 0.0;
            if (this.isPassenger()) {
                final Vec3 dck16 = this.getDeltaMovement();
                this.connection.send(new ServerboundMovePlayerPacket.PosRot(dck16.x, -999.0, dck16.z, this.yRot, this.xRot, this.onGround));
                boolean4 = false;
            }
            else if (boolean4 && boolean5) {
                this.connection.send(new ServerboundMovePlayerPacket.PosRot(this.getX(), this.getY(), this.getZ(), this.yRot, this.xRot, this.onGround));
            }
            else if (boolean4) {
                this.connection.send(new ServerboundMovePlayerPacket.Pos(this.getX(), this.getY(), this.getZ(), this.onGround));
            }
            else if (boolean5) {
                this.connection.send(new ServerboundMovePlayerPacket.Rot(this.yRot, this.xRot, this.onGround));
            }
            else if (this.lastOnGround != this.onGround) {
                this.connection.send(new ServerboundMovePlayerPacket(this.onGround));
            }
            if (boolean4) {
                this.xLast = this.getX();
                this.yLast1 = this.getY();
                this.zLast = this.getZ();
                this.positionReminder = 0;
            }
            if (boolean5) {
                this.yRotLast = this.yRot;
                this.xRotLast = this.xRot;
            }
            this.lastOnGround = this.onGround;
            this.autoJumpEnabled = this.minecraft.options.autoJump;
        }
    }
    
    @Override
    public boolean drop(final boolean boolean1) {
        final ServerboundPlayerActionPacket.Action a3 = boolean1 ? ServerboundPlayerActionPacket.Action.DROP_ALL_ITEMS : ServerboundPlayerActionPacket.Action.DROP_ITEM;
        this.connection.send(new ServerboundPlayerActionPacket(a3, BlockPos.ZERO, Direction.DOWN));
        return this.inventory.removeItem(this.inventory.selected, (boolean1 && !this.inventory.getSelected().isEmpty()) ? this.inventory.getSelected().getCount() : 1) != ItemStack.EMPTY;
    }
    
    public void chat(final String string) {
        this.connection.send(new ServerboundChatPacket(string));
    }
    
    @Override
    public void swing(final InteractionHand aoq) {
        super.swing(aoq);
        this.connection.send(new ServerboundSwingPacket(aoq));
    }
    
    @Override
    public void respawn() {
        this.connection.send(new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.PERFORM_RESPAWN));
    }
    
    @Override
    protected void actuallyHurt(final DamageSource aph, final float float2) {
        if (this.isInvulnerableTo(aph)) {
            return;
        }
        this.setHealth(this.getHealth() - float2);
    }
    
    public void closeContainer() {
        this.connection.send(new ServerboundContainerClosePacket(this.containerMenu.containerId));
        this.clientSideCloseContainer();
    }
    
    public void clientSideCloseContainer() {
        this.inventory.setCarried(ItemStack.EMPTY);
        super.closeContainer();
        this.minecraft.setScreen(null);
    }
    
    public void hurtTo(final float float1) {
        if (this.flashOnSetHealth) {
            final float float2 = this.getHealth() - float1;
            if (float2 <= 0.0f) {
                this.setHealth(float1);
                if (float2 < 0.0f) {
                    this.invulnerableTime = 10;
                }
            }
            else {
                this.lastHurt = float2;
                this.setHealth(this.getHealth());
                this.invulnerableTime = 20;
                this.actuallyHurt(DamageSource.GENERIC, float2);
                this.hurtDuration = 10;
                this.hurtTime = this.hurtDuration;
            }
        }
        else {
            this.setHealth(float1);
            this.flashOnSetHealth = true;
        }
    }
    
    @Override
    public void onUpdateAbilities() {
        this.connection.send(new ServerboundPlayerAbilitiesPacket(this.abilities));
    }
    
    @Override
    public boolean isLocalPlayer() {
        return true;
    }
    
    @Override
    public boolean isSuppressingSlidingDownLadder() {
        return !this.abilities.flying && super.isSuppressingSlidingDownLadder();
    }
    
    @Override
    public boolean canSpawnSprintParticle() {
        return !this.abilities.flying && super.canSpawnSprintParticle();
    }
    
    @Override
    public boolean canSpawnSoulSpeedParticle() {
        return !this.abilities.flying && super.canSpawnSoulSpeedParticle();
    }
    
    protected void sendRidingJump() {
        this.connection.send(new ServerboundPlayerCommandPacket(this, ServerboundPlayerCommandPacket.Action.START_RIDING_JUMP, Mth.floor(this.getJumpRidingScale() * 100.0f)));
    }
    
    public void sendOpenInventory() {
        this.connection.send(new ServerboundPlayerCommandPacket(this, ServerboundPlayerCommandPacket.Action.OPEN_INVENTORY));
    }
    
    public void setServerBrand(final String string) {
        this.serverBrand = string;
    }
    
    public String getServerBrand() {
        return this.serverBrand;
    }
    
    public StatsCounter getStats() {
        return this.stats;
    }
    
    public ClientRecipeBook getRecipeBook() {
        return this.recipeBook;
    }
    
    public void removeRecipeHighlight(final Recipe<?> bon) {
        if (this.recipeBook.willHighlight(bon)) {
            this.recipeBook.removeHighlight(bon);
            this.connection.send(new ServerboundRecipeBookSeenRecipePacket(bon));
        }
    }
    
    @Override
    protected int getPermissionLevel() {
        return this.permissionLevel;
    }
    
    public void setPermissionLevel(final int integer) {
        this.permissionLevel = integer;
    }
    
    @Override
    public void displayClientMessage(final Component nr, final boolean boolean2) {
        if (boolean2) {
            this.minecraft.gui.setOverlayMessage(nr, false);
        }
        else {
            this.minecraft.gui.getChat().addMessage(nr);
        }
    }
    
    private void moveTowardsClosestSpace(final double double1, final double double2) {
        final BlockPos fx6 = new BlockPos(double1, this.getY(), double2);
        if (!this.suffocatesAt(fx6)) {
            return;
        }
        final double double3 = double1 - fx6.getX();
        final double double4 = double2 - fx6.getZ();
        Direction gc11 = null;
        double double5 = Double.MAX_VALUE;
        final Direction[] array;
        final Direction[] arr14 = array = new Direction[] { Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH };
        for (final Direction gc12 : array) {
            final double double6 = gc12.getAxis().choose(double3, 0.0, double4);
            final double double7 = (gc12.getAxisDirection() == Direction.AxisDirection.POSITIVE) ? (1.0 - double6) : double6;
            if (double7 < double5 && !this.suffocatesAt(fx6.relative(gc12))) {
                double5 = double7;
                gc11 = gc12;
            }
        }
        if (gc11 != null) {
            final Vec3 dck15 = this.getDeltaMovement();
            if (gc11.getAxis() == Direction.Axis.X) {
                this.setDeltaMovement(0.1 * gc11.getStepX(), dck15.y, dck15.z);
            }
            else {
                this.setDeltaMovement(dck15.x, dck15.y, 0.1 * gc11.getStepZ());
            }
        }
    }
    
    private boolean suffocatesAt(final BlockPos fx) {
        final AABB dcf3 = this.getBoundingBox();
        final AABB dcf4 = new AABB(fx.getX(), dcf3.minY, fx.getZ(), fx.getX() + 1.0, dcf3.maxY, fx.getZ() + 1.0).deflate(1.0E-7);
        return !this.level.noBlockCollision(this, dcf4, (BiPredicate<BlockState, BlockPos>)((cee, fx) -> cee.isSuffocating(this.level, fx)));
    }
    
    @Override
    public void setSprinting(final boolean boolean1) {
        super.setSprinting(boolean1);
        this.sprintTime = 0;
    }
    
    public void setExperienceValues(final float float1, final int integer2, final int integer3) {
        this.experienceProgress = float1;
        this.totalExperience = integer2;
        this.experienceLevel = integer3;
    }
    
    @Override
    public void sendMessage(final Component nr, final UUID uUID) {
        this.minecraft.gui.getChat().addMessage(nr);
    }
    
    @Override
    public void handleEntityEvent(final byte byte1) {
        if (byte1 >= 24 && byte1 <= 28) {
            this.setPermissionLevel(byte1 - 24);
        }
        else {
            super.handleEntityEvent(byte1);
        }
    }
    
    public void setShowDeathScreen(final boolean boolean1) {
        this.showDeathScreen = boolean1;
    }
    
    public boolean shouldShowDeathScreen() {
        return this.showDeathScreen;
    }
    
    @Override
    public void playSound(final SoundEvent adn, final float float2, final float float3) {
        this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), adn, this.getSoundSource(), float2, float3, false);
    }
    
    @Override
    public void playNotifySound(final SoundEvent adn, final SoundSource adp, final float float3, final float float4) {
        this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), adn, adp, float3, float4, false);
    }
    
    @Override
    public boolean isEffectiveAi() {
        return true;
    }
    
    @Override
    public void startUsingItem(final InteractionHand aoq) {
        final ItemStack bly3 = this.getItemInHand(aoq);
        if (bly3.isEmpty() || this.isUsingItem()) {
            return;
        }
        super.startUsingItem(aoq);
        this.startedUsingItem = true;
        this.usingItemHand = aoq;
    }
    
    @Override
    public boolean isUsingItem() {
        return this.startedUsingItem;
    }
    
    @Override
    public void stopUsingItem() {
        super.stopUsingItem();
        this.startedUsingItem = false;
    }
    
    @Override
    public InteractionHand getUsedItemHand() {
        return this.usingItemHand;
    }
    
    @Override
    public void onSyncedDataUpdated(final EntityDataAccessor<?> us) {
        super.onSyncedDataUpdated(us);
        if (LocalPlayer.DATA_LIVING_ENTITY_FLAGS.equals(us)) {
            final boolean boolean3 = (this.entityData.<Byte>get(LocalPlayer.DATA_LIVING_ENTITY_FLAGS) & 0x1) > 0;
            final InteractionHand aoq4 = ((this.entityData.<Byte>get(LocalPlayer.DATA_LIVING_ENTITY_FLAGS) & 0x2) > 0) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
            if (boolean3 && !this.startedUsingItem) {
                this.startUsingItem(aoq4);
            }
            else if (!boolean3 && this.startedUsingItem) {
                this.stopUsingItem();
            }
        }
        if (LocalPlayer.DATA_SHARED_FLAGS_ID.equals(us) && this.isFallFlying() && !this.wasFallFlying) {
            this.minecraft.getSoundManager().play(new ElytraOnPlayerSoundInstance(this));
        }
    }
    
    public boolean isRidingJumpable() {
        final Entity apx2 = this.getVehicle();
        return this.isPassenger() && apx2 instanceof PlayerRideableJumping && ((PlayerRideableJumping)apx2).canJump();
    }
    
    public float getJumpRidingScale() {
        return this.jumpRidingScale;
    }
    
    @Override
    public void openTextEdit(final SignBlockEntity cdc) {
        this.minecraft.setScreen(new SignEditScreen(cdc));
    }
    
    @Override
    public void openMinecartCommandBlock(final BaseCommandBlock bqv) {
        this.minecraft.setScreen(new MinecartCommandBlockEditScreen(bqv));
    }
    
    @Override
    public void openCommandBlock(final CommandBlockEntity ccl) {
        this.minecraft.setScreen(new CommandBlockEditScreen(ccl));
    }
    
    @Override
    public void openStructureBlock(final StructureBlockEntity cdg) {
        this.minecraft.setScreen(new StructureBlockEditScreen(cdg));
    }
    
    @Override
    public void openJigsawBlock(final JigsawBlockEntity ccw) {
        this.minecraft.setScreen(new JigsawBlockEditScreen(ccw));
    }
    
    @Override
    public void openItemGui(final ItemStack bly, final InteractionHand aoq) {
        final Item blu4 = bly.getItem();
        if (blu4 == Items.WRITABLE_BOOK) {
            this.minecraft.setScreen(new BookEditScreen(this, bly, aoq));
        }
    }
    
    @Override
    public void crit(final Entity apx) {
        this.minecraft.particleEngine.createTrackingEmitter(apx, ParticleTypes.CRIT);
    }
    
    @Override
    public void magicCrit(final Entity apx) {
        this.minecraft.particleEngine.createTrackingEmitter(apx, ParticleTypes.ENCHANTED_HIT);
    }
    
    @Override
    public boolean isShiftKeyDown() {
        return this.input != null && this.input.shiftKeyDown;
    }
    
    @Override
    public boolean isCrouching() {
        return this.crouching;
    }
    
    public boolean isMovingSlowly() {
        return this.isCrouching() || this.isVisuallyCrawling();
    }
    
    public void serverAiStep() {
        super.serverAiStep();
        if (this.isControlledCamera()) {
            this.xxa = this.input.leftImpulse;
            this.zza = this.input.forwardImpulse;
            this.jumping = this.input.jumping;
            this.yBobO = this.yBob;
            this.xBobO = this.xBob;
            this.xBob += (float)((this.xRot - this.xBob) * 0.5);
            this.yBob += (float)((this.yRot - this.yBob) * 0.5);
        }
    }
    
    protected boolean isControlledCamera() {
        return this.minecraft.getCameraEntity() == this;
    }
    
    @Override
    public void aiStep() {
        ++this.sprintTime;
        if (this.sprintTriggerTime > 0) {
            --this.sprintTriggerTime;
        }
        this.handleNetherPortalClient();
        final boolean boolean2 = this.input.jumping;
        final boolean boolean3 = this.input.shiftKeyDown;
        final boolean boolean4 = this.hasEnoughImpulseToStartSprinting();
        this.crouching = (!this.abilities.flying && !this.isSwimming() && this.canEnterPose(Pose.CROUCHING) && (this.isShiftKeyDown() || (!this.isSleeping() && !this.canEnterPose(Pose.STANDING))));
        this.input.tick(this.isMovingSlowly());
        this.minecraft.getTutorial().onInput(this.input);
        if (this.isUsingItem() && !this.isPassenger()) {
            final Input input = this.input;
            input.leftImpulse *= 0.2f;
            final Input input2 = this.input;
            input2.forwardImpulse *= 0.2f;
            this.sprintTriggerTime = 0;
        }
        boolean boolean5 = false;
        if (this.autoJumpTime > 0) {
            --this.autoJumpTime;
            boolean5 = true;
            this.input.jumping = true;
        }
        if (!this.noPhysics) {
            this.moveTowardsClosestSpace(this.getX() - this.getBbWidth() * 0.35, this.getZ() + this.getBbWidth() * 0.35);
            this.moveTowardsClosestSpace(this.getX() - this.getBbWidth() * 0.35, this.getZ() - this.getBbWidth() * 0.35);
            this.moveTowardsClosestSpace(this.getX() + this.getBbWidth() * 0.35, this.getZ() - this.getBbWidth() * 0.35);
            this.moveTowardsClosestSpace(this.getX() + this.getBbWidth() * 0.35, this.getZ() + this.getBbWidth() * 0.35);
        }
        if (boolean3) {
            this.sprintTriggerTime = 0;
        }
        final boolean boolean6 = this.getFoodData().getFoodLevel() > 6.0f || this.abilities.mayfly;
        if ((this.onGround || this.isUnderWater()) && !boolean3 && !boolean4 && this.hasEnoughImpulseToStartSprinting() && !this.isSprinting() && boolean6 && !this.isUsingItem() && !this.hasEffect(MobEffects.BLINDNESS)) {
            if (this.sprintTriggerTime > 0 || this.minecraft.options.keySprint.isDown()) {
                this.setSprinting(true);
            }
            else {
                this.sprintTriggerTime = 7;
            }
        }
        if (!this.isSprinting() && (!this.isInWater() || this.isUnderWater()) && this.hasEnoughImpulseToStartSprinting() && boolean6 && !this.isUsingItem() && !this.hasEffect(MobEffects.BLINDNESS) && this.minecraft.options.keySprint.isDown()) {
            this.setSprinting(true);
        }
        if (this.isSprinting()) {
            final boolean boolean7 = !this.input.hasForwardImpulse() || !boolean6;
            final boolean boolean8 = boolean7 || this.horizontalCollision || (this.isInWater() && !this.isUnderWater());
            if (this.isSwimming()) {
                if ((!this.onGround && !this.input.shiftKeyDown && boolean7) || !this.isInWater()) {
                    this.setSprinting(false);
                }
            }
            else if (boolean8) {
                this.setSprinting(false);
            }
        }
        boolean boolean7 = false;
        if (this.abilities.mayfly) {
            if (this.minecraft.gameMode.isAlwaysFlying()) {
                if (!this.abilities.flying) {
                    this.abilities.flying = true;
                    boolean7 = true;
                    this.onUpdateAbilities();
                }
            }
            else if (!boolean2 && this.input.jumping && !boolean5) {
                if (this.jumpTriggerTime == 0) {
                    this.jumpTriggerTime = 7;
                }
                else if (!this.isSwimming()) {
                    this.abilities.flying = !this.abilities.flying;
                    boolean7 = true;
                    this.onUpdateAbilities();
                    this.jumpTriggerTime = 0;
                }
            }
        }
        if (this.input.jumping && !boolean7 && !boolean2 && !this.abilities.flying && !this.isPassenger() && !this.onClimbable()) {
            final ItemStack bly8 = this.getItemBySlot(EquipmentSlot.CHEST);
            if (bly8.getItem() == Items.ELYTRA && ElytraItem.isFlyEnabled(bly8) && this.tryToStartFallFlying()) {
                this.connection.send(new ServerboundPlayerCommandPacket(this, ServerboundPlayerCommandPacket.Action.START_FALL_FLYING));
            }
        }
        this.wasFallFlying = this.isFallFlying();
        if (this.isInWater() && this.input.shiftKeyDown && this.isAffectedByFluids()) {
            this.goDownInWater();
        }
        if (this.isEyeInFluid(FluidTags.WATER)) {
            final int integer8 = this.isSpectator() ? 10 : 1;
            this.waterVisionTime = Mth.clamp(this.waterVisionTime + integer8, 0, 600);
        }
        else if (this.waterVisionTime > 0) {
            this.isEyeInFluid(FluidTags.WATER);
            this.waterVisionTime = Mth.clamp(this.waterVisionTime - 10, 0, 600);
        }
        if (this.abilities.flying && this.isControlledCamera()) {
            int integer8 = 0;
            if (this.input.shiftKeyDown) {
                --integer8;
            }
            if (this.input.jumping) {
                ++integer8;
            }
            if (integer8 != 0) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, integer8 * this.abilities.getFlyingSpeed() * 3.0f, 0.0));
            }
        }
        if (this.isRidingJumpable()) {
            final PlayerRideableJumping aqt8 = (PlayerRideableJumping)this.getVehicle();
            if (this.jumpRidingTicks < 0) {
                ++this.jumpRidingTicks;
                if (this.jumpRidingTicks == 0) {
                    this.jumpRidingScale = 0.0f;
                }
            }
            if (boolean2 && !this.input.jumping) {
                this.jumpRidingTicks = -10;
                aqt8.onPlayerJump(Mth.floor(this.getJumpRidingScale() * 100.0f));
                this.sendRidingJump();
            }
            else if (!boolean2 && this.input.jumping) {
                this.jumpRidingTicks = 0;
                this.jumpRidingScale = 0.0f;
            }
            else if (boolean2) {
                ++this.jumpRidingTicks;
                if (this.jumpRidingTicks < 10) {
                    this.jumpRidingScale = this.jumpRidingTicks * 0.1f;
                }
                else {
                    this.jumpRidingScale = 0.8f + 2.0f / (this.jumpRidingTicks - 9) * 0.1f;
                }
            }
        }
        else {
            this.jumpRidingScale = 0.0f;
        }
        super.aiStep();
        if (this.onGround && this.abilities.flying && !this.minecraft.gameMode.isAlwaysFlying()) {
            this.abilities.flying = false;
            this.onUpdateAbilities();
        }
    }
    
    private void handleNetherPortalClient() {
        this.oPortalTime = this.portalTime;
        if (this.isInsidePortal) {
            if (this.minecraft.screen != null && !this.minecraft.screen.isPauseScreen()) {
                if (this.minecraft.screen instanceof AbstractContainerScreen) {
                    this.closeContainer();
                }
                this.minecraft.setScreen(null);
            }
            if (this.portalTime == 0.0f) {
                this.minecraft.getSoundManager().play(SimpleSoundInstance.forLocalAmbience(SoundEvents.PORTAL_TRIGGER, this.random.nextFloat() * 0.4f + 0.8f, 0.25f));
            }
            this.portalTime += 0.0125f;
            if (this.portalTime >= 1.0f) {
                this.portalTime = 1.0f;
            }
            this.isInsidePortal = false;
        }
        else if (this.hasEffect(MobEffects.CONFUSION) && this.getEffect(MobEffects.CONFUSION).getDuration() > 60) {
            this.portalTime += 0.006666667f;
            if (this.portalTime > 1.0f) {
                this.portalTime = 1.0f;
            }
        }
        else {
            if (this.portalTime > 0.0f) {
                this.portalTime -= 0.05f;
            }
            if (this.portalTime < 0.0f) {
                this.portalTime = 0.0f;
            }
        }
        this.processPortalCooldown();
    }
    
    @Override
    public void rideTick() {
        super.rideTick();
        this.handsBusy = false;
        if (this.getVehicle() instanceof Boat) {
            final Boat bhk2 = (Boat)this.getVehicle();
            bhk2.setInput(this.input.left, this.input.right, this.input.up, this.input.down);
            this.handsBusy |= (this.input.left || this.input.right || this.input.up || this.input.down);
        }
    }
    
    public boolean isHandsBusy() {
        return this.handsBusy;
    }
    
    @Nullable
    @Override
    public MobEffectInstance removeEffectNoUpdate(@Nullable final MobEffect app) {
        if (app == MobEffects.CONFUSION) {
            this.oPortalTime = 0.0f;
            this.portalTime = 0.0f;
        }
        return super.removeEffectNoUpdate(app);
    }
    
    @Override
    public void move(final MoverType aqo, final Vec3 dck) {
        final double double4 = this.getX();
        final double double5 = this.getZ();
        super.move(aqo, dck);
        this.updateAutoJump((float)(this.getX() - double4), (float)(this.getZ() - double5));
    }
    
    public boolean isAutoJumpEnabled() {
        return this.autoJumpEnabled;
    }
    
    protected void updateAutoJump(final float float1, final float float2) {
        if (!this.canAutoJump()) {
            return;
        }
        final Vec3 dck4 = this.position();
        final Vec3 dck5 = dck4.add(float1, 0.0, float2);
        Vec3 dck6 = new Vec3(float1, 0.0, float2);
        final float float3 = this.getSpeed();
        float float4 = (float)dck6.lengthSqr();
        if (float4 <= 0.001f) {
            final Vec2 dcj9 = this.input.getMoveVector();
            final float float5 = float3 * dcj9.x;
            final float float6 = float3 * dcj9.y;
            final float float7 = Mth.sin(this.yRot * 0.017453292f);
            final float float8 = Mth.cos(this.yRot * 0.017453292f);
            dck6 = new Vec3(float5 * float8 - float6 * float7, dck6.y, float6 * float8 + float5 * float7);
            float4 = (float)dck6.lengthSqr();
            if (float4 <= 0.001f) {
                return;
            }
        }
        final float float9 = Mth.fastInvSqrt(float4);
        final Vec3 dck7 = dck6.scale(float9);
        final Vec3 dck8 = this.getForward();
        final float float7 = (float)(dck8.x * dck7.x + dck8.z * dck7.z);
        if (float7 < -0.15f) {
            return;
        }
        final CollisionContext dcp13 = CollisionContext.of(this);
        BlockPos fx14 = new BlockPos(this.getX(), this.getBoundingBox().maxY, this.getZ());
        final BlockState cee15 = this.level.getBlockState(fx14);
        if (!cee15.getCollisionShape(this.level, fx14, dcp13).isEmpty()) {
            return;
        }
        fx14 = fx14.above();
        final BlockState cee16 = this.level.getBlockState(fx14);
        if (!cee16.getCollisionShape(this.level, fx14, dcp13).isEmpty()) {
            return;
        }
        final float float10 = 7.0f;
        float float11 = 1.2f;
        if (this.hasEffect(MobEffects.JUMP)) {
            float11 += (this.getEffect(MobEffects.JUMP).getAmplifier() + 1) * 0.75f;
        }
        final float float12 = Math.max(float3 * 7.0f, 1.0f / float9);
        Vec3 dck9 = dck4;
        Vec3 dck10 = dck5.add(dck7.scale(float12));
        final float float13 = this.getBbWidth();
        final float float14 = this.getBbHeight();
        final AABB dcf24 = new AABB(dck9, dck10.add(0.0, float14, 0.0)).inflate(float13, 0.0, float13);
        dck9 = dck9.add(0.0, 0.5099999904632568, 0.0);
        dck10 = dck10.add(0.0, 0.5099999904632568, 0.0);
        final Vec3 dck11 = dck7.cross(new Vec3(0.0, 1.0, 0.0));
        final Vec3 dck12 = dck11.scale(float13 * 0.5f);
        final Vec3 dck13 = dck9.subtract(dck12);
        final Vec3 dck14 = dck10.subtract(dck12);
        final Vec3 dck15 = dck9.add(dck12);
        final Vec3 dck16 = dck10.add(dck12);
        final Iterator<AABB> iterator31 = (Iterator<AABB>)this.level.getCollisions(this, dcf24, (Predicate<Entity>)(apx -> true)).flatMap(dde -> dde.toAabbs().stream()).iterator();
        float float15 = Float.MIN_VALUE;
        while (iterator31.hasNext()) {
            final AABB dcf25 = (AABB)iterator31.next();
            if (!dcf25.intersects(dck13, dck14) && !dcf25.intersects(dck15, dck16)) {
                continue;
            }
            float15 = (float)dcf25.maxY;
            final Vec3 dck17 = dcf25.getCenter();
            final BlockPos fx15 = new BlockPos(dck17);
            for (int integer37 = 1; integer37 < float11; ++integer37) {
                final BlockPos fx16 = fx15.above(integer37);
                final BlockState cee17 = this.level.getBlockState(fx16);
                final VoxelShape dde34;
                if (!(dde34 = cee17.getCollisionShape(this.level, fx16, dcp13)).isEmpty()) {
                    float15 = (float)dde34.max(Direction.Axis.Y) + fx16.getY();
                    if (float15 - this.getY() > float11) {
                        return;
                    }
                }
                if (integer37 > 1) {
                    fx14 = fx14.above();
                    final BlockState cee18 = this.level.getBlockState(fx14);
                    if (!cee18.getCollisionShape(this.level, fx14, dcp13).isEmpty()) {
                        return;
                    }
                }
            }
            break;
        }
        if (float15 == Float.MIN_VALUE) {
            return;
        }
        final float float16 = (float)(float15 - this.getY());
        if (float16 <= 0.5f || float16 > float11) {
            return;
        }
        this.autoJumpTime = 1;
    }
    
    private boolean canAutoJump() {
        return this.isAutoJumpEnabled() && this.autoJumpTime <= 0 && this.onGround && !this.isStayingOnGroundSurface() && !this.isPassenger() && this.isMoving() && this.getBlockJumpFactor() >= 1.0;
    }
    
    private boolean isMoving() {
        final Vec2 dcj2 = this.input.getMoveVector();
        return dcj2.x != 0.0f || dcj2.y != 0.0f;
    }
    
    private boolean hasEnoughImpulseToStartSprinting() {
        final double double2 = 0.8;
        return this.isUnderWater() ? this.input.hasForwardImpulse() : (this.input.forwardImpulse >= 0.8);
    }
    
    public float getWaterVision() {
        if (!this.isEyeInFluid(FluidTags.WATER)) {
            return 0.0f;
        }
        final float float2 = 600.0f;
        final float float3 = 100.0f;
        if (this.waterVisionTime >= 600.0f) {
            return 1.0f;
        }
        final float float4 = Mth.clamp(this.waterVisionTime / 100.0f, 0.0f, 1.0f);
        final float float5 = (this.waterVisionTime < 100.0f) ? 0.0f : Mth.clamp((this.waterVisionTime - 100.0f) / 500.0f, 0.0f, 1.0f);
        return float4 * 0.6f + float5 * 0.39999998f;
    }
    
    @Override
    public boolean isUnderWater() {
        return this.wasUnderwater;
    }
    
    @Override
    protected boolean updateIsUnderwater() {
        final boolean boolean2 = this.wasUnderwater;
        final boolean boolean3 = super.updateIsUnderwater();
        if (this.isSpectator()) {
            return this.wasUnderwater;
        }
        if (!boolean2 && boolean3) {
            this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.AMBIENT_UNDERWATER_ENTER, SoundSource.AMBIENT, 1.0f, 1.0f, false);
            this.minecraft.getSoundManager().play(new UnderwaterAmbientSoundInstances.UnderwaterAmbientSoundInstance(this));
        }
        if (boolean2 && !boolean3) {
            this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.AMBIENT_UNDERWATER_EXIT, SoundSource.AMBIENT, 1.0f, 1.0f, false);
        }
        return this.wasUnderwater;
    }
    
    @Override
    public Vec3 getRopeHoldPosition(final float float1) {
        if (this.minecraft.options.getCameraType().isFirstPerson()) {
            final float float2 = Mth.lerp(float1 * 0.5f, this.yRot, this.yRotO) * 0.017453292f;
            final float float3 = Mth.lerp(float1 * 0.5f, this.xRot, this.xRotO) * 0.017453292f;
            final double double5 = (this.getMainArm() == HumanoidArm.RIGHT) ? -1.0 : 1.0;
            final Vec3 dck7 = new Vec3(0.39 * double5, -0.6, 0.3);
            return dck7.xRot(-float3).yRot(-float2).add(this.getEyePosition(float1));
        }
        return super.getRopeHoldPosition(float1);
    }
}
