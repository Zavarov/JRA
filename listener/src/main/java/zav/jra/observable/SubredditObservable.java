package zav.jra.observable;

import zav.jra.listener.SubredditListener;
import zav.jra.observer.SubredditObserver;

public class SubredditObservable extends AbstractObservable<SubredditListener, SubredditObserver>{
    @Override
    public void notifyObserver(SubredditObserver observer) {
        observer.notifyAllListener();
    }
}
