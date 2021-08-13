package net.minecraft.client.gui.screens;

import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.protocol.game.ServerboundLockDifficultyPacket;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.packs.repository.Pack;
import java.util.Collection;
import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.server.packs.repository.PackRepository;
import java.util.function.Consumer;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraft.client.gui.components.OptionButton;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundChangeDifficultyPacket;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.client.gui.components.LockIconButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.Options;
import net.minecraft.client.Option;

public class OptionsScreen extends Screen {
    private static final Option[] OPTION_SCREEN_OPTIONS;
    private final Screen lastScreen;
    private final Options options;
    private Button difficultyButton;
    private LockIconButton lockButton;
    private Difficulty currentDifficulty;
    
    public OptionsScreen(final Screen doq, final Options dka) {
        super(new TranslatableComponent("options.title"));
        this.lastScreen = doq;
        this.options = dka;
    }
    
    @Override
    protected void init() {
        int integer2 = 0;
        for (final Option djz6 : OptionsScreen.OPTION_SCREEN_OPTIONS) {
            final int integer3 = this.width / 2 - 155 + integer2 % 2 * 160;
            final int integer4 = this.height / 6 - 12 + 24 * (integer2 >> 1);
            this.<AbstractWidget>addButton(djz6.createButton(this.minecraft.options, integer3, integer4, 150));
            ++integer2;
        }
        if (this.minecraft.level != null) {
            this.currentDifficulty = this.minecraft.level.getDifficulty();
            this.difficultyButton = this.<Button>addButton(new Button(this.width / 2 - 155 + integer2 % 2 * 160, this.height / 6 - 12 + 24 * (integer2 >> 1), 150, 20, this.getDifficultyText(this.currentDifficulty), dlg -> {
                this.currentDifficulty = Difficulty.byId(this.currentDifficulty.getId() + 1);
                this.minecraft.getConnection().send(new ServerboundChangeDifficultyPacket(this.currentDifficulty));
                this.difficultyButton.setMessage(this.getDifficultyText(this.currentDifficulty));
                return;
            }));
            if (this.minecraft.hasSingleplayerServer() && !this.minecraft.level.getLevelData().isHardcore()) {
                this.difficultyButton.setWidth(this.difficultyButton.getWidth() - 20);
                final Minecraft minecraft;
                final TranslatableComponent nr2;
                final Object[] arr;
                final TranslatableComponent translatableComponent;
                final Object o;
                final Component nr3;
                final String string;
                final Screen screen;
                final BooleanConsumer booleanConsumer;
                (this.lockButton = this.<LockIconButton>addButton(new LockIconButton(this.difficultyButton.x + this.difficultyButton.getWidth(), this.difficultyButton.y, dlg -> {
                    minecraft = this.minecraft;
                    // new(net.minecraft.client.gui.screens.ConfirmScreen.class)
                    this::lockCallback;
                    nr2 = new TranslatableComponent("difficulty.lock.title");
                    // new(net.minecraft.network.chat.TranslatableComponent.class)
                    arr = new Object[] { null };
                    new TranslatableComponent("options.difficulty." + this.minecraft.level.getLevelData().getDifficulty().getKey());
                    arr[o] = translatableComponent;
                    new TranslatableComponent(string, arr);
                    new ConfirmScreen(booleanConsumer, nr2, nr3);
                    minecraft.setScreen(screen);
                    return;
                }))).setLocked(this.minecraft.level.getLevelData().isDifficultyLocked());
                this.lockButton.active = !this.lockButton.isLocked();
                this.difficultyButton.active = !this.lockButton.isLocked();
            }
            else {
                this.difficultyButton.active = false;
            }
        }
        else {
            this.<OptionButton>addButton(new OptionButton(this.width / 2 - 155 + integer2 % 2 * 160, this.height / 6 - 12 + 24 * (integer2 >> 1), 150, 20, Option.REALMS_NOTIFICATIONS, Option.REALMS_NOTIFICATIONS.getMessage(this.options), dlg -> {
                Option.REALMS_NOTIFICATIONS.toggle(this.options);
                this.options.save();
                dlg.setMessage(Option.REALMS_NOTIFICATIONS.getMessage(this.options));
                return;
            }));
        }
        this.<Button>addButton(new Button(this.width / 2 - 155, this.height / 6 + 48 - 6, 150, 20, new TranslatableComponent("options.skinCustomisation"), dlg -> this.minecraft.setScreen(new SkinCustomizationScreen(this, this.options))));
        this.<Button>addButton(new Button(this.width / 2 + 5, this.height / 6 + 48 - 6, 150, 20, new TranslatableComponent("options.sounds"), dlg -> this.minecraft.setScreen(new SoundOptionsScreen(this, this.options))));
        this.<Button>addButton(new Button(this.width / 2 - 155, this.height / 6 + 72 - 6, 150, 20, new TranslatableComponent("options.video"), dlg -> this.minecraft.setScreen(new VideoSettingsScreen(this, this.options))));
        this.<Button>addButton(new Button(this.width / 2 + 5, this.height / 6 + 72 - 6, 150, 20, new TranslatableComponent("options.controls"), dlg -> this.minecraft.setScreen(new ControlsScreen(this, this.options))));
        this.<Button>addButton(new Button(this.width / 2 - 155, this.height / 6 + 96 - 6, 150, 20, new TranslatableComponent("options.language"), dlg -> this.minecraft.setScreen(new LanguageSelectScreen(this, this.options, this.minecraft.getLanguageManager()))));
        this.<Button>addButton(new Button(this.width / 2 + 5, this.height / 6 + 96 - 6, 150, 20, new TranslatableComponent("options.chat.title"), dlg -> this.minecraft.setScreen(new ChatOptionsScreen(this, this.options))));
        final Minecraft minecraft2;
        final PackSelectionScreen screen2;
        this.<Button>addButton(new Button(this.width / 2 - 155, this.height / 6 + 120 - 6, 150, 20, new TranslatableComponent("options.resourcepack"), dlg -> {
            minecraft2 = this.minecraft;
            new PackSelectionScreen(this, this.minecraft.getResourcePackRepository(), (Consumer<PackRepository>)this::updatePackList, this.minecraft.getResourcePackDirectory(), new TranslatableComponent("resourcePack.title"));
            minecraft2.setScreen(screen2);
            return;
        }));
        this.<Button>addButton(new Button(this.width / 2 + 5, this.height / 6 + 120 - 6, 150, 20, new TranslatableComponent("options.accessibility.title"), dlg -> this.minecraft.setScreen(new AccessibilityOptionsScreen(this, this.options))));
        this.<Button>addButton(new Button(this.width / 2 - 100, this.height / 6 + 168, 200, 20, CommonComponents.GUI_DONE, dlg -> this.minecraft.setScreen(this.lastScreen)));
    }
    
