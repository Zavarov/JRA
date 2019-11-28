/*
 * Copyright (c) 2019 Zavarov
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

package vartas.reddit.jraw;

import net.dean.jraw.ApiException;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dean.jraw.pagination.Paginator;
import net.dean.jraw.references.SubmissionReference;
import net.dean.jraw.tree.RootCommentNode;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;
import vartas.reddit.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This class implements the Reddit client using the JRAW library.
 */
public class JrawClient implements Client {
    /**
     * The underlying client.
     */
    protected RedditClient client;
    /**
     * The adapter responsible for the communication between the client and the Reddit API.
     */
    protected NetworkAdapter adapter;
    /**
     * The id of the client.
     */
    protected String clientId;
    /**
     * The secret of the client.
     */
    protected String secret;

    /**
     * Initializes the client but doesn't establish a connection to Reddit.
     * @param redditName The account that registered the bot.
     * @param version This version of the bot.
     * @param clientId The id of the client.
     * @param secret The secret of the client.
     */
    public JrawClient(String redditName, String version, String clientId, String secret){
        UserAgent userAgent = new UserAgent("bot", getClass().getPackage().getName(), version, redditName);

        this.adapter = new OkHttpNetworkAdapter(userAgent);
        this.clientId = clientId;
        this.secret = secret;
    }

    /**
     * Establishes a connection to the Reddit API.
     * @throws IllegalStateException If a connection was already established since the last logout.
     */
    @Override
    public void login() throws IllegalStateException{
        if(client != null)
            throw new IllegalStateException("You are already logged in.");

        client = OAuthHelper.automatic(adapter, Credentials.userless(clientId, secret, UUID.randomUUID()));
        client.setLogHttp(false);
        client.setAutoRenew(true);
    }

    /**
     * Cuts the connection to the Reddit API.
     */
    @Override
    public void logout() {
        client = null;
    }

    /**
     * Unlike the other entities, the API doesn't return an error if the user doesn't exist.
     * Instead an empty {@link Optional} is returned.
     * @param name The user name.
     * @return The Reddit user instance with that name.
     * @throws HttpResponseException If the API returned an unresolvable error.
     */
    @Override
    public Optional<RedditUser> requestUser(String name) throws HttpResponseException {
        return request(() -> Optional.ofNullable(client.user(name).query().getAccount()).map(JrawUser::new));
    }

    /**
     * @param name The subreddit name.
     * @return The subreddit with that name.
     * @throws HttpResponseException If the API returned an unresolvable error.
     */
    @Override
    public Optional<Subreddit> requestSubreddit(String name) throws HttpResponseException{
        return request(() -> {
            net.dean.jraw.models.Subreddit subreddit = client.subreddit(name).about();
            return Optional.of(new JrawSubreddit(subreddit));
        });
    }

    /**
     * Request submissions within a given interval sorted by their creation date.
     * Note that Reddit will -at most- return the past 1000 submissions.
     * @param subreddit The subreddit name.
     * @param start The (inclusive) age of the oldest submissions.
     * @param end The (exclusive) age of the newest submissions.
     * @return All submissions within the given interval sorted by their creation time.
     * @throws HttpResponseException If the API returned an unresolvable error.
     */
    @Override
    public Optional<TreeSet<Submission>> requestSubmission(String subreddit, LocalDateTime start, LocalDateTime end) throws HttpResponseException{
        return request(() -> {
            DefaultPaginator<net.dean.jraw.models.Submission> paginator = client
                    .subreddit(subreddit)
                    .posts()
                    .sorting(SubredditSort.NEW)
                    .limit(Paginator.RECOMMENDED_MAX_LIMIT)
                    .timePeriod(TimePeriod.ALL)
                    .build();

            TreeSet<Submission> submissions = new TreeSet<>();
            List<Submission> current;
            LocalDateTime newest;
            //We have to do the iterative way because we can't specify an interval
            do{
                //The newest value should be the last one
                current = paginator.next().stream()
                        .map(JrawSubmission::new)
                        .filter(s -> s.getCreated().isBefore(end))          //Before 'end'       -> Exclusive 'end'
                        .filter(s -> !s.getCreated().isBefore(start))       //Not before 'start' -> Inclusive 'start'
                        .collect(Collectors.toList());
                newest = current.isEmpty() ? end : current.get(current.size()-1).getCreated();
                submissions.addAll(current);
                //Repeat when we haven't found the last submission
            }while(newest.isBefore(end));

            return Optional.of(submissions);
        });
    }

