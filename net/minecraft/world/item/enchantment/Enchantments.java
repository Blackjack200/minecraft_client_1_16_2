package net.minecraft.world.item.enchantment;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.EquipmentSlot;

public class Enchantments {
    private static final EquipmentSlot[] ARMOR_SLOTS;
    public static final Enchantment ALL_DAMAGE_PROTECTION;
    public static final Enchantment FIRE_PROTECTION;
    public static final Enchantment FALL_PROTECTION;
    public static final Enchantment BLAST_PROTECTION;
    public static final Enchantment PROJECTILE_PROTECTION;
    public static final Enchantment RESPIRATION;
    public static final Enchantment AQUA_AFFINITY;
    public static final Enchantment THORNS;
    public static final Enchantment DEPTH_STRIDER;
    public static final Enchantment FROST_WALKER;
    public static final Enchantment BINDING_CURSE;
    public static final Enchantment SOUL_SPEED;
    public static final Enchantment SHARPNESS;
    public static final Enchantment SMITE;
    public static final Enchantment BANE_OF_ARTHROPODS;
    public static final Enchantment KNOCKBACK;
    public static final Enchantment FIRE_ASPECT;
    public static final Enchantment MOB_LOOTING;
    public static final Enchantment SWEEPING_EDGE;
    public static final Enchantment BLOCK_EFFICIENCY;
    public static final Enchantment SILK_TOUCH;
    public static final Enchantment UNBREAKING;
    public static final Enchantment BLOCK_FORTUNE;
    public static final Enchantment POWER_ARROWS;
    public static final Enchantment PUNCH_ARROWS;
    public static final Enchantment FLAMING_ARROWS;
    public static final Enchantment INFINITY_ARROWS;
    public static final Enchantment FISHING_LUCK;
    public static final Enchantment FISHING_SPEED;
    public static final Enchantment LOYALTY;
    public static final Enchantment IMPALING;
    public static final Enchantment RIPTIDE;
    public static final Enchantment CHANNELING;
    public static final Enchantment MULTISHOT;
    public static final Enchantment QUICK_CHARGE;
    public static final Enchantment PIERCING;
    public static final Enchantment MENDING;
    public static final Enchantment VANISHING_CURSE;
    
