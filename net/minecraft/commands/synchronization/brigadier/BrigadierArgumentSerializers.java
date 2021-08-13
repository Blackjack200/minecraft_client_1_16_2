package net.minecraft.commands.synchronization.brigadier;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.commands.synchronization.ArgumentSerializer;
import net.minecraft.commands.synchronization.ArgumentTypes;
import java.util.function.Supplier;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.synchronization.EmptyArgumentSerializer;
import com.mojang.brigadier.arguments.BoolArgumentType;

public class BrigadierArgumentSerializers {
    public static void bootstrap() {
        ArgumentTypes.<ArgumentType>register("brigadier:bool", (java.lang.Class<ArgumentType>)BoolArgumentType.class, (ArgumentSerializer<ArgumentType>)new EmptyArgumentSerializer<ArgumentType>((java.util.function.Supplier<ArgumentType>)BoolArgumentType::bool));
        ArgumentTypes.<ArgumentType>register("brigadier:float", (java.lang.Class<ArgumentType>)FloatArgumentType.class, (ArgumentSerializer<ArgumentType>)new FloatArgumentSerializer());
        ArgumentTypes.<ArgumentType>register("brigadier:double", (java.lang.Class<ArgumentType>)DoubleArgumentType.class, (ArgumentSerializer<ArgumentType>)new DoubleArgumentSerializer());
        ArgumentTypes.<ArgumentType>register("brigadier:integer", (java.lang.Class<ArgumentType>)IntegerArgumentType.class, (ArgumentSerializer<ArgumentType>)new IntegerArgumentSerializer());
        ArgumentTypes.<ArgumentType>register("brigadier:long", (java.lang.Class<ArgumentType>)LongArgumentType.class, (ArgumentSerializer<ArgumentType>)new LongArgumentSerializer());
        ArgumentTypes.<ArgumentType>register("brigadier:string", (java.lang.Class<ArgumentType>)StringArgumentType.class, (ArgumentSerializer<ArgumentType>)new StringArgumentSerializer());
    }
    
    public static byte createNumberFlags(final boolean boolean1, final boolean boolean2) {
        byte byte3 = 0;
        if (boolean1) {
            byte3 |= 0x1;
        }
        if (boolean2) {
            byte3 |= 0x2;
        }
        return byte3;
    }
    
    public static boolean numberHasMin(final byte byte1) {
        return (byte1 & 0x1) != 0x0;
    }
    
    public static boolean numberHasMax(final byte byte1) {
        return (byte1 & 0x2) != 0x0;
    }
}
