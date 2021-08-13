package net.minecraft.client.gui.screens.debug;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.Iterator;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.GameType;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.chat.NarratorChatListener;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.screens.Screen;

public class GameModeSwitcherScreen extends Screen {
    private static final ResourceLocation GAMEMODE_SWITCHER_LOCATION;
    private static final int ALL_SLOTS_WIDTH;
    private static final Component SELECT_KEY;
    private final Optional<GameModeIcon> previousHovered;
    private Optional<GameModeIcon> currentlyHovered;
    private int firstMouseX;
    private int firstMouseY;
    private boolean setFirstMousePos;
    private final List<GameModeSlot> slots;
    
    public GameModeSwitcherScreen() {
        super(NarratorChatListener.NO_TITLE);
        this.currentlyHovered = (Optional<GameModeIcon>)Optional.empty();
        this.slots = (List<GameModeSlot>)Lists.newArrayList();
        this.previousHovered = getFromGameType(this.getDefaultSelected());
    }
    
    private GameType getDefaultSelected() {
        final GameType brr2 = Minecraft.getInstance().gameMode.getPlayerMode();
        GameType brr3 = Minecraft.getInstance().gameMode.getPreviousPlayerMode();
        if (brr3 == GameType.NOT_SET) {
            if (brr2 == GameType.CREATIVE) {
                brr3 = GameType.SURVIVAL;
            }
            else {
                brr3 = GameType.CREATIVE;
            }
        }
        return brr3;
    }
    
    @Override
    protected void init() {
        super.init();
        this.currentlyHovered = (this.previousHovered.isPresent() ? this.previousHovered : getFromGameType(this.minecraft.gameMode.getPlayerMode()));
        for (int integer2 = 0; integer2 < GameModeIcon.VALUES.length; ++integer2) {
            final GameModeIcon a3 = GameModeIcon.VALUES[integer2];
            this.slots.add(new GameModeSlot(a3, this.width / 2 - GameModeSwitcherScreen.ALL_SLOTS_WIDTH / 2 + integer2 * 30, this.height / 2 - 30));
        }
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        if (this.checkToClose()) {
            return;
        }
        dfj.pushPose();
        RenderSystem.enableBlend();
        this.minecraft.getTextureManager().bind(GameModeSwitcherScreen.GAMEMODE_SWITCHER_LOCATION);
        final int integer4 = this.width / 2 - 62;
        final int integer5 = this.height / 2 - 30 - 27;
        GuiComponent.blit(dfj, integer4, integer5, 0.0f, 0.0f, 125, 75, 128, 128);
        dfj.popPose();
        super.render(dfj, integer2, integer3, float4);
        this.currentlyHovered.ifPresent(a -> GuiComponent.drawCenteredString(dfj, this.font, a.getName(), this.width / 2, this.height / 2 - 30 - 20, -1));
        GuiComponent.drawCenteredString(dfj, this.font, GameModeSwitcherScreen.SELECT_KEY, this.width / 2, this.height / 2 + 5, 16777215);
        if (!this.setFirstMousePos) {
            this.firstMouseX = integer2;
            this.firstMouseY = integer3;
            this.setFirstMousePos = true;
        }
        final boolean boolean8 = this.firstMouseX == integer2 && this.firstMouseY == integer3;
        for (final GameModeSlot b10 : this.slots) {
            b10.render(dfj, integer2, integer3, float4);
            this.currentlyHovered.ifPresent(a -> b10.setSelected(a == b10.icon));
            if (!boolean8 && b10.isHovered()) {
                this.currentlyHovered = (Optional<GameModeIcon>)Optional.of(b10.icon);
            }
        }
    }
    
    private void switchToHoveredGameMode() {
        switchToHoveredGameMode(this.minecraft, this.currentlyHovered);
    }
    
    private static void switchToHoveredGameMode(final Minecraft djw, final Optional<GameModeIcon> optional) {
        if (djw.gameMode == null || djw.player == null || !optional.isPresent()) {
            return;
        }
        final Optional<GameModeIcon> optional2 = getFromGameType(djw.gameMode.getPlayerMode());
        final GameModeIcon a4 = (GameModeIcon)optional.get();
        if (optional2.isPresent() && djw.player.hasPermissions(2) && a4 != optional2.get()) {
            djw.player.chat(a4.getCommand());
        }
    }
    
