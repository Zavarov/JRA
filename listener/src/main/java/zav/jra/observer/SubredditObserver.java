package zav.jra.observer;

import zav.jra.Link;
import zav.jra.Subreddit;
import zav.jra.listener.SubredditListener;
import zav.jra.requester.LinkRequester;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class SubredditObserver <T extends SubredditListener> extends AbstractObserver<T>{
    @Nonnull
    private final LinkRequester requester;
    @Nonnull
    protected final Subreddit subreddit;
    @Nullable
    private List<? extends Link> history;

    public SubredditObserver(@Nonnull Subreddit subreddit){
        this.subreddit = subreddit;
        this.requester = new LinkRequester(subreddit);
    }

    @Override
    public void notifyAllListener() throws IOException{
        try {
            history = requester.next(); //History is computed once for all listeners
            super.notifyAllListener();
        } catch(LinkRequester.IteratorException e){
            throw e.getCause();
        } finally {
            history = null; //Clear cache even in case an IOException was thrown.
        }
    }

    @Override
    public void notifyListener(T listener) throws IOException {
        try {
            //History may be null when a listener is called explicitly instead of via notifyAllListener.
            history = history == null ? requester.next() : history;
            //Notify the listener starting with the oldest link first
            history.stream().sorted(Comparator.comparing(Link::getCreatedUtc)).forEach(link -> listener.newLink(subreddit, link));
        } catch(LinkRequester.IteratorException e){
            throw e.getCause();
        }
    }

    @Override
    public boolean equals(Object o){
        if (o == null) {
            return false;
        } else if (o instanceof SubredditObserver) {
            SubredditObserver<?> other = (SubredditObserver<?>)o;
            return Objects.equals(subreddit.getDisplayName(), other.subreddit.getDisplayName());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode(){
        return subreddit.hashCodeDisplayName();
    }
}
