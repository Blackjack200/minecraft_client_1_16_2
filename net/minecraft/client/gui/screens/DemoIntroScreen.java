package net.minecraft.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Options;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.client.gui.components.Button;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.resources.ResourceLocation;

public class DemoIntroScreen extends Screen {
    private static final ResourceLocation DEMO_BACKGROUND_LOCATION;
    private MultiLineLabel movementMessage;
    private MultiLineLabel durationMessage;
    
    public DemoIntroScreen() {
        super(new TranslatableComponent("demo.help.title"));
        this.movementMessage = MultiLineLabel.EMPTY;
        this.durationMessage = MultiLineLabel.EMPTY;
    }
    
    @Override
    protected void init() {
        final int integer2 = -16;
        this.<Button>addButton(new Button(this.width / 2 - 116, this.height / 2 + 62 - 16, 114, 20, new TranslatableComponent("demo.help.buy"), dlg -> {
            dlg.active = false;
            Util.getPlatform().openUri("http://www.minecraft.net/store?source=demo");
            return;
        }));
        this.<Button>addButton(new Button(this.width / 2 + 2, this.height / 2 + 62 - 16, 114, 20, new TranslatableComponent("demo.help.later"), dlg -> {
            this.minecraft.setScreen(null);
            this.minecraft.mouseHandler.grabMouse();
            return;
        }));
        final Options dka3 = this.minecraft.options;
        this.movementMessage = MultiLineLabel.create(this.font, new TranslatableComponent("demo.help.movementShort", new Object[] { dka3.keyUp.getTranslatedKeyMessage(), dka3.keyLeft.getTranslatedKeyMessage(), dka3.keyDown.getTranslatedKeyMessage(), dka3.keyRight.getTranslatedKeyMessage() }), new TranslatableComponent("demo.help.movementMouse"), new TranslatableComponent("demo.help.jump", new Object[] { dka3.keyJump.getTranslatedKeyMessage() }), new TranslatableComponent("demo.help.inventory", new Object[] { dka3.keyInventory.getTranslatedKeyMessage() }));
        this.durationMessage = MultiLineLabel.create(this.font, new TranslatableComponent("demo.help.fullWrapped"), 218);
    }
    
    @Override
    public void renderBackground(final PoseStack dfj) {
        super.renderBackground(dfj);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(DemoIntroScreen.DEMO_BACKGROUND_LOCATION);
        final int integer3 = (this.width - 248) / 2;
        final int integer4 = (this.height - 166) / 2;
        this.blit(dfj, integer3, integer4, 0, 0, 248, 166);
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        final int integer4 = (this.width - 248) / 2 + 10;
        int integer5 = (this.height - 166) / 2 + 8;
        this.font.draw(dfj, this.title, (float)integer4, (float)integer5, 2039583);
        integer5 = this.movementMessage.renderLeftAlignedNoShadow(dfj, integer4, integer5 + 12, 12, 5197647);
        final MultiLineLabel durationMessage = this.durationMessage;
        final int integer6 = integer4;
        final int integer7 = integer5 + 20;
        this.font.getClass();
        durationMessage.renderLeftAlignedNoShadow(dfj, integer6, integer7, 9, 2039583);
        super.render(dfj, integer2, integer3, float4);
    }
    
    static {
        DEMO_BACKGROUND_LOCATION = new ResourceLocation("textures/gui/demo_background.png");
    }
}
