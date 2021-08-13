package com.mojang.blaze3d.platform;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.opengl.GLDebugMessageARBCallback;
import org.lwjgl.opengl.GLDebugMessageARBCallbackI;
import org.lwjgl.opengl.ARBDebugOutput;
import java.util.function.Consumer;
import org.lwjgl.opengl.GLDebugMessageCallbackI;
import org.lwjgl.opengl.KHRDebug;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL;
import java.util.function.Supplier;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GLDebugMessageCallback;
import java.util.List;
import java.util.Map;
import com.google.common.base.Joiner;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import org.apache.logging.log4j.Logger;

public class GlDebug {
    private static final Logger LOGGER;
    protected static final ByteBuffer BYTE_BUFFER;
    protected static final FloatBuffer FLOAT_BUFFER;
    protected static final IntBuffer INT_BUFFER;
    private static final Joiner NEWLINE_JOINER;
    private static final Joiner STATEMENT_JOINER;
    private static final Map<Integer, String> BY_ID;
    private static final List<Integer> DEBUG_LEVELS;
    private static final List<Integer> DEBUG_LEVELS_ARB;
    private static final Map<String, List<String>> SAVED_STATES;
    
    private static String printUnknownToken(final int integer) {
        return "Unknown (0x" + Integer.toHexString(integer).toUpperCase() + ")";
    }
    
    private static String sourceToString(final int integer) {
        switch (integer) {
            case 33350: {
                return "API";
            }
            case 33351: {
                return "WINDOW SYSTEM";
            }
            case 33352: {
                return "SHADER COMPILER";
            }
            case 33353: {
                return "THIRD PARTY";
            }
            case 33354: {
                return "APPLICATION";
            }
            case 33355: {
                return "OTHER";
            }
            default: {
                return printUnknownToken(integer);
            }
        }
    }
    
    private static String typeToString(final int integer) {
        switch (integer) {
            case 33356: {
                return "ERROR";
            }
            case 33357: {
                return "DEPRECATED BEHAVIOR";
            }
            case 33358: {
                return "UNDEFINED BEHAVIOR";
            }
            case 33359: {
                return "PORTABILITY";
            }
            case 33360: {
                return "PERFORMANCE";
            }
            case 33361: {
                return "OTHER";
            }
            case 33384: {
                return "MARKER";
            }
            default: {
                return printUnknownToken(integer);
            }
        }
    }
    
    private static String severityToString(final int integer) {
        switch (integer) {
            case 37190: {
                return "HIGH";
            }
            case 37191: {
                return "MEDIUM";
            }
            case 37192: {
                return "LOW";
            }
            case 33387: {
                return "NOTIFICATION";
            }
            default: {
                return printUnknownToken(integer);
            }
        }
    }
    
    private static void printDebugLog(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final long long6, final long long7) {
        GlDebug.LOGGER.info("OpenGL debug message, id={}, source={}, type={}, severity={}, message={}", integer3, sourceToString(integer1), typeToString(integer2), severityToString(integer4), GLDebugMessageCallback.getMessage(integer5, long6));
    }
    
    private static void setup(final int integer, final String string) {
        GlDebug.BY_ID.merge(integer, string, (string1, string2) -> string1 + "/" + string2);
    }
    
