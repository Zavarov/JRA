package zav.jra.observer;

import java.util.EventListener;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class AbstractObserver <T extends EventListener> implements Observer<T>{
    private final Set<T> listeners = new CopyOnWriteArraySet<>();

    @Override
    public boolean addListener(T listener) {
        return listeners.add(listener);
    }

    @Override
    public boolean removeListener(T listener) {
        return listeners.remove(listener);
    }

    @Override
    public void notifyAllListener() {
        listeners.forEach(this::notifyListener);
    }

    @Override
    public int size() {
        return listeners.size();
    }
}
