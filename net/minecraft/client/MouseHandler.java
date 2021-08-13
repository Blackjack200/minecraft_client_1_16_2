package net.minecraft.client;

import java.util.Arrays;
import java.nio.file.Paths;
import org.lwjgl.glfw.GLFWDropCallback;
import net.minecraft.client.gui.components.events.GuiEventListener;
import java.nio.file.Path;
import java.util.List;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.screens.Screen;
import com.mojang.blaze3d.Blaze3D;
import net.minecraft.util.SmoothDouble;

public class MouseHandler {
    private final Minecraft minecraft;
    private boolean isLeftPressed;
    private boolean isMiddlePressed;
    private boolean isRightPressed;
    private double xpos;
    private double ypos;
    private int fakeRightMouse;
    private int activeButton;
    private boolean ignoreFirstMove;
    private int clickDepth;
    private double mousePressedTime;
    private final SmoothDouble smoothTurnX;
    private final SmoothDouble smoothTurnY;
    private double accumulatedDX;
    private double accumulatedDY;
    private double accumulatedScroll;
    private double lastMouseEventTime;
    private boolean mouseGrabbed;
    
    public MouseHandler(final Minecraft djw) {
        this.activeButton = -1;
        this.ignoreFirstMove = true;
        this.smoothTurnX = new SmoothDouble();
        this.smoothTurnY = new SmoothDouble();
        this.lastMouseEventTime = Double.MIN_VALUE;
        this.minecraft = djw;
    }
    
    private void onPress(final long long1, int integer2, final int integer3, final int integer4) {
        if (long1 != this.minecraft.getWindow().getWindow()) {
            return;
        }
        final boolean boolean7 = integer3 == 1;
        if (Minecraft.ON_OSX && integer2 == 0) {
            if (boolean7) {
                if ((integer4 & 0x2) == 0x2) {
                    integer2 = 1;
                    ++this.fakeRightMouse;
                }
            }
            else if (this.fakeRightMouse > 0) {
                integer2 = 1;
                --this.fakeRightMouse;
            }
        }
        final int integer5 = integer2;
        if (boolean7) {
            if (this.minecraft.options.touchscreen && this.clickDepth++ > 0) {
                return;
            }
            this.activeButton = integer5;
            this.mousePressedTime = Blaze3D.getTime();
        }
        else if (this.activeButton != -1) {
            if (this.minecraft.options.touchscreen && --this.clickDepth > 0) {
                return;
            }
            this.activeButton = -1;
        }
        final boolean[] arr9 = { false };
        if (this.minecraft.overlay == null) {
            if (this.minecraft.screen == null) {
                if (!this.mouseGrabbed && boolean7) {
                    this.grabMouse();
                }
            }
            else {
                final double double10 = this.xpos * this.minecraft.getWindow().getGuiScaledWidth() / this.minecraft.getWindow().getScreenWidth();
                final double double11 = this.ypos * this.minecraft.getWindow().getGuiScaledHeight() / this.minecraft.getWindow().getScreenHeight();
                if (boolean7) {
                    Screen.wrapScreenError(() -> arr9[0] = this.minecraft.screen.mouseClicked(double10, double11, integer5), "mouseClicked event handler", this.minecraft.screen.getClass().getCanonicalName());
                }
                else {
                    Screen.wrapScreenError(() -> arr9[0] = this.minecraft.screen.mouseReleased(double10, double11, integer5), "mouseReleased event handler", this.minecraft.screen.getClass().getCanonicalName());
                }
            }
        }
        if (!arr9[0] && (this.minecraft.screen == null || this.minecraft.screen.passEvents) && this.minecraft.overlay == null) {
            if (integer5 == 0) {
                this.isLeftPressed = boolean7;
            }
            else if (integer5 == 2) {
                this.isMiddlePressed = boolean7;
            }
            else if (integer5 == 1) {
                this.isRightPressed = boolean7;
            }
            KeyMapping.set(InputConstants.Type.MOUSE.getOrCreate(integer5), boolean7);
            if (boolean7) {
                if (this.minecraft.player.isSpectator() && integer5 == 2) {
                    this.minecraft.gui.getSpectatorGui().onMouseMiddleClick();
                }
                else {
                    KeyMapping.click(InputConstants.Type.MOUSE.getOrCreate(integer5));
                }
            }
        }
    }
    
