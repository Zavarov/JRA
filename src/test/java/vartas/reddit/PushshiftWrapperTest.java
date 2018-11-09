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

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Sets;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import vartas.offlinejraw.OfflineCommentListingResponse;
import vartas.offlinejraw.OfflineSubmissionListingResponse;
import vartas.reddit.PushshiftWrapper.CompactComment;
import vartas.reddit.PushshiftWrapper.CompactSubmission;

/**
 *
 * @author u/Zavarov
 */
public class PushshiftWrapperTest {
    String json = "{\"data\": [{\"id\": \"submission\"}]}";
        
    OfflineSubmissionListingResponse.OfflineSubmission sub = new OfflineSubmissionListingResponse.OfflineSubmission()
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
            .addCreatedUtc(1L);
        
    OfflineSubmissionListingResponse.OfflineSubmission sub2 = new OfflineSubmissionListingResponse.OfflineSubmission()
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
            .addCreatedUtc(2L);

    OfflineCommentListingResponse.OfflineComment com1 = new OfflineCommentListingResponse.OfflineComment()
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
            .addCreatedUtc(1L)
            .addAuthor("author")
            .addId("id1");

    OfflineCommentListingResponse.OfflineComment com2 = new OfflineCommentListingResponse.OfflineComment()
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
            .addCreatedUtc(1L)
            .addAuthor("author")
            .addId("id2");

    OfflineCommentListingResponse.OfflineComment com3 = new OfflineCommentListingResponse.OfflineComment()
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
            .addCreatedUtc(1L)
            .addAuthor("author")
            .addId("id3");

    OfflineRedditBot bot = new OfflineRedditBot();
    
