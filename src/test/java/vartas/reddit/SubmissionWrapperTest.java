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

import java.util.Date;
import java.util.List;
import net.dean.jraw.models.Submission;
import net.dean.jraw.pagination.Paginator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import vartas.offlinejraw.OfflineSubmissionListingResponse;

/**
 *
 * @author u/Zavarov
 */
public class SubmissionWrapperTest {
    OfflineRedditBot bot;
    SubmissionWrapper wrapper;
    @Before
    public void setUp(){
        bot = new OfflineRedditBot();
        OfflineSubmissionListingResponse.OfflineSubmission sub1 = new OfflineSubmissionListingResponse.OfflineSubmission()
                .addNumComments(10)
                .addThumbnailHeight(0)
                .addThumbnailWidth(0)
                .addThumbnail(null)
                .addSelftext("selftext")
                .addLinkFlairText("flair")
                .addOver18(true)
                .addSpoilerTest(true)
                .addLikes()
                .addUrl("sub1.jpg")
                .addTitle("title1")
                .addSubredditId("subreddit_id")
                .addSubreddit("subreddit")
                .addPermalink("permalink1")
                .addId("id1")
                .addName("submission1")
                .addAuthor("author1")
                .addDomain("i.redd.it")
                .addDistinguished()
                .addCreatedUtc(1);
        
        OfflineSubmissionListingResponse.OfflineSubmission sub2 = new OfflineSubmissionListingResponse.OfflineSubmission()
                .addNumComments(10)
                .addThumbnailHeight(10)
                .addThumbnailWidth(10)
                .addThumbnail("https://www.reddit.com/thumbnail4.jpg")
                .addSelftext("selftext")
                .addLinkFlairText("flair")
                .addOver18(true)
                .addSpoilerTest(true)
                .addLikes()
                .addUrl("sub2.jpg")
                .addTitle("title2")
                .addSubredditId("subreddit_id")
                .addSubreddit("subreddit")
                .addPermalink("permalink2")
                .addId("id2")
                .addName("submission2")
                .addAuthor("author2")
                .addDomain("i.redd.it")
                .addDistinguished()
                .addCreatedUtc(2);
        
        
        bot.adapter.addResponse(new OfflineSubmissionListingResponse()
                .addSubmission(sub1)
                .addSubreddit("subreddit")
                .addAfter("submission1")
                .addAfterRequest(null)
                .addLimit(Paginator.RECOMMENDED_MAX_LIMIT)
                .addSort("new")
                .build());
        bot.adapter.addResponse(new OfflineSubmissionListingResponse()
                .addSubmission(sub2)
                .addSubreddit("subreddit")
                .addAfter("submission2")
                .addAfterRequest("submission1")
                .addLimit(Paginator.RECOMMENDED_MAX_LIMIT)
                .addSort("new")
                .build());
        bot.adapter.addResponse(new OfflineSubmissionListingResponse()
                .addSubreddit("subreddit")
                .addAfterRequest("submission2")
                .addLimit(Paginator.RECOMMENDED_MAX_LIMIT)
                .addSort("new")
                .build());
        wrapper = new SubmissionWrapper(bot);
    }
    @Test
    public void parameterTest(){
        assertNull(wrapper.subreddit);
        assertNull(wrapper.start);
        assertNull(wrapper.end);
        wrapper.parameter("subreddit", new Date(0), new Date(1));
        assertEquals(wrapper.subreddit,"subreddit");
        assertEquals(wrapper.start,new Date(0));
        assertEquals(wrapper.end,new Date(1));
    }
    @Test
    public void requestTest(){
        wrapper.parameter("subreddit", new Date(0), new Date(3000));
        List<Submission> submissions = wrapper.request();
        assertEquals(submissions.size(),2);
        Submission submission;
        submission = submissions.get(0);
        assertEquals(submission.getId(),"id1");
        submission = submissions.get(1);
        assertEquals(submission.getId(),"id2");
    }
    @Test
    public void requestTooMuchTest(){
        wrapper.parameter("subreddit", new Date(0), new Date(2000));
        List<Submission> submissions = wrapper.request();
        assertEquals(submissions.size(),1);
        Submission submission;
        submission = submissions.get(0);
        assertEquals(submission.getId(),"id1");
    }
}
