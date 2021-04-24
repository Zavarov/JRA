package zav.jra.requester;

import com.google.common.collect.AbstractIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zav.jra.Link;
import zav.jra.Subreddit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class LinkRequester extends AbstractIterator<List<? extends Link>> {
    @Nonnull
    private static final Logger LOGGER = LoggerFactory.getLogger(LinkRequester.class);
    @Nonnull
    private final Subreddit subreddit;
    @Nullable
    private Link head;

    public LinkRequester(@Nonnull Subreddit subreddit){
        this.subreddit = subreddit;
    }

    @Override
    protected List<? extends Link> computeNext() throws IteratorException{
        try {
            LOGGER.info("Computing next links for r/{}.", subreddit.getDisplayName());
            return head == null ? init() : request();
        } catch(InterruptedException e){
            LOGGER.warn(e.getMessage(), e);
            return Collections.emptyList();
        } catch(IOException e) {
            throw new IteratorException(e);
        }
    }

    private List<? extends Link> init() throws InterruptedException, IOException {
        LOGGER.info("Possible first time this requester is used? Retrieve head.");
        List<? extends Link> submissions = subreddit.getNewLinks().limit(1).collect(Collectors.toList());

        if(!submissions.isEmpty()) {
            head = submissions.get(0);
            LOGGER.info("Retrieved {} as the new head.", head.getName());
        }

        return Collections.emptyList();
    }

    private List<Link> request() throws InterruptedException, IOException {
        assert head != null;
        LOGGER.info("Requesting links after {}.", head.getName());

        List<Link> result = new ArrayList<>();
        Iterator<Link> iterator = subreddit.getNewLinks().iterator();

        while (iterator.hasNext()) {
            Link link = iterator.next();

            //If the current link is lexicographically larger then the head
            //Then that means it was created after the head, i.e at a later point in time
            if (link.getId().compareTo(head.getId()) > 0) {
                result.add(link);
            } else {
                break;
            }
        }

        if(!result.isEmpty()) {
            //Change head to the newest submission
            head = result.get(0);
            LOGGER.info("Update 'head' to {}.", head.getName());
        }

        return result;
    }

    public final static class IteratorException extends RuntimeException{
        private final IOException cause;

        public IteratorException(IOException cause){
            this.cause = cause;
        }

        @Override
        public IOException getCause(){
            return cause;
        }
    }
}
