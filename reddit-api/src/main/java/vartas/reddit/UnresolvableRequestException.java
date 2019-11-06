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
 * This exception is thrown whenever a request was made that was impossible to resolve.<br>
 * E.g. when a subreddit was requested that doesn't exist or a submission the bot doesn't have access to.
 */
public class UnresolvableRequestException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public static final String ERROR_MESSAGE = "The request failed with the following error code: %d";
    public UnresolvableRequestException(int errorCode){
        super(String.format(ERROR_MESSAGE, errorCode));
    }
}
