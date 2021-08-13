package net.minecraft.commands.arguments.coordinates;

import com.mojang.brigadier.Message;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class WorldCoordinate {
    public static final SimpleCommandExceptionType ERROR_EXPECTED_DOUBLE;
    public static final SimpleCommandExceptionType ERROR_EXPECTED_INT;
    private final boolean relative;
    private final double value;
    
    public WorldCoordinate(final boolean boolean1, final double double2) {
        this.relative = boolean1;
        this.value = double2;
    }
    
    public double get(final double double1) {
        if (this.relative) {
            return this.value + double1;
        }
        return this.value;
    }
    
    public static WorldCoordinate parseDouble(final StringReader stringReader, final boolean boolean2) throws CommandSyntaxException {
        if (stringReader.canRead() && stringReader.peek() == '^') {
            throw Vec3Argument.ERROR_MIXED_TYPE.createWithContext((ImmutableStringReader)stringReader);
        }
        if (!stringReader.canRead()) {
            throw WorldCoordinate.ERROR_EXPECTED_DOUBLE.createWithContext((ImmutableStringReader)stringReader);
        }
        final boolean boolean3 = isRelative(stringReader);
        final int integer4 = stringReader.getCursor();
        double double5 = (stringReader.canRead() && stringReader.peek() != ' ') ? stringReader.readDouble() : 0.0;
        final String string7 = stringReader.getString().substring(integer4, stringReader.getCursor());
        if (boolean3 && string7.isEmpty()) {
            return new WorldCoordinate(true, 0.0);
        }
        if (!string7.contains(".") && !boolean3 && boolean2) {
            double5 += 0.5;
        }
        return new WorldCoordinate(boolean3, double5);
    }
    
    public static WorldCoordinate parseInt(final StringReader stringReader) throws CommandSyntaxException {
        if (stringReader.canRead() && stringReader.peek() == '^') {
            throw Vec3Argument.ERROR_MIXED_TYPE.createWithContext((ImmutableStringReader)stringReader);
        }
        if (!stringReader.canRead()) {
            throw WorldCoordinate.ERROR_EXPECTED_INT.createWithContext((ImmutableStringReader)stringReader);
        }
        final boolean boolean2 = isRelative(stringReader);
        double double3;
        if (stringReader.canRead() && stringReader.peek() != ' ') {
            double3 = (boolean2 ? stringReader.readDouble() : stringReader.readInt());
        }
        else {
            double3 = 0.0;
        }
        return new WorldCoordinate(boolean2, double3);
    }
    
    public static boolean isRelative(final StringReader stringReader) {
        boolean boolean2;
        if (stringReader.peek() == '~') {
            boolean2 = true;
            stringReader.skip();
        }
        else {
            boolean2 = false;
        }
        return boolean2;
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof WorldCoordinate)) {
            return false;
        }
        final WorldCoordinate es3 = (WorldCoordinate)object;
        return this.relative == es3.relative && Double.compare(es3.value, this.value) == 0;
    }
    
    public int hashCode() {
        int integer2 = this.relative ? 1 : 0;
        final long long3 = Double.doubleToLongBits(this.value);
        integer2 = 31 * integer2 + (int)(long3 ^ long3 >>> 32);
        return integer2;
    }
    
    public boolean isRelative() {
        return this.relative;
    }
    
    static {
        ERROR_EXPECTED_DOUBLE = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.pos.missing.double"));
        ERROR_EXPECTED_INT = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.pos.missing.int"));
    }
}
