package io.anuke.ucore.util;

import com.badlogic.gdx.utils.ObjectSet;

public class ThreadSet<T> extends ObjectSet<T>{
    private ThreadLocal<ThreadSetIterator<T>> threaditer1 = new ThreadLocal<>();
    private ThreadLocal<ThreadSetIterator<T>> threaditer2 = new ThreadLocal<>();

    @Override
    public ObjectSetIterator<T> iterator(){
        ThreadSetIterator<T> iterator1, iterator2;

        if(threaditer1.get() == null){
            threaditer1.set(iterator1 = new ThreadSetIterator<>(this));
        }else{
            iterator1 = threaditer1.get();
        }

        if(threaditer2.get() == null){
            threaditer2.set(iterator2 = new ThreadSetIterator<>(this));
        }else{
            iterator2 = threaditer2.get();
        }

        if(!iterator1.valid){
            iterator1.reset();
            iterator1.valid = true;
            iterator2.valid = false;
            return iterator1;
        }
        iterator2.reset();
        iterator2.valid = true;
        iterator1.valid = false;
        return iterator2;
    }

    static public class ThreadSetIterator<T> extends ObjectSetIterator<T>{
        public boolean valid;

        public ThreadSetIterator(ObjectSet<T> set){
            super(set);
        }
    }
}
