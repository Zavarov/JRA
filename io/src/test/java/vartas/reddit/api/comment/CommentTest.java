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

package vartas.reddit.api.comment;

import org.junit.Before;
import org.junit.Test;
import vartas.reddit.CommentInterface;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentTest {
    CommentInterface comment;
    @Before
    public void setUp(){
        comment = CommentHelper.parse("src/test/resources/comment.com").get(0);
    }

    @Test
    public void testGetAuthor(){
        assertThat(comment.getAuthor()).isEqualTo("author");
    }

    @Test
    public void testgetId(){
        assertThat(comment.getId()).isEqualTo("id");
    }

    @Test
    public void testGetSubreddit(){
        assertThat(comment.getSubreddit()).isEqualTo("subreddit");
    }

    @Test
    public void testGetScore(){
        assertThat(comment.getScore()).isEqualTo(1);
    }

    @Test
    public void testGetSubmission(){
        assertThat(comment.getSubmission()).isEqualTo("submission");
    }

    @Test
    public void testGetSubmissionTitle(){
        assertThat(comment.getSubmissionTitle()).isEqualTo("submissionTitle");
    }
}
