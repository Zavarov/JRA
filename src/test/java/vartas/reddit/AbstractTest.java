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

package vartas.reddit;

import org.json.JSONObject;
import org.junit.BeforeClass;
import vartas.reddit.jraw.JrawClient;
import vartas.reddit.pushshift.PushshiftClient;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.fail;

public class AbstractTest {
    static Client client;
    @BeforeClass
    public static void setUpBeforeClass(){
        client = getJrawClient();
    }

    protected static Client getJrawClient(){
        JSONObject config;
        try{
            String content = new String(Files.readAllBytes(Paths.get("src/test/resources/config.json")));
            config = new JSONObject(content);
        }catch(Exception e){
            fail();
            config = null;
        }

        String name = config.getString("name");
        String version = config.getString("version");
        String clientId = config.getString("clientId");
        String secret = config.getString("secret");
        return new JrawClient(name, version, clientId, secret);
    }

    protected static Client getPushshiftClient(){
        return new PushshiftClient(getJrawClient());
    }
}
