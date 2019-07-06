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

import net.dean.jraw.models.Account;
import vartas.reddit.UserInterface;

import java.util.Date;
/**
 * This class implements the users backed by their respective JRAW users.
 */
public class JrawUser implements UserInterface {
    /**
     * The underlying user instance.
     */
    protected Account referee;
    /**
     * Creates a new instance of an user.
     * @param referee the underlying JRAW user .
     */
    public JrawUser(Account referee){
        this.referee = referee;
    }

    /**
     * @return the comment karma of the user.
     */
    @Override
    public int getCommentKarma() {
        return referee.getCommentKarma();
    }

    /**
     * @return the date when the account was created.
     */
    @Override
    public Date getCreated() {
        return referee.getCreated();
    }

    /**
     * @return the link karma.
     */
    @Override
    public int getLinkKarma() {
        return referee.getLinkKarma();
    }

    /**
     * @return name of the user.
     */
    @Override
    public String getName() {
        return referee.getName();
    }
    /**
     * @return a hash code based on the name of the user.
     */
    @Override
    public int hashCode(){
        return getName().hashCode();
    }
    /**
     * Two users are equal if they have the same name.
     * @param o an object this user is compared to.
     * @return true if the object is a user with the same name.
     */
    @Override
    public boolean equals(Object o){
        if(o instanceof UserInterface){
            UserInterface user = (UserInterface)o;
            return user.getName().equals(this.getName());
        }else{
            return false;
        }
    }
}
