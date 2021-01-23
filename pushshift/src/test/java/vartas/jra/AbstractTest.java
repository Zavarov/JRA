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

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;

import java.nio.file.Files;
import java.nio.file.Paths;

public class AbstractTest {
    protected static Client client;

    @BeforeAll
    public static void setUpAll(){
        JSONObject config;
        try{
            String content = new String(Files.readAllBytes(Paths.get("src/test/resources/config.json")));
            config = new JSONObject(content);
        }catch(Exception e){
            throw new RuntimeException(e);
        }

        String name = config.getString("name");
        String version = config.getString("version");
        String clientId = config.getString("clientId");
        String secret = config.getString("secret");

        client = new PushshiftClient(name, version, clientId, secret);
    }
}
