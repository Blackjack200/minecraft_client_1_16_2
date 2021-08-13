package net.minecraft.world.item;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.network.chat.Component;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class AirItem extends Item {
    private final Block block;
    
    public AirItem(final Block bul, final Properties a) {
        super(a);
        this.block = bul;
    }
    
    @Override
    public String getDescriptionId() {
        return this.block.getDescriptionId();
    }
    
    @Override
    public void appendHoverText(final ItemStack bly, @Nullable final Level bru, final List<Component> list, final TooltipFlag bni) {
        super.appendHoverText(bly, bru, list, bni);
        this.block.appendHoverText(bly, bru, list, bni);
    }
}
