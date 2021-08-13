package net.minecraft.client.gui.screens;

import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.client.gui.components.OptionButton;
import net.minecraft.client.Option;
import net.minecraft.client.gui.components.VolumeSlider;
import net.minecraft.sounds.SoundSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.Options;

public class SoundOptionsScreen extends OptionsSubScreen {
    public SoundOptionsScreen(final Screen doq, final Options dka) {
        super(doq, dka, new TranslatableComponent("options.sounds.title"));
    }
    
    @Override
    protected void init() {
        int integer2 = 0;
        this.<VolumeSlider>addButton(new VolumeSlider(this.minecraft, this.width / 2 - 155 + integer2 % 2 * 160, this.height / 6 - 12 + 24 * (integer2 >> 1), SoundSource.MASTER, 310));
        integer2 += 2;
        for (final SoundSource adp6 : SoundSource.values()) {
            if (adp6 != SoundSource.MASTER) {
                this.<VolumeSlider>addButton(new VolumeSlider(this.minecraft, this.width / 2 - 155 + integer2 % 2 * 160, this.height / 6 - 12 + 24 * (integer2 >> 1), adp6, 150));
                ++integer2;
            }
        }
        this.<OptionButton>addButton(new OptionButton(this.width / 2 - 75, this.height / 6 - 12 + 24 * (++integer2 >> 1), 150, 20, Option.SHOW_SUBTITLES, Option.SHOW_SUBTITLES.getMessage(this.options), dlg -> {
            Option.SHOW_SUBTITLES.toggle(this.minecraft.options);
            dlg.setMessage(Option.SHOW_SUBTITLES.getMessage(this.minecraft.options));
            this.minecraft.options.save();
            return;
        }));
        this.<Button>addButton(new Button(this.width / 2 - 100, this.height / 6 + 168, 200, 20, CommonComponents.GUI_DONE, dlg -> this.minecraft.setScreen(this.lastScreen)));
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 15, 16777215);
        super.render(dfj, integer2, integer3, float4);
    }
}
