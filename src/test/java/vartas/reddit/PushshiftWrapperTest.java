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

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.Arrays;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import vartas.offlinejraw.OfflineCommentListingResponse;
import vartas.offlinejraw.OfflineCommentListingResponse.OfflineComment;
import vartas.offlinejraw.OfflineSubmissionListingResponse;
import vartas.offlinejraw.OfflineSubmissionListingResponse.OfflineSubmission;
import vartas.reddit.PushshiftWrapper.CompactComment;
import vartas.reddit.PushshiftWrapper.CompactSubmission;

/**
 *
 * @author u/Zavarov
 */
public class PushshiftWrapperTest {
    static String json = "{\"data\": [{\"id\": \"submission\"}]}";
        
    static OfflineSubmission sub = new OfflineSubmission()
            .addNumComments(3)
            .addThumbnailHeight(0)
            .addThumbnailWidth(0)
            .addThumbnail(null)
            .addSelftext("selftext")
            .addLinkFlairText("flair")
            .addOver18(true)
            .addSpoilerTest(true)
            .addLikes()
            .addUrl("sub1.jpg")
            .addTitle("title")
            .addSubredditId("subreddit_id")
            .addSubreddit("subreddit")
            .addPermalink("permalink")
            .addId("id0")
            .addName("submission")
            .addAuthor("author")
            .addDomain("i.redd.it")
            .addDistinguished()
            .addCreatedUtc(946684800);
        
    static OfflineSubmission sub2 = new OfflineSubmission()
            .addNumComments(0)
            .addThumbnailHeight(0)
            .addThumbnailWidth(0)
            .addThumbnail(null)
            .addSelftext("selftext")
            .addLinkFlairText(null)
            .addOver18(true)
            .addSpoilerTest(true)
            .addLikes()
            .addUrl("sub2.jpg")
            .addTitle("title")
            .addSubredditId("subreddit_id")
            .addSubreddit("subreddit")
            .addPermalink("permalink")
            .addId("id1")
            .addName("submission2")
            .addAuthor("author")
            .addDomain("i.redd.it")
            .addDistinguished()
            .addCreatedUtc(946684800);

    static OfflineComment com1 = new OfflineComment()
            .addNumComments(3)
            .addSubredditType("public")
            .addLinkId("link")
            .addParentId("parent")
            .addBody("content")
            .addReplies()
            .addLikes()
            .addUrl("url")
            .addTitle("title")
            .addSubredditId("subreddit_id")
            .addSubreddit("subreddit")
            .addPermalink("permalink")
            .addName("name")
            .addDomain("i.redd.it")
            .addDistinguished()
            .addCreatedUtc(946684800)
            .addAuthor("author")
            .addId("id1");

    static OfflineComment com2 = new OfflineComment()
            .addNumComments(3)
            .addSubredditType("public")
            .addLinkId("link")
            .addParentId("parent")
            .addBody("content")
            .addReplies()
            .addLikes()
            .addUrl("url")
            .addTitle("title")
            .addSubredditId("subreddit_id")
            .addSubreddit("subreddit")
            .addPermalink("permalink")
            .addName("name")
            .addDomain("i.redd.it")
            .addDistinguished()
            .addCreatedUtc(946684800)
            .addAuthor("author")
            .addId("id2");

    static OfflineComment com3 = new OfflineComment()
            .addNumComments(3)
            .addSubredditType("public")
            .addLinkId("link")
            .addParentId("parent")
            .addBody("content")
            .addReplies(com2)
            .addLikes()
            .addUrl("url")
            .addTitle("title")
            .addSubredditId("subreddit_id")
            .addSubreddit("subreddit")
            .addPermalink("permalink")
            .addName("name")
            .addDomain("i.redd.it")
            .addDistinguished()
            .addCreatedUtc(946684800)
            .addAuthor("author")
            .addId("id3");

    static OfflineRedditBot bot = new OfflineRedditBot();
    static CompactSubmission submission = new CompactSubmission();
    static CompactComment comment = new CompactComment();
    
    PushshiftWrapper wrapper;
    Instant instant = Instant.ofEpochMilli(0);
    String comments;
    String submissions;
    