    /**
     * @param submissionId the id of the submission.
     * @return All comments of the submission.
     * @throws HttpResponseException If the API returned an unresolvable error.
     */
    @Override
    public Optional<List<Comment>> requestComment(String submissionId) throws HttpResponseException{
        return request(() -> {
            List<Comment> comments = new LinkedList<>();

            SubmissionReference submissionReference = client.submission(submissionId);

            RootCommentNode root;
            try {
                //null if the submission doesn't exist -> Not a communication error
                root = submissionReference.comments();
            }catch(NullPointerException e){
                Logger.getGlobal().warning(e.getMessage());
                return Optional.of(Collections.emptyList());
            }
            net.dean.jraw.models.Submission submissionInstance = root.getSubject();
            //Acquire all the comments
            root.loadFully(client);

            root.walkTree().iterator().forEachRemaining(node -> {
                if(node.getSubject() instanceof net.dean.jraw.models.Comment)
                    comments.add(new JrawComment((net.dean.jraw.models.Comment)node.getSubject(), submissionInstance));
            });

            return Optional.of(comments);
        });
    }

    /**
     * Requests a single submission.
     * @param submissionId The id of the submission.
     * @return The submission instance.
     * @throws HttpResponseException If the requested was denied by the API.
     */
    @Override
    public Optional<Submission> requestSubmission(String submissionId) throws HttpResponseException{
        return request(() -> {
            net.dean.jraw.models.Submission submission = client.submission(submissionId).inspect();
            return Optional.of(new JrawSubmission(submission));
        });
    }

    /**
     * A wrapper for the requests that cleans up the messy exceptions of JRAW.<br>
     * In case the request was successful, the returned {@link Optional} contains the requested entity.<br>
     * Otherwise it will be empty.
     * @param request The request.
     * @param <T> The expected return type.
     * @return The requested entity.
     * @throws HttpResponseException If the requested was denied by the API.
     */
    protected <T> Optional<T> request(Supplier<Optional<T>> request) throws HttpResponseException{
        if(client == null)
            throw new IllegalStateException("Please login first.");

        Optional<T> requestOpt = Optional.empty();
        try{
            requestOpt = request.get();
        } catch(NetworkException e){
            handle(e.getRes().getCode(), Integer.toString(e.getRes().getCode()));
        } catch(ApiException e){
            handle(Integer.parseInt(e.getCode()), e.getExplanation());
        } catch(Exception e){
            //In case JRAW fucks up
            Logger.getGlobal().severe(e.getMessage());
        }
        return requestOpt;
    }

    /**
     * This method handles the different error codes.<br>
     * Even though JRAW reconnects automatically, once the token has expired, the current request still causes an
     * {@link HttpStatus#SC_UNAUTHORIZED}.
     * In such a case, we simply have to repeat the request.<br>
     * An API error usually indicates that something went horribly wrong, hence why we can't just ignore it without
     * risking getting blocked by the server.
     * @param errorCode The error code returned by the Reddit API.
     * @param explanation The reason for the error.
     * @throws HttpResponseException If the error code is not {@link HttpStatus#SC_UNAUTHORIZED}.
     */
    protected void handle(int errorCode, String explanation) throws HttpResponseException {
        switch(errorCode){
            case HttpStatus.SC_UNAUTHORIZED:
                Logger.getGlobal().warning(explanation);
                break;
            default:
                Logger.getGlobal().severe(explanation);
                throw new HttpResponseException(errorCode, explanation);
        }
    }
}
