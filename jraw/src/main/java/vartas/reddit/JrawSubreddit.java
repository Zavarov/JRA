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
import net.dean.jraw.references.SubmissionReference;
import net.dean.jraw.tree.RootCommentNode;
import org.apache.http.client.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vartas.reddit.factory.SubredditFactory;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class JrawSubreddit extends Subreddit{
    /**
     * This classes logger.
     */
    private final Logger log = LoggerFactory.getLogger(getClass().getSimpleName());

    protected final RedditClient jrawClient;

    public JrawSubreddit(RedditClient jrawClient){
        this.jrawClient = jrawClient;
    }

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

    public static Subreddit create(net.dean.jraw.models.Subreddit jrawSubreddit, RedditClient jrawClient){
        return create(() -> new JrawSubreddit(jrawClient), jrawSubreddit);
    }

    @Override
    public Submission getSubmissions(String key) throws ExecutionException {
        return getSubmissions(key, () -> requestSubmissions(key));
    }

    @Override
    protected void request() throws UnsuccessfulRequestException, TimeoutException, HttpResponseException {
        //Start from the end of the last interval
        Instant inclusiveFrom = super.now;
        Instant exclusiveTo = Instant.now();

        log.info("Requesting submissions from r/{} from {} to {}.", getName(), inclusiveFrom, exclusiveTo);
        for(Submission submission : requestSubmissions(inclusiveFrom, exclusiveTo))
            putSubmissions(submission.getId(), submission);
    }
    private Submission requestSubmissions(String key) throws TimeoutException, UnsuccessfulRequestException, HttpResponseException {
        Supplier<Optional<Submission>> supplier = () -> Optional.of(
                createAndLoad(jrawClient.submission(key).inspect())
        );

        return JrawClient.request(supplier, 0);
    }

    protected List<Submission> requestSubmissions(Instant inclusiveFrom, Instant exclusiveTo) throws TimeoutException, UnsuccessfulRequestException, HttpResponseException {
        return JrawClient.request(() -> {
                    DefaultPaginator<net.dean.jraw.models.Submission> paginator = jrawClient
                            .subreddit(getName())
                            .posts()
                            .sorting(SubredditSort.NEW)
                            .limit(Paginator.RECOMMENDED_MAX_LIMIT)
                            .timePeriod(TimePeriod.ALL)
                            .build();

                    Set<Submission> submissions = new HashSet<>();
                    List<Submission> current;
                    Instant newest;
                    //We have to do the iterative way because we can't specify an interval
                    do{
                        //The newest value should be the last one
                        current = paginator.next()
                                .stream()
                                //Filter before transforming to avoid requesting comments
                                //Ignore submissions after #exclusiveTo
                                .filter(s -> s.getCreated().toInstant().isBefore(exclusiveTo))
                                //Ignore submissions before #inclusiveFrom
                                .filter(s -> !s.getCreated().toInstant().isBefore(inclusiveFrom))
                                .map(this::createAndLoad)
                                .collect(Collectors.toList());
                        newest = current.isEmpty() ? exclusiveTo : current.get(current.size()-1).getCreated();
                        submissions.addAll(current);

                        //Repeat when we haven't found the last submission
                    }while(newest.isBefore(exclusiveTo));

                    return Optional.of(List.copyOf(submissions));
                },
                0
        );
    }

    private void requestComments(net.dean.jraw.models.Submission jrawSubmission, Submission submission){
        RootCommentNode root;
        SubmissionReference submissionReference = jrawSubmission.toReference(jrawClient);

        try {
            root = submissionReference.comments();
            //Acquire all the comments
            root.loadFully(jrawClient);

            root.walkTree().iterator().forEachRemaining(node -> {
                if(node.getSubject() instanceof net.dean.jraw.models.Comment)
                    submission.addComments(
                            JrawComment.create((net.dean.jraw.models.Comment)node.getSubject())
                    );
            });

        }catch(NullPointerException e){
            //null if the submission doesn't exist -> Not a communication error
            LoggerFactory.getLogger(getClass().getSimpleName()).warn(e.getMessage(), e);
        }
    }

    //TODO Optimize
    private Submission createAndLoad(net.dean.jraw.models.Submission jrawSubmission){
        log.info("Received submission '{}'", jrawSubmission.getTitle());
        Submission submission = JrawSubmission.create(jrawSubmission);
        requestComments(jrawSubmission, submission);
        return submission;
    }
}