    @BeforeClass
    public static void startUp(){
        bot.adapter.addResponse(new OfflineCommentListingResponse()
                .addComment(com1)
                .addComment(com3)
                .addSubmission(sub,"subreddit","submission")
                .addAfter(null)
                .addAfterRequest(null)
                .addLimit(1)
                .addSort("new")
                .build());
        
        bot.adapter.addResponse(new OfflineSubmissionListingResponse()
                .addSubreddit("subreddit")
                .addSubmission(sub2)
                .addLimit(1)
                .addAfter(null)
                .addAfterRequest(null)
                .addSort("new")
                .build());
        
        comment.put("author","author");
        comment.put("id","id");
        comment.put("score","1");
        comment.put("submission","submission");
        comment.put("subreddit","subreddit");
        comment.put("title","title");
        
        submission.put("author","author");
        submission.put("id","id");
        submission.put("score","1");
        submission.put("title","title");
        submission.put("link_flair_text","flair");
        submission.put("subreddit","subreddit");
        submission.put("over18", "true");
        submission.put("spoiler","true");
    }
    
    @Before
    public void setUp() throws ParseException{
        wrapper = new PushshiftWrapper(bot);
        instant = PushshiftWrapper.DATE.parse("01-01-2000").toInstant();
        
        comments = PushshiftWrapper.COMMENTS_FILE;
        submissions = PushshiftWrapper.SUBMISSIONS_FILE;
        PushshiftWrapper.COMMENTS_FILE = "src/test/resources/comments";
        PushshiftWrapper.SUBMISSIONS_FILE = "src/test/resources/submissions";
    }
    @After
    public void tearDown() throws IOException{
        FileUtils.deleteDirectory(new File("src/test/resources/comments"));
        FileUtils.deleteDirectory(new File("src/test/resources/submissions"));
        
        PushshiftWrapper.COMMENTS_FILE = comments;
        PushshiftWrapper.SUBMISSIONS_FILE = submissions;
    }
    
    @Test
    public void parameterTest(){
        Instant before = Instant.ofEpochMilli(0);
        Instant after = Instant.ofEpochMilli(0);
        assertNull(wrapper.after);
        assertNull(wrapper.before);
        assertNull(wrapper.subreddit);
        wrapper.parameter("subreddit", before, after);
        assertEquals(wrapper.subreddit, "subreddit");
        assertEquals(wrapper.before, before);
        assertEquals(wrapper.after, after);
    }
    @Test
    public void getSubmissionsTest(){
        PushshiftWrapper.COMMENTS_FILE = "src/test/resources/comments_fix";
        PushshiftWrapper.SUBMISSIONS_FILE = "src/test/resources/submissions_fix";
        assertEquals(Arrays.asList(submission),wrapper.getSubmissions(instant, "subreddit"));
    }
    @Test
    public void getSubmissionsEmptyTest(){
        assertTrue(wrapper.getSubmissions(instant, "junk").isEmpty());
    }
    @Test
    public void getCommentsTest(){
        PushshiftWrapper.COMMENTS_FILE = "src/test/resources/comments_fix";
        PushshiftWrapper.SUBMISSIONS_FILE = "src/test/resources/submissions_fix";
        assertEquals(Arrays.asList(comment),wrapper.getComments(instant, "subreddit"));
    }
    @Test
    public void getCommentsEmptyTest(){
        assertTrue(wrapper.getComments(instant, "junk").isEmpty());
    }
    @Test
    public void createUrlTest(){
        wrapper.parameter("subreddit", Instant.ofEpochSecond(1),Instant.ofEpochSecond(2));
        String url = String.format("https://api.pushshift.io/reddit/search/submission/?subreddit=subreddit&after=2&before=1&sort=desc&size=500");
        assertEquals(url,wrapper.createUrl());
    }
    @Test
    public void requestJsonContentTest() throws IOException{
        //1st January 2018 00:00:00
        wrapper.parameter("subreddit", Instant.ofEpochSecond(1514764800), Instant.ofEpochSecond(1514764800));
        assertEquals("{    \"data\": []}",wrapper.requestJsonContent());
    }
    @Test
    public void extractIdsTest(){
        assertEquals(wrapper.extractIds(json),Arrays.asList("submission"));
    }
    @Test
    public void requestTest() throws IOException, InterruptedException{
        wrapper = new PushshiftWrapper(bot){
            @Override
            public String requestJsonContent(){
                return json;
            }
        };
        
        assertFalse(new File("src/test/resources/submissions/subreddit/01-01-2000.sub").exists());
        assertFalse(new File("src/test/resources/comments/subreddit/01-01-2000.com").exists());
        
        wrapper.parameter("subreddit", instant.plus(1, DAYS), instant);
        
        wrapper.request();
        
        assertTrue(new File("src/test/resources/submissions/subreddit/01-01-2000.sub").exists());
        assertTrue(new File("src/test/resources/comments/subreddit/01-01-2000.com").exists());
    }
}