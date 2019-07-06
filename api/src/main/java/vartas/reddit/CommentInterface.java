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

/**
 * This is the interface for all Reddit comments.
 */
public interface CommentInterface {
    /**
     * The URL frame that will link to the comment.
     */
    String COMMENT_URL = "https://www.reddit.com/r/%s/comments/%s/-/%s";
    /**
     * @return the author of the comment.
     */
    String getAuthor();
    /**
     * @return the id of the comment.
     */
    String getId();
    /**
     * @return the upvotes minus the downvotes.
     */
    int getScore();
    /**
     * @return the  name of the submission, the comment is in.
     */
    String getSubmission();
    /**
     * @return the name of the subreddit, the comment is in.
     */
    String getSubreddit();
    /**
     * @return the title of the submission, the comment is in.
     */
    String getSubmissionTitle();
    /**
     * @return a permalink to the comment.
     */
    default String getPermalink(){
        return String.format(COMMENT_URL,getSubreddit(),getSubmission(),getId());
    }
}