    private void onScroll(final long long1, final double double2, final double double3) {
        if (long1 == Minecraft.getInstance().getWindow().getWindow()) {
            final double double4 = (this.minecraft.options.discreteMouseScroll ? Math.signum(double3) : double3) * this.minecraft.options.mouseWheelSensitivity;
            if (this.minecraft.overlay == null) {
                if (this.minecraft.screen != null) {
                    final double double5 = this.xpos * this.minecraft.getWindow().getGuiScaledWidth() / this.minecraft.getWindow().getScreenWidth();
                    final double double6 = this.ypos * this.minecraft.getWindow().getGuiScaledHeight() / this.minecraft.getWindow().getScreenHeight();
                    this.minecraft.screen.mouseScrolled(double5, double6, double4);
                }
                else if (this.minecraft.player != null) {
                    if (this.accumulatedScroll != 0.0 && Math.signum(double4) != Math.signum(this.accumulatedScroll)) {
                        this.accumulatedScroll = 0.0;
                    }
                    this.accumulatedScroll += double4;
                    final float float10 = (float)(int)this.accumulatedScroll;
                    if (float10 == 0.0f) {
                        return;
                    }
                    this.accumulatedScroll -= float10;
                    if (this.minecraft.player.isSpectator()) {
                        if (this.minecraft.gui.getSpectatorGui().isMenuActive()) {
                            this.minecraft.gui.getSpectatorGui().onMouseScrolled(-float10);
                        }
                        else {
                            final float float11 = Mth.clamp(this.minecraft.player.abilities.getFlyingSpeed() + float10 * 0.005f, 0.0f, 0.2f);
                            this.minecraft.player.abilities.setFlyingSpeed(float11);
                        }
                    }
                    else {
                        this.minecraft.player.inventory.swapPaint(float10);
                    }
                }
            }
        }
    }
    
    private void onDrop(final long long1, final List<Path> list) {
        if (this.minecraft.screen != null) {
            this.minecraft.screen.onFilesDrop(list);
        }
    }
    
    public void setup(final long long1) {
        InputConstants.setupMouseCallbacks(long1, (long1, double2, double3) -> this.minecraft.execute(() -> this.onMove(long1, double2, double3)), (long1, integer2, integer3, integer4) -> this.minecraft.execute(() -> this.onPress(long1, integer2, integer3, integer4)), (long1, double2, double3) -> this.minecraft.execute(() -> this.onScroll(long1, double2, double3)), (long1, integer, long3) -> {
            final Path[] arr7 = new Path[integer];
            for (int integer2 = 0; integer2 < integer; ++integer2) {
                arr7[integer2] = Paths.get(GLFWDropCallback.getName(long3, integer2), new String[0]);
            }
            this.minecraft.execute(() -> this.onDrop(long1, (List<Path>)Arrays.asList((Object[])arr7)));
        });
    }
    
    private void onMove(final long long1, final double double2, final double double3) {
        if (long1 != Minecraft.getInstance().getWindow().getWindow()) {
            return;
        }
        if (this.ignoreFirstMove) {
            this.xpos = double2;
            this.ypos = double3;
            this.ignoreFirstMove = false;
        }
        final GuiEventListener dmf8 = this.minecraft.screen;
        if (dmf8 != null && this.minecraft.overlay == null) {
            final double double4 = double2 * this.minecraft.getWindow().getGuiScaledWidth() / this.minecraft.getWindow().getScreenWidth();
            final double double5 = double3 * this.minecraft.getWindow().getGuiScaledHeight() / this.minecraft.getWindow().getScreenHeight();
            Screen.wrapScreenError(() -> dmf8.mouseMoved(double4, double5), "mouseMoved event handler", dmf8.getClass().getCanonicalName());
            if (this.activeButton != -1 && this.mousePressedTime > 0.0) {
                final double double6 = (double2 - this.xpos) * this.minecraft.getWindow().getGuiScaledWidth() / this.minecraft.getWindow().getScreenWidth();
                final double double7 = (double3 - this.ypos) * this.minecraft.getWindow().getGuiScaledHeight() / this.minecraft.getWindow().getScreenHeight();
                Screen.wrapScreenError(() -> dmf8.mouseDragged(double4, double5, this.activeButton, double6, double7), "mouseDragged event handler", dmf8.getClass().getCanonicalName());
            }
        }
        this.minecraft.getProfiler().push("mouse");
        if (this.isMouseGrabbed() && this.minecraft.isWindowActive()) {
            this.accumulatedDX += double2 - this.xpos;
            this.accumulatedDY += double3 - this.ypos;
        }
        this.turnPlayer();
        this.xpos = double2;
        this.ypos = double3;
        this.minecraft.getProfiler().pop();
    }
    
