package zav.jra.observable;

import zav.jra.listener.SubredditListener;
import zav.jra.observer.SubredditObserver;

public class SubredditObservable <T extends SubredditListener, Q extends SubredditObserver<T>> extends AbstractObservable<T, Q>{
}
