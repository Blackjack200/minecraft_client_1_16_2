package net.minecraft.realms;

import javax.annotation.Nullable;
import com.mojang.realmsclient.gui.screens.RealmsNotificationsScreen;
import com.mojang.realmsclient.RealmsMainScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public class RealmsBridge extends RealmsScreen {
    private Screen previousScreen;
    
    public void switchToRealms(final Screen doq) {
        this.previousScreen = doq;
        Minecraft.getInstance().setScreen(new RealmsMainScreen(this));
    }
    
    @Nullable
    public RealmsScreen getNotificationScreen(final Screen doq) {
        this.previousScreen = doq;
        return new RealmsNotificationsScreen();
    }
    
    public void init() {
        Minecraft.getInstance().setScreen(this.previousScreen);
    }
}
