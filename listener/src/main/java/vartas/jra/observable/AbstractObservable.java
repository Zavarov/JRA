package vartas.jra.observable;

import vartas.jra.observer.Observer;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class AbstractObservable <T extends Observer> implements Observable<T>{
    private final Set<T> observers = new CopyOnWriteArraySet<>();

    @Override
    public boolean addObserver(T observer) {
        return observers.add(observer);
    }

    @Override
    public boolean removeObserver(T observer) {
        return observers.remove(observer);
    }

    @Override
    public void notifyAllObservers() {
        observers.forEach(this::notifyObserver);
    }
}