    public void turnPlayer() {
        final double double2 = Blaze3D.getTime();
        final double double3 = double2 - this.lastMouseEventTime;
        this.lastMouseEventTime = double2;
        if (!this.isMouseGrabbed() || !this.minecraft.isWindowActive()) {
            this.accumulatedDX = 0.0;
            this.accumulatedDY = 0.0;
            return;
        }
        final double double4 = this.minecraft.options.sensitivity * 0.6000000238418579 + 0.20000000298023224;
        final double double5 = double4 * double4 * double4 * 8.0;
        double double8;
        double double9;
        if (this.minecraft.options.smoothCamera) {
            final double double6 = this.smoothTurnX.getNewDeltaValue(this.accumulatedDX * double5, double3 * double5);
            final double double7 = this.smoothTurnY.getNewDeltaValue(this.accumulatedDY * double5, double3 * double5);
            double8 = double6;
            double9 = double7;
        }
        else {
            this.smoothTurnX.reset();
            this.smoothTurnY.reset();
            double8 = this.accumulatedDX * double5;
            double9 = this.accumulatedDY * double5;
        }
        this.accumulatedDX = 0.0;
        this.accumulatedDY = 0.0;
        int integer14 = 1;
        if (this.minecraft.options.invertYMouse) {
            integer14 = -1;
        }
        this.minecraft.getTutorial().onMouse(double8, double9);
        if (this.minecraft.player != null) {
            this.minecraft.player.turn(double8, double9 * integer14);
        }
    }
    
    public boolean isLeftPressed() {
        return this.isLeftPressed;
    }
    
    public boolean isRightPressed() {
        return this.isRightPressed;
    }
    
    public double xpos() {
        return this.xpos;
    }
    
    public double ypos() {
        return this.ypos;
    }
    
    public void setIgnoreFirstMove() {
        this.ignoreFirstMove = true;
    }
    
    public boolean isMouseGrabbed() {
        return this.mouseGrabbed;
    }
    
    public void grabMouse() {
        if (!this.minecraft.isWindowActive()) {
            return;
        }
        if (this.mouseGrabbed) {
            return;
        }
        if (!Minecraft.ON_OSX) {
            KeyMapping.setAll();
        }
        this.mouseGrabbed = true;
        this.xpos = this.minecraft.getWindow().getScreenWidth() / 2;
        this.ypos = this.minecraft.getWindow().getScreenHeight() / 2;
        InputConstants.grabOrReleaseMouse(this.minecraft.getWindow().getWindow(), 212995, this.xpos, this.ypos);
        this.minecraft.setScreen(null);
        this.minecraft.missTime = 10000;
        this.ignoreFirstMove = true;
    }
    
    public void releaseMouse() {
        if (!this.mouseGrabbed) {
            return;
        }
        this.mouseGrabbed = false;
        this.xpos = this.minecraft.getWindow().getScreenWidth() / 2;
        this.ypos = this.minecraft.getWindow().getScreenHeight() / 2;
        InputConstants.grabOrReleaseMouse(this.minecraft.getWindow().getWindow(), 212993, this.xpos, this.ypos);
    }
    
    public void cursorEntered() {
        this.ignoreFirstMove = true;
    }
}
