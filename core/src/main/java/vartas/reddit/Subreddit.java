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

import org.apache.http.client.HttpResponseException;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class Subreddit extends SubredditTOP{
    /**
     * The instant indicating the last time, submissions from the associated {@link Subreddit}
     * have been requested.
     */
    protected Instant now = Instant.now();

    /**
     * Reddit's API is a huge mess, so we can't request submissions within a specific interval.<br>
     * Instead we cache all submissions up to {@link #now}.
     */
    protected void request() throws UnsuccessfulRequestException, TimeoutException, HttpResponseException {
        throw new UnsupportedOperationException();
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
    public List<Submission> getSubmissions(Instant inclusiveFrom, Instant exclusiveTo) throws UnsuccessfulRequestException, TimeoutException, HttpResponseException {
        //Update the cache if there might be new submissions.
        if(now.isBefore(exclusiveTo)) {
            request();
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
}