    private void updatePackList(final PackRepository abu) {
        final List<String> list3 = (List<String>)ImmutableList.copyOf((Collection)this.options.resourcePacks);
        this.options.resourcePacks.clear();
        this.options.incompatibleResourcePacks.clear();
        for (final Pack abs5 : abu.getSelectedPacks()) {
            if (!abs5.isFixedPosition()) {
                this.options.resourcePacks.add(abs5.getId());
                if (abs5.getCompatibility().isCompatible()) {
                    continue;
                }
                this.options.incompatibleResourcePacks.add(abs5.getId());
            }
        }
        this.options.save();
        final List<String> list4 = (List<String>)ImmutableList.copyOf((Collection)this.options.resourcePacks);
        if (!list4.equals(list3)) {
            this.minecraft.reloadResourcePacks();
        }
    }
    
    private Component getDifficultyText(final Difficulty aoo) {
        return new TranslatableComponent("options.difficulty").append(": ").append(aoo.getDisplayName());
    }
    
    private void lockCallback(final boolean boolean1) {
        this.minecraft.setScreen(this);
        if (boolean1 && this.minecraft.level != null) {
            this.minecraft.getConnection().send(new ServerboundLockDifficultyPacket(true));
            this.lockButton.setLocked(true);
            this.lockButton.active = false;
            this.difficultyButton.active = false;
        }
    }
    
    @Override
    public void removed() {
        this.options.save();
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 15, 16777215);
        super.render(dfj, integer2, integer3, float4);
    }
    
    static {
        OPTION_SCREEN_OPTIONS = new Option[] { Option.FOV };
    }
}
