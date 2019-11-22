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

import java.time.Instant;

public interface RedditSnowflake extends Comparable<RedditSnowflake>{
    /**
     * @return the time in UTC when this snowflake was made
     */
    Instant getCreated();

    /**
     * Compares two snowflakes based on their creation date
     * @param snowflake the snowflake this one is compared to.
     * @return 0 if the argument snowflake was created exactly when this snowflake was.
     * A negative value if this snowflake was created before the argument snowflake and a positive value otherwise.
     */
    @Override
    default int compareTo(RedditSnowflake snowflake){
        return getCreated().compareTo(snowflake.getCreated());
    }
}
