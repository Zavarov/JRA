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
 * This is the interface for a single subreddit.
 */
public interface Subreddit extends RedditSnowflake{
    /**
     * The URL body that will link to the subreddit, given its name.
     */
    String SUBREDDIT_URL = "https://www.reddit.com/%s";

    /**
     * We can't determine the number of active accounts if the subreddit is private.
     * @return The number of currently active accounts
     */
    Optional<Integer> getAccountsActive();

    /**
     * @return The URL to the banner image, if it exists.
     */
    Optional<String> getBannerImage();

    /**
     * @return The name of the subreddit.
     */
    String getName();

    /**
     * @return The public description of the subreddit.
     */
    String getPublicDescription();

    /**
     * In case the program can't access the subreddit, -1 is returned.
     * @return The number of users that subscribed to the subreddit.
     */
    int getSubscribers();

    /**
     * @return The permalink to the subreddit.
     */
    @Override
    default String getPermalink(){
        return String.format(SUBREDDIT_URL, getName());
    }
}
