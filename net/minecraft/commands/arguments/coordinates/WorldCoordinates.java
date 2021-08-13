package net.minecraft.commands.arguments.coordinates;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.StringReader;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.commands.CommandSourceStack;

public class WorldCoordinates implements Coordinates {
    private final WorldCoordinate x;
    private final WorldCoordinate y;
    private final WorldCoordinate z;
    
    public WorldCoordinates(final WorldCoordinate es1, final WorldCoordinate es2, final WorldCoordinate es3) {
        this.x = es1;
        this.y = es2;
        this.z = es3;
    }
    
    public Vec3 getPosition(final CommandSourceStack db) {
        final Vec3 dck3 = db.getPosition();
        return new Vec3(this.x.get(dck3.x), this.y.get(dck3.y), this.z.get(dck3.z));
    }
    
    public Vec2 getRotation(final CommandSourceStack db) {
        final Vec2 dcj3 = db.getRotation();
        return new Vec2((float)this.x.get(dcj3.x), (float)this.y.get(dcj3.y));
    }
    
    public boolean isXRelative() {
        return this.x.isRelative();
    }
    
    public boolean isYRelative() {
        return this.y.isRelative();
    }
    
    public boolean isZRelative() {
        return this.z.isRelative();
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof WorldCoordinates)) {
            return false;
        }
        final WorldCoordinates et3 = (WorldCoordinates)object;
        return this.x.equals(et3.x) && this.y.equals(et3.y) && this.z.equals(et3.z);
    }
    
    public static WorldCoordinates parseInt(final StringReader stringReader) throws CommandSyntaxException {
        final int integer2 = stringReader.getCursor();
        final WorldCoordinate es3 = WorldCoordinate.parseInt(stringReader);
        if (!stringReader.canRead() || stringReader.peek() != ' ') {
            stringReader.setCursor(integer2);
            throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext((ImmutableStringReader)stringReader);
        }
        stringReader.skip();
        final WorldCoordinate es4 = WorldCoordinate.parseInt(stringReader);
        if (!stringReader.canRead() || stringReader.peek() != ' ') {
            stringReader.setCursor(integer2);
            throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext((ImmutableStringReader)stringReader);
        }
        stringReader.skip();
        final WorldCoordinate es5 = WorldCoordinate.parseInt(stringReader);
        return new WorldCoordinates(es3, es4, es5);
    }
    
    public static WorldCoordinates parseDouble(final StringReader stringReader, final boolean boolean2) throws CommandSyntaxException {
        final int integer3 = stringReader.getCursor();
        final WorldCoordinate es4 = WorldCoordinate.parseDouble(stringReader, boolean2);
        if (!stringReader.canRead() || stringReader.peek() != ' ') {
            stringReader.setCursor(integer3);
            throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext((ImmutableStringReader)stringReader);
        }
        stringReader.skip();
        final WorldCoordinate es5 = WorldCoordinate.parseDouble(stringReader, false);
        if (!stringReader.canRead() || stringReader.peek() != ' ') {
            stringReader.setCursor(integer3);
            throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext((ImmutableStringReader)stringReader);
        }
        stringReader.skip();
        final WorldCoordinate es6 = WorldCoordinate.parseDouble(stringReader, boolean2);
        return new WorldCoordinates(es4, es5, es6);
    }
    
    public static WorldCoordinates current() {
        return new WorldCoordinates(new WorldCoordinate(true, 0.0), new WorldCoordinate(true, 0.0), new WorldCoordinate(true, 0.0));
    }
    
    public int hashCode() {
        int integer2 = this.x.hashCode();
        integer2 = 31 * integer2 + this.y.hashCode();
        integer2 = 31 * integer2 + this.z.hashCode();
        return integer2;
    }
}
