package io.anuke.ucore.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Bits;
import com.badlogic.gdx.utils.IntFloatMap;
import com.badlogic.gdx.utils.IntSet;
import io.anuke.ucore.core.KeyBinds.Section;
import io.anuke.ucore.input.Input;
import io.anuke.ucore.input.Input.Type;
import io.anuke.ucore.util.Log;
import io.anuke.ucore.util.OS;

public class Inputs{
    private static final float deadzone = 0.3f;
    private static boolean useControllers = true;
    private static boolean[] buttons = new boolean[5];
    private static IntSet keysReleased = new IntSet();
    private static InputMultiplexer plex = new InputMultiplexer();
    private static int scroll = 0;
    private static boolean debug = false;
    private static Array<InputDevice> devices = new Array<>();
    private static InputProcessor listen = new InputAdapter(){
        @Override
        public boolean scrolled(int amount){
            scroll = -amount;
            return false;
        }

        @Override
        public boolean keyUp(int keycode){
            keysReleased.add(keycode);
            return false;
        }
    };

    public static void initialize(){
        plex.addProcessor(listen);

        devices.add(new InputDevice(DeviceType.keyboard, "Keyboard"));

        loadControllers();
    }

    public static void setDebug(boolean debug){
        Inputs.debug = debug;
    }

    public static void useControllers(boolean use){
        useControllers = use;
        if(!use){
            InputDevice keyboard = devices.get(0);
            devices.clear();
            devices.add(keyboard);
        }
    }

    protected static void loadControllers(){
        try{
            int i = 0;
            for(Controller c : Controllers.getControllers()){
                Inputs.getDevices().add(new InputDevice(DeviceType.controller, "Controller " + (++i), c));
            }

            Controllers.addListener(new ControllerAdapter(){
                @Override
                public void connected(Controller controller){
                    if(!useControllers) return;
                    InputDevice device = new InputDevice(DeviceType.controller, "Controller " + Controllers.getControllers().size, controller);
                    Inputs.getDevices().add(device);
                }

                @Override
                public void disconnected(Controller controller){
                    for(InputDevice d : Inputs.getDevices()){
                        if(d.controller == controller){
                            Inputs.getDevices().removeValue(d, true);
                            for(Section s : KeyBinds.getSections()){
                                if(s.device == d){
                                    s.device = devices.get(0);
                                }
                            }
                            break;
                        }
                    }
                }

                @Override
                public boolean axisMoved(Controller controller, int axisIndex, float value){
                    if(Math.abs(value) > 0.3f && debug){
                        Log.info("Axis: {0}, Code: {1}, Value: {2},", Input.findByType(Type.controller, axisIndex, true), axisIndex, value);
                    }
                    InputDevice device = findBy(controller);
                    if(device == null) return false;
                    device.axes.put(axisIndex, value);

                    return false;
                }

                @Override
                public boolean povMoved(Controller controller, int povIndex, PovDirection value){
                    if(debug){
                        Log.info("POV: {0}, Code: {1}, Value: {2}", Input.findByType(Type.controller, povIndex, false), povIndex, value);
                    }
                    return false;
                }

                @Override
                public boolean buttonDown(Controller controller, int buttonCode){
                    if(debug)
                        Log.info("Button: {0}, Code: {1}", Input.findByType(Type.controller, buttonCode, false), buttonCode);

                    InputDevice device = findBy(controller);
                    if(device == null) return false;
                    device.pressed.set(buttonCode);
                    return false;
                }

                @Override
                public boolean buttonUp(Controller controller, int buttonCode){
                    InputDevice device = findBy(controller);
                    if(device == null) return false;
                    device.released.set(buttonCode);
                    return false;
                }
            });

        }catch(Throwable ignored){}
    }

    private static InputDevice findBy(Controller controller){
        for(InputDevice d : devices){
            if(d.controller == controller)
                return d;
        }
        return null;
    }

    public static InputMultiplexer getProcessor(){
        return plex;
    }

    public static Array<InputDevice> getDevices(){
        return devices;
    }

    /** Call this at the end of each render loop. */
    public static void update(){
        for(int i = 0; i < buttons.length; i++){
            buttons[i] = Gdx.input.isButtonPressed(i);
        }
        scroll = 0;
        for(InputDevice device : devices){
            if(device.type == DeviceType.keyboard) continue;
            device.pressed.clear();
            device.released.clear();

            device.lastPOV = device.controller.getPov(0);
        }
        keysReleased.clear();
    }

    public static void clearProcessors(){
        plex.getProcessors().clear();
    }

