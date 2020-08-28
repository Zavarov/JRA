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

import org.json.JSONArray;
import org.json.JSONObject;
import vartas.reddit.factory.CommentFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

public class JSONComment extends Comment{
    private static final String AUTHOR = "author";
    private static final String CONTENT = "content";
    private static final String SCORE = "score";
    private static final String ID = "id";
    private static final String CREATED = "created";
    private static final String CHILDREN = "children";

    public static Comment of(Path guildPath) throws IOException {
        return of(Files.readString(guildPath));
    }

    public static Comment of(String content){
        return of(new JSONObject(content));
    }

    public static Comment of(JSONObject jsonObject){
        Comment comment = CommentFactory.create(
                JSONComment::new,
                jsonObject.getString(AUTHOR),
                jsonObject.getString(CONTENT),
                jsonObject.getInt(SCORE),
                jsonObject.getString(ID),
                Instant.ofEpochMilli(jsonObject.getLong(CREATED))
        );

        JSONArray children = jsonObject.getJSONArray(CHILDREN);
        for(int i = 0 ; i < children.length() ; ++i)
            comment.addChildren(of(children.getJSONObject(i)));

        return comment;
    }

    public static JSONObject of(Comment comment){
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(AUTHOR, comment.getAuthor());
        jsonObject.put(CONTENT, comment.getContent());
        jsonObject.put(SCORE, comment.getScore());
        jsonObject.put(ID, comment.getId());
        jsonObject.put(CREATED, comment.getCreated().toEpochMilli());

        JSONArray children = new JSONArray();
        for(Comment child : comment.getChildren())
            children.put(of(child));
        jsonObject.put(CHILDREN, children);

        return jsonObject;
    }
}
