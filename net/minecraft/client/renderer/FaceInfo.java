package net.minecraft.client.renderer;

import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.core.Direction;

public enum FaceInfo {
    DOWN(new VertexInfo[] { new VertexInfo(Constants.MIN_X, Constants.MIN_Y, Constants.MAX_Z), new VertexInfo(Constants.MIN_X, Constants.MIN_Y, Constants.MIN_Z), new VertexInfo(Constants.MAX_X, Constants.MIN_Y, Constants.MIN_Z), new VertexInfo(Constants.MAX_X, Constants.MIN_Y, Constants.MAX_Z) }), 
    UP(new VertexInfo[] { new VertexInfo(Constants.MIN_X, Constants.MAX_Y, Constants.MIN_Z), new VertexInfo(Constants.MIN_X, Constants.MAX_Y, Constants.MAX_Z), new VertexInfo(Constants.MAX_X, Constants.MAX_Y, Constants.MAX_Z), new VertexInfo(Constants.MAX_X, Constants.MAX_Y, Constants.MIN_Z) }), 
    NORTH(new VertexInfo[] { new VertexInfo(Constants.MAX_X, Constants.MAX_Y, Constants.MIN_Z), new VertexInfo(Constants.MAX_X, Constants.MIN_Y, Constants.MIN_Z), new VertexInfo(Constants.MIN_X, Constants.MIN_Y, Constants.MIN_Z), new VertexInfo(Constants.MIN_X, Constants.MAX_Y, Constants.MIN_Z) }), 
    SOUTH(new VertexInfo[] { new VertexInfo(Constants.MIN_X, Constants.MAX_Y, Constants.MAX_Z), new VertexInfo(Constants.MIN_X, Constants.MIN_Y, Constants.MAX_Z), new VertexInfo(Constants.MAX_X, Constants.MIN_Y, Constants.MAX_Z), new VertexInfo(Constants.MAX_X, Constants.MAX_Y, Constants.MAX_Z) }), 
    WEST(new VertexInfo[] { new VertexInfo(Constants.MIN_X, Constants.MAX_Y, Constants.MIN_Z), new VertexInfo(Constants.MIN_X, Constants.MIN_Y, Constants.MIN_Z), new VertexInfo(Constants.MIN_X, Constants.MIN_Y, Constants.MAX_Z), new VertexInfo(Constants.MIN_X, Constants.MAX_Y, Constants.MAX_Z) }), 
    EAST(new VertexInfo[] { new VertexInfo(Constants.MAX_X, Constants.MAX_Y, Constants.MAX_Z), new VertexInfo(Constants.MAX_X, Constants.MIN_Y, Constants.MAX_Z), new VertexInfo(Constants.MAX_X, Constants.MIN_Y, Constants.MIN_Z), new VertexInfo(Constants.MAX_X, Constants.MAX_Y, Constants.MIN_Z) });
    
    private static final FaceInfo[] BY_FACING;
    private final VertexInfo[] infos;
    
    public static FaceInfo fromFacing(final Direction gc) {
        return FaceInfo.BY_FACING[gc.get3DDataValue()];
    }
    
    private FaceInfo(final VertexInfo[] arr) {
        this.infos = arr;
    }
    
    public VertexInfo getVertexInfo(final int integer) {
        return this.infos[integer];
    }
    
    static {
        BY_FACING = Util.<FaceInfo[]>make(new FaceInfo[6], (java.util.function.Consumer<FaceInfo[]>)(arr -> {
            arr[Constants.MIN_Y] = FaceInfo.DOWN;
            arr[Constants.MAX_Y] = FaceInfo.UP;
            arr[Constants.MIN_Z] = FaceInfo.NORTH;
            arr[Constants.MAX_Z] = FaceInfo.SOUTH;
            arr[Constants.MIN_X] = FaceInfo.WEST;
            arr[Constants.MAX_X] = FaceInfo.EAST;
        }));
    }
    
    public static final class Constants {
        public static final int MAX_Z;
        public static final int MAX_Y;
        public static final int MAX_X;
        public static final int MIN_Z;
        public static final int MIN_Y;
        public static final int MIN_X;
        
        static {
            MAX_Z = Direction.SOUTH.get3DDataValue();
            MAX_Y = Direction.UP.get3DDataValue();
            MAX_X = Direction.EAST.get3DDataValue();
            MIN_Z = Direction.NORTH.get3DDataValue();
            MIN_Y = Direction.DOWN.get3DDataValue();
            MIN_X = Direction.WEST.get3DDataValue();
        }
    }
    
    public static class VertexInfo {
        public final int xFace;
        public final int yFace;
        public final int zFace;
        
        private VertexInfo(final int integer1, final int integer2, final int integer3) {
            this.xFace = integer1;
            this.yFace = integer2;
            this.zFace = integer3;
        }
    }
}
