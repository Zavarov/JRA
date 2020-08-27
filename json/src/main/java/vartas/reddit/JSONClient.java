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

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;

public class JSONClient extends Client{
    @Nonnull
    private final Client client;
    @Nonnull
    private final Path rootPath;

    public JSONClient(@Nonnull Client client, @Nonnull Path rootPath){
        this.client = client;
        this.rootPath = rootPath;
    }

    @Override
    public Subreddit getSubreddits(String key) throws ExecutionException {
        return getSubreddits(key, () -> requestSubreddit(key));
    }

    private Subreddit requestSubreddit(String key) throws ExecutionException{
        return JSONSubreddit.of(client.getSubreddits(key), rootPath.resolve(key));
    }
}
