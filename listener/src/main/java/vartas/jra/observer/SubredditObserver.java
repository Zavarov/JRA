package vartas.jra.observer;

import vartas.jra.Subreddit;
import vartas.jra.listener.SubredditListener;
import vartas.jra.requester.LinkRequester;

public class SubredditObserver extends AbstractObserver<SubredditListener>{
    private final Subreddit subreddit;
    private final LinkRequester requester;

    public SubredditObserver(Subreddit subreddit){
        this.subreddit = subreddit;
        this.requester = new LinkRequester(subreddit);
    }

    @Override
    public void notifyListener(SubredditListener listener) {
        checkLinks(listener);
    }

    private void checkLinks(SubredditListener listener){
        requester.peek().forEach(listener::newLink);
        requester.next();
    }
}
