package net.minecraft.world.item;

import java.util.Comparator;
import java.util.Arrays;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import java.util.Collection;
import net.minecraft.network.chat.TextComponent;
import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;

public class FireworkRocketItem extends Item {
    public FireworkRocketItem(final Properties a) {
        super(a);
    }
    
    @Override
    public InteractionResult useOn(final UseOnContext bnx) {
        final Level bru3 = bnx.getLevel();
        if (!bru3.isClientSide) {
            final ItemStack bly4 = bnx.getItemInHand();
            final Vec3 dck5 = bnx.getClickLocation();
            final Direction gc6 = bnx.getClickedFace();
            final FireworkRocketEntity bge7 = new FireworkRocketEntity(bru3, bnx.getPlayer(), dck5.x + gc6.getStepX() * 0.15, dck5.y + gc6.getStepY() * 0.15, dck5.z + gc6.getStepZ() * 0.15, bly4);
            bru3.addFreshEntity(bge7);
            bly4.shrink(1);
        }
        return InteractionResult.sidedSuccess(bru3.isClientSide);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(final Level bru, final Player bft, final InteractionHand aoq) {
        if (bft.isFallFlying()) {
            final ItemStack bly5 = bft.getItemInHand(aoq);
            if (!bru.isClientSide) {
                bru.addFreshEntity(new FireworkRocketEntity(bru, bly5, bft));
                if (!bft.abilities.instabuild) {
                    bly5.shrink(1);
                }
            }
            return InteractionResultHolder.<ItemStack>sidedSuccess(bft.getItemInHand(aoq), bru.isClientSide());
        }
        return InteractionResultHolder.<ItemStack>pass(bft.getItemInHand(aoq));
    }
    
    @Override
    public void appendHoverText(final ItemStack bly, @Nullable final Level bru, final List<Component> list, final TooltipFlag bni) {
        final CompoundTag md6 = bly.getTagElement("Fireworks");
        if (md6 == null) {
            return;
        }
        if (md6.contains("Flight", 99)) {
            list.add(new TranslatableComponent("item.minecraft.firework_rocket.flight").append(" ").append(String.valueOf((int)md6.getByte("Flight"))).withStyle(ChatFormatting.GRAY));
        }
        final ListTag mj7 = md6.getList("Explosions", 10);
        if (!mj7.isEmpty()) {
            for (int integer8 = 0; integer8 < mj7.size(); ++integer8) {
                final CompoundTag md7 = mj7.getCompound(integer8);
                final List<Component> list2 = (List<Component>)Lists.newArrayList();
                FireworkStarItem.appendHoverText(md7, list2);
                if (!list2.isEmpty()) {
                    for (int integer9 = 1; integer9 < list2.size(); ++integer9) {
                        list2.set(integer9, new TextComponent("  ").append((Component)list2.get(integer9)).withStyle(ChatFormatting.GRAY));
                    }
                    list.addAll((Collection)list2);
                }
            }
        }
    }
    
    public enum Shape {
        SMALL_BALL(0, "small_ball"), 
        LARGE_BALL(1, "large_ball"), 
        STAR(2, "star"), 
        CREEPER(3, "creeper"), 
        BURST(4, "burst");
        
        private static final Shape[] BY_ID;
        private final int id;
        private final String name;
        
        private Shape(final int integer3, final String string4) {
            this.id = integer3;
            this.name = string4;
        }
        
        public int getId() {
            return this.id;
        }
        
        public String getName() {
            return this.name;
        }
        
        public static Shape byId(final int integer) {
            if (integer < 0 || integer >= Shape.BY_ID.length) {
                return Shape.SMALL_BALL;
            }
            return Shape.BY_ID[integer];
        }
        
        static {
            BY_ID = (Shape[])Arrays.stream((Object[])values()).sorted(Comparator.comparingInt(a -> a.id)).toArray(Shape[]::new);
        }
    }
}
