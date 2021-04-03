package zav.jra.observer;

import zav.jra.Link;
import zav.jra.Subreddit;
import zav.jra.listener.SubredditListener;
import zav.jra.requester.LinkRequester;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Collection;

public class SubredditObserver extends AbstractObserver<SubredditListener>{
    @Nonnull
    private final LinkRequester requester;
    @Nonnull
    private final Subreddit subreddit;
    @Nullable
    private Collection<? extends Link> history;

    public SubredditObserver(@Nonnull Subreddit subreddit){
        this.subreddit = subreddit;
        this.requester = new LinkRequester(subreddit);
    }

    @Override
    public void notifyAllListener() throws IOException{
        history = requester.next(); //History is computed once for all listeners
        super.notifyAllListener();
        history = null;
    }

    @Override
    public void notifyListener(SubredditListener listener) throws IOException {
        //History may be null when a listener is called explicitly instead of via notifyAllListener.
        history = history == null ? requester.next() : history;

        try {
            history.forEach(link -> listener.newLink(subreddit, link));
        } catch(LinkRequester.IteratorException e){
            throw e.getCause();
        }
    }
}
