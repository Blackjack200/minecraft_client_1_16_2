package net.minecraft.client.gui.spectator.categories;

import com.google.common.base.MoreObjects;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.gui.spectator.SpectatorMenuItem;
import java.util.List;
import net.minecraft.client.gui.spectator.SpectatorMenuCategory;

public class SpectatorPage {
    private final SpectatorMenuCategory category;
    private final List<SpectatorMenuItem> items;
    private final int selection;
    
    public SpectatorPage(final SpectatorMenuCategory dsj, final List<SpectatorMenuItem> list, final int integer) {
        this.category = dsj;
        this.items = list;
        this.selection = integer;
    }
    
    public SpectatorMenuItem getItem(final int integer) {
        if (integer < 0 || integer >= this.items.size()) {
            return SpectatorMenu.EMPTY_SLOT;
        }
        return (SpectatorMenuItem)MoreObjects.firstNonNull(this.items.get(integer), SpectatorMenu.EMPTY_SLOT);
    }
    
    public int getSelectedSlot() {
        return this.selection;
    }
}
