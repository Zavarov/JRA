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

import org.apache.http.HttpStatus;
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

        assertThat(submission.getComments()).isNotEmpty();
    }

    @Test
    public void testGetSubmissions() throws UnsuccessfulRequestException, TimeoutException, HttpResponseException {
        Instant inclusiveFrom = Instant.now().minus(1, ChronoUnit.MINUTES);
        Instant exclusiveTo = Instant.now();
        subreddit.getSubmissions(inclusiveFrom, exclusiveTo);
    }

    @Test
    public void testGetInvalidSubmission() {
        ExecutionException exception = assertThrows(
                ExecutionException.class ,
                () -> subreddit.getSubmissions("#####")
        );
        assertThat(exception.getCause()).isInstanceOf(HttpResponseException.class);

        HttpResponseException response = (HttpResponseException)exception.getCause();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SC_NOT_FOUND);
    }


    /*
    @Test
    public void requestSubmissionTest() throws HttpResponseException {
        LocalDateTime before = LocalDateTime.now(ZoneId.of("UTC"));
        LocalDateTime after = before.minus(1, ChronoUnit.HOURS);
        //System.out.println(instant);
        //client.requestSubmission("discordapp", after, before, 1).orElseThrow().stream().map(s -> s.getCreated() + ", "+s.getTitle()).forEach(System.out::println);
        Assertions.assertThat(client.requestSubmission("RedditDev", after, before, 1)).isPresent();
    }
    @Test
    public void requestCommentTest() throws HttpResponseException {
        //https://www.reddit.com/r/announcements/comments/blev4z/how_to_keep_your_reddit_account_safe/
        Assertions.assertThat(client.requestComment("blev4z", 1)).isPresent();
    }
    @Test
    public void requestSubmissionByIdTest() throws HttpResponseException {
        //https://www.reddit.com/r/announcements/comments/blev4z/how_to_keep_your_reddit_account_safe/
        Assertions.assertThat(client.requestSubmission("blev4z", 1)).isPresent();
    }
    @Test(expected=HttpResponseException.class)
    public void requestInvalidSubmissionTest() throws HttpResponseException {
        LocalDateTime before = LocalDateTime.now(ZoneId.of("UTC"));
        LocalDateTime after = before.minus(1, ChronoUnit.HOURS);
        client.requestSubmission("#####", after, before, 1);
    }
    @Test(expected=HttpResponseException.class)
    public void requestInvalidCommentTest() throws HttpResponseException {
        client.requestComment("#####", 1);
    }
    @Test(expected=HttpResponseException.class)
    public void requestSubmissionByInvalidIdTest() throws HttpResponseException {
        client.requestSubmission("#####", 1);
    }
    @Test
    public void handleUnauthorizedTest() throws HttpResponseException {
        ((JrawClient)client).handle(HttpStatus.SC_UNAUTHORIZED, "Unauthorized");
    }
    @Test(expected=HttpResponseException.class)
    public void handleTest() throws HttpResponseException {
        ((JrawClient)client).handle(HttpStatus.SC_NOT_FOUND,"Not Found");
    }
    */
}