    private static Enchantment register(final String string, final Enchantment bpp) {
        return Registry.<Enchantment>register(Registry.ENCHANTMENT, string, bpp);
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: anewarray       Lnet/minecraft/world/entity/EquipmentSlot;
        //     4: dup            
        //     5: iconst_0       
        //     6: getstatic       net/minecraft/world/entity/EquipmentSlot.HEAD:Lnet/minecraft/world/entity/EquipmentSlot;
        //     9: aastore        
        //    10: dup            
        //    11: iconst_1       
        //    12: getstatic       net/minecraft/world/entity/EquipmentSlot.CHEST:Lnet/minecraft/world/entity/EquipmentSlot;
        //    15: aastore        
        //    16: dup            
        //    17: iconst_2       
        //    18: getstatic       net/minecraft/world/entity/EquipmentSlot.LEGS:Lnet/minecraft/world/entity/EquipmentSlot;
        //    21: aastore        
        //    22: dup            
        //    23: iconst_3       
        //    24: getstatic       net/minecraft/world/entity/EquipmentSlot.FEET:Lnet/minecraft/world/entity/EquipmentSlot;
        //    27: aastore        
        //    28: putstatic       net/minecraft/world/item/enchantment/Enchantments.ARMOR_SLOTS:[Lnet/minecraft/world/entity/EquipmentSlot;
        //    31: ldc             "protection"
        //    33: new             Lnet/minecraft/world/item/enchantment/ProtectionEnchantment;
        //    36: dup            
        //    37: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.COMMON:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //    40: getstatic       net/minecraft/world/item/enchantment/ProtectionEnchantment$Type.ALL:Lnet/minecraft/world/item/enchantment/ProtectionEnchantment$Type;
        //    43: getstatic       net/minecraft/world/item/enchantment/Enchantments.ARMOR_SLOTS:[Lnet/minecraft/world/entity/EquipmentSlot;
        //    46: invokespecial   net/minecraft/world/item/enchantment/ProtectionEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;Lnet/minecraft/world/item/enchantment/ProtectionEnchantment$Type;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //    49: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //    52: putstatic       net/minecraft/world/item/enchantment/Enchantments.ALL_DAMAGE_PROTECTION:Lnet/minecraft/world/item/enchantment/Enchantment;
        //    55: ldc             "fire_protection"
        //    57: new             Lnet/minecraft/world/item/enchantment/ProtectionEnchantment;
        //    60: dup            
        //    61: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.UNCOMMON:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //    64: getstatic       net/minecraft/world/item/enchantment/ProtectionEnchantment$Type.FIRE:Lnet/minecraft/world/item/enchantment/ProtectionEnchantment$Type;
        //    67: getstatic       net/minecraft/world/item/enchantment/Enchantments.ARMOR_SLOTS:[Lnet/minecraft/world/entity/EquipmentSlot;
        //    70: invokespecial   net/minecraft/world/item/enchantment/ProtectionEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;Lnet/minecraft/world/item/enchantment/ProtectionEnchantment$Type;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //    73: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //    76: putstatic       net/minecraft/world/item/enchantment/Enchantments.FIRE_PROTECTION:Lnet/minecraft/world/item/enchantment/Enchantment;
        //    79: ldc             "feather_falling"
        //    81: new             Lnet/minecraft/world/item/enchantment/ProtectionEnchantment;
        //    84: dup            
        //    85: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.UNCOMMON:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //    88: getstatic       net/minecraft/world/item/enchantment/ProtectionEnchantment$Type.FALL:Lnet/minecraft/world/item/enchantment/ProtectionEnchantment$Type;
        //    91: getstatic       net/minecraft/world/item/enchantment/Enchantments.ARMOR_SLOTS:[Lnet/minecraft/world/entity/EquipmentSlot;
        //    94: invokespecial   net/minecraft/world/item/enchantment/ProtectionEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;Lnet/minecraft/world/item/enchantment/ProtectionEnchantment$Type;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //    97: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   100: putstatic       net/minecraft/world/item/enchantment/Enchantments.FALL_PROTECTION:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   103: ldc             "blast_protection"
        //   105: new             Lnet/minecraft/world/item/enchantment/ProtectionEnchantment;
        //   108: dup            
        //   109: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.RARE:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   112: getstatic       net/minecraft/world/item/enchantment/ProtectionEnchantment$Type.EXPLOSION:Lnet/minecraft/world/item/enchantment/ProtectionEnchantment$Type;
        //   115: getstatic       net/minecraft/world/item/enchantment/Enchantments.ARMOR_SLOTS:[Lnet/minecraft/world/entity/EquipmentSlot;
        //   118: invokespecial   net/minecraft/world/item/enchantment/ProtectionEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;Lnet/minecraft/world/item/enchantment/ProtectionEnchantment$Type;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   121: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   124: putstatic       net/minecraft/world/item/enchantment/Enchantments.BLAST_PROTECTION:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   127: ldc             "projectile_protection"
        //   129: new             Lnet/minecraft/world/item/enchantment/ProtectionEnchantment;
        //   132: dup            
        //   133: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.UNCOMMON:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   136: getstatic       net/minecraft/world/item/enchantment/ProtectionEnchantment$Type.PROJECTILE:Lnet/minecraft/world/item/enchantment/ProtectionEnchantment$Type;
        //   139: getstatic       net/minecraft/world/item/enchantment/Enchantments.ARMOR_SLOTS:[Lnet/minecraft/world/entity/EquipmentSlot;
        //   142: invokespecial   net/minecraft/world/item/enchantment/ProtectionEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;Lnet/minecraft/world/item/enchantment/ProtectionEnchantment$Type;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   145: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   148: putstatic       net/minecraft/world/item/enchantment/Enchantments.PROJECTILE_PROTECTION:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   151: ldc             "respiration"
        //   153: new             Lnet/minecraft/world/item/enchantment/OxygenEnchantment;
        //   156: dup            
        //   157: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.RARE:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   160: getstatic       net/minecraft/world/item/enchantment/Enchantments.ARMOR_SLOTS:[Lnet/minecraft/world/entity/EquipmentSlot;
        //   163: invokespecial   net/minecraft/world/item/enchantment/OxygenEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   166: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   169: putstatic       net/minecraft/world/item/enchantment/Enchantments.RESPIRATION:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   172: ldc             "aqua_affinity"
        //   174: new             Lnet/minecraft/world/item/enchantment/WaterWorkerEnchantment;
        //   177: dup            
        //   178: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.RARE:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   181: getstatic       net/minecraft/world/item/enchantment/Enchantments.ARMOR_SLOTS:[Lnet/minecraft/world/entity/EquipmentSlot;
        //   184: invokespecial   net/minecraft/world/item/enchantment/WaterWorkerEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   187: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   190: putstatic       net/minecraft/world/item/enchantment/Enchantments.AQUA_AFFINITY:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   193: ldc             "thorns"
        //   195: new             Lnet/minecraft/world/item/enchantment/ThornsEnchantment;
        //   198: dup            
        //   199: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.VERY_RARE:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   202: getstatic       net/minecraft/world/item/enchantment/Enchantments.ARMOR_SLOTS:[Lnet/minecraft/world/entity/EquipmentSlot;
        //   205: invokespecial   net/minecraft/world/item/enchantment/ThornsEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   208: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   211: putstatic       net/minecraft/world/item/enchantment/Enchantments.THORNS:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   214: ldc             "depth_strider"
        //   216: new             Lnet/minecraft/world/item/enchantment/WaterWalkerEnchantment;
        //   219: dup            
        //   220: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.RARE:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   223: getstatic       net/minecraft/world/item/enchantment/Enchantments.ARMOR_SLOTS:[Lnet/minecraft/world/entity/EquipmentSlot;
        //   226: invokespecial   net/minecraft/world/item/enchantment/WaterWalkerEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   229: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   232: putstatic       net/minecraft/world/item/enchantment/Enchantments.DEPTH_STRIDER:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   235: ldc             "frost_walker"
        //   237: new             Lnet/minecraft/world/item/enchantment/FrostWalkerEnchantment;
        //   240: dup            
        //   241: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.RARE:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   244: iconst_1       
        //   245: anewarray       Lnet/minecraft/world/entity/EquipmentSlot;
        //   248: dup            
        //   249: iconst_0       
        //   250: getstatic       net/minecraft/world/entity/EquipmentSlot.FEET:Lnet/minecraft/world/entity/EquipmentSlot;
        //   253: aastore        
        //   254: invokespecial   net/minecraft/world/item/enchantment/FrostWalkerEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   257: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   260: putstatic       net/minecraft/world/item/enchantment/Enchantments.FROST_WALKER:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   263: ldc             "binding_curse"
        //   265: new             Lnet/minecraft/world/item/enchantment/BindingCurseEnchantment;
        //   268: dup            
        //   269: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.VERY_RARE:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   272: getstatic       net/minecraft/world/item/enchantment/Enchantments.ARMOR_SLOTS:[Lnet/minecraft/world/entity/EquipmentSlot;
        //   275: invokespecial   net/minecraft/world/item/enchantment/BindingCurseEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   278: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   281: putstatic       net/minecraft/world/item/enchantment/Enchantments.BINDING_CURSE:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   284: ldc             "soul_speed"
        //   286: new             Lnet/minecraft/world/item/enchantment/SoulSpeedEnchantment;
        //   289: dup            
        //   290: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.VERY_RARE:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   293: iconst_1       
        //   294: anewarray       Lnet/minecraft/world/entity/EquipmentSlot;
        //   297: dup            
        //   298: iconst_0       
        //   299: getstatic       net/minecraft/world/entity/EquipmentSlot.FEET:Lnet/minecraft/world/entity/EquipmentSlot;
        //   302: aastore        
        //   303: invokespecial   net/minecraft/world/item/enchantment/SoulSpeedEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   306: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   309: putstatic       net/minecraft/world/item/enchantment/Enchantments.SOUL_SPEED:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   312: ldc             "sharpness"
        //   314: new             Lnet/minecraft/world/item/enchantment/DamageEnchantment;
        //   317: dup            
        //   318: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.COMMON:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   321: iconst_0       
        //   322: iconst_1       
        //   323: anewarray       Lnet/minecraft/world/entity/EquipmentSlot;
        //   326: dup            
        //   327: iconst_0       
        //   328: getstatic       net/minecraft/world/entity/EquipmentSlot.MAINHAND:Lnet/minecraft/world/entity/EquipmentSlot;
        //   331: aastore        
        //   332: invokespecial   net/minecraft/world/item/enchantment/DamageEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;I[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   335: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   338: putstatic       net/minecraft/world/item/enchantment/Enchantments.SHARPNESS:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   341: ldc             "smite"
        //   343: new             Lnet/minecraft/world/item/enchantment/DamageEnchantment;
        //   346: dup            
        //   347: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.UNCOMMON:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   350: iconst_1       
        //   351: iconst_1       
        //   352: anewarray       Lnet/minecraft/world/entity/EquipmentSlot;
        //   355: dup            
        //   356: iconst_0       
        //   357: getstatic       net/minecraft/world/entity/EquipmentSlot.MAINHAND:Lnet/minecraft/world/entity/EquipmentSlot;
        //   360: aastore        
        //   361: invokespecial   net/minecraft/world/item/enchantment/DamageEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;I[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   364: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   367: putstatic       net/minecraft/world/item/enchantment/Enchantments.SMITE:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   370: ldc             "bane_of_arthropods"
        //   372: new             Lnet/minecraft/world/item/enchantment/DamageEnchantment;
        //   375: dup            
        //   376: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.UNCOMMON:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   379: iconst_2       
        //   380: iconst_1       
        //   381: anewarray       Lnet/minecraft/world/entity/EquipmentSlot;
        //   384: dup            
        //   385: iconst_0       
        //   386: getstatic       net/minecraft/world/entity/EquipmentSlot.MAINHAND:Lnet/minecraft/world/entity/EquipmentSlot;
        //   389: aastore        
        //   390: invokespecial   net/minecraft/world/item/enchantment/DamageEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;I[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   393: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   396: putstatic       net/minecraft/world/item/enchantment/Enchantments.BANE_OF_ARTHROPODS:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   399: ldc             "knockback"
        //   401: new             Lnet/minecraft/world/item/enchantment/KnockbackEnchantment;
        //   404: dup            
        //   405: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.UNCOMMON:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   408: iconst_1       
        //   409: anewarray       Lnet/minecraft/world/entity/EquipmentSlot;
        //   412: dup            
        //   413: iconst_0       
        //   414: getstatic       net/minecraft/world/entity/EquipmentSlot.MAINHAND:Lnet/minecraft/world/entity/EquipmentSlot;
        //   417: aastore        
        //   418: invokespecial   net/minecraft/world/item/enchantment/KnockbackEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   421: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   424: putstatic       net/minecraft/world/item/enchantment/Enchantments.KNOCKBACK:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   427: ldc             "fire_aspect"
        //   429: new             Lnet/minecraft/world/item/enchantment/FireAspectEnchantment;
        //   432: dup            
        //   433: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.RARE:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   436: iconst_1       
        //   437: anewarray       Lnet/minecraft/world/entity/EquipmentSlot;
        //   440: dup            
        //   441: iconst_0       
        //   442: getstatic       net/minecraft/world/entity/EquipmentSlot.MAINHAND:Lnet/minecraft/world/entity/EquipmentSlot;
        //   445: aastore        
        //   446: invokespecial   net/minecraft/world/item/enchantment/FireAspectEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   449: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   452: putstatic       net/minecraft/world/item/enchantment/Enchantments.FIRE_ASPECT:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   455: ldc             "looting"
        //   457: new             Lnet/minecraft/world/item/enchantment/LootBonusEnchantment;
        //   460: dup            
        //   461: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.RARE:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   464: getstatic       net/minecraft/world/item/enchantment/EnchantmentCategory.WEAPON:Lnet/minecraft/world/item/enchantment/EnchantmentCategory;
        //   467: iconst_1       
        //   468: anewarray       Lnet/minecraft/world/entity/EquipmentSlot;
        //   471: dup            
        //   472: iconst_0       
        //   473: getstatic       net/minecraft/world/entity/EquipmentSlot.MAINHAND:Lnet/minecraft/world/entity/EquipmentSlot;
        //   476: aastore        
        //   477: invokespecial   net/minecraft/world/item/enchantment/LootBonusEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;Lnet/minecraft/world/item/enchantment/EnchantmentCategory;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   480: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   483: putstatic       net/minecraft/world/item/enchantment/Enchantments.MOB_LOOTING:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   486: ldc             "sweeping"
        //   488: new             Lnet/minecraft/world/item/enchantment/SweepingEdgeEnchantment;
        //   491: dup            
        //   492: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.RARE:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   495: iconst_1       
        //   496: anewarray       Lnet/minecraft/world/entity/EquipmentSlot;
        //   499: dup            
        //   500: iconst_0       
        //   501: getstatic       net/minecraft/world/entity/EquipmentSlot.MAINHAND:Lnet/minecraft/world/entity/EquipmentSlot;
        //   504: aastore        
        //   505: invokespecial   net/minecraft/world/item/enchantment/SweepingEdgeEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   508: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   511: putstatic       net/minecraft/world/item/enchantment/Enchantments.SWEEPING_EDGE:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   514: ldc             "efficiency"
        //   516: new             Lnet/minecraft/world/item/enchantment/DiggingEnchantment;
        //   519: dup            
        //   520: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.COMMON:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   523: iconst_1       
        //   524: anewarray       Lnet/minecraft/world/entity/EquipmentSlot;
        //   527: dup            
        //   528: iconst_0       
        //   529: getstatic       net/minecraft/world/entity/EquipmentSlot.MAINHAND:Lnet/minecraft/world/entity/EquipmentSlot;
        //   532: aastore        
        //   533: invokespecial   net/minecraft/world/item/enchantment/DiggingEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   536: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   539: putstatic       net/minecraft/world/item/enchantment/Enchantments.BLOCK_EFFICIENCY:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   542: ldc_w           "silk_touch"
        //   545: new             Lnet/minecraft/world/item/enchantment/UntouchingEnchantment;
        //   548: dup            
        //   549: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.VERY_RARE:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   552: iconst_1       
        //   553: anewarray       Lnet/minecraft/world/entity/EquipmentSlot;
        //   556: dup            
        //   557: iconst_0       
        //   558: getstatic       net/minecraft/world/entity/EquipmentSlot.MAINHAND:Lnet/minecraft/world/entity/EquipmentSlot;
        //   561: aastore        
        //   562: invokespecial   net/minecraft/world/item/enchantment/UntouchingEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   565: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   568: putstatic       net/minecraft/world/item/enchantment/Enchantments.SILK_TOUCH:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   571: ldc_w           "unbreaking"
        //   574: new             Lnet/minecraft/world/item/enchantment/DigDurabilityEnchantment;
        //   577: dup            
        //   578: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.UNCOMMON:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   581: iconst_1       
        //   582: anewarray       Lnet/minecraft/world/entity/EquipmentSlot;
        //   585: dup            
        //   586: iconst_0       
        //   587: getstatic       net/minecraft/world/entity/EquipmentSlot.MAINHAND:Lnet/minecraft/world/entity/EquipmentSlot;
        //   590: aastore        
        //   591: invokespecial   net/minecraft/world/item/enchantment/DigDurabilityEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   594: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   597: putstatic       net/minecraft/world/item/enchantment/Enchantments.UNBREAKING:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   600: ldc_w           "fortune"
        //   603: new             Lnet/minecraft/world/item/enchantment/LootBonusEnchantment;
        //   606: dup            
        //   607: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.RARE:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   610: getstatic       net/minecraft/world/item/enchantment/EnchantmentCategory.DIGGER:Lnet/minecraft/world/item/enchantment/EnchantmentCategory;
        //   613: iconst_1       
        //   614: anewarray       Lnet/minecraft/world/entity/EquipmentSlot;
        //   617: dup            
        //   618: iconst_0       
        //   619: getstatic       net/minecraft/world/entity/EquipmentSlot.MAINHAND:Lnet/minecraft/world/entity/EquipmentSlot;
        //   622: aastore        
        //   623: invokespecial   net/minecraft/world/item/enchantment/LootBonusEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;Lnet/minecraft/world/item/enchantment/EnchantmentCategory;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   626: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   629: putstatic       net/minecraft/world/item/enchantment/Enchantments.BLOCK_FORTUNE:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   632: ldc_w           "power"
        //   635: new             Lnet/minecraft/world/item/enchantment/ArrowDamageEnchantment;
        //   638: dup            
        //   639: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.COMMON:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   642: iconst_1       
        //   643: anewarray       Lnet/minecraft/world/entity/EquipmentSlot;
        //   646: dup            
        //   647: iconst_0       
        //   648: getstatic       net/minecraft/world/entity/EquipmentSlot.MAINHAND:Lnet/minecraft/world/entity/EquipmentSlot;
        //   651: aastore        
        //   652: invokespecial   net/minecraft/world/item/enchantment/ArrowDamageEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   655: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   658: putstatic       net/minecraft/world/item/enchantment/Enchantments.POWER_ARROWS:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   661: ldc_w           "punch"
        //   664: new             Lnet/minecraft/world/item/enchantment/ArrowKnockbackEnchantment;
        //   667: dup            
        //   668: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.RARE:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   671: iconst_1       
        //   672: anewarray       Lnet/minecraft/world/entity/EquipmentSlot;
        //   675: dup            
        //   676: iconst_0       
        //   677: getstatic       net/minecraft/world/entity/EquipmentSlot.MAINHAND:Lnet/minecraft/world/entity/EquipmentSlot;
        //   680: aastore        
        //   681: invokespecial   net/minecraft/world/item/enchantment/ArrowKnockbackEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   684: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   687: putstatic       net/minecraft/world/item/enchantment/Enchantments.PUNCH_ARROWS:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   690: ldc_w           "flame"
        //   693: new             Lnet/minecraft/world/item/enchantment/ArrowFireEnchantment;
        //   696: dup            
        //   697: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.RARE:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   700: iconst_1       
        //   701: anewarray       Lnet/minecraft/world/entity/EquipmentSlot;
        //   704: dup            
        //   705: iconst_0       
        //   706: getstatic       net/minecraft/world/entity/EquipmentSlot.MAINHAND:Lnet/minecraft/world/entity/EquipmentSlot;
        //   709: aastore        
        //   710: invokespecial   net/minecraft/world/item/enchantment/ArrowFireEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   713: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   716: putstatic       net/minecraft/world/item/enchantment/Enchantments.FLAMING_ARROWS:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   719: ldc_w           "infinity"
        //   722: new             Lnet/minecraft/world/item/enchantment/ArrowInfiniteEnchantment;
        //   725: dup            
        //   726: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.VERY_RARE:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   729: iconst_1       
        //   730: anewarray       Lnet/minecraft/world/entity/EquipmentSlot;
        //   733: dup            
        //   734: iconst_0       
        //   735: getstatic       net/minecraft/world/entity/EquipmentSlot.MAINHAND:Lnet/minecraft/world/entity/EquipmentSlot;
        //   738: aastore        
        //   739: invokespecial   net/minecraft/world/item/enchantment/ArrowInfiniteEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   742: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   745: putstatic       net/minecraft/world/item/enchantment/Enchantments.INFINITY_ARROWS:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   748: ldc_w           "luck_of_the_sea"
        //   751: new             Lnet/minecraft/world/item/enchantment/LootBonusEnchantment;
        //   754: dup            
        //   755: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.RARE:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   758: getstatic       net/minecraft/world/item/enchantment/EnchantmentCategory.FISHING_ROD:Lnet/minecraft/world/item/enchantment/EnchantmentCategory;
        //   761: iconst_1       
        //   762: anewarray       Lnet/minecraft/world/entity/EquipmentSlot;
        //   765: dup            
        //   766: iconst_0       
        //   767: getstatic       net/minecraft/world/entity/EquipmentSlot.MAINHAND:Lnet/minecraft/world/entity/EquipmentSlot;
        //   770: aastore        
        //   771: invokespecial   net/minecraft/world/item/enchantment/LootBonusEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;Lnet/minecraft/world/item/enchantment/EnchantmentCategory;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   774: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   777: putstatic       net/minecraft/world/item/enchantment/Enchantments.FISHING_LUCK:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   780: ldc_w           "lure"
        //   783: new             Lnet/minecraft/world/item/enchantment/FishingSpeedEnchantment;
        //   786: dup            
        //   787: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.RARE:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   790: getstatic       net/minecraft/world/item/enchantment/EnchantmentCategory.FISHING_ROD:Lnet/minecraft/world/item/enchantment/EnchantmentCategory;
        //   793: iconst_1       
        //   794: anewarray       Lnet/minecraft/world/entity/EquipmentSlot;
        //   797: dup            
        //   798: iconst_0       
        //   799: getstatic       net/minecraft/world/entity/EquipmentSlot.MAINHAND:Lnet/minecraft/world/entity/EquipmentSlot;
        //   802: aastore        
        //   803: invokespecial   net/minecraft/world/item/enchantment/FishingSpeedEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;Lnet/minecraft/world/item/enchantment/EnchantmentCategory;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   806: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   809: putstatic       net/minecraft/world/item/enchantment/Enchantments.FISHING_SPEED:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   812: ldc_w           "loyalty"
        //   815: new             Lnet/minecraft/world/item/enchantment/TridentLoyaltyEnchantment;
        //   818: dup            
        //   819: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.UNCOMMON:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   822: iconst_1       
        //   823: anewarray       Lnet/minecraft/world/entity/EquipmentSlot;
        //   826: dup            
        //   827: iconst_0       
        //   828: getstatic       net/minecraft/world/entity/EquipmentSlot.MAINHAND:Lnet/minecraft/world/entity/EquipmentSlot;
        //   831: aastore        
        //   832: invokespecial   net/minecraft/world/item/enchantment/TridentLoyaltyEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   835: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   838: putstatic       net/minecraft/world/item/enchantment/Enchantments.LOYALTY:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   841: ldc_w           "impaling"
        //   844: new             Lnet/minecraft/world/item/enchantment/TridentImpalerEnchantment;
        //   847: dup            
        //   848: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.RARE:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   851: iconst_1       
        //   852: anewarray       Lnet/minecraft/world/entity/EquipmentSlot;
        //   855: dup            
        //   856: iconst_0       
        //   857: getstatic       net/minecraft/world/entity/EquipmentSlot.MAINHAND:Lnet/minecraft/world/entity/EquipmentSlot;
        //   860: aastore        
        //   861: invokespecial   net/minecraft/world/item/enchantment/TridentImpalerEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   864: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   867: putstatic       net/minecraft/world/item/enchantment/Enchantments.IMPALING:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   870: ldc_w           "riptide"
        //   873: new             Lnet/minecraft/world/item/enchantment/TridentRiptideEnchantment;
        //   876: dup            
        //   877: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.RARE:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   880: iconst_1       
        //   881: anewarray       Lnet/minecraft/world/entity/EquipmentSlot;
        //   884: dup            
        //   885: iconst_0       
        //   886: getstatic       net/minecraft/world/entity/EquipmentSlot.MAINHAND:Lnet/minecraft/world/entity/EquipmentSlot;
        //   889: aastore        
        //   890: invokespecial   net/minecraft/world/item/enchantment/TridentRiptideEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   893: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   896: putstatic       net/minecraft/world/item/enchantment/Enchantments.RIPTIDE:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   899: ldc_w           "channeling"
        //   902: new             Lnet/minecraft/world/item/enchantment/TridentChannelingEnchantment;
        //   905: dup            
        //   906: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.VERY_RARE:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   909: iconst_1       
        //   910: anewarray       Lnet/minecraft/world/entity/EquipmentSlot;
        //   913: dup            
        //   914: iconst_0       
        //   915: getstatic       net/minecraft/world/entity/EquipmentSlot.MAINHAND:Lnet/minecraft/world/entity/EquipmentSlot;
        //   918: aastore        
        //   919: invokespecial   net/minecraft/world/item/enchantment/TridentChannelingEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   922: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   925: putstatic       net/minecraft/world/item/enchantment/Enchantments.CHANNELING:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   928: ldc_w           "multishot"
        //   931: new             Lnet/minecraft/world/item/enchantment/MultiShotEnchantment;
        //   934: dup            
        //   935: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.RARE:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   938: iconst_1       
        //   939: anewarray       Lnet/minecraft/world/entity/EquipmentSlot;
        //   942: dup            
        //   943: iconst_0       
        //   944: getstatic       net/minecraft/world/entity/EquipmentSlot.MAINHAND:Lnet/minecraft/world/entity/EquipmentSlot;
        //   947: aastore        
        //   948: invokespecial   net/minecraft/world/item/enchantment/MultiShotEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   951: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   954: putstatic       net/minecraft/world/item/enchantment/Enchantments.MULTISHOT:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   957: ldc_w           "quick_charge"
        //   960: new             Lnet/minecraft/world/item/enchantment/QuickChargeEnchantment;
        //   963: dup            
        //   964: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.UNCOMMON:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   967: iconst_1       
        //   968: anewarray       Lnet/minecraft/world/entity/EquipmentSlot;
        //   971: dup            
        //   972: iconst_0       
        //   973: getstatic       net/minecraft/world/entity/EquipmentSlot.MAINHAND:Lnet/minecraft/world/entity/EquipmentSlot;
        //   976: aastore        
        //   977: invokespecial   net/minecraft/world/item/enchantment/QuickChargeEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //   980: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //   983: putstatic       net/minecraft/world/item/enchantment/Enchantments.QUICK_CHARGE:Lnet/minecraft/world/item/enchantment/Enchantment;
        //   986: ldc_w           "piercing"
        //   989: new             Lnet/minecraft/world/item/enchantment/ArrowPiercingEnchantment;
        //   992: dup            
        //   993: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.COMMON:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //   996: iconst_1       
        //   997: anewarray       Lnet/minecraft/world/entity/EquipmentSlot;
        //  1000: dup            
        //  1001: iconst_0       
        //  1002: getstatic       net/minecraft/world/entity/EquipmentSlot.MAINHAND:Lnet/minecraft/world/entity/EquipmentSlot;
        //  1005: aastore        
        //  1006: invokespecial   net/minecraft/world/item/enchantment/ArrowPiercingEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //  1009: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //  1012: putstatic       net/minecraft/world/item/enchantment/Enchantments.PIERCING:Lnet/minecraft/world/item/enchantment/Enchantment;
        //  1015: ldc_w           "mending"
        //  1018: new             Lnet/minecraft/world/item/enchantment/MendingEnchantment;
        //  1021: dup            
        //  1022: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.RARE:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //  1025: invokestatic    net/minecraft/world/entity/EquipmentSlot.values:()[Lnet/minecraft/world/entity/EquipmentSlot;
        //  1028: invokespecial   net/minecraft/world/item/enchantment/MendingEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //  1031: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //  1034: putstatic       net/minecraft/world/item/enchantment/Enchantments.MENDING:Lnet/minecraft/world/item/enchantment/Enchantment;
        //  1037: ldc_w           "vanishing_curse"
        //  1040: new             Lnet/minecraft/world/item/enchantment/VanishingCurseEnchantment;
        //  1043: dup            
        //  1044: getstatic       net/minecraft/world/item/enchantment/Enchantment$Rarity.VERY_RARE:Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;
        //  1047: invokestatic    net/minecraft/world/entity/EquipmentSlot.values:()[Lnet/minecraft/world/entity/EquipmentSlot;
        //  1050: invokespecial   net/minecraft/world/item/enchantment/VanishingCurseEnchantment.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment$Rarity;[Lnet/minecraft/world/entity/EquipmentSlot;)V
        //  1053: invokestatic    net/minecraft/world/item/enchantment/Enchantments.register:(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/item/enchantment/Enchantment;
        //  1056: putstatic       net/minecraft/world/item/enchantment/Enchantments.VANISHING_CURSE:Lnet/minecraft/world/item/enchantment/Enchantment;
        //  1059: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 18
        //     at java.util.Vector.get(Vector.java:751)
        //     at com.strobel.assembler.metadata.MetadataResolver.lookupType(MetadataResolver.java:39)
        //     at com.strobel.assembler.metadata.ClassFileReader.populateAnonymousInnerTypes(ClassFileReader.java:744)
        //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:443)
        //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:377)
        //     at com.strobel.assembler.metadata.MetadataSystem.resolveType(MetadataSystem.java:129)
        //     at com.strobel.assembler.metadata.MetadataSystem.resolveCore(MetadataSystem.java:81)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:104)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.ClassFileReader.populateAnonymousInnerTypes(ClassFileReader.java:764)
        //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:443)
        //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:377)
        //     at com.strobel.assembler.metadata.MetadataSystem.resolveType(MetadataSystem.java:129)
        //     at com.strobel.assembler.metadata.MetadataSystem.resolveCore(MetadataSystem.java:81)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:104)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:111)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:621)
        //     at com.strobel.assembler.metadata.FieldReference.resolve(FieldReference.java:61)
        //     at com.strobel.decompiler.ast.TypeAnalysis.getFieldType(TypeAnalysis.java:2920)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1051)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:881)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:75)
        //     at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:266)
        //     at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:242)
        //     at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:193)
        //     at java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1382)
        //     at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:482)
        //     at java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:291)
        //     at java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:731)
        //     at java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:289)
        //     at java.util.concurrent.ForkJoinPool$WorkQueue.runTask(ForkJoinPool.java:1056)
        //     at java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1692)
        //     at java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:157)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
}