    public static void removeProcessor(InputProcessor listener){
        plex.removeProcessor(listener);
        Gdx.input.setInputProcessor(plex);
    }

    /** Adds another input processor to the chain. */
    public static void addProcessor(InputProcessor listener){
        plex.addProcessor(listener);
        Gdx.input.setInputProcessor(plex);
    }

    /** Adds another input processor to the chain at a specific index. */
    public static void addProcessor(int index, InputProcessor listener){
        plex.addProcessor(index, listener);
        Gdx.input.setInputProcessor(plex);
    }

    public static void flipProcessors(){
        plex.getProcessors().reverse();
    }

    public static boolean keyDown(String name){
        return keyDown("default", name);
    }

    public static boolean keyTap(String name){
        return keyTap("default", name);
    }

    public static boolean keyRelease(String name){
        return keyRelease("default", name);
    }

    public static boolean keyDown(Input input){
        return keyDown(input, KeyBinds.getSection("default").device);
    }

    public static boolean keyDown(Input input, InputDevice device){
        if(input == Input.UNSET)
            return false;

        if(input.type == Input.Type.controller){
            if(input.axis) return device.controller.getAxis(input.code) > 0f;
            if(input.pov) return device.controller.getPov(input.code) == input.direction;
            return input.code >= 0 && device.controller.getButton(input.code);
        }else if(input.type == Input.Type.key){
            return input.code >= 0 && Gdx.input.isKeyPressed(input.code);
        }else if(input.type == Input.Type.mouse){
            return Gdx.input.isButtonPressed(input.code);
        }
        return false;
    }

    public static boolean keyDown(String section, String name){
        KeyBinds.Section s = KeyBinds.getSection(section);
        if(KeyBinds.has(section, name)){
            Input input = (Input) KeyBinds.get(section, s.device.type, name);
            return keyDown(input, s.device);
        }else{
            Input input = (Input) KeyBinds.get(section, DeviceType.keyboard, name);
            return keyDown(input, getKeyboard());
        }
    }

    public static boolean keyTap(Input input){
        return keyTap(input, KeyBinds.getSection("default").device);
    }

    public static boolean keyTap(Input input, InputDevice device){
        if(input == Input.UNSET)
            return false;

        if(input.type == Input.Type.controller){
            if(input.axis) return device.controller.getAxis(input.code) > 0f && device.axes.get(input.code, 0) < 0;
            if(input.pov)
                return device.controller.getPov(input.code) == input.direction && device.lastPOV != input.direction;
            return input.code >= 0 && device.pressed.get(input.code);
        }else if(input.type == Input.Type.key){
            return Gdx.input.isKeyJustPressed(input.code);
        }else if(input.type == Input.Type.mouse){
            return Gdx.input.isButtonPressed(input.code) && !buttons[input.code];
        }
        return false;
    }

    public static boolean keyTap(String section, String name){
        KeyBinds.Section s = KeyBinds.getSection(section);
        if(KeyBinds.has(section, name)){
            Input input = (Input) KeyBinds.get(section, name);
            return keyTap(input, s.device);
        }else{
            Input input = (Input) KeyBinds.get(section, DeviceType.keyboard, name);
            return keyTap(input, getKeyboard());
        }
    }

    public static boolean keyRelease(Input input){
        return keyRelease(input, KeyBinds.getSection("default").device);
    }

    public static boolean keyRelease(Input input, InputDevice device){
        if(input == Input.UNSET)
            return false;

        if(input.type == Input.Type.controller){
            if(input.pov)
                return device.controller.getPov(input.code) != input.direction && device.lastPOV == input.direction;
            return input.code >= 0 && device.released.get(input.code);
        }else if(input.type == Input.Type.key){
            return keysReleased.contains(input.code);
        }else if(input.type == Input.Type.mouse){
            return !Gdx.input.isButtonPressed(input.code) && buttons[input.code];
        }
        return false;
    }

    public static boolean keyRelease(String section, String name){
        KeyBinds.Section s = KeyBinds.getSection(section);
        Input input = (Input) KeyBinds.get(section, name);
        if(KeyBinds.has(section, name)){
            return keyRelease(input, s.device);
        }else{
            return keyRelease(input, getKeyboard());
        }
    }

    public static boolean getAxisActive(String axis){
        return Math.abs(getAxis("default", axis)) > 0;
    }

    public static float getAxis(String axis){
        return getAxis("default", axis);
    }

