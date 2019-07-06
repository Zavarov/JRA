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

package vartas.reddit.jraw;

import net.dean.jraw.models.Subreddit;
import org.apache.commons.text.StringEscapeUtils;
import vartas.reddit.SubredditInterface;

import java.util.Date;
import java.util.Optional;
/**
 * This class implements the subreddits backed by their respective JRAW subreddits.
 */
public class JrawSubreddit implements SubredditInterface {
    /**
     * The underlying subreddit instance.
     */
    protected Subreddit referee;
    /**
     * Creates a new instance of a subreddit.
     * @param referee the underlying JRAW subreddit .
     */
    public JrawSubreddit(Subreddit referee){
        this.referee = referee;
    }

    /**
     * @return the number of currently active accounts
     */
    @Override
    public int getAccountsActive() {
        return referee.getAccountsActive() == null ? 0 : referee.getAccountsActive();
    }

    /**
     * @return the optional url to the banner image.
     */
    @Override
    public Optional<String> getBannerImage() {
        return Optional.ofNullable(referee.getBannerImage());
    }

    /**
     * @return the date when the subreddit was created.
     */
    @Override
    public Date getCreated() {
        return referee.getCreated();
    }

    /**
     * @return the name of the subreddit.
     */
    @Override
    public String getName() {
        return referee.getName();
    }

    /**
     * @return the public description of the subreddit.
     */
    @Override
    public String getPublicDescription() {
        return StringEscapeUtils.unescapeHtml4(referee.getPublicDescription());
    }

    /**
     * @return the number of subscribed user or -1 if the bot can't access it.
     */
    @Override
    public int getSubscribers() {
        return referee.getSubscribers();
    }
    /**
     * @return a hash code based on the name of the subreddit.
     */
    @Override
    public int hashCode(){
        return getName().hashCode();
    }
    /**
     * Two subreddits are equal if they have the same name.
     * @param o an object this subreddit is compared to.
     * @return true if the object is a subreddit with the same name.
     */
    @Override
    public boolean equals(Object o){
        if(o instanceof SubredditInterface){
            SubredditInterface subreddit = (SubredditInterface)o;
            return subreddit.getName().equals(this.getName());
        }else{
            return false;
        }
    }
}
