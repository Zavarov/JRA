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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import vartas.reddit.PushshiftWrapper.CompactSubmission;

/**
 *
 * @author u/Zavarov
 */
public class CompactSubmissionTest {
    CompactSubmission submission;
    @Before
    public void setUp(){
        submission = new CompactSubmission();
        submission.put("author","author");
        submission.put("id","id");
        submission.put("score","1");
        submission.put("title","title");
        submission.put("link_flair_text","flair");
        submission.put("subreddit","subreddit");
        submission.put("over18", "true");
        submission.put("spoiler","true");
    }
    @Test
    public void getAuthorTest(){
        assertEquals(submission.getAuthor(),"author");
    }
    @Test
    public void getIdTest(){
        assertEquals(submission.getId(),"id");
    }
    @Test
    public void getScoreTest(){
        assertEquals(submission.getScore(),1);
    }
    @Test
    public void getTitleTest(){
        assertEquals(submission.getTitle(),"title");
    }
    @Test
    public void getLinkFlairTextTest(){
        assertEquals(submission.getLinkFlairText(),"flair");
    }
    @Test
    public void getSubredditTest(){
        assertEquals(submission.getSubreddit(),"subreddit");
    }
    @Test
    public void isNsfwTest(){
        assertTrue(submission.isNsfw());
    }
    @Test
    public void isSpoilerTest(){
        assertTrue(submission.isSpoiler());
    }
    @Test
    public void getPermalinkTest(){
        assertEquals(submission.getPermalink(),String.format("https://redd.it/%s","id"));
    }
    @Test
    public void putNullTest(){
        submission.put("key", null);
        assertEquals(submission.get("key"),"");
    }
}
