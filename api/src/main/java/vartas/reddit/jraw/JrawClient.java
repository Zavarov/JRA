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
import net.dean.jraw.models.*;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dean.jraw.pagination.Paginator;
import net.dean.jraw.references.SubmissionReference;
import net.dean.jraw.tree.RootCommentNode;
import org.apache.http.HttpStatus;
import vartas.reddit.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This class implements the Reddit client using the JRAW library.
 */
public class JrawClient implements ClientInterface {
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
     * @param redditName the account that created the bot.
     * @param version this version of the bot.
     * @param clientId the id of the client.
     * @param secret the secret of the client.
     */
    public JrawClient(String redditName, String version, String clientId, String secret){
        UserAgent userAgent = new UserAgent("bot", getClass().getPackage().getName(), version, redditName);

        this.adapter = new OkHttpNetworkAdapter(userAgent);
        this.clientId = clientId;
        this.secret = secret;
    }

    /**
     * Establishes a connection to the Reddit API.
     * @throws IllegalStateException if a connection was already established since the last logout.
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
     * @param name the name of the user.
     * @return the Reddit user instance with that name.
     * @throws UnresolvableRequestException if the API returned an unresolvable error.
     */
    @Override
    public Optional<UserInterface> requestUser(String name) throws UnresolvableRequestException {
        return request(() -> {
            Account account = client.user(name).query().getAccount();
            return Optional.of(new JrawUser(account));
        });
    }

    /**
     * @param name the name of the subreddit.
     * @return the subreddit with that name.
     * @throws UnresolvableRequestException if the API returned an unresolvable error.
     */
    @Override
    public Optional<SubredditInterface> requestSubreddit(String name) throws UnresolvableRequestException{
        return request(() -> {
            Subreddit subreddit = client.subreddit(name).about();
            return Optional.of(new JrawSubreddit(subreddit));
        });
    }

    /**
     * Request submissions within a given interval sorted by their creation date.
     * Note that Reddit will -at most- return the past 1000 submissions.
     * @param subreddit the name of the subreddit.
     * @param after the (exclusive) minimum age of the submissions.
     * @param before the (exclusive) maximum age of the submissions.
     * @return all submissions within the given interval sorted by their creation time.
     * @throws UnresolvableRequestException if the API returned an unresolvable error.
     */
    @Override
    public Optional<SortedSet<SubmissionInterface>> requestSubmission(String subreddit, Date after, Date before) throws UnresolvableRequestException{
        return request(() -> {
            DefaultPaginator<Submission> paginator = client
                    .subreddit(subreddit)
                    .posts()
                    .sorting(SubredditSort.NEW)
                    .limit(Paginator.RECOMMENDED_MAX_LIMIT)
                    .timePeriod(TimePeriod.ALL)
                    .build();

            SortedSet<SubmissionInterface> submissions = new TreeSet<>(Comparator.comparing(SubmissionInterface::getCreated));
            List<SubmissionInterface> current;
            Date newest;
            //We have to do the iterative way because we can't specify an interval
            do{
                //The newest value should be the last one
                current = paginator.next().stream()
                        .filter(s -> s.getCreated().before(before))
                        .filter(s -> !s.getCreated().before(after))
                        .map(JrawSubmission::new)
                        .collect(Collectors.toList());
                newest = current.isEmpty() ? before : current.get(current.size()-1).getCreated();
                submissions.addAll(current);
                //Repeat when we haven't found the last submission
            }while(newest.before(before));

            return Optional.of(submissions);
        });
    }

    /**
     * @param submission the id of the submission.
     * @return all comments of the submission.
     * @throws UnresolvableRequestException if the API returned an unresolvable error.
     */
    @Override
    public Optional<List<CommentInterface>> requestComment(String submission) throws UnresolvableRequestException{
        return request(() -> {
            List<CommentInterface> comments = new LinkedList<>();

            SubmissionReference submissionReference = client.submission(submission);

            RootCommentNode root = submissionReference.comments();
            Submission submissionInstance = root.getSubject();
            //Acquire all the comments
            root.loadFully(client);

            root.walkTree().iterator().forEachRemaining(node -> {
                if(node.getSubject() instanceof Comment)
                    comments.add(new JrawComment((Comment)node.getSubject(), submissionInstance));
            });

            return Optional.of(comments);
        });
    }

    /**
     * Requests a single submission.
     * @param submissionId the id of the submission.
     * @return the submission instance.
     * @throws UnresolvableRequestException if the API returned an unresolvable error.
     */
    @Override
    public Optional<SubmissionInterface> requestSubmission(String submissionId) throws UnresolvableRequestException{
        return request(() -> {
            Submission submission = client.submission(submissionId).inspect();
            return Optional.of(new JrawSubmission(submission));
        });
    }

    /**
     * A wrapper for the requests that cleans up the messy exceptions from JRAW.
     * @param request the request that is executed.
     * @param <T> the type of the return value.
     * @return whatever the requests returns.
     * @throws UnresolvableRequestException if the API returned an unresolvable error.
     */
    protected <T> Optional<T> request(Supplier<Optional<T>> request) throws UnresolvableRequestException{
        if(client == null)
            throw new IllegalStateException("Please login first.");
        try{
            return request.get();
        } catch(NetworkException e){
            Logger.getGlobal().warning(e.getMessage());
            return handle(e.getRes().getCode());
        } catch(ApiException e){
            Logger.getGlobal().warning(e.getMessage());
            return handle(Integer.parseInt(e.getCode()));
        } catch (Exception e) {
            Logger.getGlobal().warning(e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * This method handles the different error exceptions.
     * @param errorCode the error code the Reddit API returned.
     * @param <T> the return type of the request.
     * @return Optional.empty()
     * @throws UnresolvableRequestException if a 403 or 404 was returned.
     */
    protected <T> Optional<T> handle(int errorCode) throws UnresolvableRequestException{
        //Because the API exception can't be bothered to give us an useful error
        if( errorCode == HttpStatus.SC_FORBIDDEN || errorCode == HttpStatus.SC_NOT_FOUND)
            throw new UnresolvableRequestException(errorCode);
        else
            return Optional.empty();
    }
}
