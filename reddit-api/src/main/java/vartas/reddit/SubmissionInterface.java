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

import java.util.Date;
import java.util.Optional;
/**
 * This is the interface for all Reddit submissions.
 */
public interface SubmissionInterface extends Comparable<SubmissionInterface> {
    /**
     * The URL frame that will link to the submission.
     */
    String SHORT_URL = "https://redd.it/%s";
    /**
     * The URL frame that will link to the submission.
     */
    String PERMALINK_URL = "https://www.reddit.com%s";
    /**
     * @return the author of the submission.
     */
    String getAuthor();
    /**
     * @return the id of the submission.
     */
    String getId();
    /**
     * @return the flair text, if one exists, or null otherwise.
     */
    Optional<String> getLinkFlairText();
    /**
     * @return the subreddit the submission was posted in.
     */
    String getSubreddit();
    /**
     * @return true, if the submission is marked as NSFW.
     */
    boolean isNsfw();
    /**
     * @return true, if the submission is marked as spoiler.
     */
    boolean isSpoiler();
    /**
     * @return the upvotes minus the downvotes.
     */
    int getScore();
    /**
     * @return the title of the submission.
     */
    String getTitle();
    /**
     * @return the timestamp when this submission was created.
     */
    Date getCreated();
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

    /**
     * Compares two submissions based on their creation date
     * @param submission the submission this one is compared to.
     * @return 0 if the argument submission was created exactly when this submission was.
     * A negative value if this submission was created before the argument submission and a positive value otherwise.
     */
    @Override
    default int compareTo(SubmissionInterface submission){
        return getCreated().compareTo(submission.getCreated());
    }
}
