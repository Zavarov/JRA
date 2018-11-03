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

import java.util.UUID;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;

/**
 * A class to help the bot interacting with the Reddit API.
 * @author u/Zavarov
 */
public class RedditBot {
    /**
     * The Reddit client.
     */
    protected final RedditClient client;
    /**
     * @param client the current instance of the reddit client.
     */
    public RedditBot(RedditClient client){
        this.client = client;
        client.setLogHttp(false);
        client.setAutoRenew(true);
    }
    /**
     * Initializes this instance with the default client from the JRAW library.
     * @param config the configuration file
     * @param adapter the communicator between Reddit and this program
     */
    public RedditBot(RedditCredentials config, NetworkAdapter adapter){
        this(OAuthHelper.automatic(adapter, 
                Credentials.userless(config.getId(), config.getSecret(),UUID.randomUUID())));
    }
    /**
     * @return the Reddit client.
     */
    public final RedditClient getClient(){
        return client;
    }
}