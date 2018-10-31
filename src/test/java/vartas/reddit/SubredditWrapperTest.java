/*
 * Copyright (C) 2018 u/Zavarov
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

import net.dean.jraw.models.Subreddit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import vartas.offlinejraw.OfflineSubredditResponse;

/**
 *
 * @author u/Zavarov
 */
public class SubredditWrapperTest {
    OfflineRedditBot bot;
    SubredditWrapper wrapper;
    @Before
    public void setUp(){
        bot = new OfflineRedditBot();
        bot.adapter.addResponse(new OfflineSubredditResponse()
                        .addName("abcde")
                        .addAccountsActive(1)
                        .addBannerImg("banner.jpg")
                        .addCreatedUtc(2)
                        .addDisplayName("subreddit")
                        .addId("id")
                        .addPublicDescription("desc")
                        .addSubscribers(3)
                        .addTitle("title")
                        .addUrl("r/subreddit").build());
        wrapper = new SubredditWrapper(bot);
    }
    @Test
    public void parameterTest(){
        assertNull(wrapper.subreddit);
        wrapper.parameter("subreddit");
        assertEquals(wrapper.subreddit,"subreddit");
    }
    @Test
    public void requestTest(){
        wrapper.parameter("subreddit");
        Subreddit subreddit = wrapper.request();
        assertEquals(subreddit.getFullName(),"abcde");
        assertEquals(subreddit.getAccountsActive().intValue(),1);
        assertEquals(subreddit.getBannerImage(),"banner.jpg");
        assertEquals(subreddit.getCreated().getTime(),2000);
        assertEquals(subreddit.getName(),"subreddit");
        assertEquals(subreddit.getId(),"id");
        assertEquals(subreddit.getPublicDescription(),"desc");
        assertEquals(subreddit.getSubscribers(),3);
        assertEquals(subreddit.getTitle(),"title");
        assertEquals(subreddit.getUrl(),"r/subreddit");
    }
}
