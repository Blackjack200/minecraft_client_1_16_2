package net.minecraft.server.packs.repository;

import net.minecraft.SharedConstants;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;

public enum PackCompatibility {
    TOO_OLD("old"), 
    TOO_NEW("new"), 
    COMPATIBLE("compatible");
    
    private final Component description;
    private final Component confirmation;
    
    private PackCompatibility(final String string3) {
        this.description = new TranslatableComponent("pack.incompatible." + string3);
        this.confirmation = new TranslatableComponent("pack.incompatible.confirm." + string3);
    }
    
    public boolean isCompatible() {
        return this == PackCompatibility.COMPATIBLE;
    }
    
    public static PackCompatibility forFormat(final int integer) {
        if (integer < SharedConstants.getCurrentVersion().getPackVersion()) {
            return PackCompatibility.TOO_OLD;
        }
        if (integer > SharedConstants.getCurrentVersion().getPackVersion()) {
            return PackCompatibility.TOO_NEW;
        }
        return PackCompatibility.COMPATIBLE;
    }
    
    public Component getDescription() {
        return this.description;
    }
    
    public Component getConfirmation() {
        return this.confirmation;
    }
}
