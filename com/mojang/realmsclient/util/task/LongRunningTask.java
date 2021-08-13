package com.mojang.realmsclient.util.task;

import org.apache.logging.log4j.LogManager;
import net.minecraft.network.chat.Component;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
import org.apache.logging.log4j.Logger;
import com.mojang.realmsclient.gui.ErrorCallback;

public abstract class LongRunningTask implements ErrorCallback, Runnable {
    public static final Logger LOGGER;
    protected RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen;
    
    protected static void pause(final int integer) {
        try {
            Thread.sleep((long)(integer * 1000));
        }
        catch (InterruptedException interruptedException2) {
            LongRunningTask.LOGGER.error("", (Throwable)interruptedException2);
        }
    }
    
    public static void setScreen(final Screen doq) {
        final Minecraft djw2 = Minecraft.getInstance();
        djw2.execute(() -> djw2.setScreen(doq));
    }
    
    public void setScreen(final RealmsLongRunningMcoTaskScreen dhw) {
        this.longRunningMcoTaskScreen = dhw;
    }
    
    public void error(final Component nr) {
        this.longRunningMcoTaskScreen.error(nr);
    }
    
    public void setTitle(final Component nr) {
        this.longRunningMcoTaskScreen.setTitle(nr);
    }
    
    public boolean aborted() {
        return this.longRunningMcoTaskScreen.aborted();
    }
    
    public void tick() {
    }
    
    public void init() {
    }
    
    public void abortTask() {
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
