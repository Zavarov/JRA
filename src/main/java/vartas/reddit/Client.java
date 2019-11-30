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

package vartas.reddit;

import org.apache.http.client.HttpResponseException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

/**
 * This interface provides the communication between this library and the Reddit API.
 */
public interface Client {
    /**
     * Establishes a connection to the Reddit API.
     */
    default void login(){
        throw new UnsupportedOperationException("Not implemented for this interface");
    }

    /**
     * Cuts the connection to the Reddit API.
     */
    default void logout(){
        throw new UnsupportedOperationException("Not implemented for this interface");
    }

    /**
     * @param user The user name.
     * @return The user corresponding to that name.
     * @throws HttpResponseException If the API returned an unresolvable error.
     */
    default Optional<RedditUser> requestUser(String user) throws HttpResponseException{
        throw new UnsupportedOperationException("Not implemented for this interface");
    }

    /**
     * @param user The user name.
     * @param retries The number of times the request is repeated upon failure.
     * @return The user corresponding to that name.
     * @throws HttpResponseException If the API returned an unresolvable error.
     */
    default Optional<RedditUser> requestUser(String user, int retries) throws HttpResponseException{
        return request(() -> requestUser(user), retries);
    }

    /**
     * @param subreddit The subreddit name.
     * @return The subreddit corresponding to that name.
     * @throws HttpResponseException If the API returned an unresolvable error.
     */
    default Optional<Subreddit> requestSubreddit(String subreddit) throws HttpResponseException{
        throw new UnsupportedOperationException("Not implemented for this interface");
    }

    /**
     * @param subreddit The subreddit name.
     * @param retries The number of times the request is repeated upon failure.
     * @return The subreddit corresponding to that name.
     * @throws HttpResponseException If the API returned an unresolvable error.
     */
    default Optional<Subreddit> requestSubreddit(String subreddit, int retries) throws HttpResponseException{
        return request(() -> requestSubreddit(subreddit), retries);
    }

    /**
     * Request submissions within a given interval sorted by their creation date.
     * Note that Reddit will -at most- return the past 1000 submissions.
     * @param subreddit The subreddit name.
     * @param start The (inclusive) age of the oldest submissions.
     * @param end The (exclusive) age of the newest submissions.
     * @return All submissions within the given interval.
     * @throws HttpResponseException If the API returned an unresolvable error.
     */
    default Optional<Collection<Submission>> requestSubmission(String subreddit, LocalDateTime start, LocalDateTime end) throws HttpResponseException{
        throw new UnsupportedOperationException("Not implemented for this interface");
    }

    /**
     * Request submissions within a given interval sorted by their creation date.
     * Note that Reddit will -at most- return the past 1000 submissions.
     * @param subreddit The subreddit name.
     * @param start The (inclusive) age of the oldest submissions.
     * @param end The (exclusive) age of the newest submissions.
     * @param retries The number of times the request is repeated upon failure.
     * @return All submissions within the given interval.
     * @throws HttpResponseException If the API returned an unresolvable error.
     */
    default Optional<Collection<Submission>> requestSubmission(String subreddit, LocalDateTime start, LocalDateTime end, int retries) throws HttpResponseException{
        return request(() -> requestSubmission(subreddit, start, end), retries);
    }

    /**
     * Requests a single submission.
     * @param submissionId The submission id.
     * @return The submission instance.
     * @throws HttpResponseException If the API returned an unresolvable error.
     */
    default Optional<Submission> requestSubmission(String submissionId) throws HttpResponseException{
        throw new UnsupportedOperationException("Not implemented for this interface");
    }

    /**
     * Requests a single submission.
     * @param submissionId The submission id.
     * @param retries The number of times the request is repeated upon failure.
     * @return The submission instance.
     * @throws HttpResponseException If the API returned an unresolvable error.
     */
    default Optional<Submission> requestSubmission(String submissionId, int retries) throws HttpResponseException{
        return request(() -> requestSubmission(submissionId), retries);
    }
    /**
     * @param commentId The comment id.
     * @param retries The number of times the request is repeated upon failure.
     * @return All comments of the submission.
     * @throws HttpResponseException If the API returned an unresolvable error.
     */
    default Optional<Collection<Comment>> requestComment(String commentId, int retries) throws HttpResponseException{
        return request(() -> requestComment(commentId), retries);
    }

    /**
     * @param commentId The comment id.
     * @return All comments of the submission.
     * @throws HttpResponseException If the API returned an unresolvable error.
     */
    default Optional<Collection<Comment>> requestComment(String commentId) throws HttpResponseException{
        throw new UnsupportedOperationException("Not implemented for this interface");
    }

    /**
     * There are three cases when processing the request.
     * <p><ul>
     * <li>An {@link HttpResponseException} is thrown upon failure
     * <li>An empty {@link Optional} is returned in case the request failed without error
     * <li>An {@link Optional} is returned containing the requested entity upon success.
     * </ul><p>
     * In the second case, the request is repeated up to {@code attempts}
     * @param request The API request.
     * @param attempts The number of times the request is repeated when it was unsuccessful.
     * @param <T> The expected entity type.
     * @return The requested entity.
     * @throws HttpResponseException If the API returned an unresolvable error.
     */
    default <T> Optional<T> request(HttpSupplier<Optional<T>> request, int attempts) throws HttpResponseException{
        Optional<T> result = Optional.empty();
        while(attempts > 0 && result.isEmpty()){
            result = request.get();
            attempts--;
        }
        return result;
    }

    /**
     * This class is defined analogously to {@link java.util.function.Supplier}, with the exception that it can
     * throw an {@link HttpResponseException} upon failure.
     * @param <T> The expected entity type.
     */
    @FunctionalInterface
    interface HttpSupplier<T>{
        T get() throws HttpResponseException;
    }
}
