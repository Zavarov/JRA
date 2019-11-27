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

import vartas.reddit.visitor.SubredditSnowflakeVisitor;

/**
 * This is the interface for all Reddit comments.
 */
public interface Comment extends SubredditSnowflake{
    /**
     * The URL for the comment, given the subreddit name, submission name and comment id.
     */
    String COMMENT_URL = "https://www.reddit.com/r/%s/comments/%s/-/%s";
    /**
     * @return The  name of the submission, the comment is in.
     */
    String getSubmission();
    /**
     * @return The title of the submission, the comment is in.
     */
    String getSubmissionTitle();
    /**
     * @return A permalink to the comment.
     */
    @Override
    default String getPermalink(){
        return String.format(COMMENT_URL,getSubreddit(),getSubmission(),getId());
    }

    /**
     * This method is part of the visitor pattern to visit the individual comments.
     * @param visitor The visitor for this snowflake.
     */
    @Override
    default void accept(SubredditSnowflakeVisitor visitor){
        visitor.handle(this);
    }
}
