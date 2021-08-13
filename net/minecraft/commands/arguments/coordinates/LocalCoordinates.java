package net.minecraft.commands.arguments.coordinates;

import java.util.Objects;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.StringReader;
import net.minecraft.world.phys.Vec2;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.commands.CommandSourceStack;

public class LocalCoordinates implements Coordinates {
    private final double left;
    private final double up;
    private final double forwards;
    
    public LocalCoordinates(final double double1, final double double2, final double double3) {
        this.left = double1;
        this.up = double2;
        this.forwards = double3;
    }
    
    public Vec3 getPosition(final CommandSourceStack db) {
        final Vec2 dcj3 = db.getRotation();
        final Vec3 dck4 = db.getAnchor().apply(db);
        final float float5 = Mth.cos((dcj3.y + 90.0f) * 0.017453292f);
        final float float6 = Mth.sin((dcj3.y + 90.0f) * 0.017453292f);
        final float float7 = Mth.cos(-dcj3.x * 0.017453292f);
        final float float8 = Mth.sin(-dcj3.x * 0.017453292f);
        final float float9 = Mth.cos((-dcj3.x + 90.0f) * 0.017453292f);
        final float float10 = Mth.sin((-dcj3.x + 90.0f) * 0.017453292f);
        final Vec3 dck5 = new Vec3(float5 * float7, float8, float6 * float7);
        final Vec3 dck6 = new Vec3(float5 * float9, float10, float6 * float9);
        final Vec3 dck7 = dck5.cross(dck6).scale(-1.0);
        final double double14 = dck5.x * this.forwards + dck6.x * this.up + dck7.x * this.left;
        final double double15 = dck5.y * this.forwards + dck6.y * this.up + dck7.y * this.left;
        final double double16 = dck5.z * this.forwards + dck6.z * this.up + dck7.z * this.left;
        return new Vec3(dck4.x + double14, dck4.y + double15, dck4.z + double16);
    }
    
    public Vec2 getRotation(final CommandSourceStack db) {
        return Vec2.ZERO;
    }
    
    public boolean isXRelative() {
        return true;
    }
    
    public boolean isYRelative() {
        return true;
    }
    
    public boolean isZRelative() {
        return true;
    }
    
    public static LocalCoordinates parse(final StringReader stringReader) throws CommandSyntaxException {
        final int integer2 = stringReader.getCursor();
        final double double3 = readDouble(stringReader, integer2);
        if (!stringReader.canRead() || stringReader.peek() != ' ') {
            stringReader.setCursor(integer2);
            throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext((ImmutableStringReader)stringReader);
        }
        stringReader.skip();
        final double double4 = readDouble(stringReader, integer2);
        if (!stringReader.canRead() || stringReader.peek() != ' ') {
            stringReader.setCursor(integer2);
            throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext((ImmutableStringReader)stringReader);
        }
        stringReader.skip();
        final double double5 = readDouble(stringReader, integer2);
        return new LocalCoordinates(double3, double4, double5);
    }
    
    private static double readDouble(final StringReader stringReader, final int integer) throws CommandSyntaxException {
        if (!stringReader.canRead()) {
            throw WorldCoordinate.ERROR_EXPECTED_DOUBLE.createWithContext((ImmutableStringReader)stringReader);
        }
        if (stringReader.peek() != '^') {
            stringReader.setCursor(integer);
            throw Vec3Argument.ERROR_MIXED_TYPE.createWithContext((ImmutableStringReader)stringReader);
        }
        stringReader.skip();
        return (stringReader.canRead() && stringReader.peek() != ' ') ? stringReader.readDouble() : 0.0;
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof LocalCoordinates)) {
            return false;
        }
        final LocalCoordinates en3 = (LocalCoordinates)object;
        return this.left == en3.left && this.up == en3.up && this.forwards == en3.forwards;
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.left, this.up, this.forwards });
    }
}
