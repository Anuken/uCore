package io.anuke.ucore.core;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import io.anuke.ucore.core.Inputs.Axis;
import io.anuke.ucore.core.Inputs.DeviceType;
import io.anuke.ucore.core.Inputs.InputDevice;
import io.anuke.ucore.core.Inputs.InputType;
import io.anuke.ucore.input.Input;
import io.anuke.ucore.util.Mathf;

public class KeyBinds{
    private static OrderedMap<String, Section> map = new OrderedMap<>();
    private static ObjectMap<String, String> aliases = new ObjectMap<>();

    public static void setSectionAlias(String alias, String section){
        if(!map.containsKey(section)){
            throw new RuntimeException("Section to alias '" + section + "' does not exist!");
        }
        map.remove(alias);
        aliases.put(alias, section);
    }

    /**
     * Format:
     * name, keybind, name2, keybind2...
     * Make sure you define a default for each key you use!
     */
    public static void defaults(Object... keys){
        defaultSection("default", DeviceType.keyboard, keys);
    }

    public static void defaults(DeviceType type, Object... keys){
        defaultSection("default", type, keys);
    }

    public static void defaultSection(String sectionName, DeviceType type, Object... keys){
        if(getSection(sectionName) == null){
            map.put(aliases.get(sectionName, sectionName), new Section(sectionName));
        }
        Section section = getSection(sectionName);

        for(DeviceType other : DeviceType.values()){
            if(!section.defaults.containsKey(other)){
                section.defaults.put(other, new OrderedMap<>());
                section.binds.put(other, new OrderedMap<>());
                section.keybinds.put(other, new Array<>());
            }
        }

        Category lastCategory = null;

        for(int i = 0; i < keys.length; ){
            if(keys[i] instanceof String){
                String key = (String) keys[i];
                Object to = keys[i + 1];

                if(!(to instanceof InputType)){
                    throw new IllegalArgumentException("Invalid keybind format: all keys must be InputTypes!");
                }else{
                    section.defaults.get(type).put(key, (InputType) to);
                    section.keybinds.get(type).add(new Keybind(key, (InputType) to, lastCategory));
                }
                i += 2;
            }else if(keys[i] instanceof Category){
                lastCategory = (Category) keys[i];
                i++;
            }else{
                throw new IllegalArgumentException("Invalid keybind format!");
            }
        }
    }

    public static void save(){
        for(Section sec : map.values()){
            for(DeviceType type : DeviceType.values()){
                for(String name : sec.binds.get(type).keys()){
                    String rname = "keybind-" + sec.name + "-" + type.name() + "-" + name;
                    InputType input = sec.binds.get(type).get(name);
                    save(input, rname);
                }
            }
            Settings.putInt(sec.name + "-last-device-type", Inputs.getDevices().indexOf(sec.device, true));
        }

        Settings.save();
    }

    public static void load(){
        for(Section sec : map.values()){
            for(DeviceType type : DeviceType.values()){

                for(String name : sec.defaults.get(type).keys()){
                    String rname = "keybind-" + sec.name + "-" + type.name() + "-" + name;

                    InputType loaded = load(sec.defaults.get(type).get(name), rname);

                    if(loaded != null){
                        sec.binds.get(type).put(name, loaded);
                    }
                }
            }
            sec.device = Inputs.getDevices().get(Mathf.clamp(Settings.getInt(sec.name + "-last-device-type", 0), 0, Inputs.getDevices().size - 1));
        }
    }


    public static void resetToDefaults(){
        for(Section sec : map.values()){
            for(DeviceType type : DeviceType.values()){
                for(String name : sec.defaults.get(type).keys()){
                    sec.binds.get(type).put(name, sec.defaults.get(type).get(name).copy());
                }
            }
        }
    }

    private static int saveId(Input input){
        return input == Input.UNSET ? -1 : input.ordinal();
    }

    private static void save(InputType type, String name){
        if(type instanceof Input){
            Input input = (Input) type;
            Settings.putInt(name, saveId(input));
        }else if(type instanceof Axis){
            Axis axis = (Axis) type;
            Settings.putInt(name + "-min", saveId(axis.min));
            Settings.putInt(name + "-max", saveId(axis.max));
        }else{
            throw new RuntimeException("Unknown input class type!");
        }
    }

    private static InputType load(InputType def, String name){
        if(def instanceof Input){
            int key = Settings.getInt(name, -1);
            Input input = key == -1 ? null : Input.values()[key];
            return input;
        }else if(def instanceof Axis){
            int min = Settings.getInt(name + "-min", -1);
            int max = Settings.getInt(name + "-max", -1);

            if(min != -1 || max != -1){
                Axis axis = new Axis(Input.values()[min], Input.values()[max]);
                return axis;
            }
            return null;
        }else{
            throw new RuntimeException("Unknown input class type!");
        }
    }

    public static Array<Section> getSections(){
        return map.values().toArray();
    }

    public static Section getSection(String name){
        return map.get(aliases.get(name, name));
    }

    public static InputType get(String section, String name){
        Section s = getSection(section);
        if(s == null)
            throw new IllegalArgumentException("No section \"" + section + "\" found!");
        return get(section, s.device.type, name);
    }

    public static InputType get(String section, DeviceType type, String name){
        Section s = getSection(section);
        if(s == null)
            throw new IllegalArgumentException("No section \"" + section + "\" found!");
        if(!s.defaults.get(type).containsKey(name)){
            if(s.defaults.get(DeviceType.keyboard).containsKey(name)){
                return s.binds.get(DeviceType.keyboard).get(name, s.defaults.get(DeviceType.keyboard).get(name));
            }
            throw new IllegalArgumentException("No keybind \"" + name + "\" found in section \"" + section + "\"");
        }

        return s.binds.get(type).get(name, s.defaults.get(type).get(name));
    }

    public static boolean has(String section, String name){
        Section s = getSection(section);
        if(s == null)
            throw new IllegalArgumentException("No section \"" + section + "\" found!");
        return s.defaults.get(s.device.type).containsKey(name);
    }

    public static InputType get(String name){
        return get("default", name);
    }

    /**
     * A section represents a set of input binds, like controls for a specific player.
     * Each section has a device, which may be a controller or keyboard, and a name (for example, "player2")
     * The default section uses a keyboard.
     */
    public static class Section{
        public ObjectMap<DeviceType, OrderedMap<String, InputType>> binds = new ObjectMap<>();
        public ObjectMap<DeviceType, OrderedMap<String, InputType>> defaults = new ObjectMap<>();
        public ObjectMap<DeviceType, Array<Keybind>> keybinds = new ObjectMap<>();
        public InputDevice device = Inputs.getDevices().first();
        public String name;

        public Section(String name){
            this.name = name;
        }
    }

    public static class Keybind{
        public final String name;
        public final InputType input;
        public final Category category;

        public Keybind(String name, InputType input, Category category){
            this.name = name;
            this.input = input;
            this.category = category;
        }
    }

    /** Represents a keybind category. */
    public static class Category{
        public final String name;

        public Category(String name){
            this.name = name;
        }
    }
}
