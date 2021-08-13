package net.minecraft.client.gui.screens;

import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.client.gui.components.OptionButton;
import net.minecraft.client.Option;
import net.minecraft.client.gui.components.Button;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.Options;

public class SkinCustomizationScreen extends OptionsSubScreen {
    public SkinCustomizationScreen(final Screen doq, final Options dka) {
        super(doq, dka, new TranslatableComponent("options.skinCustomisation.title"));
    }
    
    @Override
    protected void init() {
        int integer2 = 0;
        for (final PlayerModelPart bfu6 : PlayerModelPart.values()) {
            final PlayerModelPart playerModelPart;
            this.<Button>addButton(new Button(this.width / 2 - 155 + integer2 % 2 * 160, this.height / 6 + 24 * (integer2 >> 1), 150, 20, this.getMessage(bfu6), dlg -> {
                this.options.toggleModelPart(playerModelPart);
                dlg.setMessage(this.getMessage(playerModelPart));
                return;
            }));
            ++integer2;
        }
        this.<OptionButton>addButton(new OptionButton(this.width / 2 - 155 + integer2 % 2 * 160, this.height / 6 + 24 * (integer2 >> 1), 150, 20, Option.MAIN_HAND, Option.MAIN_HAND.getMessage(this.options), dlg -> {
            Option.MAIN_HAND.toggle(this.options, 1);
            this.options.save();
            dlg.setMessage(Option.MAIN_HAND.getMessage(this.options));
            this.options.broadcastOptions();
            return;
        }));
        if (++integer2 % 2 == 1) {
            ++integer2;
        }
        this.<Button>addButton(new Button(this.width / 2 - 100, this.height / 6 + 24 * (integer2 >> 1), 200, 20, CommonComponents.GUI_DONE, dlg -> this.minecraft.setScreen(this.lastScreen)));
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 20, 16777215);
        super.render(dfj, integer2, integer3, float4);
    }
    
    private Component getMessage(final PlayerModelPart bfu) {
        return CommonComponents.optionStatus(bfu.getName(), this.options.getModelParts().contains(bfu));
    }
}
