package net.zav.jra.observable;

import net.zav.jra.observer.SubredditObserver;

public class SubredditObservable extends AbstractObservable<SubredditObserver>{
    @Override
    public void notifyObserver(SubredditObserver observer) {
        observer.notifyAllListener();
    }
}