    public static float getAxis(String section, String name){
        KeyBinds.Section s = KeyBinds.getSection(section);
        Axis axis = (Axis) KeyBinds.get(section, name);

        if(s.device.type == DeviceType.controller){
            Controller c = s.device.controller;

            if(axis.min.axis){
                float value = c.getAxis(axis.min.code) * (axis.min.name().contains("VERTICAL") && !OS.isWindows ? -1 : 1);
                return Math.abs(value) < deadzone ? 0f : value;
            }else{
                boolean min = axis.min.pov ? c.getPov(0) == axis.min.direction : c.getButton(axis.min.code),
                        max = axis.max.pov ? c.getPov(0) == axis.max.direction : c.getButton(axis.max.code);
                return (min && max) || (!min && !max) ? 0 : min ? -1 : 1;
            }
        }else{
            if(axis.min == Input.SCROLL){
                return scroll();
            }else{
                boolean min = keyDown(axis.min, s.device), max = keyDown(axis.max, s.device);
                return (min && max) || (!min && !max) ? 0 : min ? -1 : 1;
            }
        }
    }

    public static float getAxisTapped(String axis){
        return getAxisTapped("default", axis);
    }

    public static float getAxisTapped(String section, String name){
        KeyBinds.Section s = KeyBinds.getSection(section);
        Axis axis = (Axis) KeyBinds.get(section, name);

        if(s.device.type == DeviceType.controller){
            Controller c = s.device.controller;

            if(axis.min.axis){
                return c.getAxis(axis.min.code) * (axis.min.name().contains("VERTICAL") && !OS.isWindows ? -1 : 1);
            }else{
                boolean min = axis.min.pov ? c.getPov(0) == axis.min.direction : c.getButton(axis.min.code),
                        max = axis.max.pov ? c.getPov(0) == axis.max.direction : c.getButton(axis.max.code);
                return (min && max) || (!min && !max) ? 0 : min ? -1 : 1;
            }
        }else{
            if(axis.min == Input.SCROLL){
                return scroll();
            }else{
                boolean min = keyTap(axis.min, s.device), max = keyTap(axis.max, s.device);
                return (min && max) || (!min && !max) ? 0 : min ? -1 : 1;
            }
        }
    }

    public static InputDevice getKeyboard(){
        return devices.get(0);
    }

    public static boolean buttonDown(int button){
        return Gdx.input.isButtonPressed(button);
    }

    public static boolean buttonUp(int button){
        return Gdx.input.isButtonPressed(button) && !buttons[button];
    }

    public static boolean buttonRelease(int button){
        return !Gdx.input.isButtonPressed(button) && buttons[button];
    }

    public static int scroll(){
        return scroll;
    }

    public static boolean scrolled(){
        return Math.abs(scroll) > 0;
    }

    static void dispose(){
        plex.getProcessors().clear();
    }

    public static ControllerType getControllerType(Controller controller){
        String check = controller.getName().toLowerCase();
        if(check.contains("x-box") || check.contains("xbox")){
            return ControllerType.xbox;
        }else{
            return ControllerType.unknown;
        }
    }

    public enum DeviceType{
        keyboard, controller
    }

    //TODO 2D axes, like controller sticks, as having two different axes is confusing.
    //TODO don't treat triggers as axes?

    public enum ControllerType{
        xbox, unknown
    }

    public interface InputType{
        InputType copy();
    }

    /** Represents either a keyboard or controller. */
    public static class InputDevice{
        public static final int BUTTONS = 256;

        public final DeviceType type;
        public final String name;
        public final Controller controller;
        public final ControllerType controllerType;
        public final Bits pressed = new Bits();
        public final Bits released = new Bits();
        public final IntFloatMap axes = new IntFloatMap();
        public PovDirection lastPOV = PovDirection.center;

        public InputDevice(DeviceType type, String name){
            this(type, name, null);
        }

        public InputDevice(DeviceType type, String name, Controller controller){
            this.type = type;
            this.name = name;
            this.controller = controller;
            if(controller != null){
                controllerType = getControllerType(controller);
            }else{
                controllerType = null;
            }
        }
    }

    /**
     * Represents a 1-dimensional input axis. If a button already has an axis, such as the scrollwheel or directional
     * stick on the controller, only the 'min' input is used.
     */
    public static class Axis implements InputType{
        public Input min, max;

        public Axis(){
        }

        /** Cosntructor for axes only. */
        public Axis(Input axis){
            min = max = axis;
        }

        /** Constructor for keyboards, or multiple buttons on a controller. */
        public Axis(Input min, Input max){
            this.min = min;
            this.max = max;
        }

        @Override
        public InputType copy(){
            return new Axis(min, max);
        }
    }
}
