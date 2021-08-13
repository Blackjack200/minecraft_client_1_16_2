package com.mojang.realmsclient.util.task;

import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.client.RealmsClient;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.gui.screens.Screen;

public class WorldCreationTask extends LongRunningTask {
    private final String name;
    private final String motd;
    private final long worldId;
    private final Screen lastScreen;
    
    public WorldCreationTask(final long long1, final String string2, final String string3, final Screen doq) {
        this.worldId = long1;
        this.name = string2;
        this.motd = string3;
        this.lastScreen = doq;
    }
    
    public void run() {
        this.setTitle(new TranslatableComponent("mco.create.world.wait"));
        final RealmsClient dfy2 = RealmsClient.create();
        try {
            dfy2.initializeWorld(this.worldId, this.name, this.motd);
            LongRunningTask.setScreen(this.lastScreen);
        }
        catch (RealmsServiceException dhf3) {
            WorldCreationTask.LOGGER.error("Couldn't create world");
            this.error(dhf3.toString());
        }
        catch (Exception exception3) {
            WorldCreationTask.LOGGER.error("Could not create world");
            this.error(exception3.getLocalizedMessage());
        }
    }
}
