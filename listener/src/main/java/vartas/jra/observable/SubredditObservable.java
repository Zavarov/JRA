package vartas.jra.observable;

import vartas.jra.observer.SubredditObserver;

public class SubredditObservable extends AbstractObservable<SubredditObserver>{
    @Override
    public void notifyObserver(SubredditObserver observer) {
        observer.notifyAllListener();
    }
}
