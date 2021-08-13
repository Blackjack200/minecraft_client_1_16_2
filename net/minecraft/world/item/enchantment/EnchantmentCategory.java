package net.minecraft.world.item.enchantment;

import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Wearable;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;

public enum EnchantmentCategory {
    ARMOR {
        @Override
        public boolean canEnchant(final Item blu) {
            return blu instanceof ArmorItem;
        }
    }, 
    ARMOR_FEET {
        @Override
        public boolean canEnchant(final Item blu) {
            return blu instanceof ArmorItem && ((ArmorItem)blu).getSlot() == EquipmentSlot.FEET;
        }
    }, 
    ARMOR_LEGS {
        @Override
        public boolean canEnchant(final Item blu) {
            return blu instanceof ArmorItem && ((ArmorItem)blu).getSlot() == EquipmentSlot.LEGS;
        }
    }, 
    ARMOR_CHEST {
        @Override
        public boolean canEnchant(final Item blu) {
            return blu instanceof ArmorItem && ((ArmorItem)blu).getSlot() == EquipmentSlot.CHEST;
        }
    }, 
    ARMOR_HEAD {
        @Override
        public boolean canEnchant(final Item blu) {
            return blu instanceof ArmorItem && ((ArmorItem)blu).getSlot() == EquipmentSlot.HEAD;
        }
    }, 
    WEAPON {
        @Override
        public boolean canEnchant(final Item blu) {
            return blu instanceof SwordItem;
        }
    }, 
    DIGGER {
        @Override
        public boolean canEnchant(final Item blu) {
            return blu instanceof DiggerItem;
        }
    }, 
    FISHING_ROD {
        @Override
        public boolean canEnchant(final Item blu) {
            return blu instanceof FishingRodItem;
        }
    }, 
    TRIDENT {
        @Override
        public boolean canEnchant(final Item blu) {
            return blu instanceof TridentItem;
        }
    }, 
    BREAKABLE {
        @Override
        public boolean canEnchant(final Item blu) {
            return blu.canBeDepleted();
        }
    }, 
    BOW {
        @Override
        public boolean canEnchant(final Item blu) {
            return blu instanceof BowItem;
        }
    }, 
    WEARABLE {
        @Override
        public boolean canEnchant(final Item blu) {
            return blu instanceof Wearable || Block.byItem(blu) instanceof Wearable;
        }
    }, 
    CROSSBOW {
        @Override
        public boolean canEnchant(final Item blu) {
            return blu instanceof CrossbowItem;
        }
    }, 
    VANISHABLE {
        @Override
        public boolean canEnchant(final Item blu) {
            return blu instanceof Vanishable || Block.byItem(blu) instanceof Vanishable || EnchantmentCategory$14.BREAKABLE.canEnchant(blu);
        }
    };
    
    public abstract boolean canEnchant(final Item blu);
}