    private boolean checkToClose() {
        if (!InputConstants.isKeyDown(this.minecraft.getWindow().getWindow(), 292)) {
            this.switchToHoveredGameMode();
            this.minecraft.setScreen(null);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (integer1 == 293 && this.currentlyHovered.isPresent()) {
            this.setFirstMousePos = false;
            this.currentlyHovered = ((GameModeIcon)this.currentlyHovered.get()).getNext();
            return true;
        }
        return super.keyPressed(integer1, integer2, integer3);
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
    static {
        GAMEMODE_SWITCHER_LOCATION = new ResourceLocation("textures/gui/container/gamemode_switcher.png");
        ALL_SLOTS_WIDTH = GameModeIcon.values().length * 30 - 5;
        SELECT_KEY = new TranslatableComponent("debug.gamemodes.select_next", new Object[] { new TranslatableComponent("debug.gamemodes.press_f4").withStyle(ChatFormatting.AQUA) });
    }
    
    enum GameModeIcon {
        CREATIVE((Component)new TranslatableComponent("gameMode.creative"), "/gamemode creative", new ItemStack(Blocks.GRASS_BLOCK)), 
        SURVIVAL((Component)new TranslatableComponent("gameMode.survival"), "/gamemode survival", new ItemStack(Items.IRON_SWORD)), 
        ADVENTURE((Component)new TranslatableComponent("gameMode.adventure"), "/gamemode adventure", new ItemStack(Items.MAP)), 
        SPECTATOR((Component)new TranslatableComponent("gameMode.spectator"), "/gamemode spectator", new ItemStack(Items.ENDER_EYE));
        
        protected static final GameModeIcon[] VALUES;
        final Component name;
        final String command;
        final ItemStack renderStack;
        
        private GameModeIcon(final Component nr, final String string4, final ItemStack bly) {
            this.name = nr;
            this.command = string4;
            this.renderStack = bly;
        }
        
        private void drawIcon(final ItemRenderer efg, final int integer2, final int integer3) {
            efg.renderAndDecorateItem(this.renderStack, integer2, integer3);
        }
        
        private Component getName() {
            return this.name;
        }
        
        private String getCommand() {
            return this.command;
        }
        
        private Optional<GameModeIcon> getNext() {
            switch (this) {
                case CREATIVE: {
                    return (Optional<GameModeIcon>)Optional.of(GameModeIcon.SURVIVAL);
                }
                case SURVIVAL: {
                    return (Optional<GameModeIcon>)Optional.of(GameModeIcon.ADVENTURE);
                }
                case ADVENTURE: {
                    return (Optional<GameModeIcon>)Optional.of(GameModeIcon.SPECTATOR);
                }
                default: {
                    return (Optional<GameModeIcon>)Optional.of(GameModeIcon.CREATIVE);
                }
            }
        }
        
        private static Optional<GameModeIcon> getFromGameType(final GameType brr) {
            switch (brr) {
                case SPECTATOR: {
                    return (Optional<GameModeIcon>)Optional.of(GameModeIcon.SPECTATOR);
                }
                case SURVIVAL: {
                    return (Optional<GameModeIcon>)Optional.of(GameModeIcon.SURVIVAL);
                }
                case CREATIVE: {
                    return (Optional<GameModeIcon>)Optional.of(GameModeIcon.CREATIVE);
                }
                case ADVENTURE: {
                    return (Optional<GameModeIcon>)Optional.of(GameModeIcon.ADVENTURE);
                }
                default: {
                    return (Optional<GameModeIcon>)Optional.empty();
                }
            }
        }
        
        static {
            VALUES = values();
        }
    }
    
    public class GameModeSlot extends AbstractWidget {
        private final GameModeIcon icon;
        private boolean isSelected;
        
        public GameModeSlot(final GameModeIcon a, final int integer3, final int integer4) {
            super(integer3, integer4, 25, 25, a.getName());
            this.icon = a;
        }
        
        @Override
        public void renderButton(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
            final Minecraft djw6 = Minecraft.getInstance();
            this.drawSlot(dfj, djw6.getTextureManager());
            this.icon.drawIcon(GameModeSwitcherScreen.this.itemRenderer, this.x + 5, this.y + 5);
            if (this.isSelected) {
                this.drawSelection(dfj, djw6.getTextureManager());
            }
        }
        
        @Override
        public boolean isHovered() {
            return super.isHovered() || this.isSelected;
        }
        
        public void setSelected(final boolean boolean1) {
            this.isSelected = boolean1;
            this.narrate();
        }
        
        private void drawSlot(final PoseStack dfj, final TextureManager ejv) {
            ejv.bind(GameModeSwitcherScreen.GAMEMODE_SWITCHER_LOCATION);
            dfj.pushPose();
            dfj.translate(this.x, this.y, 0.0);
            GuiComponent.blit(dfj, 0, 0, 0.0f, 75.0f, 25, 25, 128, 128);
            dfj.popPose();
        }
        
        private void drawSelection(final PoseStack dfj, final TextureManager ejv) {
            ejv.bind(GameModeSwitcherScreen.GAMEMODE_SWITCHER_LOCATION);
            dfj.pushPose();
            dfj.translate(this.x, this.y, 0.0);
            GuiComponent.blit(dfj, 0, 0, 25.0f, 75.0f, 25, 25, 128, 128);
            dfj.popPose();
        }
    }
}
