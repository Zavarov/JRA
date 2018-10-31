/*
 * Copyright (C) 2018 u/Zavarov
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import vartas.offlinejraw.OfflineAccountResponse;

/**
 *
 * @author u/Zavarov
 */
public class UserWrapperTest {
    OfflineRedditBot bot;
    UserWrapper wrapper;
    @Before
    public void setUp(){
        bot = new OfflineRedditBot();
        bot.adapter.addResponse(new OfflineAccountResponse()
                        .addName("user")
                        .addCreated(1)
                        .addLinkKarma(2)
                        .addCommentKarma(3).build());
        wrapper = new UserWrapper(bot);
    }
    @Test
    public void parameterTest(){
        assertNull(wrapper.user);
        wrapper.parameter("user");
        assertEquals(wrapper.user,"user");
    }
    @Test
    public void requestTest(){
        wrapper.parameter("user");
        Account account = wrapper.request();
        assertEquals(account.getName(),"user");
        assertEquals(account.getCreated().getTime(),1000L);
        assertEquals(account.getLinkKarma(),2);
        assertEquals(account.getCommentKarma(),3);
    }
}
