/*
 * Copyright (C) 2019 u/Zavarov
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
 * This exception is thrown when a subreddit is requested that the bot can't
 * access.
 * @author u/Zavarov
 */
public class InvalidSubredditException extends RuntimeException{
    private final static long serialVersionUID = 0L;
    /**
     * @param subreddit the subreddit that was requested. 
     */
    public InvalidSubredditException(String subreddit){
        super(String.format("The subreddit %s couldn't be accessed.",subreddit));
    }
}
