package com.mojang.realmsclient.util.task;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.gui.screens.Screen;
import com.mojang.realmsclient.dto.RealmsServerAddress;
import net.minecraft.realms.RealmsConnect;

public class ConnectTask extends LongRunningTask {
    private final RealmsConnect realmsConnect;
    private final RealmsServerAddress address;
    
    public ConnectTask(final Screen doq, final RealmsServerAddress dgo) {
        this.address = dgo;
        this.realmsConnect = new RealmsConnect(doq);
    }
    
    public void run() {
        this.setTitle(new TranslatableComponent("mco.connect.connecting"));
        final net.minecraft.realms.RealmsServerAddress eoh2 = net.minecraft.realms.RealmsServerAddress.parseString(this.address.address);
        this.realmsConnect.connect(eoh2.getHost(), eoh2.getPort());
    }
    
    @Override
    public void abortTask() {
        this.realmsConnect.abort();
        Minecraft.getInstance().getClientPackSource().clearServerPack();
    }
    
    @Override
    public void tick() {
        this.realmsConnect.tick();
    }
}
