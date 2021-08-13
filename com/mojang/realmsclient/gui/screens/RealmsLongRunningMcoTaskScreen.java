package com.mojang.realmsclient.gui.screens;

import org.apache.logging.log4j.LogManager;
import net.minecraft.client.gui.components.events.GuiEventListener;
import java.util.Set;
import com.google.common.collect.Sets;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.realms.NarrationHelper;
import com.mojang.realmsclient.exception.RealmsDefaultUncaughtExceptionHandler;
import net.minecraft.network.chat.TextComponent;
import com.mojang.realmsclient.util.task.LongRunningTask;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.Screen;
import org.apache.logging.log4j.Logger;
import com.mojang.realmsclient.gui.ErrorCallback;
import net.minecraft.realms.RealmsScreen;

public class RealmsLongRunningMcoTaskScreen extends RealmsScreen implements ErrorCallback {
    private static final Logger LOGGER;
    private final Screen lastScreen;
    private volatile Component title;
    @Nullable
    private volatile Component errorMessage;
    private volatile boolean aborted;
    private int animTicks;
    private final LongRunningTask task;
    private final int buttonLength = 212;
    public static final String[] SYMBOLS;
    
    public RealmsLongRunningMcoTaskScreen(final Screen doq, final LongRunningTask dix) {
        this.title = TextComponent.EMPTY;
        this.lastScreen = doq;
        (this.task = dix).setScreen(this);
        final Thread thread4 = new Thread((Runnable)dix, "Realms-long-running-task");
        thread4.setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new RealmsDefaultUncaughtExceptionHandler(RealmsLongRunningMcoTaskScreen.LOGGER));
        thread4.start();
    }
    
    @Override
    public void tick() {
        super.tick();
        NarrationHelper.repeatedly(this.title.getString());
        ++this.animTicks;
        this.task.tick();
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (integer1 == 256) {
            this.cancelOrBackButtonClicked();
            return true;
        }
        return super.keyPressed(integer1, integer2, integer3);
    }
    
    public void init() {
        this.task.init();
        this.<Button>addButton(new Button(this.width / 2 - 106, RealmsScreen.row(12), 212, 20, CommonComponents.GUI_CANCEL, dlg -> this.cancelOrBackButtonClicked()));
    }
    
    private void cancelOrBackButtonClicked() {
        this.aborted = true;
        this.task.abortTask();
        this.minecraft.setScreen(this.lastScreen);
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, RealmsScreen.row(3), 16777215);
        final Component nr6 = this.errorMessage;
        if (nr6 == null) {
            GuiComponent.drawCenteredString(dfj, this.font, RealmsLongRunningMcoTaskScreen.SYMBOLS[this.animTicks % RealmsLongRunningMcoTaskScreen.SYMBOLS.length], this.width / 2, RealmsScreen.row(8), 8421504);
        }
        else {
            GuiComponent.drawCenteredString(dfj, this.font, nr6, this.width / 2, RealmsScreen.row(8), 16711680);
        }
        super.render(dfj, integer2, integer3, float4);
    }
    
    @Override
    public void error(final Component nr) {
        this.errorMessage = nr;
        NarrationHelper.now(nr.getString());
        this.buttonsClear();
        this.<Button>addButton(new Button(this.width / 2 - 106, this.height / 4 + 120 + 12, 200, 20, CommonComponents.GUI_BACK, dlg -> this.cancelOrBackButtonClicked()));
    }
    
    private void buttonsClear() {
        final Set<GuiEventListener> set2 = (Set<GuiEventListener>)Sets.newHashSet((Iterable)this.buttons);
        this.children.removeIf(set2::contains);
        this.buttons.clear();
    }
    
    public void setTitle(final Component nr) {
        this.title = nr;
    }
    
    public boolean aborted() {
        return this.aborted;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        SYMBOLS = new String[] { "\u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583", "_ \u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584", "_ _ \u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585", "_ _ _ \u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586", "_ _ _ _ \u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587", "_ _ _ _ _ \u2583 \u2584 \u2585 \u2586 \u2587 \u2588", "_ _ _ _ \u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587", "_ _ _ \u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586", "_ _ \u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585", "_ \u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584", "\u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583", "\u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583 _", "\u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583 _ _", "\u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583 _ _ _", "\u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583 _ _ _ _", "\u2588 \u2587 \u2586 \u2585 \u2584 \u2583 _ _ _ _ _", "\u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583 _ _ _ _", "\u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583 _ _ _", "\u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583 _ _", "\u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583 _" };
    }
}
