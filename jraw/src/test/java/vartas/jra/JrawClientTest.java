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

package vartas.jra;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JrawClientTest extends AbstractTest {
    @Test
    public void testGetAccount() {
        Account account = client.getUncheckedAccounts("Zavarov");
        assertThat(account.getName()).isEqualTo("Zavarov");
    }

    @Test
    public void testGetInvalidAccount() {
        ExecutionException exception = assertThrows(
                ExecutionException.class ,
                () -> client.getAccounts("#####")
        );
        assertThat(exception.getCause()).isInstanceOf(UnsuccessfulRequestException.class);
    }

    @Test
    public void testGetSubreddit() {
        client.getUncheckedSubreddits("RedditDev");
    }

    @Test
    public void testGetInvalidSubreddit() {
        ExecutionException exception = assertThrows(
                ExecutionException.class ,
                () -> client.getSubreddits("#####")
        );
        assertThat(exception.getCause()).isInstanceOf(ClientException.class);
    }
}
