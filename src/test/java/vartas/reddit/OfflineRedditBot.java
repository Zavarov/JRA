package vartas.reddit;

import vartas.offlinejraw.OfflineNetworkAdapter;
import vartas.offlinejraw.OfflineRateLimiter;

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

/**
 *
 * @author u/Zavarov
 */
public class OfflineRedditBot extends RedditBot{
    OfflineNetworkAdapter adapter;

    private OfflineRedditBot(OfflineNetworkAdapter adapter) {
        super(new Config(), adapter);
        this.adapter = adapter;
        super.client.setLogHttp(false);
        super.client.setRateLimiter(new OfflineRateLimiter());
    }
    
    public OfflineRedditBot(){
        this(new OfflineNetworkAdapter());
    }
    
    
    private static class Config implements RedditCredentials{
        @Override
        public String getId() { return "id";}
        @Override
        public String getSecret() { return "secret";}
        @Override
        public String getAppid() { return "appid";}
        @Override
        public String getUser() { return "user";}
        @Override
        public String getRedirect() { return "redirect";}
        @Override
        public String getScope() { return "scope";}
        @Override
        public String getPlatform() { return "platform";}
        @Override
        public String getVersion() { return "version";}
    }
}
