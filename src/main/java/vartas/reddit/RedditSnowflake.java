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

import java.time.LocalDateTime;

/**
 * This interface should be implemented by all Reddit entities.
 */
public interface RedditSnowflake extends Comparable<RedditSnowflake>{
    /**
     * To ensure compatibility, the date has to be in the GMT time zone.
     * @return The date when this snowflake was made.
     */
    LocalDateTime getCreated();
    /**
     * @return the permalink to the snowflake.
     */
    String getPermalink();

    /**
     * Compares two snowflakes based on their creation date.
     * @param snowflake The snowflake this one is compared to.
     * @return 0 If the snowflakes where created at exactly the same time,
     * a negative value if this snowflake was created earlier, otherwise a positive value.
     */
    @Override
    default int compareTo(RedditSnowflake snowflake){
        return getCreated().compareTo(snowflake.getCreated());
    }
}
