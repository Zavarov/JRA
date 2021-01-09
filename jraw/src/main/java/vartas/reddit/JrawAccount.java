/*
 * Copyright (c) 2020 Zavarov
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

import org.json.JSONObject;

public class JrawAccount extends Account {
    public JrawAccount(JSONObject source) {
        super(source);
    }

    public static Account create(net.dean.jraw.models.Account jrawAccount){
        JSONObject source = new JSONObject();

        source.put(NAME, jrawAccount.getName());
        source.put(LINK_KARMA, jrawAccount.getLinkKarma());
        source.put(COMMENT_KARMA, jrawAccount.getCommentKarma());
        source.put(CREATED_UTC, jrawAccount.getCreated().getTime()/1000);
        source.put(HAS_SUBSCRIBED, jrawAccount.getHasSubscribed());
        source.put(HAS_VERIFIED_MAIL, jrawAccount.getHasVerifiedEmail());

        return new JrawAccount(source);
    }
}
