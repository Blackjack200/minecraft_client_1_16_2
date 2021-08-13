package net.minecraft.core.particles;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.item.ItemParser;
import com.mojang.brigadier.StringReader;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import com.mojang.serialization.Codec;
import net.minecraft.world.item.ItemStack;

public class ItemParticleOption implements ParticleOptions {
    public static final Deserializer<ItemParticleOption> DESERIALIZER;
    private final ParticleType<ItemParticleOption> type;
    private final ItemStack itemStack;
    
    public static Codec<ItemParticleOption> codec(final ParticleType<ItemParticleOption> hg) {
        return (Codec<ItemParticleOption>)ItemStack.CODEC.xmap(bly -> new ItemParticleOption(hg, bly), he -> he.itemStack);
    }
    
    public ItemParticleOption(final ParticleType<ItemParticleOption> hg, final ItemStack bly) {
        this.type = hg;
        this.itemStack = bly;
    }
    
    public void writeToNetwork(final FriendlyByteBuf nf) {
        nf.writeItem(this.itemStack);
    }
    
    public String writeToString() {
        return new StringBuilder().append(Registry.PARTICLE_TYPE.getKey(this.getType())).append(" ").append(new ItemInput(this.itemStack.getItem(), this.itemStack.getTag()).serialize()).toString();
    }
    
    public ParticleType<ItemParticleOption> getType() {
        return this.type;
    }
    
    public ItemStack getItem() {
        return this.itemStack;
    }
    
    static {
        DESERIALIZER = new Deserializer<ItemParticleOption>() {
            public ItemParticleOption fromCommand(final ParticleType<ItemParticleOption> hg, final StringReader stringReader) throws CommandSyntaxException {
                stringReader.expect(' ');
                final ItemParser ey4 = new ItemParser(stringReader, false).parse();
                final ItemStack bly5 = new ItemInput(ey4.getItem(), ey4.getNbt()).createItemStack(1, false);
                return new ItemParticleOption(hg, bly5);
            }
            
            public ItemParticleOption fromNetwork(final ParticleType<ItemParticleOption> hg, final FriendlyByteBuf nf) {
                return new ItemParticleOption(hg, nf.readItem());
            }
        };
    }
}
