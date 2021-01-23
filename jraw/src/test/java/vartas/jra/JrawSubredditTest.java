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

package vartas.jra;

import org.apache.http.client.HttpResponseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JrawSubredditTest extends AbstractTest{
    public static Subreddit subreddit;
    @BeforeAll
    public static void setUpAll(){
        AbstractTest.setUpAll();
        subreddit = client.getUncheckedSubreddits("RedditDev");
    }

    @Test
    public void testGetSubmission(){
        //https://www.reddit.com/r/announcements/comments/blev4z/how_to_keep_your_reddit_account_safe/
        Submission submission = subreddit.getUncheckedSubmissions("blev4z");

        assertThat(submission.getAllComments()).isNotEmpty();
    }

    @Test
    public void testGetSubmissions() throws UnsuccessfulRequestException, HttpResponseException {
        Instant exclusiveTo = Instant.now();
        Instant inclusiveFrom = exclusiveTo.minus(1, ChronoUnit.DAYS);
        subreddit.getSubmissions(inclusiveFrom, exclusiveTo);
    }

    @Test
    public void testGetInvalidSubmission() {
        ExecutionException exception = assertThrows(
                ExecutionException.class ,
                () -> subreddit.getSubmissions("#####")
        );
        assertThat(exception.getCause()).isInstanceOf(ClientException.class);
    }
}
