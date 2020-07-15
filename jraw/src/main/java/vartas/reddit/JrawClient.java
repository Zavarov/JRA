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

import net.dean.jraw.ApiException;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class JrawClient extends Client {
    /**
     * Indicates the number of retries until a request fails.
     * This is to avoid repeating it indefinitely.
     */
    private static final int MAX_RETRY = 3;
    /**
     * This classes logger.
     */
    private final Logger log = LoggerFactory.getLogger(getClass().getSimpleName());
    /**
     * The JRAW client.
     */
    protected final RedditClient jrawClient;

    public JrawClient(String redditName, String version, String clientId, String secret){
        UserAgent userAgent = new UserAgent("bot", getClass().getPackage().getName(), version, redditName);
        OkHttpNetworkAdapter adapter = new OkHttpNetworkAdapter(userAgent);

        jrawClient = OAuthHelper.automatic(adapter, Credentials.userless(clientId, secret, UUID.randomUUID()));
        jrawClient.setLogHttp(false);
        jrawClient.setAutoRenew(true);
    }

    @Override
    public Subreddit getSubreddits(String key) throws ExecutionException {
        log.debug("Accessing subreddit {}.", key);
        return getSubreddits(key, () -> requestSubreddit(key));
    }

    @Override
    public Account getAccounts(String key) throws ExecutionException {
        log.debug("Accessing account {}.", key);
        return getAccounts(key, () -> requestAccount(key));
    }

    public Account requestAccount(String accountName) throws TimeoutException, UnsuccessfulRequestException, HttpResponseException {
        log.debug("Requesting account {}", accountName);
        return JrawAccount.create(
                request(() -> Optional.ofNullable(jrawClient.user(accountName).query().getAccount()), 0)
        );
    }

    public Subreddit requestSubreddit(String subredditName) throws TimeoutException, UnsuccessfulRequestException, HttpResponseException {
        log.debug("Requesting subreddit {}", subredditName);
        return JrawSubreddit.create(
                request(() -> Optional.of(jrawClient.subreddit(subredditName).about()), 0),
                jrawClient
        );
    }

    /**
     * A wrapper for the requests that cleans up the messy exceptions of JRAW.<br>
     * In case the request was successful, the returned {@link Optional} contains the requested entity.<br>
     * Otherwise it will be empty.
     * @param request The request.
     * @param <T> The expected return type.
     * @return The requested entity.
     * @throws HttpResponseException If the server returned an unknown error.
     * @throws TimeoutException In case the server returned {@link HttpStatus#SC_GATEWAY_TIMEOUT}
     *                          or {@link HttpStatus#SC_SERVICE_UNAVAILABLE}.
     */
    public static <T> Optional<T> request(Supplier<Optional<T>> request) throws HttpResponseException, TimeoutException{
        Optional<T> requestOpt = Optional.empty();
        try{
            requestOpt = request.get();
        } catch(NetworkException e){
            handle(e.getRes().getCode(), Integer.toString(e.getRes().getCode()));
        } catch(ApiException e){
            handle(Integer.parseInt(e.getCode()), e.getExplanation());
        } catch(Exception e){
            //In case JRAW fucks up
            LoggerFactory.getLogger(RedditClient.class.getSimpleName()).error(e.getMessage(), e);
        }
        return requestOpt;
    }

    public static <T> T request(Supplier<Optional<T>> supplier, int attempt) throws HttpResponseException, TimeoutException, UnsuccessfulRequestException{
        LoggerFactory.getLogger(RedditClient.class.getSimpleName()).debug("Processing request. Attempt {}/{}", attempt, MAX_RETRY);
        if(attempt == MAX_RETRY)
            throw new UnsuccessfulRequestException();

        Optional<T> requestOpt = request(supplier);

        if(requestOpt.isPresent())
            return requestOpt.get();
        else
            return request(supplier, attempt + 1);
    }

    /**
     * This method handles the different error codes.
     * <br>
     * Even though JRAW reconnects automatically, once the token has expired, the current request may still cause an
     * {@link HttpStatus#SC_UNAUTHORIZED}. In such a case, we simply have to repeat the request.
     * <br>
     * An API error usually indicates that something went horribly wrong, hence why we can't just ignore it without
     * risking getting blocked by the server.
     *
     * @param errorCode The error code returned by the Reddit API.
     * @param explanation The reason for the error.
     * @throws HttpResponseException If the server returned an unknown error.
     * @throws TimeoutException In case the server returned {@link HttpStatus#SC_GATEWAY_TIMEOUT}
     *                          or {@link HttpStatus#SC_SERVICE_UNAVAILABLE}.
     */
    public static void handle(int errorCode, String explanation) throws HttpResponseException, TimeoutException {
        switch(errorCode){
            case HttpStatus.SC_UNAUTHORIZED:
                LoggerFactory.getLogger(RedditClient.class.getSimpleName()).warn(explanation);
                break;
            case HttpStatus.SC_GATEWAY_TIMEOUT:
            case HttpStatus.SC_SERVICE_UNAVAILABLE:
                LoggerFactory.getLogger(RedditClient.class.getSimpleName()).warn(explanation);
                throw new TimeoutException();
            default:
                LoggerFactory.getLogger(RedditClient.class.getSimpleName()).error(explanation);
                throw new HttpResponseException(errorCode, explanation);
        }
    }
}
