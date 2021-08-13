package net.minecraft.world.item.trading;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import java.util.OptionalInt;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;

public interface Merchant {
    void setTradingPlayer(@Nullable final Player bft);
    
    @Nullable
    Player getTradingPlayer();
    
    MerchantOffers getOffers();
    
    void overrideOffers(@Nullable final MerchantOffers bqt);
    
    void notifyTrade(final MerchantOffer bqs);
    
    void notifyTradeUpdated(final ItemStack bly);
    
    Level getLevel();
    
    int getVillagerXp();
    
    void overrideXp(final int integer);
    
    boolean showProgressBar();
    
    SoundEvent getNotifyTradeSound();
    
    default boolean canRestock() {
        return false;
    }
    
    default void openTradingScreen(final Player bft, final Component nr, final int integer) {
        final OptionalInt optionalInt5 = bft.openMenu(new SimpleMenuProvider((integer, bfs, bft) -> new MerchantMenu(integer, bfs, this), nr));
        if (optionalInt5.isPresent()) {
            final MerchantOffers bqt6 = this.getOffers();
            if (!bqt6.isEmpty()) {
                bft.sendMerchantOffers(optionalInt5.getAsInt(), bqt6, integer, this.getVillagerXp(), this.showProgressBar(), this.canRestock());
            }
        }
    }
}
