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

import java.util.Optional;
/**
 * This is the interface for all Reddit submissions.
 */
public interface SubmissionInterface extends SubredditSnowflake {
    /**
     * The URL frame that will link to the submission.
     */
    String SHORT_URL = "https://redd.it/%s";
    /**
     * The URL frame that will link to the submission.
     */
    String PERMALINK_URL = "https://www.reddit.com%s";
    /**
     * @return the flair text, if one exists, or null otherwise.
     */
    Optional<String> getLinkFlairText();
    /**
     * @return true, if the submission is marked as NSFW.
     */
    boolean isNsfw();
    /**
     * @return true, if the submission is marked as spoiler.
     */
    boolean isSpoiler();
    /**
     * @return the title of the submission.
     */
    String getTitle();
    /**
     * @return the selftext of the submission.
     */
    Optional<String> getSelfText();

    /**
     * @return the thumbnail of the submission.
     */
    Optional<String> getThumbnail();

    /**
     * @return an absolute URL to the comments in a self post, otherwise an URL to the submission content.
     */
    String getUrl();
    /**
     * @return the permalink to the submission.
     */
    String getPermalink();

    /**
     * @return the short link to the submission.
     */
    default String getShortLink(){
        return String.format(SHORT_URL, this.getId());
    }
}
