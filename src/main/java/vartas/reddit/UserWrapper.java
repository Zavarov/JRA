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

import net.dean.jraw.models.Account;

/**
 * A helper class that looks for a Reddit user based on the name.
 * @author u/Zavarov
 */
public class UserWrapper extends Wrapper<Account>{
    /**
     * The name of the user.
     */
    String user;
    /**
     * @param bot the bot instance that contains the Reddit client.
     */
    public UserWrapper(RedditBot bot) {
        super(bot);
    }
    /**
     * Sets the name of the user.
     * @param user the user name.
     */
    public void parameter(String user){
        this.user=user;
    }
    /**
     * @return the account associated with the name.
     */
    @Override
    public Account request() {
        return bot.getClient().user(user).query().getAccount();
    }
}