    public static void enableDebugCallback(final int integer, final boolean boolean2) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isInInitPhase);
        if (integer <= 0) {
            return;
        }
        final GLCapabilities gLCapabilities3 = GL.getCapabilities();
        if (gLCapabilities3.GL_KHR_debug) {
            GL11.glEnable(37600);
            if (boolean2) {
                GL11.glEnable(33346);
            }
            for (int integer2 = 0; integer2 < GlDebug.DEBUG_LEVELS.size(); ++integer2) {
                final boolean boolean3 = integer2 < integer;
                KHRDebug.glDebugMessageControl(4352, 4352, (int)GlDebug.DEBUG_LEVELS.get(integer2), (int[])null, boolean3);
            }
            KHRDebug.glDebugMessageCallback((GLDebugMessageCallbackI)GLX.<GLDebugMessageCallbackI>make((GLDebugMessageCallbackI)GLDebugMessageCallback.create(GlDebug::printDebugLog), (java.util.function.Consumer<GLDebugMessageCallbackI>)DebugMemoryUntracker::untrack), 0L);
        }
        else if (gLCapabilities3.GL_ARB_debug_output) {
            if (boolean2) {
                GL11.glEnable(33346);
            }
            for (int integer2 = 0; integer2 < GlDebug.DEBUG_LEVELS_ARB.size(); ++integer2) {
                final boolean boolean3 = integer2 < integer;
                ARBDebugOutput.glDebugMessageControlARB(4352, 4352, (int)GlDebug.DEBUG_LEVELS_ARB.get(integer2), (int[])null, boolean3);
            }
            ARBDebugOutput.glDebugMessageCallbackARB((GLDebugMessageARBCallbackI)GLX.<GLDebugMessageARBCallbackI>make((GLDebugMessageARBCallbackI)GLDebugMessageARBCallback.create(GlDebug::printDebugLog), (java.util.function.Consumer<GLDebugMessageARBCallbackI>)DebugMemoryUntracker::untrack), 0L);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        BYTE_BUFFER = MemoryTracker.createByteBuffer(64);
        FLOAT_BUFFER = GlDebug.BYTE_BUFFER.asFloatBuffer();
        INT_BUFFER = GlDebug.BYTE_BUFFER.asIntBuffer();
        NEWLINE_JOINER = Joiner.on('\n');
        STATEMENT_JOINER = Joiner.on("; ");
        BY_ID = (Map)Maps.newHashMap();
        DEBUG_LEVELS = (List)ImmutableList.of(37190, 37191, 37192, 33387);
        DEBUG_LEVELS_ARB = (List)ImmutableList.of(37190, 37191, 37192);
        setup(256, "GL11.GL_ACCUM");
        setup(257, "GL11.GL_LOAD");
        setup(258, "GL11.GL_RETURN");
        setup(259, "GL11.GL_MULT");
        setup(260, "GL11.GL_ADD");
        setup(512, "GL11.GL_NEVER");
        setup(513, "GL11.GL_LESS");
        setup(514, "GL11.GL_EQUAL");
        setup(515, "GL11.GL_LEQUAL");
        setup(516, "GL11.GL_GREATER");
        setup(517, "GL11.GL_NOTEQUAL");
        setup(518, "GL11.GL_GEQUAL");
        setup(519, "GL11.GL_ALWAYS");
        setup(0, "GL11.GL_POINTS");
        setup(1, "GL11.GL_LINES");
        setup(2, "GL11.GL_LINE_LOOP");
        setup(3, "GL11.GL_LINE_STRIP");
        setup(4, "GL11.GL_TRIANGLES");
        setup(5, "GL11.GL_TRIANGLE_STRIP");
        setup(6, "GL11.GL_TRIANGLE_FAN");
        setup(7, "GL11.GL_QUADS");
        setup(8, "GL11.GL_QUAD_STRIP");
        setup(9, "GL11.GL_POLYGON");
        setup(0, "GL11.GL_ZERO");
        setup(1, "GL11.GL_ONE");
        setup(768, "GL11.GL_SRC_COLOR");
        setup(769, "GL11.GL_ONE_MINUS_SRC_COLOR");
        setup(770, "GL11.GL_SRC_ALPHA");
        setup(771, "GL11.GL_ONE_MINUS_SRC_ALPHA");
        setup(772, "GL11.GL_DST_ALPHA");
        setup(773, "GL11.GL_ONE_MINUS_DST_ALPHA");
        setup(774, "GL11.GL_DST_COLOR");
        setup(775, "GL11.GL_ONE_MINUS_DST_COLOR");
        setup(776, "GL11.GL_SRC_ALPHA_SATURATE");
        setup(32769, "GL14.GL_CONSTANT_COLOR");
        setup(32770, "GL14.GL_ONE_MINUS_CONSTANT_COLOR");
        setup(32771, "GL14.GL_CONSTANT_ALPHA");
        setup(32772, "GL14.GL_ONE_MINUS_CONSTANT_ALPHA");
        setup(1, "GL11.GL_TRUE");
        setup(0, "GL11.GL_FALSE");
        setup(12288, "GL11.GL_CLIP_PLANE0");
        setup(12289, "GL11.GL_CLIP_PLANE1");
        setup(12290, "GL11.GL_CLIP_PLANE2");
        setup(12291, "GL11.GL_CLIP_PLANE3");
        setup(12292, "GL11.GL_CLIP_PLANE4");
        setup(12293, "GL11.GL_CLIP_PLANE5");
        setup(5120, "GL11.GL_BYTE");
        setup(5121, "GL11.GL_UNSIGNED_BYTE");
        setup(5122, "GL11.GL_SHORT");
        setup(5123, "GL11.GL_UNSIGNED_SHORT");
        setup(5124, "GL11.GL_INT");
        setup(5125, "GL11.GL_UNSIGNED_INT");
        setup(5126, "GL11.GL_FLOAT");
        setup(5127, "GL11.GL_2_BYTES");
        setup(5128, "GL11.GL_3_BYTES");
        setup(5129, "GL11.GL_4_BYTES");
        setup(5130, "GL11.GL_DOUBLE");
        setup(0, "GL11.GL_NONE");
        setup(1024, "GL11.GL_FRONT_LEFT");
        setup(1025, "GL11.GL_FRONT_RIGHT");
        setup(1026, "GL11.GL_BACK_LEFT");
        setup(1027, "GL11.GL_BACK_RIGHT");
        setup(1028, "GL11.GL_FRONT");
        setup(1029, "GL11.GL_BACK");
        setup(1030, "GL11.GL_LEFT");
        setup(1031, "GL11.GL_RIGHT");
        setup(1032, "GL11.GL_FRONT_AND_BACK");
        setup(1033, "GL11.GL_AUX0");
        setup(1034, "GL11.GL_AUX1");
        setup(1035, "GL11.GL_AUX2");
        setup(1036, "GL11.GL_AUX3");
        setup(0, "GL11.GL_NO_ERROR");
        setup(1280, "GL11.GL_INVALID_ENUM");
        setup(1281, "GL11.GL_INVALID_VALUE");
        setup(1282, "GL11.GL_INVALID_OPERATION");
        setup(1283, "GL11.GL_STACK_OVERFLOW");
        setup(1284, "GL11.GL_STACK_UNDERFLOW");
        setup(1285, "GL11.GL_OUT_OF_MEMORY");
        setup(1536, "GL11.GL_2D");
        setup(1537, "GL11.GL_3D");
        setup(1538, "GL11.GL_3D_COLOR");
        setup(1539, "GL11.GL_3D_COLOR_TEXTURE");
        setup(1540, "GL11.GL_4D_COLOR_TEXTURE");
        setup(1792, "GL11.GL_PASS_THROUGH_TOKEN");
        setup(1793, "GL11.GL_POINT_TOKEN");
        setup(1794, "GL11.GL_LINE_TOKEN");
        setup(1795, "GL11.GL_POLYGON_TOKEN");
        setup(1796, "GL11.GL_BITMAP_TOKEN");
        setup(1797, "GL11.GL_DRAW_PIXEL_TOKEN");
        setup(1798, "GL11.GL_COPY_PIXEL_TOKEN");
        setup(1799, "GL11.GL_LINE_RESET_TOKEN");
        setup(2048, "GL11.GL_EXP");
        setup(2049, "GL11.GL_EXP2");
        setup(2304, "GL11.GL_CW");
        setup(2305, "GL11.GL_CCW");
        setup(2560, "GL11.GL_COEFF");
        setup(2561, "GL11.GL_ORDER");
        setup(2562, "GL11.GL_DOMAIN");
        setup(2816, "GL11.GL_CURRENT_COLOR");
        setup(2817, "GL11.GL_CURRENT_INDEX");
        setup(2818, "GL11.GL_CURRENT_NORMAL");
        setup(2819, "GL11.GL_CURRENT_TEXTURE_COORDS");
        setup(2820, "GL11.GL_CURRENT_RASTER_COLOR");
        setup(2821, "GL11.GL_CURRENT_RASTER_INDEX");
        setup(2822, "GL11.GL_CURRENT_RASTER_TEXTURE_COORDS");
        setup(2823, "GL11.GL_CURRENT_RASTER_POSITION");
        setup(2824, "GL11.GL_CURRENT_RASTER_POSITION_VALID");
        setup(2825, "GL11.GL_CURRENT_RASTER_DISTANCE");
        setup(2832, "GL11.GL_POINT_SMOOTH");
        setup(2833, "GL11.GL_POINT_SIZE");
        setup(2834, "GL11.GL_POINT_SIZE_RANGE");
        setup(2835, "GL11.GL_POINT_SIZE_GRANULARITY");
        setup(2848, "GL11.GL_LINE_SMOOTH");
        setup(2849, "GL11.GL_LINE_WIDTH");
        setup(2850, "GL11.GL_LINE_WIDTH_RANGE");
        setup(2851, "GL11.GL_LINE_WIDTH_GRANULARITY");
        setup(2852, "GL11.GL_LINE_STIPPLE");
        setup(2853, "GL11.GL_LINE_STIPPLE_PATTERN");
        setup(2854, "GL11.GL_LINE_STIPPLE_REPEAT");
        setup(2864, "GL11.GL_LIST_MODE");
        setup(2865, "GL11.GL_MAX_LIST_NESTING");
        setup(2866, "GL11.GL_LIST_BASE");
        setup(2867, "GL11.GL_LIST_INDEX");
        setup(2880, "GL11.GL_POLYGON_MODE");
        setup(2881, "GL11.GL_POLYGON_SMOOTH");
        setup(2882, "GL11.GL_POLYGON_STIPPLE");
        setup(2883, "GL11.GL_EDGE_FLAG");
        setup(2884, "GL11.GL_CULL_FACE");
        setup(2885, "GL11.GL_CULL_FACE_MODE");
        setup(2886, "GL11.GL_FRONT_FACE");
        setup(2896, "GL11.GL_LIGHTING");
        setup(2897, "GL11.GL_LIGHT_MODEL_LOCAL_VIEWER");
        setup(2898, "GL11.GL_LIGHT_MODEL_TWO_SIDE");
        setup(2899, "GL11.GL_LIGHT_MODEL_AMBIENT");
        setup(2900, "GL11.GL_SHADE_MODEL");
        setup(2901, "GL11.GL_COLOR_MATERIAL_FACE");
        setup(2902, "GL11.GL_COLOR_MATERIAL_PARAMETER");
        setup(2903, "GL11.GL_COLOR_MATERIAL");
        setup(2912, "GL11.GL_FOG");
        setup(2913, "GL11.GL_FOG_INDEX");
        setup(2914, "GL11.GL_FOG_DENSITY");
        setup(2915, "GL11.GL_FOG_START");
        setup(2916, "GL11.GL_FOG_END");
        setup(2917, "GL11.GL_FOG_MODE");
        setup(2918, "GL11.GL_FOG_COLOR");
        setup(2928, "GL11.GL_DEPTH_RANGE");
        setup(2929, "GL11.GL_DEPTH_TEST");
        setup(2930, "GL11.GL_DEPTH_WRITEMASK");
        setup(2931, "GL11.GL_DEPTH_CLEAR_VALUE");
        setup(2932, "GL11.GL_DEPTH_FUNC");
        setup(2944, "GL11.GL_ACCUM_CLEAR_VALUE");
        setup(2960, "GL11.GL_STENCIL_TEST");
        setup(2961, "GL11.GL_STENCIL_CLEAR_VALUE");
        setup(2962, "GL11.GL_STENCIL_FUNC");
        setup(2963, "GL11.GL_STENCIL_VALUE_MASK");
        setup(2964, "GL11.GL_STENCIL_FAIL");
        setup(2965, "GL11.GL_STENCIL_PASS_DEPTH_FAIL");
        setup(2966, "GL11.GL_STENCIL_PASS_DEPTH_PASS");
        setup(2967, "GL11.GL_STENCIL_REF");
        setup(2968, "GL11.GL_STENCIL_WRITEMASK");
        setup(2976, "GL11.GL_MATRIX_MODE");
        setup(2977, "GL11.GL_NORMALIZE");
        setup(2978, "GL11.GL_VIEWPORT");
        setup(2979, "GL11.GL_MODELVIEW_STACK_DEPTH");
        setup(2980, "GL11.GL_PROJECTION_STACK_DEPTH");
        setup(2981, "GL11.GL_TEXTURE_STACK_DEPTH");
        setup(2982, "GL11.GL_MODELVIEW_MATRIX");
        setup(2983, "GL11.GL_PROJECTION_MATRIX");
        setup(2984, "GL11.GL_TEXTURE_MATRIX");
        setup(2992, "GL11.GL_ATTRIB_STACK_DEPTH");
        setup(2993, "GL11.GL_CLIENT_ATTRIB_STACK_DEPTH");
        setup(3008, "GL11.GL_ALPHA_TEST");
        setup(3009, "GL11.GL_ALPHA_TEST_FUNC");
        setup(3010, "GL11.GL_ALPHA_TEST_REF");
        setup(3024, "GL11.GL_DITHER");
        setup(3040, "GL11.GL_BLEND_DST");
        setup(3041, "GL11.GL_BLEND_SRC");
        setup(3042, "GL11.GL_BLEND");
        setup(3056, "GL11.GL_LOGIC_OP_MODE");
        setup(3057, "GL11.GL_INDEX_LOGIC_OP");
        setup(3058, "GL11.GL_COLOR_LOGIC_OP");
        setup(3072, "GL11.GL_AUX_BUFFERS");
        setup(3073, "GL11.GL_DRAW_BUFFER");
        setup(3074, "GL11.GL_READ_BUFFER");
        setup(3088, "GL11.GL_SCISSOR_BOX");
        setup(3089, "GL11.GL_SCISSOR_TEST");
        setup(3104, "GL11.GL_INDEX_CLEAR_VALUE");
        setup(3105, "GL11.GL_INDEX_WRITEMASK");
        setup(3106, "GL11.GL_COLOR_CLEAR_VALUE");
        setup(3107, "GL11.GL_COLOR_WRITEMASK");
        setup(3120, "GL11.GL_INDEX_MODE");
        setup(3121, "GL11.GL_RGBA_MODE");
        setup(3122, "GL11.GL_DOUBLEBUFFER");
        setup(3123, "GL11.GL_STEREO");
        setup(3136, "GL11.GL_RENDER_MODE");
        setup(3152, "GL11.GL_PERSPECTIVE_CORRECTION_HINT");
        setup(3153, "GL11.GL_POINT_SMOOTH_HINT");
        setup(3154, "GL11.GL_LINE_SMOOTH_HINT");
        setup(3155, "GL11.GL_POLYGON_SMOOTH_HINT");
        setup(3156, "GL11.GL_FOG_HINT");
        setup(3168, "GL11.GL_TEXTURE_GEN_S");
        setup(3169, "GL11.GL_TEXTURE_GEN_T");
        setup(3170, "GL11.GL_TEXTURE_GEN_R");
        setup(3171, "GL11.GL_TEXTURE_GEN_Q");
        setup(3184, "GL11.GL_PIXEL_MAP_I_TO_I");
        setup(3185, "GL11.GL_PIXEL_MAP_S_TO_S");
        setup(3186, "GL11.GL_PIXEL_MAP_I_TO_R");
        setup(3187, "GL11.GL_PIXEL_MAP_I_TO_G");
        setup(3188, "GL11.GL_PIXEL_MAP_I_TO_B");
        setup(3189, "GL11.GL_PIXEL_MAP_I_TO_A");
        setup(3190, "GL11.GL_PIXEL_MAP_R_TO_R");
        setup(3191, "GL11.GL_PIXEL_MAP_G_TO_G");
        setup(3192, "GL11.GL_PIXEL_MAP_B_TO_B");
        setup(3193, "GL11.GL_PIXEL_MAP_A_TO_A");
        setup(3248, "GL11.GL_PIXEL_MAP_I_TO_I_SIZE");
        setup(3249, "GL11.GL_PIXEL_MAP_S_TO_S_SIZE");
        setup(3250, "GL11.GL_PIXEL_MAP_I_TO_R_SIZE");
        setup(3251, "GL11.GL_PIXEL_MAP_I_TO_G_SIZE");
        setup(3252, "GL11.GL_PIXEL_MAP_I_TO_B_SIZE");
        setup(3253, "GL11.GL_PIXEL_MAP_I_TO_A_SIZE");
        setup(3254, "GL11.GL_PIXEL_MAP_R_TO_R_SIZE");
        setup(3255, "GL11.GL_PIXEL_MAP_G_TO_G_SIZE");
        setup(3256, "GL11.GL_PIXEL_MAP_B_TO_B_SIZE");
        setup(3257, "GL11.GL_PIXEL_MAP_A_TO_A_SIZE");
        setup(3312, "GL11.GL_UNPACK_SWAP_BYTES");
        setup(3313, "GL11.GL_UNPACK_LSB_FIRST");
        setup(3314, "GL11.GL_UNPACK_ROW_LENGTH");
        setup(3315, "GL11.GL_UNPACK_SKIP_ROWS");
        setup(3316, "GL11.GL_UNPACK_SKIP_PIXELS");
        setup(3317, "GL11.GL_UNPACK_ALIGNMENT");
        setup(3328, "GL11.GL_PACK_SWAP_BYTES");
        setup(3329, "GL11.GL_PACK_LSB_FIRST");
        setup(3330, "GL11.GL_PACK_ROW_LENGTH");
        setup(3331, "GL11.GL_PACK_SKIP_ROWS");
        setup(3332, "GL11.GL_PACK_SKIP_PIXELS");
        setup(3333, "GL11.GL_PACK_ALIGNMENT");
        setup(3344, "GL11.GL_MAP_COLOR");
        setup(3345, "GL11.GL_MAP_STENCIL");
        setup(3346, "GL11.GL_INDEX_SHIFT");
        setup(3347, "GL11.GL_INDEX_OFFSET");
        setup(3348, "GL11.GL_RED_SCALE");
        setup(3349, "GL11.GL_RED_BIAS");
        setup(3350, "GL11.GL_ZOOM_X");
        setup(3351, "GL11.GL_ZOOM_Y");
        setup(3352, "GL11.GL_GREEN_SCALE");
        setup(3353, "GL11.GL_GREEN_BIAS");
        setup(3354, "GL11.GL_BLUE_SCALE");
        setup(3355, "GL11.GL_BLUE_BIAS");
        setup(3356, "GL11.GL_ALPHA_SCALE");
        setup(3357, "GL11.GL_ALPHA_BIAS");
        setup(3358, "GL11.GL_DEPTH_SCALE");
        setup(3359, "GL11.GL_DEPTH_BIAS");
        setup(3376, "GL11.GL_MAX_EVAL_ORDER");
        setup(3377, "GL11.GL_MAX_LIGHTS");
        setup(3378, "GL11.GL_MAX_CLIP_PLANES");
        setup(3379, "GL11.GL_MAX_TEXTURE_SIZE");
        setup(3380, "GL11.GL_MAX_PIXEL_MAP_TABLE");
        setup(3381, "GL11.GL_MAX_ATTRIB_STACK_DEPTH");
        setup(3382, "GL11.GL_MAX_MODELVIEW_STACK_DEPTH");
        setup(3383, "GL11.GL_MAX_NAME_STACK_DEPTH");
        setup(3384, "GL11.GL_MAX_PROJECTION_STACK_DEPTH");
        setup(3385, "GL11.GL_MAX_TEXTURE_STACK_DEPTH");
        setup(3386, "GL11.GL_MAX_VIEWPORT_DIMS");
        setup(3387, "GL11.GL_MAX_CLIENT_ATTRIB_STACK_DEPTH");
        setup(3408, "GL11.GL_SUBPIXEL_BITS");
        setup(3409, "GL11.GL_INDEX_BITS");
        setup(3410, "GL11.GL_RED_BITS");
        setup(3411, "GL11.GL_GREEN_BITS");
        setup(3412, "GL11.GL_BLUE_BITS");
        setup(3413, "GL11.GL_ALPHA_BITS");
        setup(3414, "GL11.GL_DEPTH_BITS");
        setup(3415, "GL11.GL_STENCIL_BITS");
        setup(3416, "GL11.GL_ACCUM_RED_BITS");
        setup(3417, "GL11.GL_ACCUM_GREEN_BITS");
        setup(3418, "GL11.GL_ACCUM_BLUE_BITS");
        setup(3419, "GL11.GL_ACCUM_ALPHA_BITS");
        setup(3440, "GL11.GL_NAME_STACK_DEPTH");
        setup(3456, "GL11.GL_AUTO_NORMAL");
        setup(3472, "GL11.GL_MAP1_COLOR_4");
        setup(3473, "GL11.GL_MAP1_INDEX");
        setup(3474, "GL11.GL_MAP1_NORMAL");
        setup(3475, "GL11.GL_MAP1_TEXTURE_COORD_1");
        setup(3476, "GL11.GL_MAP1_TEXTURE_COORD_2");
        setup(3477, "GL11.GL_MAP1_TEXTURE_COORD_3");
        setup(3478, "GL11.GL_MAP1_TEXTURE_COORD_4");
        setup(3479, "GL11.GL_MAP1_VERTEX_3");
        setup(3480, "GL11.GL_MAP1_VERTEX_4");
        setup(3504, "GL11.GL_MAP2_COLOR_4");
        setup(3505, "GL11.GL_MAP2_INDEX");
        setup(3506, "GL11.GL_MAP2_NORMAL");
        setup(3507, "GL11.GL_MAP2_TEXTURE_COORD_1");
        setup(3508, "GL11.GL_MAP2_TEXTURE_COORD_2");
        setup(3509, "GL11.GL_MAP2_TEXTURE_COORD_3");
        setup(3510, "GL11.GL_MAP2_TEXTURE_COORD_4");
        setup(3511, "GL11.GL_MAP2_VERTEX_3");
        setup(3512, "GL11.GL_MAP2_VERTEX_4");
        setup(3536, "GL11.GL_MAP1_GRID_DOMAIN");
        setup(3537, "GL11.GL_MAP1_GRID_SEGMENTS");
        setup(3538, "GL11.GL_MAP2_GRID_DOMAIN");
        setup(3539, "GL11.GL_MAP2_GRID_SEGMENTS");
        setup(3552, "GL11.GL_TEXTURE_1D");
        setup(3553, "GL11.GL_TEXTURE_2D");
        setup(3568, "GL11.GL_FEEDBACK_BUFFER_POINTER");
        setup(3569, "GL11.GL_FEEDBACK_BUFFER_SIZE");
        setup(3570, "GL11.GL_FEEDBACK_BUFFER_TYPE");
        setup(3571, "GL11.GL_SELECTION_BUFFER_POINTER");
        setup(3572, "GL11.GL_SELECTION_BUFFER_SIZE");
        setup(4096, "GL11.GL_TEXTURE_WIDTH");
        setup(4097, "GL11.GL_TEXTURE_HEIGHT");
        setup(4099, "GL11.GL_TEXTURE_INTERNAL_FORMAT");
        setup(4100, "GL11.GL_TEXTURE_BORDER_COLOR");
        setup(4101, "GL11.GL_TEXTURE_BORDER");
        setup(4352, "GL11.GL_DONT_CARE");
        setup(4353, "GL11.GL_FASTEST");
        setup(4354, "GL11.GL_NICEST");
        setup(16384, "GL11.GL_LIGHT0");
        setup(16385, "GL11.GL_LIGHT1");
        setup(16386, "GL11.GL_LIGHT2");
        setup(16387, "GL11.GL_LIGHT3");
        setup(16388, "GL11.GL_LIGHT4");
        setup(16389, "GL11.GL_LIGHT5");
        setup(16390, "GL11.GL_LIGHT6");
        setup(16391, "GL11.GL_LIGHT7");
        setup(4608, "GL11.GL_AMBIENT");
        setup(4609, "GL11.GL_DIFFUSE");
        setup(4610, "GL11.GL_SPECULAR");
        setup(4611, "GL11.GL_POSITION");
        setup(4612, "GL11.GL_SPOT_DIRECTION");
        setup(4613, "GL11.GL_SPOT_EXPONENT");
        setup(4614, "GL11.GL_SPOT_CUTOFF");
        setup(4615, "GL11.GL_CONSTANT_ATTENUATION");
        setup(4616, "GL11.GL_LINEAR_ATTENUATION");
        setup(4617, "GL11.GL_QUADRATIC_ATTENUATION");
        setup(4864, "GL11.GL_COMPILE");
        setup(4865, "GL11.GL_COMPILE_AND_EXECUTE");
        setup(5376, "GL11.GL_CLEAR");
        setup(5377, "GL11.GL_AND");
        setup(5378, "GL11.GL_AND_REVERSE");
        setup(5379, "GL11.GL_COPY");
        setup(5380, "GL11.GL_AND_INVERTED");
        setup(5381, "GL11.GL_NOOP");
        setup(5382, "GL11.GL_XOR");
        setup(5383, "GL11.GL_OR");
        setup(5384, "GL11.GL_NOR");
        setup(5385, "GL11.GL_EQUIV");
        setup(5386, "GL11.GL_INVERT");
        setup(5387, "GL11.GL_OR_REVERSE");
        setup(5388, "GL11.GL_COPY_INVERTED");
        setup(5389, "GL11.GL_OR_INVERTED");
        setup(5390, "GL11.GL_NAND");
        setup(5391, "GL11.GL_SET");
        setup(5632, "GL11.GL_EMISSION");
        setup(5633, "GL11.GL_SHININESS");
        setup(5634, "GL11.GL_AMBIENT_AND_DIFFUSE");
        setup(5635, "GL11.GL_COLOR_INDEXES");
        setup(5888, "GL11.GL_MODELVIEW");
        setup(5889, "GL11.GL_PROJECTION");
        setup(5890, "GL11.GL_TEXTURE");
        setup(6144, "GL11.GL_COLOR");
        setup(6145, "GL11.GL_DEPTH");
        setup(6146, "GL11.GL_STENCIL");
        setup(6400, "GL11.GL_COLOR_INDEX");
        setup(6401, "GL11.GL_STENCIL_INDEX");
        setup(6402, "GL11.GL_DEPTH_COMPONENT");
        setup(6403, "GL11.GL_RED");
        setup(6404, "GL11.GL_GREEN");
        setup(6405, "GL11.GL_BLUE");
        setup(6406, "GL11.GL_ALPHA");
        setup(6407, "GL11.GL_RGB");
        setup(6408, "GL11.GL_RGBA");
        setup(6409, "GL11.GL_LUMINANCE");
        setup(6410, "GL11.GL_LUMINANCE_ALPHA");
        setup(6656, "GL11.GL_BITMAP");
        setup(6912, "GL11.GL_POINT");
        setup(6913, "GL11.GL_LINE");
        setup(6914, "GL11.GL_FILL");
        setup(7168, "GL11.GL_RENDER");
        setup(7169, "GL11.GL_FEEDBACK");
        setup(7170, "GL11.GL_SELECT");
        setup(7424, "GL11.GL_FLAT");
        setup(7425, "GL11.GL_SMOOTH");
        setup(7680, "GL11.GL_KEEP");
        setup(7681, "GL11.GL_REPLACE");
        setup(7682, "GL11.GL_INCR");
        setup(7683, "GL11.GL_DECR");
        setup(7936, "GL11.GL_VENDOR");
        setup(7937, "GL11.GL_RENDERER");
        setup(7938, "GL11.GL_VERSION");
        setup(7939, "GL11.GL_EXTENSIONS");
        setup(8192, "GL11.GL_S");
        setup(8193, "GL11.GL_T");
        setup(8194, "GL11.GL_R");
        setup(8195, "GL11.GL_Q");
        setup(8448, "GL11.GL_MODULATE");
        setup(8449, "GL11.GL_DECAL");
        setup(8704, "GL11.GL_TEXTURE_ENV_MODE");
        setup(8705, "GL11.GL_TEXTURE_ENV_COLOR");
        setup(8960, "GL11.GL_TEXTURE_ENV");
        setup(9216, "GL11.GL_EYE_LINEAR");
        setup(9217, "GL11.GL_OBJECT_LINEAR");
        setup(9218, "GL11.GL_SPHERE_MAP");
        setup(9472, "GL11.GL_TEXTURE_GEN_MODE");
        setup(9473, "GL11.GL_OBJECT_PLANE");
        setup(9474, "GL11.GL_EYE_PLANE");
        setup(9728, "GL11.GL_NEAREST");
        setup(9729, "GL11.GL_LINEAR");
        setup(9984, "GL11.GL_NEAREST_MIPMAP_NEAREST");
        setup(9985, "GL11.GL_LINEAR_MIPMAP_NEAREST");
        setup(9986, "GL11.GL_NEAREST_MIPMAP_LINEAR");
        setup(9987, "GL11.GL_LINEAR_MIPMAP_LINEAR");
        setup(10240, "GL11.GL_TEXTURE_MAG_FILTER");
        setup(10241, "GL11.GL_TEXTURE_MIN_FILTER");
        setup(10242, "GL11.GL_TEXTURE_WRAP_S");
        setup(10243, "GL11.GL_TEXTURE_WRAP_T");
        setup(10496, "GL11.GL_CLAMP");
        setup(10497, "GL11.GL_REPEAT");
        setup(-1, "GL11.GL_ALL_CLIENT_ATTRIB_BITS");
        setup(32824, "GL11.GL_POLYGON_OFFSET_FACTOR");
        setup(10752, "GL11.GL_POLYGON_OFFSET_UNITS");
        setup(10753, "GL11.GL_POLYGON_OFFSET_POINT");
        setup(10754, "GL11.GL_POLYGON_OFFSET_LINE");
        setup(32823, "GL11.GL_POLYGON_OFFSET_FILL");
        setup(32827, "GL11.GL_ALPHA4");
        setup(32828, "GL11.GL_ALPHA8");
        setup(32829, "GL11.GL_ALPHA12");
        setup(32830, "GL11.GL_ALPHA16");
        setup(32831, "GL11.GL_LUMINANCE4");
        setup(32832, "GL11.GL_LUMINANCE8");
        setup(32833, "GL11.GL_LUMINANCE12");
        setup(32834, "GL11.GL_LUMINANCE16");
        setup(32835, "GL11.GL_LUMINANCE4_ALPHA4");
        setup(32836, "GL11.GL_LUMINANCE6_ALPHA2");
        setup(32837, "GL11.GL_LUMINANCE8_ALPHA8");
        setup(32838, "GL11.GL_LUMINANCE12_ALPHA4");
        setup(32839, "GL11.GL_LUMINANCE12_ALPHA12");
        setup(32840, "GL11.GL_LUMINANCE16_ALPHA16");
        setup(32841, "GL11.GL_INTENSITY");
        setup(32842, "GL11.GL_INTENSITY4");
        setup(32843, "GL11.GL_INTENSITY8");
        setup(32844, "GL11.GL_INTENSITY12");
        setup(32845, "GL11.GL_INTENSITY16");
        setup(10768, "GL11.GL_R3_G3_B2");
        setup(32847, "GL11.GL_RGB4");
        setup(32848, "GL11.GL_RGB5");
        setup(32849, "GL11.GL_RGB8");
        setup(32850, "GL11.GL_RGB10");
        setup(32851, "GL11.GL_RGB12");
        setup(32852, "GL11.GL_RGB16");
        setup(32853, "GL11.GL_RGBA2");
        setup(32854, "GL11.GL_RGBA4");
        setup(32855, "GL11.GL_RGB5_A1");
        setup(32856, "GL11.GL_RGBA8");
        setup(32857, "GL11.GL_RGB10_A2");
        setup(32858, "GL11.GL_RGBA12");
        setup(32859, "GL11.GL_RGBA16");
        setup(32860, "GL11.GL_TEXTURE_RED_SIZE");
        setup(32861, "GL11.GL_TEXTURE_GREEN_SIZE");
        setup(32862, "GL11.GL_TEXTURE_BLUE_SIZE");
        setup(32863, "GL11.GL_TEXTURE_ALPHA_SIZE");
        setup(32864, "GL11.GL_TEXTURE_LUMINANCE_SIZE");
        setup(32865, "GL11.GL_TEXTURE_INTENSITY_SIZE");
        setup(32867, "GL11.GL_PROXY_TEXTURE_1D");
        setup(32868, "GL11.GL_PROXY_TEXTURE_2D");
        setup(32870, "GL11.GL_TEXTURE_PRIORITY");
        setup(32871, "GL11.GL_TEXTURE_RESIDENT");
        setup(32872, "GL11.GL_TEXTURE_BINDING_1D");
        setup(32873, "GL11.GL_TEXTURE_BINDING_2D");
        setup(32884, "GL11.GL_VERTEX_ARRAY");
        setup(32885, "GL11.GL_NORMAL_ARRAY");
        setup(32886, "GL11.GL_COLOR_ARRAY");
        setup(32887, "GL11.GL_INDEX_ARRAY");
        setup(32888, "GL11.GL_TEXTURE_COORD_ARRAY");
        setup(32889, "GL11.GL_EDGE_FLAG_ARRAY");
        setup(32890, "GL11.GL_VERTEX_ARRAY_SIZE");
        setup(32891, "GL11.GL_VERTEX_ARRAY_TYPE");
        setup(32892, "GL11.GL_VERTEX_ARRAY_STRIDE");
        setup(32894, "GL11.GL_NORMAL_ARRAY_TYPE");
        setup(32895, "GL11.GL_NORMAL_ARRAY_STRIDE");
        setup(32897, "GL11.GL_COLOR_ARRAY_SIZE");
        setup(32898, "GL11.GL_COLOR_ARRAY_TYPE");
        setup(32899, "GL11.GL_COLOR_ARRAY_STRIDE");
        setup(32901, "GL11.GL_INDEX_ARRAY_TYPE");
        setup(32902, "GL11.GL_INDEX_ARRAY_STRIDE");
        setup(32904, "GL11.GL_TEXTURE_COORD_ARRAY_SIZE");
        setup(32905, "GL11.GL_TEXTURE_COORD_ARRAY_TYPE");
        setup(32906, "GL11.GL_TEXTURE_COORD_ARRAY_STRIDE");
        setup(32908, "GL11.GL_EDGE_FLAG_ARRAY_STRIDE");
        setup(32910, "GL11.GL_VERTEX_ARRAY_POINTER");
        setup(32911, "GL11.GL_NORMAL_ARRAY_POINTER");
        setup(32912, "GL11.GL_COLOR_ARRAY_POINTER");
        setup(32913, "GL11.GL_INDEX_ARRAY_POINTER");
        setup(32914, "GL11.GL_TEXTURE_COORD_ARRAY_POINTER");
        setup(32915, "GL11.GL_EDGE_FLAG_ARRAY_POINTER");
        setup(10784, "GL11.GL_V2F");
        setup(10785, "GL11.GL_V3F");
        setup(10786, "GL11.GL_C4UB_V2F");
        setup(10787, "GL11.GL_C4UB_V3F");
        setup(10788, "GL11.GL_C3F_V3F");
        setup(10789, "GL11.GL_N3F_V3F");
        setup(10790, "GL11.GL_C4F_N3F_V3F");
        setup(10791, "GL11.GL_T2F_V3F");
        setup(10792, "GL11.GL_T4F_V4F");
        setup(10793, "GL11.GL_T2F_C4UB_V3F");
        setup(10794, "GL11.GL_T2F_C3F_V3F");
        setup(10795, "GL11.GL_T2F_N3F_V3F");
        setup(10796, "GL11.GL_T2F_C4F_N3F_V3F");
        setup(10797, "GL11.GL_T4F_C4F_N3F_V4F");
        setup(3057, "GL11.GL_LOGIC_OP");
        setup(4099, "GL11.GL_TEXTURE_COMPONENTS");
        setup(32874, "GL12.GL_TEXTURE_BINDING_3D");
        setup(32875, "GL12.GL_PACK_SKIP_IMAGES");
        setup(32876, "GL12.GL_PACK_IMAGE_HEIGHT");
        setup(32877, "GL12.GL_UNPACK_SKIP_IMAGES");
        setup(32878, "GL12.GL_UNPACK_IMAGE_HEIGHT");
        setup(32879, "GL12.GL_TEXTURE_3D");
        setup(32880, "GL12.GL_PROXY_TEXTURE_3D");
        setup(32881, "GL12.GL_TEXTURE_DEPTH");
        setup(32882, "GL12.GL_TEXTURE_WRAP_R");
        setup(32883, "GL12.GL_MAX_3D_TEXTURE_SIZE");
        setup(32992, "GL12.GL_BGR");
        setup(32993, "GL12.GL_BGRA");
        setup(32818, "GL12.GL_UNSIGNED_BYTE_3_3_2");
        setup(33634, "GL12.GL_UNSIGNED_BYTE_2_3_3_REV");
        setup(33635, "GL12.GL_UNSIGNED_SHORT_5_6_5");
        setup(33636, "GL12.GL_UNSIGNED_SHORT_5_6_5_REV");
        setup(32819, "GL12.GL_UNSIGNED_SHORT_4_4_4_4");
        setup(33637, "GL12.GL_UNSIGNED_SHORT_4_4_4_4_REV");
        setup(32820, "GL12.GL_UNSIGNED_SHORT_5_5_5_1");
        setup(33638, "GL12.GL_UNSIGNED_SHORT_1_5_5_5_REV");
        setup(32821, "GL12.GL_UNSIGNED_INT_8_8_8_8");
        setup(33639, "GL12.GL_UNSIGNED_INT_8_8_8_8_REV");
        setup(32822, "GL12.GL_UNSIGNED_INT_10_10_10_2");
        setup(33640, "GL12.GL_UNSIGNED_INT_2_10_10_10_REV");
        setup(32826, "GL12.GL_RESCALE_NORMAL");
        setup(33272, "GL12.GL_LIGHT_MODEL_COLOR_CONTROL");
        setup(33273, "GL12.GL_SINGLE_COLOR");
        setup(33274, "GL12.GL_SEPARATE_SPECULAR_COLOR");
        setup(33071, "GL12.GL_CLAMP_TO_EDGE");
        setup(33082, "GL12.GL_TEXTURE_MIN_LOD");
        setup(33083, "GL12.GL_TEXTURE_MAX_LOD");
        setup(33084, "GL12.GL_TEXTURE_BASE_LEVEL");
        setup(33085, "GL12.GL_TEXTURE_MAX_LEVEL");
        setup(33000, "GL12.GL_MAX_ELEMENTS_VERTICES");
        setup(33001, "GL12.GL_MAX_ELEMENTS_INDICES");
        setup(33901, "GL12.GL_ALIASED_POINT_SIZE_RANGE");
        setup(33902, "GL12.GL_ALIASED_LINE_WIDTH_RANGE");
        setup(33984, "GL13.GL_TEXTURE0");
        setup(33985, "GL13.GL_TEXTURE1");
        setup(33986, "GL13.GL_TEXTURE2");
        setup(33987, "GL13.GL_TEXTURE3");
        setup(33988, "GL13.GL_TEXTURE4");
        setup(33989, "GL13.GL_TEXTURE5");
        setup(33990, "GL13.GL_TEXTURE6");
        setup(33991, "GL13.GL_TEXTURE7");
        setup(33992, "GL13.GL_TEXTURE8");
        setup(33993, "GL13.GL_TEXTURE9");
        setup(33994, "GL13.GL_TEXTURE10");
        setup(33995, "GL13.GL_TEXTURE11");
        setup(33996, "GL13.GL_TEXTURE12");
        setup(33997, "GL13.GL_TEXTURE13");
        setup(33998, "GL13.GL_TEXTURE14");
        setup(33999, "GL13.GL_TEXTURE15");
        setup(34000, "GL13.GL_TEXTURE16");
        setup(34001, "GL13.GL_TEXTURE17");
        setup(34002, "GL13.GL_TEXTURE18");
        setup(34003, "GL13.GL_TEXTURE19");
        setup(34004, "GL13.GL_TEXTURE20");
        setup(34005, "GL13.GL_TEXTURE21");
        setup(34006, "GL13.GL_TEXTURE22");
        setup(34007, "GL13.GL_TEXTURE23");
        setup(34008, "GL13.GL_TEXTURE24");
        setup(34009, "GL13.GL_TEXTURE25");
        setup(34010, "GL13.GL_TEXTURE26");
        setup(34011, "GL13.GL_TEXTURE27");
        setup(34012, "GL13.GL_TEXTURE28");
        setup(34013, "GL13.GL_TEXTURE29");
        setup(34014, "GL13.GL_TEXTURE30");
        setup(34015, "GL13.GL_TEXTURE31");
        setup(34016, "GL13.GL_ACTIVE_TEXTURE");
        setup(34017, "GL13.GL_CLIENT_ACTIVE_TEXTURE");
        setup(34018, "GL13.GL_MAX_TEXTURE_UNITS");
        setup(34065, "GL13.GL_NORMAL_MAP");
        setup(34066, "GL13.GL_REFLECTION_MAP");
        setup(34067, "GL13.GL_TEXTURE_CUBE_MAP");
        setup(34068, "GL13.GL_TEXTURE_BINDING_CUBE_MAP");
        setup(34069, "GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X");
        setup(34070, "GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X");
        setup(34071, "GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y");
        setup(34072, "GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y");
        setup(34073, "GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z");
        setup(34074, "GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z");
        setup(34075, "GL13.GL_PROXY_TEXTURE_CUBE_MAP");
        setup(34076, "GL13.GL_MAX_CUBE_MAP_TEXTURE_SIZE");
        setup(34025, "GL13.GL_COMPRESSED_ALPHA");
        setup(34026, "GL13.GL_COMPRESSED_LUMINANCE");
        setup(34027, "GL13.GL_COMPRESSED_LUMINANCE_ALPHA");
        setup(34028, "GL13.GL_COMPRESSED_INTENSITY");
        setup(34029, "GL13.GL_COMPRESSED_RGB");
        setup(34030, "GL13.GL_COMPRESSED_RGBA");
        setup(34031, "GL13.GL_TEXTURE_COMPRESSION_HINT");
        setup(34464, "GL13.GL_TEXTURE_COMPRESSED_IMAGE_SIZE");
        setup(34465, "GL13.GL_TEXTURE_COMPRESSED");
        setup(34466, "GL13.GL_NUM_COMPRESSED_TEXTURE_FORMATS");
        setup(34467, "GL13.GL_COMPRESSED_TEXTURE_FORMATS");
        setup(32925, "GL13.GL_MULTISAMPLE");
        setup(32926, "GL13.GL_SAMPLE_ALPHA_TO_COVERAGE");
        setup(32927, "GL13.GL_SAMPLE_ALPHA_TO_ONE");
        setup(32928, "GL13.GL_SAMPLE_COVERAGE");
        setup(32936, "GL13.GL_SAMPLE_BUFFERS");
        setup(32937, "GL13.GL_SAMPLES");
        setup(32938, "GL13.GL_SAMPLE_COVERAGE_VALUE");
        setup(32939, "GL13.GL_SAMPLE_COVERAGE_INVERT");
        setup(34019, "GL13.GL_TRANSPOSE_MODELVIEW_MATRIX");
        setup(34020, "GL13.GL_TRANSPOSE_PROJECTION_MATRIX");
        setup(34021, "GL13.GL_TRANSPOSE_TEXTURE_MATRIX");
        setup(34022, "GL13.GL_TRANSPOSE_COLOR_MATRIX");
        setup(34160, "GL13.GL_COMBINE");
        setup(34161, "GL13.GL_COMBINE_RGB");
        setup(34162, "GL13.GL_COMBINE_ALPHA");
        setup(34176, "GL13.GL_SOURCE0_RGB");
        setup(34177, "GL13.GL_SOURCE1_RGB");
        setup(34178, "GL13.GL_SOURCE2_RGB");
        setup(34184, "GL13.GL_SOURCE0_ALPHA");
        setup(34185, "GL13.GL_SOURCE1_ALPHA");
        setup(34186, "GL13.GL_SOURCE2_ALPHA");
        setup(34192, "GL13.GL_OPERAND0_RGB");
        setup(34193, "GL13.GL_OPERAND1_RGB");
        setup(34194, "GL13.GL_OPERAND2_RGB");
        setup(34200, "GL13.GL_OPERAND0_ALPHA");
        setup(34201, "GL13.GL_OPERAND1_ALPHA");
        setup(34202, "GL13.GL_OPERAND2_ALPHA");
        setup(34163, "GL13.GL_RGB_SCALE");
        setup(34164, "GL13.GL_ADD_SIGNED");
        setup(34165, "GL13.GL_INTERPOLATE");
        setup(34023, "GL13.GL_SUBTRACT");
        setup(34166, "GL13.GL_CONSTANT");
        setup(34167, "GL13.GL_PRIMARY_COLOR");
        setup(34168, "GL13.GL_PREVIOUS");
        setup(34478, "GL13.GL_DOT3_RGB");
        setup(34479, "GL13.GL_DOT3_RGBA");
        setup(33069, "GL13.GL_CLAMP_TO_BORDER");
        setup(33169, "GL14.GL_GENERATE_MIPMAP");
        setup(33170, "GL14.GL_GENERATE_MIPMAP_HINT");
        setup(33189, "GL14.GL_DEPTH_COMPONENT16");
        setup(33190, "GL14.GL_DEPTH_COMPONENT24");
        setup(33191, "GL14.GL_DEPTH_COMPONENT32");
        setup(34890, "GL14.GL_TEXTURE_DEPTH_SIZE");
        setup(34891, "GL14.GL_DEPTH_TEXTURE_MODE");
        setup(34892, "GL14.GL_TEXTURE_COMPARE_MODE");
        setup(34893, "GL14.GL_TEXTURE_COMPARE_FUNC");
        setup(34894, "GL14.GL_COMPARE_R_TO_TEXTURE");
        setup(33872, "GL14.GL_FOG_COORDINATE_SOURCE");
        setup(33873, "GL14.GL_FOG_COORDINATE");
        setup(33874, "GL14.GL_FRAGMENT_DEPTH");
        setup(33875, "GL14.GL_CURRENT_FOG_COORDINATE");
        setup(33876, "GL14.GL_FOG_COORDINATE_ARRAY_TYPE");
        setup(33877, "GL14.GL_FOG_COORDINATE_ARRAY_STRIDE");
        setup(33878, "GL14.GL_FOG_COORDINATE_ARRAY_POINTER");
        setup(33879, "GL14.GL_FOG_COORDINATE_ARRAY");
        setup(33062, "GL14.GL_POINT_SIZE_MIN");
        setup(33063, "GL14.GL_POINT_SIZE_MAX");
        setup(33064, "GL14.GL_POINT_FADE_THRESHOLD_SIZE");
        setup(33065, "GL14.GL_POINT_DISTANCE_ATTENUATION");
        setup(33880, "GL14.GL_COLOR_SUM");
        setup(33881, "GL14.GL_CURRENT_SECONDARY_COLOR");
        setup(33882, "GL14.GL_SECONDARY_COLOR_ARRAY_SIZE");
        setup(33883, "GL14.GL_SECONDARY_COLOR_ARRAY_TYPE");
        setup(33884, "GL14.GL_SECONDARY_COLOR_ARRAY_STRIDE");
        setup(33885, "GL14.GL_SECONDARY_COLOR_ARRAY_POINTER");
        setup(33886, "GL14.GL_SECONDARY_COLOR_ARRAY");
        setup(32968, "GL14.GL_BLEND_DST_RGB");
        setup(32969, "GL14.GL_BLEND_SRC_RGB");
        setup(32970, "GL14.GL_BLEND_DST_ALPHA");
        setup(32971, "GL14.GL_BLEND_SRC_ALPHA");
        setup(34055, "GL14.GL_INCR_WRAP");
        setup(34056, "GL14.GL_DECR_WRAP");
        setup(34048, "GL14.GL_TEXTURE_FILTER_CONTROL");
        setup(34049, "GL14.GL_TEXTURE_LOD_BIAS");
        setup(34045, "GL14.GL_MAX_TEXTURE_LOD_BIAS");
        setup(33648, "GL14.GL_MIRRORED_REPEAT");
        setup(32773, "ARBImaging.GL_BLEND_COLOR");
        setup(32777, "ARBImaging.GL_BLEND_EQUATION");
        setup(32774, "GL14.GL_FUNC_ADD");
        setup(32778, "GL14.GL_FUNC_SUBTRACT");
        setup(32779, "GL14.GL_FUNC_REVERSE_SUBTRACT");
        setup(32775, "GL14.GL_MIN");
        setup(32776, "GL14.GL_MAX");
        setup(34962, "GL15.GL_ARRAY_BUFFER");
        setup(34963, "GL15.GL_ELEMENT_ARRAY_BUFFER");
        setup(34964, "GL15.GL_ARRAY_BUFFER_BINDING");
        setup(34965, "GL15.GL_ELEMENT_ARRAY_BUFFER_BINDING");
        setup(34966, "GL15.GL_VERTEX_ARRAY_BUFFER_BINDING");
        setup(34967, "GL15.GL_NORMAL_ARRAY_BUFFER_BINDING");
        setup(34968, "GL15.GL_COLOR_ARRAY_BUFFER_BINDING");
        setup(34969, "GL15.GL_INDEX_ARRAY_BUFFER_BINDING");
        setup(34970, "GL15.GL_TEXTURE_COORD_ARRAY_BUFFER_BINDING");
        setup(34971, "GL15.GL_EDGE_FLAG_ARRAY_BUFFER_BINDING");
        setup(34972, "GL15.GL_SECONDARY_COLOR_ARRAY_BUFFER_BINDING");
        setup(34973, "GL15.GL_FOG_COORDINATE_ARRAY_BUFFER_BINDING");
        setup(34974, "GL15.GL_WEIGHT_ARRAY_BUFFER_BINDING");
        setup(34975, "GL15.GL_VERTEX_ATTRIB_ARRAY_BUFFER_BINDING");
        setup(35040, "GL15.GL_STREAM_DRAW");
        setup(35041, "GL15.GL_STREAM_READ");
        setup(35042, "GL15.GL_STREAM_COPY");
        setup(35044, "GL15.GL_STATIC_DRAW");
        setup(35045, "GL15.GL_STATIC_READ");
        setup(35046, "GL15.GL_STATIC_COPY");
        setup(35048, "GL15.GL_DYNAMIC_DRAW");
        setup(35049, "GL15.GL_DYNAMIC_READ");
        setup(35050, "GL15.GL_DYNAMIC_COPY");
        setup(35000, "GL15.GL_READ_ONLY");
        setup(35001, "GL15.GL_WRITE_ONLY");
        setup(35002, "GL15.GL_READ_WRITE");
        setup(34660, "GL15.GL_BUFFER_SIZE");
        setup(34661, "GL15.GL_BUFFER_USAGE");
        setup(35003, "GL15.GL_BUFFER_ACCESS");
        setup(35004, "GL15.GL_BUFFER_MAPPED");
        setup(35005, "GL15.GL_BUFFER_MAP_POINTER");
        setup(34138, "NVFogDistance.GL_FOG_DISTANCE_MODE_NV");
        setup(34139, "NVFogDistance.GL_EYE_RADIAL_NV");
        setup(34140, "NVFogDistance.GL_EYE_PLANE_ABSOLUTE_NV");
        SAVED_STATES = (Map)Maps.newHashMap();
    }
}
