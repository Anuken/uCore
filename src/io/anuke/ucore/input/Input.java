package io.anuke.ucore.input;

import com.badlogic.gdx.controllers.PovDirection;
import io.anuke.ucore.core.Inputs.InputType;

/** Enum for storing input codes of mouse, keyboard and controllers at once. */
public enum Input implements InputType{
    CONTROLLER_A(Type.controller, XboxBinds.A, "A"),
    CONTROLLER_B(Type.controller, XboxBinds.B, "B"),
    CONTROLLER_X(Type.controller, XboxBinds.X, "X"),
    CONTROLLER_Y(Type.controller, XboxBinds.Y, "Y"),
    CONTROLLER_GUIDE(Type.controller, XboxBinds.GUIDE, "Guide"),
    CONTROLLER_L_BUMPER(Type.controller, XboxBinds.L_BUMPER, "L Bumper"),
    CONTROLLER_R_BUMPER(Type.controller, XboxBinds.R_BUMPER, "R Bumper"),
    CONTROLLER_BACK(Type.controller, XboxBinds.BACK, "Back"),
    CONTROLLER_START(Type.controller, XboxBinds.START, "Start"),
    CONTROLLER_L_STICK(Type.controller, XboxBinds.L_STICK, "L Stick"),
    CONTROLLER_R_STICK(Type.controller, XboxBinds.R_STICK, "R Stick"),
    //pov?
    CONTROLLER_DPAD_UP(XboxBinds.DPAD_UP, PovDirection.north, "D-Pad Up"),
    CONTROLLER_DPAD_DOWN(XboxBinds.DPAD_DOWN, PovDirection.south, "D-Pad Down"),
    CONTROLLER_DPAD_LEFT(XboxBinds.DPAD_LEFT, PovDirection.west, "D-Pad Left"),
    CONTROLLER_DPAD_RIGHT(XboxBinds.DPAD_RIGHT, PovDirection.east, "D-Pad Right"),
    //controller axes
    CONTROLLER_L_TRIGGER(Type.controller, XboxBinds.L_TRIGGER, "L Trigger", true),
    CONTROLLER_R_TRIGGER(Type.controller, XboxBinds.R_TRIGGER, "R Trigger", true),
    CONTROLLER_L_STICK_VERTICAL_AXIS(Type.controller, XboxBinds.L_STICK_VERTICAL_AXIS, "L Stick Y Axis", true),
    CONTROLLER_L_STICK_HORIZONTAL_AXIS(Type.controller, XboxBinds.L_STICK_HORIZONTAL_AXIS, "L Stick X Axis", true),
    CONTROLLER_R_STICK_VERTICAL_AXIS(Type.controller, XboxBinds.R_STICK_VERTICAL_AXIS, "R Stick Y Axis", true),
    CONTROLLER_R_STICK_HORIZONTAL_AXIS(Type.controller, XboxBinds.R_STICK_HORIZONTAL_AXIS, "R Stick X Axis", true),
    //mouse
    MOUSE_LEFT(Type.mouse, 0, "Mouse Left"),
    MOUSE_RIGHT(Type.mouse, 1, "Mouse Right"),
    MOUSE_MIDDLE(Type.mouse, 2, "Mouse Middle"),
    MOUSE_BACK(Type.mouse, 3, "Mouse Back"),
    MOUSE_FORWARD(Type.mouse, 4, "Mouse Forward"),
    //scroll
    SCROLL(Type.scroll, 0, "Scrollwheel", true),
    //keyboard
    ANY_KEY(Type.key, -1, "Any Key"),

