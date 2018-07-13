package java.lang;

public class ThreadLocal<T>{
    private T value;

    public T get(){
        return value;
    }

    public void set(T value){
        this.value = value;
    }

    public void remove(){
        value = null;
    }
}
