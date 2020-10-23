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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Range;
import com.google.common.util.concurrent.UncheckedExecutionException;
import org.apache.http.client.HttpResponseException;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public abstract class Subreddit extends SubredditTOP{
    /**
     * This cache serves as a buffer for previous requests. In order to minimize the communication with the Reddit
     * API, requests over the same range should only be done once. All further requests should reuse the cached data.
     * Subranges are treated as independent entities.
     */
    protected Cache<Range<Instant>, Range<Instant>> memory = CacheBuilder.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(5))
            .build();

    @Nonnull
    @Override
    public List<Submission> getUncheckedSubmissions(@Nonnull Instant inclusiveFrom, @Nonnull Instant exclusiveTo){
        try{
            return getSubmissions(inclusiveFrom, exclusiveTo);
        }catch(UnsuccessfulRequestException | HttpResponseException e){
            throw new UncheckedExecutionException(e);
        }
    }
}
