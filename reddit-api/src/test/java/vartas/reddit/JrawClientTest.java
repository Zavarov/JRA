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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class JrawClientTest extends AbstractTest{
    @Before
    public void setUp(){
        client.login();
    }
    @After
    public void tearDown(){
        client.logout();
    }
    @Test(expected=IllegalStateException.class)
    public void testLoginTwice(){
        client.login();
    }
    @Test
    public void requestUser(){
        assertThat(client.requestUser("Zavarov", 1)).isPresent();
    }
    @Test
    public void requestSubreddit(){
        assertThat(client.requestSubreddit("RedditDev", 1)).isPresent();
    }
    @Test
    public void requestSubmission(){
        Instant instant = Instant.now();
        Date before = Date.from(instant);
        Date after = Date.from(instant.minus(1, ChronoUnit.DAYS));
        assertThat(client.requestSubmission("RedditDev", after, before, 1)).isPresent();
    }
    @Test
    public void requestComment(){
        //https://www.reddit.com/r/announcements/comments/blev4z/how_to_keep_your_reddit_account_safe/
        assertThat(client.requestComment("blev4z", 1)).isPresent();
    }
    @Test
    public void requestSubmissionId(){
        //https://www.reddit.com/r/announcements/comments/blev4z/how_to_keep_your_reddit_account_safe/
        assertThat(client.requestSubmission("blev4z", 1)).isPresent();
    }
}
