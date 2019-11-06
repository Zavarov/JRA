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
 * This is the interface for all subreddits.
 */
public interface SubredditInterface {
    /**
     * The URL frame that will link to the subreddit.
     */
    String SUBREDDIT_URL = "https://www.reddit.com/%s";

    /**
     * @return the number of currently active accounts
     */
    int getAccountsActive();

    /**
     * @return the optional url to the banner image.
     */
    Optional<String> getBannerImage();

    /**
     * @return the date when the subreddit was created.
     */
    Date getCreated();

    /**
     * @return the name of the subreddit.
     */
    String getName();

    /**
     * @return the public description of the subreddit.
     */
    String getPublicDescription();

    /**
     * @return the number of subscribed user or -1 if the bot can't access it.
     */
    int getSubscribers();

    /**
     * @return the permalink to the subreddit.
     */
    default String getPermaLink(){
        return String.format(SUBREDDIT_URL, getName());
    }
}
