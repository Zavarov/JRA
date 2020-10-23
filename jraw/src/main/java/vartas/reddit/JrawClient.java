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

import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;
import net.dean.jraw.ApiException;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import okhttp3.*;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import static com.jayway.jsonpath.Criteria.where;
import static com.jayway.jsonpath.Filter.filter;

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

    public JrawClient(String userName, String version, String clientId, String secret){
        UserAgent userAgent = new UserAgent("bot", getClass().getPackage().getName(), version, userName);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new MalformedJsonInterceptor()).build();
        OkHttpNetworkAdapter adapter = new OkHttpNetworkAdapter(userAgent, client);

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

    public Account requestAccount(String accountName) throws UnsuccessfulRequestException, HttpResponseException {
        log.debug("Requesting account {}", accountName);
        return JrawAccount.create(
                request(jrawClient, () -> Optional.ofNullable(jrawClient.user(accountName).query().getAccount()), 0)
        );
    }

    public Subreddit requestSubreddit(String subredditName) throws UnsuccessfulRequestException, HttpResponseException {
        log.debug("Requesting subreddit {}", subredditName);
        return JrawSubreddit.create(
                request(jrawClient, () -> Optional.of(jrawClient.subreddit(subredditName).about()), 0),
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
     * @throws UnsuccessfulRequestException In case the request was rejected.
     * @throws HttpResponseException In case the request was rejected due to an unknown error.
     */
    public static <T> Optional<T> request(RedditClient client, Supplier<Optional<T>> request) throws HttpResponseException, UnsuccessfulRequestException{
        Optional<T> requestOpt = Optional.empty();
        try{
            requestOpt = request.get();
        } catch(NetworkException e){
            handle(client, e.getRes().getCode(), Integer.toString(e.getRes().getCode()));
        } catch(ApiException e){
            handle(client, Integer.parseInt(e.getCode()), e.getExplanation());
        }
        return requestOpt;
    }

    public static <T> T request(RedditClient client, Supplier<Optional<T>> supplier, int attempt) throws HttpResponseException, UnsuccessfulRequestException{
        LoggerFactory.getLogger(RedditClient.class.getSimpleName()).debug("Processing request. Attempt {}/{}", attempt, MAX_RETRY);
        if(attempt == MAX_RETRY)
            throw new UnsuccessfulRequestException();

        Optional<T> requestOpt = request(client, supplier);

        if(requestOpt.isPresent())
            return requestOpt.get();
        else
            return request(client, supplier, attempt + 1);
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
     * @throws UnsuccessfulRequestException In case the request was rejected.
     * @throws HttpResponseException In case the request was rejected due to an unknown error.
     */
    public static void handle(RedditClient client, int errorCode, String explanation) throws HttpResponseException, UnsuccessfulRequestException {
        HttpResponseException cause = new HttpResponseException(errorCode, explanation);

        if(isClientError(errorCode)){
            //In case the token has expired, simply renew it instead of throwing an error.
            if(errorCode == HttpStatus.SC_UNAUTHORIZED){
                LoggerFactory.getLogger(RedditClient.class.getSimpleName()).warn(explanation);
                client.getAuthManager().renew();
            }else {
                LoggerFactory.getLogger(RedditClient.class.getSimpleName()).error(explanation);
                throw new ClientException(cause);
            }
        }else if(isServerError(errorCode)){
            LoggerFactory.getLogger(RedditClient.class.getSimpleName()).warn(explanation);
            throw new ServerException(cause);
        }else{
            LoggerFactory.getLogger(RedditClient.class.getSimpleName()).warn(explanation);
            throw cause;
        }
    }

    private static boolean isClientError(int errorCode){
        return errorCode >= 400 && errorCode < 500;
    }

    private static boolean isServerError(int errorCode){
        return errorCode >= 500 && errorCode < 600;
    }

    /**
     * Copied and slightly modified hotfix for NullPointerExceptions caused by JRAW.
     * @link https://github.com/mattbdean/JRAW/issues/300
     */
    private static class MalformedJsonInterceptor implements Interceptor {
        static final String oembedElements = "$..oembed[?]";
        static final Filter whereTypeIsNull = filter(where("type").exists(false));

        @Override
        public Response intercept(Chain chain) throws IOException {
            return updateResponse(chain.proceed(chain.request()));
        }

        Response updateResponse(Response previous) throws IOException {
            ResponseBody previousBody = previous.body();

            if(previousBody == null)
                return previous;

            MediaType contentType = previousBody.contentType();
            String content = cleanJson(previousBody.string());
            ResponseBody body = ResponseBody.create(contentType, content);
            return previous.newBuilder().body(body).build();
        }

        String cleanJson(String jsonString){
            try {
                return JsonPath.parse(jsonString)
                        .delete(oembedElements, whereTypeIsNull)
                        .jsonString();
            }catch(Exception e){
                return jsonString;
            }
        }
    }
}