    NUM_0(7, "0"),
    NUM_1(8, "1"),
    NUM_2(9, "2"),
    NUM_3(10, "3"),
    NUM_4(11, "4"),
    NUM_5(12, "5"),
    NUM_6(13, "6"),
    NUM_7(14, "7"),
    NUM_8(15, "8"),
    NUM_9(16, "9"),
    A(29, "A"),
    ALT_LEFT(57, "L-Alt"),
    ALT_RIGHT(58, "R-Alt"),
    APOSTROPHE(75, "'"),
    AT(77, "@"),
    B(30, "B"),
    BACK(4, "Back"),
    BACKSLASH(73, "\\"),
    C(31, "C"),
    CALL(5, "Call"),
    CAMERA(27, "Camera"),
    CLEAR(28, "Clear"),
    COMMA(55, ","),
    D(32, "D"),
    DEL(67, "Delete"),
    BACKSPACE(67, "Delete"),
    FORWARD_DEL(112, "Forward Delete"),
    DPAD_CENTER(23, "Center"),
    DPAD_DOWN(20, "Down"),
    DPAD_LEFT(21, "Left"),
    DPAD_RIGHT(22, "Right"),
    DPAD_UP(19, "Up"),
    CENTER(23, "Center"),
    DOWN(20, "Down"),
    LEFT(21, "Left"),
    RIGHT(22, "Right"),
    UP(19, "Up"),
    E(33, "E"),
    ENDCALL(6, "End Call"),
    ENTER(66, "Enter"),
    ENVELOPE(65, "Envelope"),
    EQUALS(70, "="),
    EXPLORER(64, "Explorer"),
    F(34, "F"),
    FOCUS(80, "Focus"),
    G(35, "G"),
    GRAVE(68, "`"),
    H(36, "H"),
    HEADSETHOOK(79, "Headset Hook"),
    HOME(3, "Home"),
    I(37, "I"),
    J(38, "J"),
    K(39, "K"),
    L(40, "L"),
    LEFT_BRACKET(71, "["),
    M(41, "M"),
    MEDIA_FAST_FORWARD(90, "Fast Forward"),
    MEDIA_NEXT(87, "Next Media"),
    MEDIA_PLAY_PAUSE(85, "Play/Pause"),
    MEDIA_PREVIOUS(88, "Prev Media"),
    MEDIA_REWIND(89, "Rewind"),
    MEDIA_STOP(86, "Stop Media"),
    MENU(82, "Menu"),
    MINUS(69, "-"),
    MUTE(91, "Mute"),
    N(42, "N"),
    NOTIFICATION(83, "Notification"),
    NUM(78, "Num"),
    O(43, "O"),
    P(44, "P"),
    PERIOD(56, "."),
    PLUS(81, "Plus"),
    POUND(18, "#"),
    POWER(26, "Power"),
    Q(45, "Q"),
    R(46, "R"),
    RIGHT_BRACKET(72, "]"),
    S(47, "S"),
    SEARCH(84, "Search"),
    SEMICOLON(74, ";"),
    SHIFT_LEFT(59, "L-Shift"),
    SHIFT_RIGHT(60, "R-Shift"),
    SLASH(76, "/"),
    SOFT_LEFT(1, "Soft Left"),
    SOFT_RIGHT(2, "Soft Right"),
    SPACE(62, "Space"),
    STAR(17, "*"),
    SYM(63, "SYM"),
    T(48, "T"),
    TAB(61, "Tab"),
    U(49, "U"),
    UNKNOWN(0, "Unknown"),
    V(50, "V"),
    VOLUME_DOWN(25, "Volume Down"),
    VOLUME_UP(24, "Volume Up"),
    W(51, "W"),
    X(52, "X"),
    Y(53, "Y"),
    Z(54, "Z"),
    META_ALT_LEFT_ON(16, "9"),
    META_ALT_ON(2, "Soft Right"),
    META_ALT_RIGHT_ON(32, "D"),
    META_SHIFT_LEFT_ON(64, "Explorer"),
    META_SHIFT_ON(1, "Soft Left"),
    META_SHIFT_RIGHT_ON(128, "null"),
    META_SYM_ON(4, "Back"),
    CONTROL_LEFT(129, "L-Ctrl"),
    CONTROL_RIGHT(130, "R-Ctrl"),
    ESCAPE(131, "Escape"),
    END(132, "End"),
    INSERT(133, "Insert"),
    PAGE_UP(92, "Page Up"),
    PAGE_DOWN(93, "Page Down"),
    PICTSYMBOLS(94, "PICTSYMBOLS"),
    SWITCH_CHARSET(95, "SWITCH_CHARSET"),
    BUTTON_CIRCLE(255, "F12"),
    BUTTON_A(96, "A Button"),
    BUTTON_B(97, "B Button"),
    BUTTON_C(98, "C Button"),
    BUTTON_X(99, "X Button"),
    BUTTON_Y(100, "Y Button"),
    BUTTON_Z(101, "Z Button"),
    BUTTON_L1(102, "L1 Button"),
    BUTTON_R1(103, "R1 Button"),
    BUTTON_L2(104, "L2 Button"),
    BUTTON_R2(105, "R2 Button"),
    BUTTON_THUMBL(106, "Left Thumb"),
    BUTTON_THUMBR(107, "Right Thumb"),
    BUTTON_START(108, "Start"),
    BUTTON_SELECT(109, "Select"),
    BUTTON_MODE(110, "Button Mode"),
    NUMPAD_0(144, "Numpad 0"),
    NUMPAD_1(145, "Numpad 1"),
    NUMPAD_2(146, "Numpad 2"),
    NUMPAD_3(147, "Numpad 3"),
    NUMPAD_4(148, "Numpad 4"),
    NUMPAD_5(149, "Numpad 5"),
    NUMPAD_6(150, "Numpad 6"),
    NUMPAD_7(151, "Numpad 7"),
    NUMPAD_8(152, "Numpad 8"),
    NUMPAD_9(153, "Numpad 9"),
    COLON(243, ":"),
    F1(244, "F1"),
    F2(245, "F2"),
    F3(246, "F3"),
    F4(247, "F4"),
    F5(248, "F5"),
    F6(249, "F6"),
    F7(250, "F7"),
    F8(251, "F8"),
    F9(252, "F9"),
    F10(253, "F10"),
    F11(254, "F11"),
    F12(255, "F12"),
    UNSET(Type.key, 0, "Unset");

    public final int code;
    public final Type type;
    public final String value;
    public final boolean axis;
    public final boolean pov;
    public final PovDirection direction;

    /** desktop keycode */
    Input(int keycode, String value){
        type = Type.key;
        this.value = value;
        code = keycode;
        axis = false;
        pov = false;
        direction = null;
    }

    Input(Type type, int keycode, String value){
        this(type, keycode, value, false);
    }

    Input(Type type, int keycode, String value, boolean axis){
        this.code = keycode;
        this.type = type;
        this.value = value;
        this.axis = axis;
        pov = false;
        direction = null;
    }

    Input(int keycode, PovDirection direction, String value){
        this.code = keycode;
        this.type = Type.controller;
        this.value = value;
        this.axis = false;
        this.pov = true;
        this.direction = direction;
    }

    public static Input findByType(Type type, int code, boolean axis){
        for(Input i : values()){
            if(i.type == type && i.code == code && i.axis == axis){
                return i;
            }
        }
        return Input.UNKNOWN;
    }

    public static Input findPOV(PovDirection direction){
        for(Input i : values()){
            if(i.type == Type.controller && i.direction == direction && i.pov){
                return i;
            }
        }
        return Input.UNKNOWN;
    }

    @Override
    public String toString(){
        return value;
    }

    @Override
    public InputType copy(){
        return this;
    }

    public enum Type{
        key, mouse, controller, scroll
    }
}
