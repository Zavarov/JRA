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

import net.dean.jraw.models.Subreddit;

/**
 * A helper class that is able to find a subreddit by its name.
 * @author u/Zavarov
 */
public class SubredditWrapper extends Wrapper<Subreddit>{
    /**
     * The name of the subreddit.
     */
    protected String subreddit;
    /**
     * @param bot the bot instance that contains the Reddit client.
     */
    public SubredditWrapper(RedditBot bot) {
        super(bot);
    }
    /**
     * Sets the name of the current subreddit that is looked for.
     * @param subreddit the name of the subreddit.
     */
    public void parameter(String subreddit){
        this.subreddit = subreddit;
    }
    /**
     * @return the subreddit with the specified name. 
     */
    @Override
    public Subreddit request() {
        return bot.getClient().subreddit(subreddit).about();
    }
}
