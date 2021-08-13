package net.minecraft.world.item;

public class TieredItem extends Item {
    private final Tier tier;
    
    public TieredItem(final Tier bne, final Properties a) {
        super(a.defaultDurability(bne.getUses()));
        this.tier = bne;
    }
    
    public Tier getTier() {
        return this.tier;
    }
    
    @Override
    public int getEnchantmentValue() {
        return this.tier.getEnchantmentValue();
    }
    
    @Override
    public boolean isValidRepairItem(final ItemStack bly1, final ItemStack bly2) {
        return this.tier.getRepairIngredient().test(bly2) || super.isValidRepairItem(bly1, bly2);
    }
}
