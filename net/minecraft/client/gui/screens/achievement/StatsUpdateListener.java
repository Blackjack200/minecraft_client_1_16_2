package net.minecraft.client.gui.screens.achievement;

public interface StatsUpdateListener {
    public static final String[] LOADING_SYMBOLS = { "oooooo", "Oooooo", "oOoooo", "ooOooo", "oooOoo", "ooooOo", "oooooO" };
    
    void onStatsUpdated();
}
