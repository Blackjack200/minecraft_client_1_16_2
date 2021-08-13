package net.minecraft.world.item.trading;

import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class MerchantOffer {
    private final ItemStack baseCostA;
    private final ItemStack costB;
    private final ItemStack result;
    private int uses;
    private final int maxUses;
    private boolean rewardExp;
    private int specialPriceDiff;
    private int demand;
    private float priceMultiplier;
    private int xp;
    
    public MerchantOffer(final CompoundTag md) {
        this.rewardExp = true;
        this.xp = 1;
        this.baseCostA = ItemStack.of(md.getCompound("buy"));
        this.costB = ItemStack.of(md.getCompound("buyB"));
        this.result = ItemStack.of(md.getCompound("sell"));
        this.uses = md.getInt("uses");
        if (md.contains("maxUses", 99)) {
            this.maxUses = md.getInt("maxUses");
        }
        else {
            this.maxUses = 4;
        }
        if (md.contains("rewardExp", 1)) {
            this.rewardExp = md.getBoolean("rewardExp");
        }
        if (md.contains("xp", 3)) {
            this.xp = md.getInt("xp");
        }
        if (md.contains("priceMultiplier", 5)) {
            this.priceMultiplier = md.getFloat("priceMultiplier");
        }
        this.specialPriceDiff = md.getInt("specialPrice");
        this.demand = md.getInt("demand");
    }
    
    public MerchantOffer(final ItemStack bly1, final ItemStack bly2, final int integer3, final int integer4, final float float5) {
        this(bly1, ItemStack.EMPTY, bly2, integer3, integer4, float5);
    }
    
    public MerchantOffer(final ItemStack bly1, final ItemStack bly2, final ItemStack bly3, final int integer4, final int integer5, final float float6) {
        this(bly1, bly2, bly3, 0, integer4, integer5, float6);
    }
    
    public MerchantOffer(final ItemStack bly1, final ItemStack bly2, final ItemStack bly3, final int integer4, final int integer5, final int integer6, final float float7) {
        this(bly1, bly2, bly3, integer4, integer5, integer6, float7, 0);
    }
    
    public MerchantOffer(final ItemStack bly1, final ItemStack bly2, final ItemStack bly3, final int integer4, final int integer5, final int integer6, final float float7, final int integer8) {
        this.rewardExp = true;
        this.xp = 1;
        this.baseCostA = bly1;
        this.costB = bly2;
        this.result = bly3;
        this.uses = integer4;
        this.maxUses = integer5;
        this.xp = integer6;
        this.priceMultiplier = float7;
        this.demand = integer8;
    }
    
    public ItemStack getBaseCostA() {
        return this.baseCostA;
    }
    
    public ItemStack getCostA() {
        final int integer2 = this.baseCostA.getCount();
        final ItemStack bly3 = this.baseCostA.copy();
        final int integer3 = Math.max(0, Mth.floor(integer2 * this.demand * this.priceMultiplier));
        bly3.setCount(Mth.clamp(integer2 + integer3 + this.specialPriceDiff, 1, this.baseCostA.getItem().getMaxStackSize()));
        return bly3;
    }
    
    public ItemStack getCostB() {
        return this.costB;
    }
    
    public ItemStack getResult() {
        return this.result;
    }
    
    public void updateDemand() {
        this.demand = this.demand + this.uses - (this.maxUses - this.uses);
    }
    
    public ItemStack assemble() {
        return this.result.copy();
    }
    
    public int getUses() {
        return this.uses;
    }
    
    public void resetUses() {
        this.uses = 0;
    }
    
    public int getMaxUses() {
        return this.maxUses;
    }
    
    public void increaseUses() {
        ++this.uses;
    }
    
    public int getDemand() {
        return this.demand;
    }
    
    public void addToSpecialPriceDiff(final int integer) {
        this.specialPriceDiff += integer;
    }
    
    public void resetSpecialPriceDiff() {
        this.specialPriceDiff = 0;
    }
    
    public int getSpecialPriceDiff() {
        return this.specialPriceDiff;
    }
    
    public void setSpecialPriceDiff(final int integer) {
        this.specialPriceDiff = integer;
    }
    
    public float getPriceMultiplier() {
        return this.priceMultiplier;
    }
    
    public int getXp() {
        return this.xp;
    }
    
    public boolean isOutOfStock() {
        return this.uses >= this.maxUses;
    }
    
    public void setToOutOfStock() {
        this.uses = this.maxUses;
    }
    
    public boolean needsRestock() {
        return this.uses > 0;
    }
    
    public boolean shouldRewardExp() {
        return this.rewardExp;
    }
    
    public CompoundTag createTag() {
        final CompoundTag md2 = new CompoundTag();
        md2.put("buy", (Tag)this.baseCostA.save(new CompoundTag()));
        md2.put("sell", (Tag)this.result.save(new CompoundTag()));
        md2.put("buyB", (Tag)this.costB.save(new CompoundTag()));
        md2.putInt("uses", this.uses);
        md2.putInt("maxUses", this.maxUses);
        md2.putBoolean("rewardExp", this.rewardExp);
        md2.putInt("xp", this.xp);
        md2.putFloat("priceMultiplier", this.priceMultiplier);
        md2.putInt("specialPrice", this.specialPriceDiff);
        md2.putInt("demand", this.demand);
        return md2;
    }
    
    public boolean satisfiedBy(final ItemStack bly1, final ItemStack bly2) {
        return this.isRequiredItem(bly1, this.getCostA()) && bly1.getCount() >= this.getCostA().getCount() && this.isRequiredItem(bly2, this.costB) && bly2.getCount() >= this.costB.getCount();
    }
    
    private boolean isRequiredItem(final ItemStack bly1, final ItemStack bly2) {
        if (bly2.isEmpty() && bly1.isEmpty()) {
            return true;
        }
        final ItemStack bly3 = bly1.copy();
        if (bly3.getItem().canBeDepleted()) {
            bly3.setDamageValue(bly3.getDamageValue());
        }
        return ItemStack.isSame(bly3, bly2) && (!bly2.hasTag() || (bly3.hasTag() && NbtUtils.compareNbt(bly2.getTag(), bly3.getTag(), false)));
    }
    
    public boolean take(final ItemStack bly1, final ItemStack bly2) {
        if (!this.satisfiedBy(bly1, bly2)) {
            return false;
        }
        bly1.shrink(this.getCostA().getCount());
        if (!this.getCostB().isEmpty()) {
            bly2.shrink(this.getCostB().getCount());
        }
        return true;
    }
}
