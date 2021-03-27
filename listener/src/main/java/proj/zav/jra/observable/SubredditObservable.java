package proj.zav.jra.observable;

import proj.zav.jra.listener.SubredditListener;
import proj.zav.jra.observer.SubredditObserver;

public class SubredditObservable extends AbstractObservable<SubredditListener, SubredditObserver>{
    @Override
    public void notifyObserver(SubredditObserver observer) {
        observer.notifyAllListener();
    }
}