    PushshiftWrapper wrapper;
    Instant instant = Instant.ofEpochMilli(0);
    CompactSubmission submission = new CompactSubmission();
    CompactComment comment = new CompactComment();
    String comments;
    String submissions;
    @Before
    public void setUp(){
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
        
        wrapper = new PushshiftWrapper(bot);
        wrapper.comments.putSingle(instant, "subreddit", comment);
        wrapper.submissions.putSingle(instant, "subreddit", submission);
        
        comments = PushshiftWrapper.COMMENTS_FILE;
        submissions = PushshiftWrapper.SUBMISSIONS_FILE;
        PushshiftWrapper.COMMENTS_FILE = "src/test/resources/comments.xml";
        PushshiftWrapper.SUBMISSIONS_FILE = "src/test/resources/submissions.xml";
    }
    @After
    public void cleanUp(){
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
    public void removeTest(){
        assertFalse(wrapper.submissions.isEmpty());
        assertFalse(wrapper.comments.isEmpty());
        wrapper.remove(instant, "subreddit");
        assertTrue(wrapper.submissions.isEmpty());
        assertTrue(wrapper.comments.isEmpty());
    }
    
    @Test
    public void removeSubredditTest(){
        assertFalse(wrapper.submissions.isEmpty());
        assertFalse(wrapper.comments.isEmpty());
        wrapper.removeSubreddit("subreddit");
        assertTrue(wrapper.submissions.isEmpty());
        assertTrue(wrapper.comments.isEmpty());
    }
    
    @Test
    public void removeDateTest(){
        assertFalse(wrapper.submissions.isEmpty());
        assertFalse(wrapper.comments.isEmpty());
        wrapper.removeDate(instant);
        assertTrue(wrapper.submissions.isEmpty());
        assertTrue(wrapper.comments.isEmpty());
    }
    @Test
    public void getSubmissionsDateTest(){
        LinkedListMultimap<String,CompactSubmission> map = LinkedListMultimap.create();
        map.put("subreddit",submission);
        assertEquals(map,wrapper.getSubmissions(instant));
    }
    @Test
    public void getSubmissionsDateEmptyTest(){
        wrapper.submissions.clear();
        assertTrue(wrapper.getSubmissions(instant).isEmpty());
    }
    @Test
    public void getSubmissionsSubredditTest(){
        LinkedListMultimap<Instant,CompactSubmission> map = LinkedListMultimap.create();
        map.put(instant,submission);
        assertEquals(map,wrapper.getSubmissions("subreddit"));
    }
    @Test
    public void getSubmissionsSubredditEmptyTest(){
        wrapper.submissions.clear();
        assertTrue(wrapper.getSubmissions("subreddit").isEmpty());
    }
    @Test
    public void getSubmissionsDateSubredditTest(){
        assertEquals(Arrays.asList(submission),wrapper.getSubmissions(instant, "subreddit"));
    }
    @Test
    public void getSubmissionsDateSubredditEmptyTest(){
        wrapper.submissions.clear();
        assertTrue(wrapper.getSubmissions(instant, "subreddit").isEmpty());
    }
    @Test
    public void getCommentsDateTest(){
        LinkedListMultimap<String,CompactComment> map = LinkedListMultimap.create();
        map.put("subreddit",comment);
        assertEquals(map,wrapper.getComments(instant));
    }
    @Test
    public void getCommentsDateEmptyTest(){
        wrapper.comments.clear();
        assertTrue(wrapper.getComments(instant).isEmpty());
    }
    @Test
    public void getCommentsSubredditTest(){
        LinkedListMultimap<Instant,CompactComment> map = LinkedListMultimap.create();
        map.put(instant,comment);
        assertEquals(map,wrapper.getComments("subreddit"));
    }
    @Test
    public void getCommentsSubredditEmptyTest(){
        wrapper.comments.clear();
        assertTrue(wrapper.getComments("subreddit").isEmpty());
    }
    @Test
    public void getCommentsDateSubredditTest(){
        assertEquals(Arrays.asList(comment),wrapper.getComments(instant, "subreddit"));
    }
    @Test
    public void getCommentsDateSubredditEmptyTest(){
        wrapper.comments.clear();
        assertTrue(wrapper.getComments(instant, "subreddit").isEmpty());
    }
    @Test
    public void createUrlTest(){
        wrapper.parameter("subreddit", Instant.ofEpochSecond(1),Instant.ofEpochSecond(2));
        String url = String.format("https://api.pushshift.io/reddit/search/submission/?subreddit=subreddit&after=2&before=1&sort=desc&size=500");
        assertEquals(url,wrapper.createUrl());
    }
    @Test
    public void getDatesTest(){
        assertEquals(Arrays.asList(instant),wrapper.getDates());
    }
    @Test
    public void getSubredditsTest(){
        assertEquals(Arrays.asList("subreddit"),wrapper.getSubreddits());
    }
    @Test
    public void storeEmptyStringTest() throws IOException, InterruptedException, ClassNotFoundException{
        submission.put("link_flair_text", "");
        PushshiftWrapper.COMMENTS_FILE = "src/test/resources/comments_store.xml";
        PushshiftWrapper.SUBMISSIONS_FILE = "src/test/resources/submissions_store.xml";
        wrapper.store();
        
        wrapper.read();
        assertEquals(wrapper.submissions.size(),1);
        assertTrue(wrapper.submissions.values().stream().flatMap(e -> e.stream()).collect(Collectors.toList()).contains(submission));
    }
    @Test
    public void storeTest() throws IOException, InterruptedException, ClassNotFoundException{
        PushshiftWrapper.COMMENTS_FILE = "src/test/resources/comments_store.xml";
        PushshiftWrapper.SUBMISSIONS_FILE = "src/test/resources/submissions_store.xml";
        wrapper.store();
        
        wrapper.read();
        assertEquals(wrapper.submissions.size(),1);
        assertTrue(wrapper.submissions.values().stream().flatMap(e -> e.stream()).collect(Collectors.toList()).contains(submission));
    }
    @Test
    public void storeNewTest() throws IOException, InterruptedException, ClassNotFoundException{
        PushshiftWrapper.COMMENTS_FILE = "src/test/resources/comments_new.xml";
        PushshiftWrapper.SUBMISSIONS_FILE = "src/test/resources/submissions_new.xml";
        wrapper.store();
        
        wrapper.read();
        assertEquals(wrapper.submissions.size(),1);
        assertTrue(wrapper.submissions.values().stream().flatMap(e -> e.stream()).collect(Collectors.toList()).contains(submission));
        
        File file;
        file = new File(PushshiftWrapper.COMMENTS_FILE);
        file.delete();
        file = new File(PushshiftWrapper.SUBMISSIONS_FILE);
        file.delete();
    }
    @Test
    public void readTest() throws IOException, ClassNotFoundException{
        PushshiftWrapper.COMMENTS_FILE = "src/test/resources/comments.xml";
        PushshiftWrapper.SUBMISSIONS_FILE = "src/test/resources/submissions.xml";
        wrapper.comments.clear();
        wrapper.submissions.clear();
        
        wrapper.read();
        
        assertTrue(wrapper.submissions.values().stream().flatMap(e -> e.stream()).collect(Collectors.toList()).contains(submission));
        assertTrue(wrapper.comments.values().stream().flatMap(e -> e.stream()).collect(Collectors.toList()).contains(comment));
    }
    @Test
    public void readEmptyTest() throws IOException, ClassNotFoundException{
        wrapper.comments.clear();
        wrapper.submissions.clear();
        PushshiftWrapper.COMMENTS_FILE = "src/test/resources/comments_new.xml";
        PushshiftWrapper.SUBMISSIONS_FILE = "src/test/resources/submissions_new.xml";
        
        assertTrue(wrapper.comments.isEmpty());
        assertTrue(wrapper.submissions.isEmpty());
        
        wrapper.read();
        
        assertTrue(wrapper.comments.isEmpty());
        assertTrue(wrapper.submissions.isEmpty());
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
    public void requestTest() throws IOException{
        wrapper = new PushshiftWrapper(bot){
            @Override
            public String requestJsonContent(){
                return json;
            }
        };
        
        assertTrue(wrapper.comments.isEmpty());
        assertTrue(wrapper.submissions.isEmpty());
        
        wrapper.parameter("subreddit", Instant.ofEpochSecond(2), Instant.ofEpochSecond(1));
        wrapper.request();
        
        assertEquals(wrapper.submissions.size(),1);
        assertEquals(wrapper.comments.values().stream().flatMap(i -> i.stream()).count(),3);
    
        Set<String> ids = wrapper.submissions.values().stream().flatMap(e -> e.stream()).map(e -> e.getId()).collect(Collectors.toSet());
        assertEquals(ids, Sets.newHashSet("id0"));
        
        ids = wrapper.comments.values().stream().flatMap(e -> e.stream()).map(e -> e.getId()).collect(Collectors.toSet());
        assertEquals(ids, Sets.newHashSet("id1","id2","id3"));
        
        assertEquals(wrapper.submissions.values().iterator().next().get(0).getLinkFlairText(),"flair");
    }
    @Test
    public void requestNullTest() throws IOException{
        List<Submission> submissions = bot.getClient().subreddit("subreddit")
                .posts()
                .limit(1)
                .sorting(SubredditSort.NEW)
                .timePeriod(TimePeriod.ALL)
                .build()
                .accumulateMerged(1);
        
        assertEquals(submissions.size(),1);
        CompactSubmission submission = wrapper.extractData(submissions.get(0));
        assertEquals(submission.getLinkFlairText(),"");
    }
    @Test
    public void requestAndStore() throws IOException, InterruptedException, ClassNotFoundException{
        PushshiftWrapper.COMMENTS_FILE = "src/test/resources/comments_store.xml";
        PushshiftWrapper.SUBMISSIONS_FILE = "src/test/resources/submissions_store.xml";
        wrapper = new PushshiftWrapper(bot){
            @Override
            public String requestJsonContent(){
                return json;
            }
        };
        
        assertTrue(wrapper.comments.isEmpty());
        assertTrue(wrapper.submissions.isEmpty());
        
        wrapper.parameter("subreddit", Instant.ofEpochSecond(2), Instant.ofEpochSecond(1));
        wrapper.request();
        wrapper.store();
        
        wrapper.submissions.clear();
        wrapper.comments.clear();
        
        wrapper.read();
        
        assertEquals(wrapper.submissions.size(),1);
        assertEquals(wrapper.comments.values().stream().flatMap(i -> i.stream()).count(),3);
    
        Set<String> ids = wrapper.submissions.values().stream().flatMap(e -> e.stream()).map(e -> e.getId()).collect(Collectors.toSet());
        assertEquals(ids, Sets.newHashSet("id0"));
        
        ids = wrapper.comments.values().stream().flatMap(e -> e.stream()).map(e -> e.getId()).collect(Collectors.toSet());
        assertEquals(ids, Sets.newHashSet("id1","id2","id3"));
        
        assertEquals(wrapper.submissions.values().iterator().next().get(0).getLinkFlairText(),"flair");
    }
}