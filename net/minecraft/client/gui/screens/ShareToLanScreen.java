package net.minecraft.client.gui.screens;

import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.world.level.GameType;
import net.minecraft.util.HttpUtil;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class ShareToLanScreen extends Screen {
    private static final Component ALLOW_COMMANDS_LABEL;
    private static final Component GAME_MODE_LABEL;
    private static final Component INFO_TEXT;
    private final Screen lastScreen;
    private Button commandsButton;
    private Button modeButton;
    private String gameModeName;
    private boolean commands;
    
    public ShareToLanScreen(final Screen doq) {
        super(new TranslatableComponent("lanServer.title"));
        this.gameModeName = "survival";
        this.lastScreen = doq;
    }
    
    @Override
    protected void init() {
        final int integer3;
        final TranslatableComponent translatableComponent;
        Component nr4;
        this.<Button>addButton(new Button(this.width / 2 - 155, this.height - 28, 150, 20, new TranslatableComponent("lanServer.start"), dlg -> {
            this.minecraft.setScreen(null);
            integer3 = HttpUtil.getAvailablePort();
            if (this.minecraft.getSingleplayerServer().publishServer(GameType.byName(this.gameModeName), this.commands, integer3)) {
                new TranslatableComponent("commands.publish.started", new Object[] { integer3 });
                nr4 = translatableComponent;
            }
            else {
                nr4 = new TranslatableComponent("commands.publish.failed");
            }
            this.minecraft.gui.getChat().addMessage(nr4);
            this.minecraft.updateTitle();
            return;
        }));
        this.<Button>addButton(new Button(this.width / 2 + 5, this.height - 28, 150, 20, CommonComponents.GUI_CANCEL, dlg -> this.minecraft.setScreen(this.lastScreen)));
        this.modeButton = this.<Button>addButton(new Button(this.width / 2 - 155, 100, 150, 20, TextComponent.EMPTY, dlg -> {
            if ("spectator".equals(this.gameModeName)) {
                this.gameModeName = "creative";
            }
            else if ("creative".equals(this.gameModeName)) {
                this.gameModeName = "adventure";
            }
            else if ("adventure".equals(this.gameModeName)) {
                this.gameModeName = "survival";
            }
            else {
                this.gameModeName = "spectator";
            }
            this.updateSelectionStrings();
            return;
        }));
        this.commandsButton = this.<Button>addButton(new Button(this.width / 2 + 5, 100, 150, 20, ShareToLanScreen.ALLOW_COMMANDS_LABEL, dlg -> {
            this.commands = !this.commands;
            this.updateSelectionStrings();
            return;
        }));
        this.updateSelectionStrings();
    }
    
    private void updateSelectionStrings() {
        this.modeButton.setMessage(new TranslatableComponent("options.generic_value", new Object[] { ShareToLanScreen.GAME_MODE_LABEL, new TranslatableComponent("selectWorld.gameMode." + this.gameModeName) }));
        this.commandsButton.setMessage(CommonComponents.optionStatus(ShareToLanScreen.ALLOW_COMMANDS_LABEL, this.commands));
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 50, 16777215);
        GuiComponent.drawCenteredString(dfj, this.font, ShareToLanScreen.INFO_TEXT, this.width / 2, 82, 16777215);
        super.render(dfj, integer2, integer3, float4);
    }
    
    static {
        ALLOW_COMMANDS_LABEL = new TranslatableComponent("selectWorld.allowCommands");
        GAME_MODE_LABEL = new TranslatableComponent("selectWorld.gameMode");
        INFO_TEXT = new TranslatableComponent("lanServer.otherPlayers");
    }
}
