package io.anuke.ucore.util;

/**Class for thread-specific utilities. Assumes the application has two threads: logic and graphics.
 * In a single-threaded environment, the one and only thread is both logic and graphics.*/
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

    public static void wait(Object object){
        try{
            object.wait();
        }catch(InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    public static void sleep(long time){
        try{
            Thread.sleep(time);
        }catch(InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    public static void setThreadInfoProvider(ThreadInfoProvider prov){
        info = prov;
    }

    /**Returns whether the logic thread is currently active.
     * In a single-threaded environment, this should always return true.*/
    public static boolean isLogic(){
        return info.isOnLogicThread();
    }

    /**Asserts that calculations are happening on a spcific thread.*/
    public static void assertOn(Thread thread){
        if(Thread.currentThread() != thread) throw new UnsupportedOperationException("This method can only be called on the thread \"" + thread + "\". Current thread: " + Thread.currentThread());
    }

    /**Asserts that a method is being called on the logic thread.*/
    public static void assertLogic(){
        if(!info.isOnLogicThread()) throw new UnsupportedOperationException("This method can only be called on the logic thread. Current thread: " + Thread.currentThread());
    }

    /**Asserts that a method is being called on the graphics thread.*/
    public static void assertGraphics(){
        if(!info.isOnGraphicsThread()) throw new UnsupportedOperationException("This method can only be called on the graphics thread. Current thread: " + Thread.currentThread());
    }

    /**Provides information about the currently running thread.
     * The base implementation always returns true.*/
    public interface ThreadInfoProvider{
        boolean isOnLogicThread();
        boolean isOnGraphicsThread();
    }
}
