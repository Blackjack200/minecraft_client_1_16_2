package net.minecraft.client.gui.screens.inventory;

import net.minecraft.world.entity.player.Player;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.ClickType;
import java.util.Iterator;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.google.common.collect.Sets;
import net.minecraft.network.chat.Component;
import java.util.Set;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class AbstractContainerScreen<T extends AbstractContainerMenu> extends Screen implements MenuAccess<T> {
    public static final ResourceLocation INVENTORY_LOCATION;
    protected int imageWidth;
    protected int imageHeight;
    protected int titleLabelX;
    protected int titleLabelY;
    protected int inventoryLabelX;
    protected int inventoryLabelY;
    protected final T menu;
    protected final Inventory inventory;
    @Nullable
    protected Slot hoveredSlot;
    @Nullable
    private Slot clickedSlot;
    @Nullable
    private Slot snapbackEnd;
    @Nullable
    private Slot quickdropSlot;
    @Nullable
    private Slot lastClickSlot;
    protected int leftPos;
    protected int topPos;
    private boolean isSplittingStack;
    private ItemStack draggingItem;
    private int snapbackStartX;
    private int snapbackStartY;
    private long snapbackTime;
    private ItemStack snapbackItem;
    private long quickdropTime;
    protected final Set<Slot> quickCraftSlots;
    protected boolean isQuickCrafting;
    private int quickCraftingType;
    private int quickCraftingButton;
    private boolean skipNextRelease;
    private int quickCraftingRemainder;
    private long lastClickTime;
    private int lastClickButton;
    private boolean doubleclick;
    private ItemStack lastQuickMoved;
    
    public AbstractContainerScreen(final T bhz, final Inventory bfs, final Component nr) {
        super(nr);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.draggingItem = ItemStack.EMPTY;
        this.snapbackItem = ItemStack.EMPTY;
        this.quickCraftSlots = (Set<Slot>)Sets.newHashSet();
        this.lastQuickMoved = ItemStack.EMPTY;
        this.menu = bhz;
        this.inventory = bfs;
        this.skipNextRelease = true;
        this.titleLabelX = 8;
        this.titleLabelY = 6;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = this.imageHeight - 94;
    }
    
    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        final int integer4 = this.leftPos;
        final int integer5 = this.topPos;
        this.renderBg(dfj, float4, integer2, integer3);
        RenderSystem.disableRescaleNormal();
        RenderSystem.disableDepthTest();
        super.render(dfj, integer2, integer3, float4);
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float)integer4, (float)integer5, 0.0f);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.enableRescaleNormal();
        this.hoveredSlot = null;
        final int integer6 = 240;
        final int integer7 = 240;
        RenderSystem.glMultiTexCoord2f(33986, 240.0f, 240.0f);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        for (int integer8 = 0; integer8 < this.menu.slots.size(); ++integer8) {
            final Slot bjo11 = (Slot)this.menu.slots.get(integer8);
            if (bjo11.isActive()) {
                this.renderSlot(dfj, bjo11);
            }
            if (this.isHovering(bjo11, integer2, integer3) && bjo11.isActive()) {
                this.hoveredSlot = bjo11;
                RenderSystem.disableDepthTest();
                final int integer9 = bjo11.x;
                final int integer10 = bjo11.y;
                RenderSystem.colorMask(true, true, true, false);
                this.fillGradient(dfj, integer9, integer10, integer9 + 16, integer10 + 16, -2130706433, -2130706433);
                RenderSystem.colorMask(true, true, true, true);
                RenderSystem.enableDepthTest();
            }
        }
        this.renderLabels(dfj, integer2, integer3);
        final Inventory bfs10 = this.minecraft.player.inventory;
        ItemStack bly11 = this.draggingItem.isEmpty() ? bfs10.getCarried() : this.draggingItem;
        if (!bly11.isEmpty()) {
            final int integer9 = 8;
            final int integer10 = this.draggingItem.isEmpty() ? 8 : 16;
            String string14 = null;
            if (!this.draggingItem.isEmpty() && this.isSplittingStack) {
                bly11 = bly11.copy();
                bly11.setCount(Mth.ceil(bly11.getCount() / 2.0f));
            }
            else if (this.isQuickCrafting && this.quickCraftSlots.size() > 1) {
                bly11 = bly11.copy();
                bly11.setCount(this.quickCraftingRemainder);
                if (bly11.isEmpty()) {
                    string14 = new StringBuilder().append("").append(ChatFormatting.YELLOW).append("0").toString();
                }
            }
            this.renderFloatingItem(bly11, integer2 - integer4 - 8, integer3 - integer5 - integer10, string14);
        }
        if (!this.snapbackItem.isEmpty()) {
            float float5 = (Util.getMillis() - this.snapbackTime) / 100.0f;
            if (float5 >= 1.0f) {
                float5 = 1.0f;
                this.snapbackItem = ItemStack.EMPTY;
            }
            final int integer10 = this.snapbackEnd.x - this.snapbackStartX;
            final int integer11 = this.snapbackEnd.y - this.snapbackStartY;
            final int integer12 = this.snapbackStartX + (int)(integer10 * float5);
            final int integer13 = this.snapbackStartY + (int)(integer11 * float5);
            this.renderFloatingItem(this.snapbackItem, integer12, integer13, null);
        }
        RenderSystem.popMatrix();
        RenderSystem.enableDepthTest();
    }
    
    protected void renderTooltip(final PoseStack dfj, final int integer2, final int integer3) {
        if (this.minecraft.player.inventory.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
            this.renderTooltip(dfj, this.hoveredSlot.getItem(), integer2, integer3);
        }
    }
    
    private void renderFloatingItem(final ItemStack bly, final int integer2, final int integer3, final String string) {
        RenderSystem.translatef(0.0f, 0.0f, 32.0f);
        this.setBlitOffset(200);
        this.itemRenderer.blitOffset = 200.0f;
        this.itemRenderer.renderAndDecorateItem(bly, integer2, integer3);
        this.itemRenderer.renderGuiItemDecorations(this.font, bly, integer2, integer3 - (this.draggingItem.isEmpty() ? 0 : 8), string);
        this.setBlitOffset(0);
        this.itemRenderer.blitOffset = 0.0f;
    }
    
    protected void renderLabels(final PoseStack dfj, final int integer2, final int integer3) {
        this.font.draw(dfj, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 4210752);
        this.font.draw(dfj, this.inventory.getDisplayName(), (float)this.inventoryLabelX, (float)this.inventoryLabelY, 4210752);
    }
    
    protected abstract void renderBg(final PoseStack dfj, final float float2, final int integer3, final int integer4);
    
    private void renderSlot(final PoseStack dfj, final Slot bjo) {
        final int integer4 = bjo.x;
        final int integer5 = bjo.y;
        ItemStack bly6 = bjo.getItem();
        boolean boolean7 = false;
        boolean boolean8 = bjo == this.clickedSlot && !this.draggingItem.isEmpty() && !this.isSplittingStack;
        final ItemStack bly7 = this.minecraft.player.inventory.getCarried();
        String string10 = null;
        if (bjo == this.clickedSlot && !this.draggingItem.isEmpty() && this.isSplittingStack && !bly6.isEmpty()) {
            bly6 = bly6.copy();
            bly6.setCount(bly6.getCount() / 2);
        }
        else if (this.isQuickCrafting && this.quickCraftSlots.contains(bjo) && !bly7.isEmpty()) {
            if (this.quickCraftSlots.size() == 1) {
                return;
            }
            if (AbstractContainerMenu.canItemQuickReplace(bjo, bly7, true) && this.menu.canDragTo(bjo)) {
                bly6 = bly7.copy();
                boolean7 = true;
                AbstractContainerMenu.getQuickCraftSlotCount(this.quickCraftSlots, this.quickCraftingType, bly6, bjo.getItem().isEmpty() ? 0 : bjo.getItem().getCount());
                final int integer6 = Math.min(bly6.getMaxStackSize(), bjo.getMaxStackSize(bly6));
                if (bly6.getCount() > integer6) {
                    string10 = ChatFormatting.YELLOW.toString() + integer6;
                    bly6.setCount(integer6);
                }
            }
            else {
                this.quickCraftSlots.remove(bjo);
                this.recalculateQuickCraftRemaining();
            }
        }
        this.setBlitOffset(100);
        this.itemRenderer.blitOffset = 100.0f;
        if (bly6.isEmpty() && bjo.isActive()) {
            final Pair<ResourceLocation, ResourceLocation> pair11 = bjo.getNoItemIcon();
            if (pair11 != null) {
                final TextureAtlasSprite eju12 = (TextureAtlasSprite)this.minecraft.getTextureAtlas((ResourceLocation)pair11.getFirst()).apply(pair11.getSecond());
                this.minecraft.getTextureManager().bind(eju12.atlas().location());
                GuiComponent.blit(dfj, integer4, integer5, this.getBlitOffset(), 16, 16, eju12);
                boolean8 = true;
            }
        }
        if (!boolean8) {
            if (boolean7) {
                GuiComponent.fill(dfj, integer4, integer5, integer4 + 16, integer5 + 16, -2130706433);
            }
            RenderSystem.enableDepthTest();
            this.itemRenderer.renderAndDecorateItem(this.minecraft.player, bly6, integer4, integer5);
            this.itemRenderer.renderGuiItemDecorations(this.font, bly6, integer4, integer5, string10);
        }
        this.itemRenderer.blitOffset = 0.0f;
        this.setBlitOffset(0);
    }
    
    private void recalculateQuickCraftRemaining() {
        final ItemStack bly2 = this.minecraft.player.inventory.getCarried();
        if (bly2.isEmpty() || !this.isQuickCrafting) {
            return;
        }
        if (this.quickCraftingType == 2) {
            this.quickCraftingRemainder = bly2.getMaxStackSize();
            return;
        }
        this.quickCraftingRemainder = bly2.getCount();
        for (final Slot bjo4 : this.quickCraftSlots) {
            final ItemStack bly3 = bly2.copy();
            final ItemStack bly4 = bjo4.getItem();
            final int integer7 = bly4.isEmpty() ? 0 : bly4.getCount();
            AbstractContainerMenu.getQuickCraftSlotCount(this.quickCraftSlots, this.quickCraftingType, bly3, integer7);
            final int integer8 = Math.min(bly3.getMaxStackSize(), bjo4.getMaxStackSize(bly3));
            if (bly3.getCount() > integer8) {
                bly3.setCount(integer8);
            }
            this.quickCraftingRemainder -= bly3.getCount() - integer7;
        }
    }
    
    @Nullable
    private Slot findSlot(final double double1, final double double2) {
        for (int integer6 = 0; integer6 < this.menu.slots.size(); ++integer6) {
            final Slot bjo7 = (Slot)this.menu.slots.get(integer6);
            if (this.isHovering(bjo7, double1, double2) && bjo7.isActive()) {
                return bjo7;
            }
        }
        return null;
    }
    
    public boolean mouseClicked(final double double1, final double double2, final int integer) {
        if (super.mouseClicked(double1, double2, integer)) {
            return true;
        }
        final boolean boolean7 = this.minecraft.options.keyPickItem.matchesMouse(integer);
        final Slot bjo8 = this.findSlot(double1, double2);
        final long long9 = Util.getMillis();
        this.doubleclick = (this.lastClickSlot == bjo8 && long9 - this.lastClickTime < 250L && this.lastClickButton == integer);
        this.skipNextRelease = false;
        if (integer == 0 || integer == 1 || boolean7) {
            final int integer2 = this.leftPos;
            final int integer3 = this.topPos;
            final boolean boolean8 = this.hasClickedOutside(double1, double2, integer2, integer3, integer);
            int integer4 = -1;
            if (bjo8 != null) {
                integer4 = bjo8.index;
            }
            if (boolean8) {
                integer4 = -999;
            }
            if (this.minecraft.options.touchscreen && boolean8 && this.minecraft.player.inventory.getCarried().isEmpty()) {
                this.minecraft.setScreen(null);
                return true;
            }
            if (integer4 != -1) {
                if (this.minecraft.options.touchscreen) {
                    if (bjo8 != null && bjo8.hasItem()) {
                        this.clickedSlot = bjo8;
                        this.draggingItem = ItemStack.EMPTY;
                        this.isSplittingStack = (integer == 1);
                    }
                    else {
                        this.clickedSlot = null;
                    }
                }
                else if (!this.isQuickCrafting) {
                    if (this.minecraft.player.inventory.getCarried().isEmpty()) {
                        if (this.minecraft.options.keyPickItem.matchesMouse(integer)) {
                            this.slotClicked(bjo8, integer4, integer, ClickType.CLONE);
                        }
                        else {
                            final boolean boolean9 = integer4 != -999 && (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 340) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 344));
                            ClickType bih16 = ClickType.PICKUP;
                            if (boolean9) {
                                this.lastQuickMoved = ((bjo8 != null && bjo8.hasItem()) ? bjo8.getItem().copy() : ItemStack.EMPTY);
                                bih16 = ClickType.QUICK_MOVE;
                            }
                            else if (integer4 == -999) {
                                bih16 = ClickType.THROW;
                            }
                            this.slotClicked(bjo8, integer4, integer, bih16);
                        }
                        this.skipNextRelease = true;
                    }
                    else {
                        this.isQuickCrafting = true;
                        this.quickCraftingButton = integer;
                        this.quickCraftSlots.clear();
                        if (integer == 0) {
                            this.quickCraftingType = 0;
                        }
                        else if (integer == 1) {
                            this.quickCraftingType = 1;
                        }
                        else if (this.minecraft.options.keyPickItem.matchesMouse(integer)) {
                            this.quickCraftingType = 2;
                        }
                    }
                }
            }
        }
        else {
            this.checkHotbarMouseClicked(integer);
        }
        this.lastClickSlot = bjo8;
        this.lastClickTime = long9;
        this.lastClickButton = integer;
        return true;
    }
    
    private void checkHotbarMouseClicked(final int integer) {
        if (this.hoveredSlot != null && this.minecraft.player.inventory.getCarried().isEmpty()) {
            if (this.minecraft.options.keySwapOffhand.matchesMouse(integer)) {
                this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, 40, ClickType.SWAP);
                return;
            }
            for (int integer2 = 0; integer2 < 9; ++integer2) {
                if (this.minecraft.options.keyHotbarSlots[integer2].matchesMouse(integer)) {
                    this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, integer2, ClickType.SWAP);
                }
            }
        }
    }
    
    protected boolean hasClickedOutside(final double double1, final double double2, final int integer3, final int integer4, final int integer5) {
        return double1 < integer3 || double2 < integer4 || double1 >= integer3 + this.imageWidth || double2 >= integer4 + this.imageHeight;
    }
    
    public boolean mouseDragged(final double double1, final double double2, final int integer, final double double4, final double double5) {
        final Slot bjo11 = this.findSlot(double1, double2);
        final ItemStack bly12 = this.minecraft.player.inventory.getCarried();
        if (this.clickedSlot != null && this.minecraft.options.touchscreen) {
            if (integer == 0 || integer == 1) {
                if (this.draggingItem.isEmpty()) {
                    if (bjo11 != this.clickedSlot && !this.clickedSlot.getItem().isEmpty()) {
                        this.draggingItem = this.clickedSlot.getItem().copy();
                    }
                }
                else if (this.draggingItem.getCount() > 1 && bjo11 != null && AbstractContainerMenu.canItemQuickReplace(bjo11, this.draggingItem, false)) {
                    final long long13 = Util.getMillis();
                    if (this.quickdropSlot == bjo11) {
                        if (long13 - this.quickdropTime > 500L) {
                            this.slotClicked(this.clickedSlot, this.clickedSlot.index, 0, ClickType.PICKUP);
                            this.slotClicked(bjo11, bjo11.index, 1, ClickType.PICKUP);
                            this.slotClicked(this.clickedSlot, this.clickedSlot.index, 0, ClickType.PICKUP);
                            this.quickdropTime = long13 + 750L;
                            this.draggingItem.shrink(1);
                        }
                    }
                    else {
                        this.quickdropSlot = bjo11;
                        this.quickdropTime = long13;
                    }
                }
            }
        }
        else if (this.isQuickCrafting && bjo11 != null && !bly12.isEmpty() && (bly12.getCount() > this.quickCraftSlots.size() || this.quickCraftingType == 2) && AbstractContainerMenu.canItemQuickReplace(bjo11, bly12, true) && bjo11.mayPlace(bly12) && this.menu.canDragTo(bjo11)) {
            this.quickCraftSlots.add(bjo11);
            this.recalculateQuickCraftRemaining();
        }
        return true;
    }
    
    public boolean mouseReleased(final double double1, final double double2, final int integer) {
        final Slot bjo7 = this.findSlot(double1, double2);
        final int integer2 = this.leftPos;
        final int integer3 = this.topPos;
        final boolean boolean10 = this.hasClickedOutside(double1, double2, integer2, integer3, integer);
        int integer4 = -1;
        if (bjo7 != null) {
            integer4 = bjo7.index;
        }
        if (boolean10) {
            integer4 = -999;
        }
        if (this.doubleclick && bjo7 != null && integer == 0 && this.menu.canTakeItemForPickAll(ItemStack.EMPTY, bjo7)) {
            if (hasShiftDown()) {
                if (!this.lastQuickMoved.isEmpty()) {
                    for (final Slot bjo8 : this.menu.slots) {
                        if (bjo8 != null && bjo8.mayPickup(this.minecraft.player) && bjo8.hasItem() && bjo8.container == bjo7.container && AbstractContainerMenu.canItemQuickReplace(bjo8, this.lastQuickMoved, true)) {
                            this.slotClicked(bjo8, bjo8.index, integer, ClickType.QUICK_MOVE);
                        }
                    }
                }
            }
            else {
                this.slotClicked(bjo7, integer4, integer, ClickType.PICKUP_ALL);
            }
            this.doubleclick = false;
            this.lastClickTime = 0L;
        }
        else {
            if (this.isQuickCrafting && this.quickCraftingButton != integer) {
                this.isQuickCrafting = false;
                this.quickCraftSlots.clear();
                return this.skipNextRelease = true;
            }
            if (this.skipNextRelease) {
                this.skipNextRelease = false;
                return true;
            }
            if (this.clickedSlot != null && this.minecraft.options.touchscreen) {
                if (integer == 0 || integer == 1) {
                    if (this.draggingItem.isEmpty() && bjo7 != this.clickedSlot) {
                        this.draggingItem = this.clickedSlot.getItem();
                    }
                    final boolean boolean11 = AbstractContainerMenu.canItemQuickReplace(bjo7, this.draggingItem, false);
                    if (integer4 != -1 && !this.draggingItem.isEmpty() && boolean11) {
                        this.slotClicked(this.clickedSlot, this.clickedSlot.index, integer, ClickType.PICKUP);
                        this.slotClicked(bjo7, integer4, 0, ClickType.PICKUP);
                        if (this.minecraft.player.inventory.getCarried().isEmpty()) {
                            this.snapbackItem = ItemStack.EMPTY;
                        }
                        else {
                            this.slotClicked(this.clickedSlot, this.clickedSlot.index, integer, ClickType.PICKUP);
                            this.snapbackStartX = Mth.floor(double1 - integer2);
                            this.snapbackStartY = Mth.floor(double2 - integer3);
                            this.snapbackEnd = this.clickedSlot;
                            this.snapbackItem = this.draggingItem;
                            this.snapbackTime = Util.getMillis();
                        }
                    }
                    else if (!this.draggingItem.isEmpty()) {
                        this.snapbackStartX = Mth.floor(double1 - integer2);
                        this.snapbackStartY = Mth.floor(double2 - integer3);
                        this.snapbackEnd = this.clickedSlot;
                        this.snapbackItem = this.draggingItem;
                        this.snapbackTime = Util.getMillis();
                    }
                    this.draggingItem = ItemStack.EMPTY;
                    this.clickedSlot = null;
                }
            }
            else if (this.isQuickCrafting && !this.quickCraftSlots.isEmpty()) {
                this.slotClicked(null, -999, AbstractContainerMenu.getQuickcraftMask(0, this.quickCraftingType), ClickType.QUICK_CRAFT);
                for (final Slot bjo8 : this.quickCraftSlots) {
                    this.slotClicked(bjo8, bjo8.index, AbstractContainerMenu.getQuickcraftMask(1, this.quickCraftingType), ClickType.QUICK_CRAFT);
                }
                this.slotClicked(null, -999, AbstractContainerMenu.getQuickcraftMask(2, this.quickCraftingType), ClickType.QUICK_CRAFT);
            }
            else if (!this.minecraft.player.inventory.getCarried().isEmpty()) {
                if (this.minecraft.options.keyPickItem.matchesMouse(integer)) {
                    this.slotClicked(bjo7, integer4, integer, ClickType.CLONE);
                }
                else {
                    final boolean boolean11 = integer4 != -999 && (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 340) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 344));
                    if (boolean11) {
                        this.lastQuickMoved = ((bjo7 != null && bjo7.hasItem()) ? bjo7.getItem().copy() : ItemStack.EMPTY);
                    }
                    this.slotClicked(bjo7, integer4, integer, boolean11 ? ClickType.QUICK_MOVE : ClickType.PICKUP);
                }
            }
        }
        if (this.minecraft.player.inventory.getCarried().isEmpty()) {
            this.lastClickTime = 0L;
        }
        this.isQuickCrafting = false;
        return true;
    }
    
    private boolean isHovering(final Slot bjo, final double double2, final double double3) {
        return this.isHovering(bjo.x, bjo.y, 16, 16, double2, double3);
    }
    
    protected boolean isHovering(final int integer1, final int integer2, final int integer3, final int integer4, double double5, double double6) {
        final int integer5 = this.leftPos;
        final int integer6 = this.topPos;
        double5 -= integer5;
        double6 -= integer6;
        return double5 >= integer1 - 1 && double5 < integer1 + integer3 + 1 && double6 >= integer2 - 1 && double6 < integer2 + integer4 + 1;
    }
    
    protected void slotClicked(final Slot bjo, int integer2, final int integer3, final ClickType bih) {
        if (bjo != null) {
            integer2 = bjo.index;
        }
        this.minecraft.gameMode.handleInventoryMouseClick(this.menu.containerId, integer2, integer3, bih, this.minecraft.player);
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (super.keyPressed(integer1, integer2, integer3)) {
            return true;
        }
        if (this.minecraft.options.keyInventory.matches(integer1, integer2)) {
            this.onClose();
            return true;
        }
        this.checkHotbarKeyPressed(integer1, integer2);
        if (this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
            if (this.minecraft.options.keyPickItem.matches(integer1, integer2)) {
                this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, 0, ClickType.CLONE);
            }
            else if (this.minecraft.options.keyDrop.matches(integer1, integer2)) {
                this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, Screen.hasControlDown() ? 1 : 0, ClickType.THROW);
            }
        }
        return true;
    }
    
    protected boolean checkHotbarKeyPressed(final int integer1, final int integer2) {
        if (this.minecraft.player.inventory.getCarried().isEmpty() && this.hoveredSlot != null) {
            if (this.minecraft.options.keySwapOffhand.matches(integer1, integer2)) {
                this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, 40, ClickType.SWAP);
                return true;
            }
            for (int integer3 = 0; integer3 < 9; ++integer3) {
                if (this.minecraft.options.keyHotbarSlots[integer3].matches(integer1, integer2)) {
                    this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, integer3, ClickType.SWAP);
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public void removed() {
        if (this.minecraft.player == null) {
            return;
        }
        this.menu.removed(this.minecraft.player);
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
    @Override
    public void tick() {
        super.tick();
        if (!this.minecraft.player.isAlive() || this.minecraft.player.removed) {
            this.minecraft.player.closeContainer();
        }
    }
    
    @Override
    public T getMenu() {
        return this.menu;
    }
    
    @Override
    public void onClose() {
        this.minecraft.player.closeContainer();
        super.onClose();
    }
    
    static {
        INVENTORY_LOCATION = new ResourceLocation("textures/gui/container/inventory.png");
    }
}
