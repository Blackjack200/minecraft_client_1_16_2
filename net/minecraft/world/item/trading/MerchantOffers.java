package net.minecraft.world.item.trading;

import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import javax.annotation.Nullable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import java.util.ArrayList;

public class MerchantOffers extends ArrayList<MerchantOffer> {
    public MerchantOffers() {
    }
    
    public MerchantOffers(final CompoundTag md) {
        final ListTag mj3 = md.getList("Recipes", 10);
        for (int integer4 = 0; integer4 < mj3.size(); ++integer4) {
            this.add(new MerchantOffer(mj3.getCompound(integer4)));
        }
    }
    
    @Nullable
    public MerchantOffer getRecipeFor(final ItemStack bly1, final ItemStack bly2, final int integer) {
        if (integer <= 0 || integer >= this.size()) {
            for (int integer2 = 0; integer2 < this.size(); ++integer2) {
                final MerchantOffer bqs6 = (MerchantOffer)this.get(integer2);
                if (bqs6.satisfiedBy(bly1, bly2)) {
                    return bqs6;
                }
            }
            return null;
        }
        final MerchantOffer bqs7 = (MerchantOffer)this.get(integer);
        if (bqs7.satisfiedBy(bly1, bly2)) {
            return bqs7;
        }
        return null;
    }
    
    public void writeToStream(final FriendlyByteBuf nf) {
        nf.writeByte((byte)(this.size() & 0xFF));
        for (int integer3 = 0; integer3 < this.size(); ++integer3) {
            final MerchantOffer bqs4 = (MerchantOffer)this.get(integer3);
            nf.writeItem(bqs4.getBaseCostA());
            nf.writeItem(bqs4.getResult());
            final ItemStack bly5 = bqs4.getCostB();
            nf.writeBoolean(!bly5.isEmpty());
            if (!bly5.isEmpty()) {
                nf.writeItem(bly5);
            }
            nf.writeBoolean(bqs4.isOutOfStock());
            nf.writeInt(bqs4.getUses());
            nf.writeInt(bqs4.getMaxUses());
            nf.writeInt(bqs4.getXp());
            nf.writeInt(bqs4.getSpecialPriceDiff());
            nf.writeFloat(bqs4.getPriceMultiplier());
            nf.writeInt(bqs4.getDemand());
        }
    }
    
    public static MerchantOffers createFromStream(final FriendlyByteBuf nf) {
        final MerchantOffers bqt2 = new MerchantOffers();
        for (int integer3 = nf.readByte() & 0xFF, integer4 = 0; integer4 < integer3; ++integer4) {
            final ItemStack bly5 = nf.readItem();
            final ItemStack bly6 = nf.readItem();
            ItemStack bly7 = ItemStack.EMPTY;
            if (nf.readBoolean()) {
                bly7 = nf.readItem();
            }
            final boolean boolean8 = nf.readBoolean();
            final int integer5 = nf.readInt();
            final int integer6 = nf.readInt();
            final int integer7 = nf.readInt();
            final int integer8 = nf.readInt();
            final float float13 = nf.readFloat();
            final int integer9 = nf.readInt();
            final MerchantOffer bqs15 = new MerchantOffer(bly5, bly7, bly6, integer5, integer6, integer7, float13, integer9);
            if (boolean8) {
                bqs15.setToOutOfStock();
            }
            bqs15.setSpecialPriceDiff(integer8);
            bqt2.add(bqs15);
        }
        return bqt2;
    }
    
    public CompoundTag createTag() {
        final CompoundTag md2 = new CompoundTag();
        final ListTag mj3 = new ListTag();
        for (int integer4 = 0; integer4 < this.size(); ++integer4) {
            final MerchantOffer bqs5 = (MerchantOffer)this.get(integer4);
            mj3.add(bqs5.createTag());
        }
        md2.put("Recipes", (Tag)mj3);
        return md2;
    }
}
