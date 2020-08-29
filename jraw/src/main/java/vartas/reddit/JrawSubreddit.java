/*
 * Copyright (c) 2020 Zavarov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package vartas.reddit;

import com.google.common.collect.Range;
import net.dean.jraw.RedditClient;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dean.jraw.pagination.Paginator;
import org.apache.http.client.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vartas.reddit.factory.SubredditFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class JrawSubreddit extends Subreddit {
    /**
     * This classes logger.
     */
    private final Logger log = LoggerFactory.getLogger(getClass().getSimpleName());
    /**
     * The interface with the Reddit API.
     */
    protected final RedditClient jrawClient;

    /**
     * Creates a new subreddit instance.
     * @param jrawClient the interface with the Reddit API.
     */
    public JrawSubreddit(RedditClient jrawClient){
        this.jrawClient = jrawClient;
    }

    /**
     * Creates a new subreddit instance.
     * @param supplier the generator class for the created class instance.
     * @param jrawSubreddit the interface with the Reddit API
     * @return a new subreddit instance.
     */
    public static Subreddit create(Supplier<? extends Subreddit> supplier, net.dean.jraw.models.Subreddit jrawSubreddit){
        Subreddit subreddit = SubredditFactory.create(
                supplier,
                jrawSubreddit.getName(),
                jrawSubreddit.getPublicDescription(),
                jrawSubreddit.getSubscribers(),
                jrawSubreddit.getUniqueId(),
                jrawSubreddit.getCreated().toInstant()
        );

        //Add optional parameters
        subreddit.setActiveAccounts(Optional.ofNullable(jrawSubreddit.getAccountsActive()));
        subreddit.setBannerImage(Optional.ofNullable(jrawSubreddit.getBannerImage()));
        return subreddit;
    }


    /**
     * Transforms the JRAW subreddit instance into one that can be used by this program.
     * @param jrawSubreddit the JRAW subreddit.
     * @param jrawClient the interface with the Reddit API
     * @return a new subreddit instance.
     */
    public static Subreddit create(net.dean.jraw.models.Subreddit jrawSubreddit, RedditClient jrawClient){
        return create(() -> new JrawSubreddit(jrawClient), jrawSubreddit);
    }

    @Override
    public Submission getSubmissions(String key) throws ExecutionException {
        return getSubmissions(key, () -> requestSubmissions(key));
    }

    private Submission requestSubmissions(String key) throws TimeoutException, UnsuccessfulRequestException, HttpResponseException {
        log.debug("Request submission {}", key);
        Supplier<Optional<Submission>> supplier = () -> Optional.of(
                JrawSubmission.create(jrawClient.submission(key).inspect(), jrawClient)
        );

        return JrawClient.request(supplier, 0);
    }

    /**
     * Returns a list of all cached submissions within the specified interval in ascending order.<br>
     * This means that it is not possible, to retrieve submissions before
     * this object was initialized or submissions that have already been
     * removed from the cache. Instead you can only access the most recent submissions.
     * @param inclusiveFrom the (inclusive) minimum age of valid submissions.
     * @param exclusiveTo the (exclusive) maximum age of valid submissions.
     * @return A list containing all requested submissions.
     */
    @Override
    public List<Submission> getSubmissions(Instant inclusiveFrom, Instant exclusiveTo) throws UnsuccessfulRequestException, TimeoutException, HttpResponseException {
        log.debug("Request submissions for [{}, {})", inclusiveFrom, exclusiveTo);
        Range<Instant> range = Range.closedOpen(inclusiveFrom, exclusiveTo);

        //Range hasn't been requested before
        if(memory.getIfPresent(range) == null){
            requestSubmissions(inclusiveFrom, exclusiveTo);
            memory.put(range, range);
        }

        //Get cached submissions.
        return valuesSubmissions()
                .stream()
                .filter(submission -> range.contains(submission.getCreated()))
                .sorted(Comparator.comparing(Submission::getCreated))
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Reddit's API is a huge mess, so we can't request submissions within a specific interval.
     * Instead we request all submissions from now until the lower bound specified in inclusiveFrom.
     * @param inclusiveFrom the (inclusive) maximum age of valid submissions.
     * @param exclusiveTo the (exclusive) minimum age of valid submissions.
     */
    protected void requestSubmissions(Instant inclusiveFrom, Instant exclusiveTo) throws UnsuccessfulRequestException, TimeoutException, HttpResponseException {
        log.debug("Request submissions for [{}, {})", inclusiveFrom, exclusiveTo);
        for(Submission submission : requestJrawSubmissions(inclusiveFrom))
            putSubmissions(submission.getId(), submission);
    }

    private List<Submission> requestJrawSubmissions(Instant inclusiveFrom) throws TimeoutException, UnsuccessfulRequestException, HttpResponseException {
        log.debug("Request submissions until {}", inclusiveFrom);
        return JrawClient.request(() -> {
                    DefaultPaginator<net.dean.jraw.models.Submission> paginator = jrawClient
                            .subreddit(getName())
                            .posts()
                            .sorting(SubredditSort.NEW)
                            .limit(Paginator.RECOMMENDED_MAX_LIMIT)
                            .timePeriod(TimePeriod.ALL)
                            .build();

                    //Contains submissions over all pages
                    List<Submission> submissions = new ArrayList<>();
                    //Contains submissions over the current page
                    List<Submission> current;
                    //We have to do the iterative way because we can't specify an interval
                    do{
                        current = paginator.next()
                                .stream()
                                //Ignore all submissions that are older than the lower bound
                                .filter(jrawSubmission -> !jrawSubmission.getCreated().toInstant().isBefore(inclusiveFrom))
                                //Filter before creating the instances to avoid loading unnecessary comments
                                .map(jrawSubmission -> getUncheckedSubmissions(jrawSubmission.getId()))
                                .collect(Collectors.toCollection(ArrayList::new));
                    //Continue until either the 1000 submission limit has been reached
                    //or all further submissions are older than the lower bound.
                    }while(submissions.addAll(current));

                    return Optional.of(submissions);
                },
                0
        );
    }
}
