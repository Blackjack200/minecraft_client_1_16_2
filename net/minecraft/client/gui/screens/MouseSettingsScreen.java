package net.minecraft.client.gui.screens;

import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import java.util.stream.Stream;
import java.util.Arrays;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.Options;
import net.minecraft.client.Option;
import net.minecraft.client.gui.components.OptionsList;

public class MouseSettingsScreen extends OptionsSubScreen {
    private OptionsList list;
    private static final Option[] OPTIONS;
    
    public MouseSettingsScreen(final Screen doq, final Options dka) {
        super(doq, dka, new TranslatableComponent("options.mouse_settings.title"));
    }
    
    @Override
    protected void init() {
        this.list = new OptionsList(this.minecraft, this.width, this.height, 32, this.height - 32, 25);
        if (InputConstants.isRawMouseInputSupported()) {
            this.list.addSmall((Option[])Stream.concat(Arrays.stream((Object[])MouseSettingsScreen.OPTIONS), Stream.of(Option.RAW_MOUSE_INPUT)).toArray(Option[]::new));
        }
        else {
            this.list.addSmall(MouseSettingsScreen.OPTIONS);
        }
        this.children.add(this.list);
        this.<Button>addButton(new Button(this.width / 2 - 100, this.height - 27, 200, 20, CommonComponents.GUI_DONE, dlg -> {
            this.options.save();
            this.minecraft.setScreen(this.lastScreen);
        }));
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        this.list.render(dfj, integer2, integer3, float4);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 5, 16777215);
        super.render(dfj, integer2, integer3, float4);
    }
    
    static {
        OPTIONS = new Option[] { Option.SENSITIVITY, Option.INVERT_MOUSE, Option.MOUSE_WHEEL_SENSITIVITY, Option.DISCRETE_MOUSE_SCROLL, Option.TOUCHSCREEN };
    }
}
