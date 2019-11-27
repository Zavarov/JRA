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

import java.util.Optional;
/**
 * This is the interface for a single submissions.
 */
public interface Submission extends SubredditSnowflake {
    /**
     * The shortened URL for the submission, given its id.
     */
    String SHORT_URL = "https://redd.it/%s";
    /**
     * @return The flair text, if one exists.
     */
    Optional<String> getLinkFlairText();
    /**
     * @return True, if the submission is marked as NSFW.
     */
    boolean isNsfw();
    /**
     * @return True, if the submission is marked as spoiler.
     */
    boolean isSpoiler();
    /**
     * @return The title of the submission.
     */
    String getTitle();
    /**
     * @return The selftext of the submission.
     */
    Optional<String> getSelfText();

    /**
     * @return The thumbnail of the submission.
     */
    Optional<String> getThumbnail();

    /**
     * @return An absolute URL to the comments in a self post, otherwise an URL to the submission content.
     */
    String getUrl();

    /**
     * @return The short link to the submission.
     */
    default String getShortLink(){
        return String.format(SHORT_URL, this.getId());
    }

    /**
     * This method is part of the visitor pattern to visit the individual submissions.
     * @param visitor The visitor for this snowflake.
     */
    @Override
    default void accept(SubredditSnowflakeVisitor visitor){
        visitor.handle(this);
    }
}
