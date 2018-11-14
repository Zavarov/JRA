/*
 * Copyright (C) 2017 u/Zavarov
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

import java.io.IOException;

/**
 * A more general frame for a wrapper class. This class allows to make requests
 * to the Reddit api and deals with expired tokens automatically.
 * @author u/Zavarov
 * @param <T> the expected instance from the request.
 */
public abstract class Wrapper<T>{    
    /**
     * The bot instance that contains the Reddit client.
     */
    protected final RedditBot bot;
    /**
     * @param bot the bot instance that contains the Reddit client.
     */
    public Wrapper(RedditBot bot){
        this.bot=bot;
    }
    /**
     * @return the requested item. 
     * @throws java.io.IOException in case a communication error occured.
     * @throws java.lang.InterruptedException when the request was interrupted.
     */
    public abstract T request() throws IOException, InterruptedException;
}
