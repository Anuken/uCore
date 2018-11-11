package io.anuke.ucore.util;

import io.anuke.ucore.core.Core;

public class Bundles{
    private static StringBuilder build = new StringBuilder();

    public static boolean enabled(){
        return Core.bundle != null;
    }

    public static String get(String key, String normal){
        return enabled() && has(key) ? get(key) : normal;
    }

    public static boolean has(String key){
        return Core.bundle != null && !(Core.bundle.get(key).startsWith("???") && Core.bundle.get(key).endsWith("???"));
    }

    public static String get(String name){
        return Core.bundle == null ? null : Core.bundle.get(name);
    }

    public static String getOrNull(String name){
        return has(name) ? get(name) : null;
    }

    public static String getNotNull(String name){
        String s = get(name);
        if(s == null || (s.endsWith("???") && s.startsWith("???"))){
            throw new NullPointerException("No key with name \"" + name + "\" found!");
        }
        return s;
    }

    public static String format(String key, Object... args){
        if(args.length > 9) throw new IllegalArgumentException("Only up to 9 arguments are allowed.");
        String value = get(key);
        if(value == null || value.startsWith("???")) return value;

        build.setLength(0);

        for(int i = 0; i < value.length(); i++){
           char c = value.charAt(i);
           if(i < value.length() - 2 && c == '{' && Character.isDigit(value.charAt(i + 1)) && value.charAt(i + 2) == '}'){
               int argument = "0123456789".indexOf(value.charAt(i + 1));
               if(args.length > argument){
                   build.append(args[argument]);
                   i += 2;
               }else{
                   build.append(c);
               }
           }else{
               build.append(c);
           }
        }
        return build.toString();
    }

}
