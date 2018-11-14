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
import org.junit.Before;
import org.junit.Test;
import vartas.reddit.PushshiftWrapper.CompactComment;

/**
 *
 * @author u/Zavarov
 */
public class CompactCommentTest {
    CompactComment comment;
    @Before
    public void setUp(){
        comment = new CompactComment();
        comment.put("author","author");
        comment.put("id","id");
        comment.put("score","1");
        comment.put("submission","submission");
        comment.put("subreddit","subreddit");
        comment.put("title","title");
    }
    @Test
    public void getAuthorTest(){
        assertEquals(comment.getAuthor(),"author");
    }
    @Test
    public void getIdTest(){
        assertEquals(comment.getId(),"id");
    }
    @Test
    public void getScoreTest(){
        assertEquals(comment.getScore(),1);
    }
    @Test
    public void getSubmissionTest(){
        assertEquals(comment.getSubmission(),"submission");
    }
    @Test
    public void getSubredditTest(){
        assertEquals(comment.getSubreddit(),"subreddit");
    }
    @Test
    public void getTitleTest(){
        assertEquals(comment.getTitle(),"title");
    }
    @Test
    public void getPermalinkTest(){
        assertEquals(comment.getPermalink(),String.format("https://www.reddit.com/r/%s/comments/%s/-/%s","subreddit","submission","id"));
    }
    @Test
    public void putNullTest(){
        comment.put("key", null);
        assertEquals(comment.get("key"),"");
    }
}
