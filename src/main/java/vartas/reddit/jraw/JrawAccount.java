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

import vartas.reddit.Account;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * This class implements the users backed by their respective JRAW users.
 */
public class JrawAccount implements Account {
    /**
     * The underlying user instance.
     */
    protected net.dean.jraw.models.Account referee;
    /**
     * Creates a new instance of an user.
     * @param referee The underlying JRAW user .
     */
    public JrawAccount(net.dean.jraw.models.Account referee){
        this.referee = referee;
    }

    /**
     * @return {@link net.dean.jraw.models.Account#getCommentKarma}.
     */
    @Override
    public int getCommentKarma() {
        return referee.getCommentKarma();
    }

    /**
     * Transforms the date into a {@link LocalDateTime} with the GMT zone.
     * @return {@link net.dean.jraw.models.Account#getCreated}.
     */
    @Override
    public LocalDateTime getCreated() {
        return LocalDateTime.ofInstant(referee.getCreated().toInstant(), ZoneId.of("UTC"));
    }

    /**
     * @return {@link net.dean.jraw.models.Account#getLinkKarma}.
     */
    @Override
    public int getLinkKarma() {
        return referee.getLinkKarma();
    }

    /**
     * @return {@link net.dean.jraw.models.Account#getName}.
     */
    @Override
    public String getName() {
        return referee.getName();
    }
    /**
     * @return A hash code based on {@link #getName}.
     */
    @Override
    public int hashCode(){
        return getName().hashCode();
    }
    /**
     * Two users are equal if they have the same name.
     * @param o An object this user is compared to.
     * @return True if the object is a user with the same name.
     */
    @Override
    public boolean equals(Object o){
        if(o instanceof Account){
            Account user = (Account)o;
            return user.getName().equals(this.getName());
        }else{
            return false;
        }
    }
}
