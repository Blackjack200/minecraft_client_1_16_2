package com.mojang.realmsclient.gui.screens;

import javax.annotation.Nullable;
import com.mojang.realmsclient.dto.WorldTemplate;
import net.minecraft.realms.RealmsScreen;

public abstract class RealmsScreenWithCallback extends RealmsScreen {
    protected abstract void callback(@Nullable final WorldTemplate dhb);
}
