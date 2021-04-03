package zav.jra.observable;

import zav.jra.observer.Observer;

import java.io.IOException;
import java.util.EventListener;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class AbstractObservable <Q extends EventListener, T extends Observer<Q>> implements Observable<Q, T>{
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
    public void notifyAllObservers() throws  IOException{
        for(T observer : observers){
            this.notifyObserver(observer);
        }
    }

    @Override
    public void notifyObserver(T observer) throws IOException {
        observer.notifyAllListener();
    }

    @Override
    public int size(){
        return observers.size();
    }
}
