package io.anuke.ucore.threading;

public class Threads{
    private static ThreadInfoProvider info = new ThreadInfoProvider(){
        @Override
        public boolean isOnLogicThread(){
            return true;
        }

        @Override
        public boolean isOnGraphicsThread(){
            return true;
        }
    };

    public static void setThreadInfoProvider(ThreadInfoProvider prov){
        info = prov;
    }

    public static boolean isLogic(){
        return info.isOnLogicThread();
    }

    /**Asserts that a method is being called on the logic thread.*/
    public static void assertLogic(){
        if(!info.isOnLogicThread()) throw new UnsupportedOperationException("This method can only be called on the logic thread.");
    }

    /**Asserts that a method is being called on the graphics thread.*/
    public static void assertGraphics(){
        if(!info.isOnGraphicsThread()) throw new UnsupportedOperationException("This method can only be called on the graphics thread.");
    }

    public interface ThreadInfoProvider{
        boolean isOnLogicThread();
        boolean isOnGraphicsThread();
    }
}
