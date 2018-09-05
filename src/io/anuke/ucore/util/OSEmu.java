package io.anuke.ucore.util;

/**Class for getting system properties. Do not use directly.
 * Only for GWT emulation.*/
class OSEmu {

    public static String getProperty(String name){
        return System.getProperty(name);
    }

    public static String getEnv(String name){
        return System.getenv(name);
    }
}
