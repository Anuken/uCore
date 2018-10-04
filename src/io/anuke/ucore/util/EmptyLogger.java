package io.anuke.ucore.util;

import com.badlogic.gdx.ApplicationLogger;

public class EmptyLogger implements ApplicationLogger{
    @Override public void log(String tag, String message){}
    @Override public void log(String tag, String message, Throwable exception){}
    @Override public void error(String tag, String message){}
    @Override public void error(String tag, String message, Throwable exception){}
    @Override public void debug(String tag, String message){}
    @Override public void debug(String tag, String message, Throwable exception){}
}
