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
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class JrawSubreddit extends Subreddit{
    /**
     * This classes logger.
     */
    private final Logger log = LoggerFactory.getLogger(getClass().getSimpleName());
    /**
     * The instant indicating the last time, submissions from the associated {@link Subreddit}
     * have been requested.
     */
    private Instant now = Instant.now();
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
        Supplier<Optional<Submission>> supplier = () -> Optional.of(
                JrawSubmission.create(jrawClient.submission(key).inspect(), jrawClient)
        );

        return JrawClient.request(supplier, 0);
    }

    /**
     * Returns a list of all cached submissions.<br>
     * This means that it is not possible, to retrieve submissions before
     * this object was initialized or submissions that have already been
     * removed from the cache. Instead you can only access the most recent submissions.
     * @param inclusiveFrom the (inclusive) minimum age of valid submissions.
     * @param exclusiveTo the (exclusive) maximum age of valid submissions.
     * @return A list of all submissions within the specified interval.
     */
    @Override
    public List<Submission> getSubmissions(Instant inclusiveFrom, Instant exclusiveTo) throws UnsuccessfulRequestException, TimeoutException, HttpResponseException {
        //Update the cache if there might be new submissions.
        if(now.isBefore(exclusiveTo)) {
            requestSubmissions(now, exclusiveTo);
            now = exclusiveTo;
        }

        //Get cached submissions.
        return valuesSubmissions()
                .stream()
                //Ignore submissions after #exclusiveTo
                .filter(submission -> submission.getCreated().isBefore(exclusiveTo))
                //Ignore submissions before #inclusiveFrom
                .filter(submission -> !submission.getCreated().isBefore(inclusiveFrom))
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Reddit's API is a huge mess, so we can't request submissions within a specific interval.<br>
     * Instead we cache all submissions up to {@link #now}.
     * @param inclusiveFrom the (inclusive) minimum age of valid submissions.
     * @param exclusiveTo the (exclusive) maximum age of valid submissions.
     */
    protected void requestSubmissions(Instant inclusiveFrom, Instant exclusiveTo) throws UnsuccessfulRequestException, TimeoutException, HttpResponseException {
        log.debug("Requesting submissions from r/{} from {} to {}.", getName(), inclusiveFrom, exclusiveTo);
        for(Submission submission : requestJrawSubmissions(inclusiveFrom, exclusiveTo))
            putSubmissions(submission.getId(), submission);
    }

    protected List<Submission> requestJrawSubmissions(Instant inclusiveFrom, Instant exclusiveTo) throws TimeoutException, UnsuccessfulRequestException, HttpResponseException {
        return JrawClient.request(() -> {
                    DefaultPaginator<net.dean.jraw.models.Submission> paginator = jrawClient
                            .subreddit(getName())
                            .posts()
                            .sorting(SubredditSort.NEW)
                            .limit(Paginator.RECOMMENDED_MAX_LIMIT)
                            .timePeriod(TimePeriod.ALL)
                            .build();

                    Set<Submission> submissions = new TreeSet<>(Comparator.comparing(Submission::getCreated));
                    //Contains submissions sorted by their creaton date. The oldest submission will be the first element.
                    Deque<Submission> current;
                    Instant newest;
                    //We have to do the iterative way because we can't specify an interval
                    do{
                        //The newest value should be the last one
                        current = paginator.next()
                                .stream()
                                //Filter before transforming to avoid unnecessary comment requests
                                //Ignore submissions after #exclusiveTo
                                .filter(s -> s.getCreated().toInstant().isBefore(exclusiveTo))
                                //Ignore submissions before #inclusiveFrom
                                .filter(s -> !s.getCreated().toInstant().isBefore(inclusiveFrom))
                                .map(jrawSubmission -> JrawSubmission.create(jrawSubmission, jrawClient))
                                .sorted(Comparator.comparing(Submission::getCreated))
                                .collect(Collectors.toCollection(LinkedList::new));

                        //No new submissions retrieved
                        if(!submissions.addAll(current))
                            newest = exclusiveTo;
                        //Take the latest submission
                        else
                            newest = current.getLast().getCreated();
                    //Repeat when we haven't found the last submission
                    }while(newest.isBefore(exclusiveTo));

                    return Optional.of(List.copyOf(submissions));
                },
                0
        );
    }
}
