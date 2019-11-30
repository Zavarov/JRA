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

package vartas.reddit.pushshift;

import org.apache.http.client.HttpResponseException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import vartas.reddit.AbstractTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class PushshiftClientTest extends AbstractTest {
    @BeforeClass
    public static void setUpBeforeClass(){
        client = getPushshiftClient();
    }
    @Before
    public void setUp(){
        client.login();
    }
    @After
    public void tearDown(){
        client.logout();
    }
    @Test
    public void requestSubmission() throws HttpResponseException {
        LocalDateTime instant = LocalDateTime.now(ZoneId.of("UTC"));
        LocalDateTime before = instant;
        LocalDateTime after = instant.minus(1, ChronoUnit.DAYS);
        assertThat(client.requestSubmission("RedditDev", after, before, 1)).isPresent();
    }
}
