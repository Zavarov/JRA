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

import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import vartas.reddit.AbstractTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class JrawClientTest extends AbstractTest {
    @Before
    public void setUp(){
        client.login();
    }
    @After
    public void tearDown(){
        client.logout();
    }
    @Test(expected=IllegalStateException.class)
    public void loginTwiceTest(){
        client.login();
    }
    @Test
    public void requestUserTest() throws HttpResponseException {
        assertThat(client.requestUser("Zavarov", 1)).isPresent();
    }
    @Test
    public void requestSubredditTest() throws HttpResponseException {
        assertThat(client.requestSubreddit("RedditDev", 1)).isPresent();
    }
    @Test
    public void requestSubmissionTest() throws HttpResponseException {
        LocalDateTime instant = LocalDateTime.now(ZoneId.of("UTC"));
        LocalDateTime before = instant;
        LocalDateTime after = instant.minus(1, ChronoUnit.HOURS);
        //System.out.println(instant);
        //client.requestSubmission("discordapp", after, before, 1).orElseThrow().stream().map(s -> s.getCreated() + ", "+s.getTitle()).forEach(System.out::println);
        assertThat(client.requestSubmission("RedditDev", after, before, 1)).isPresent();
    }
    @Test
    public void requestCommentTest() throws HttpResponseException {
        //https://www.reddit.com/r/announcements/comments/blev4z/how_to_keep_your_reddit_account_safe/
        assertThat(client.requestComment("blev4z", 1)).isPresent();
    }
    @Test
    public void requestSubmissionByIdTest() throws HttpResponseException {
        //https://www.reddit.com/r/announcements/comments/blev4z/how_to_keep_your_reddit_account_safe/
        assertThat(client.requestSubmission("blev4z", 1)).isPresent();
    }
    @Test
    public void requestInvalidUserTest() throws HttpResponseException {
        assertThat(client.requestUser("#####", 1)).isEmpty();
    }
    @Test(expected=HttpResponseException.class)
    public void requestInvalidSubredditTest() throws HttpResponseException {
        client.requestSubreddit("#####", 1);
    }
    @Test(expected=HttpResponseException.class)
    public void requestInvalidSubmissionTest() throws HttpResponseException {
        LocalDateTime instant = LocalDateTime.now(ZoneId.of("UTC"));
        LocalDateTime before = instant;
        LocalDateTime after = instant.minus(1, ChronoUnit.HOURS);
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
}
