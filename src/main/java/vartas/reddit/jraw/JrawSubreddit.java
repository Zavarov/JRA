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

import org.apache.commons.text.StringEscapeUtils;
import vartas.reddit.Subreddit;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
/**
 * This class implements the subreddits backed by their respective JRAW subreddits.
 */
public class JrawSubreddit implements Subreddit {
    /**
     * The underlying subreddit instance.
     */
    protected net.dean.jraw.models.Subreddit referee;
    /**
     * Creates a new instance of a subreddit.
     * @param referee The underlying JRAW subreddit .
     */
    public JrawSubreddit(net.dean.jraw.models.Subreddit referee){
        this.referee = referee;
    }

    /**
     * The active accounts might be null, so we wrap it around an {@link Optional}.
     * @return {@link net.dean.jraw.models.Subreddit#getAccountsActive()}.
     */
    @Override
    public Optional<Integer> getAccountsActive() {
        return Optional.ofNullable(referee.getAccountsActive());
    }

    /**
     * The banner image might be null, so we wrap it around an {@link Optional}.
     * @return {@link net.dean.jraw.models.Subreddit#getBannerImage()}.
     */
    @Override
    public Optional<String> getBannerImage() {
        return Optional.ofNullable(referee.getBannerImage());
    }

    /**
     * Transforms the date into a {@link LocalDateTime} with the GMT zone.
     * @return {@link net.dean.jraw.models.Subreddit#getCreated}.
     */
    @Override
    public LocalDateTime getCreated() {
        return LocalDateTime.ofInstant(referee.getCreated().toInstant(), ZoneId.of("UTC"));
    }

    /**
     * @return {@link net.dean.jraw.models.Subreddit#getName()}.
     */
    @Override
    public String getName() {
        return referee.getName();
    }

    /**
     * The description in the referee still contains HTML4 symbols.<br>
     * Those are translated into their respective ASCII symbols before the result is returned.
     * @return {@link net.dean.jraw.models.Subreddit#getPublicDescription()}.
     */
    @Override
    public String getPublicDescription() {
        return StringEscapeUtils.unescapeHtml4(referee.getPublicDescription());
    }

    /**
     * @return {@link net.dean.jraw.models.Subreddit#getSubscribers()}.
     */
    @Override
    public int getSubscribers() {
        return referee.getSubscribers();
    }
    /**
     * @return A hash code based on ${@link #getName()}.
     */
    @Override
    public int hashCode(){
        return getName().hashCode();
    }
    /**
     * Two subreddits are equal if they have the same name.
     * @param o An object this subreddit is compared to.
     * @return True if the object is a subreddit with the same name.
     */
    @Override
    public boolean equals(Object o){
        if(o instanceof Subreddit){
            Subreddit subreddit = (Subreddit)o;
            return subreddit.getName().equals(this.getName());
        }else{
            return false;
        }
    }
}
