package net.zav.jra.requester;

import com.google.common.collect.AbstractIterator;
import net.zav.jra.Link;
import net.zav.jra.Parameter;
import net.zav.jra.Subreddit;
import net.zav.jra._factory.ParameterFactory;
import net.zav.jra.exceptions.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LinkRequester extends AbstractIterator<Collection<? extends Link>> {
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
    protected Collection<? extends Link> computeNext() {
        try{
            LOGGER.info("Computing next links.");
            return head == null ? init() : request();
        }catch(Exception e){
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    private Collection<? extends Link> init() throws InterruptedException, IOException, HttpException {
        LOGGER.info("Possible first time this requester is used? Retrieve head.");
        List<? extends Link> submissions = subreddit.getNewLinks().limit(1).collect(Collectors.toList());

        if(!submissions.isEmpty()) {
            head = submissions.get(0);
            LOGGER.info("Retrieved {} as the new head.", head.getName());
        }

        return Collections.emptyList();
    }

    private Collection<? extends Link> request() throws InterruptedException, IOException, HttpException {
        assert head != null;
        LOGGER.info("Requesting links after {}.", head.getName());

        Parameter before = ParameterFactory.create("before", head.getName());
        List<? extends Link> result = subreddit.getNewLinks(before).collect(Collectors.toList());

        if(!result.isEmpty()) {
            Link first = result.get(0);
            LOGGER.info("Update 'head' to {}.", first.getName());
            //Change head to latest submission
            head = first;
        }

        return result;
    }
}
