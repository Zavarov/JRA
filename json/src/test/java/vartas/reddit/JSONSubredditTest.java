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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class JSONSubredditTest extends AbstractTest{
    public static Subreddit subreddit;
    @BeforeAll
    public static void setUpAll(){
        AbstractTest.setUpAll();
        subreddit = client.getUncheckedSubreddits("RedditDev");
    }

    @Test
    public void testGetSubmissions() throws UnsuccessfulRequestException, HttpResponseException {
        Instant exclusiveTo = Instant.now();
        Instant inclusiveFrom = exclusiveTo.minus(1, ChronoUnit.DAYS);
        subreddit.getSubmissions(inclusiveFrom, exclusiveTo);
    }
}
