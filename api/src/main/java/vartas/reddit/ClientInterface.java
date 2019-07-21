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

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.function.Supplier;

/**
 * This interface provides the communication between this library and the Reddit API.
 */
public interface ClientInterface {
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
     * @param name the name of the user.
     * @return the Reddit user instance with that name.
     * @throws UnresolvableRequestException if the API returned an unresolvable error.
     */
    default Optional<UserInterface> requestUser(String name) throws UnresolvableRequestException{
        throw new UnsupportedOperationException("Not implemented for this interface");
    }

    /**
     * @param name the name of the user.
     * @param retries the number of times the request is repeated upon failure.
     * @return the Reddit user instance with that name.
     * @throws UnresolvableRequestException if the API returned an unresolvable error.
     */
    default Optional<UserInterface> requestUser(String name, int retries) throws UnresolvableRequestException{
        return request(() -> requestUser(name), retries);
    }

    /**
     * @param name the name of the subreddit.
     * @return the subreddit with that name.
     * @throws UnresolvableRequestException if the API returned an unresolvable error.
     */
    default Optional<SubredditInterface> requestSubreddit(String name) throws UnresolvableRequestException{
        throw new UnsupportedOperationException("Not implemented for this interface");
    }

    /**
     * @param name the name of the subreddit.
     * @param retries the number of times the request is repeated upon failure.
     * @return the subreddit with that name.
     * @throws UnresolvableRequestException if the API returned an unresolvable error.
     */
    default Optional<SubredditInterface> requestSubreddit(String name, int retries) throws UnresolvableRequestException{
        return request(() -> requestSubreddit(name), retries);
    }

    /**
     * Request submissions within a given interval sorted by their creation date.
     * Note that Reddit will -at most- return the past 1000 submissions.
     * @param subreddit the name of the subreddit.
     * @param after the (exclusive) minimum age of the submissions.
     * @param before the (exclusive) maximum age of the submissions.
     * @return all submissions within the given interval.
     * @throws UnresolvableRequestException if the API returned an unresolvable error.
     */
    default Optional<TreeSet<SubmissionInterface>> requestSubmission(String subreddit, Date after, Date before) throws UnresolvableRequestException{
        throw new UnsupportedOperationException("Not implemented for this interface");
    }

    /**
     * Request submissions within a given interval sorted by their creation date.
     * Note that Reddit will -at most- return the past 1000 submissions.
     * @param subreddit the name of the subreddit.
     * @param after the (exclusive) minimum age of the submissions.
     * @param before the (exclusive) maximum age of the submissions.
     * @param retries the number of times the request is repeated upon failure.
     * @return all submissions within the given interval.
     * @throws UnresolvableRequestException if the API returned an unresolvable error.
     */
    default Optional<TreeSet<SubmissionInterface>> requestSubmission(String subreddit, Date after, Date before, int retries) throws UnresolvableRequestException{
        return request(() -> requestSubmission(subreddit, after, before), retries);
    }

    /**
     * Requests a single submission.
     * @param submissionId the id of the submission.
     * @return the submission instance.
     * @throws UnresolvableRequestException if the API returned an unresolvable error.
     */
    default Optional<SubmissionInterface> requestSubmission(String submissionId) throws UnresolvableRequestException{
        throw new UnsupportedOperationException("Not implemented for this interface");
    }

    /**
     * Requests a single submission.
     * @param submissionId the id of the submission.
     * @param retries the number of times the request is repeated upon failure.
     * @return the submission instance.
     * @throws UnresolvableRequestException if the API returned an unresolvable error.
     */
    default Optional<SubmissionInterface> requestSubmission(String submissionId, int retries){
        return request(() -> requestSubmission(submissionId), retries);
    }
    /**
     * @param submission the id of the submission.
     * @param retries the number of times the request is repeated upon failure.
     * @return all comments of the submission.
     * @throws UnresolvableRequestException if the API returned an unresolvable error.
     */
    default Optional<List<CommentInterface>> requestComment(String submission, int retries) throws UnresolvableRequestException{
        return request(() -> requestComment(submission), retries);
    }

    /**
     * @param submission the id of the submission.
     * @return all comments of the submission.
     * @throws UnresolvableRequestException if the API returned an unresolvable error.
     */
    default Optional<List<CommentInterface>> requestComment(String submission) throws UnresolvableRequestException{
        throw new UnsupportedOperationException("Not implemented for this interface");
    }

    /**
     * A wrapper for the requests that cleans up the messy exceptions from JRAW.
     * @param request the request that is executed.
     * @param <T> the type of the return value.
     * @return whatever the requests returns.
     * @throws UnresolvableRequestException if the API returned an unresolvable error.
     */
    default <T> Optional<T> request(Supplier<Optional<T>> request, int attempts){
        Optional<T> result = Optional.empty();
        while(attempts > 0 && !result.isPresent()){
            result = request.get();
            attempts--;
        }
        return result;
    }
}
