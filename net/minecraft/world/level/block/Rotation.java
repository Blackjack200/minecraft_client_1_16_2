package net.minecraft.world.level.block;

import java.util.Collections;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.Util;
import java.util.Random;
import net.minecraft.core.Direction;
import com.mojang.math.OctahedralGroup;

public enum Rotation {
    NONE(OctahedralGroup.IDENTITY), 
    CLOCKWISE_90(OctahedralGroup.ROT_90_Y_NEG), 
    CLOCKWISE_180(OctahedralGroup.ROT_180_FACE_XZ), 
    COUNTERCLOCKWISE_90(OctahedralGroup.ROT_90_Y_POS);
    
    private final OctahedralGroup rotation;
    
    private Rotation(final OctahedralGroup c) {
        this.rotation = c;
    }
    
    public Rotation getRotated(final Rotation bzj) {
        Label_0148: {
            switch (bzj) {
                case CLOCKWISE_180: {
                    switch (this) {
                        case NONE: {
                            return Rotation.CLOCKWISE_180;
                        }
                        case CLOCKWISE_90: {
                            return Rotation.COUNTERCLOCKWISE_90;
                        }
                        case CLOCKWISE_180: {
                            return Rotation.NONE;
                        }
                        case COUNTERCLOCKWISE_90: {
                            return Rotation.CLOCKWISE_90;
                        }
                        default: {
                            break Label_0148;
                        }
                    }
                    break;
                }
                case COUNTERCLOCKWISE_90: {
                    switch (this) {
                        case NONE: {
                            return Rotation.COUNTERCLOCKWISE_90;
                        }
                        case CLOCKWISE_90: {
                            return Rotation.NONE;
                        }
                        case CLOCKWISE_180: {
                            return Rotation.CLOCKWISE_90;
                        }
                        case COUNTERCLOCKWISE_90: {
                            return Rotation.CLOCKWISE_180;
                        }
                        default: {
                            break Label_0148;
                        }
                    }
                    break;
                }
                case CLOCKWISE_90: {
                    switch (this) {
                        case NONE: {
                            return Rotation.CLOCKWISE_90;
                        }
                        case CLOCKWISE_90: {
                            return Rotation.CLOCKWISE_180;
                        }
                        case CLOCKWISE_180: {
                            return Rotation.COUNTERCLOCKWISE_90;
                        }
                        case COUNTERCLOCKWISE_90: {
                            return Rotation.NONE;
                        }
                        default: {
                            break Label_0148;
                        }
                    }
                    break;
                }
            }
        }
        return this;
    }
    
    public OctahedralGroup rotation() {
        return this.rotation;
    }
    
    public Direction rotate(final Direction gc) {
        if (gc.getAxis() == Direction.Axis.Y) {
            return gc;
        }
        switch (this) {
            case CLOCKWISE_180: {
                return gc.getOpposite();
            }
            case COUNTERCLOCKWISE_90: {
                return gc.getCounterClockWise();
            }
            case CLOCKWISE_90: {
                return gc.getClockWise();
            }
            default: {
                return gc;
            }
        }
    }
    
    public int rotate(final int integer1, final int integer2) {
        switch (this) {
            case CLOCKWISE_180: {
                return (integer1 + integer2 / 2) % integer2;
            }
            case COUNTERCLOCKWISE_90: {
                return (integer1 + integer2 * 3 / 4) % integer2;
            }
            case CLOCKWISE_90: {
                return (integer1 + integer2 / 4) % integer2;
            }
            default: {
                return integer1;
            }
        }
    }
    
    public static Rotation getRandom(final Random random) {
        return Util.<Rotation>getRandom(values(), random);
    }
    
    public static List<Rotation> getShuffled(final Random random) {
        final List<Rotation> list2 = (List<Rotation>)Lists.newArrayList((Object[])values());
        Collections.shuffle((List)list2, random);
        return list2;
    }
}
