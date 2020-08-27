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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

public class JSONSubmission extends Submission{
    private static final String THUMBNAIL = "thumbnail";
    private static final String LINK_FLAIR_TEXT = "linkFlairText";
    private static final String CONTENT = "content";
    private static final String AUTHOR = "author";
    private static final String TITLE = "title";
    private static final String SCORE = "score";
    private static final String NSFW = "nsfw";
    private static final String SPOILER = "spoiler";
    private static final String ID = "id";
    private static final String CREATED = "created";
    private static final String COMMENTS = "comments";

    public static JSONSubmission of(Path guildPath) throws IOException {
        return of(Files.readString(guildPath));
    }

    public static JSONSubmission of(String content){
        return of(new JSONObject(content));
    }

    public static JSONSubmission of(JSONObject jsonObject){
        JSONSubmission submission = new JSONSubmission();

        submission.setThumbnail(jsonObject.optString(THUMBNAIL));
        submission.setLinkFlairText(jsonObject.optString(LINK_FLAIR_TEXT));
        submission.setContent(jsonObject.optString(CONTENT));
        submission.setAuthor(jsonObject.getString(AUTHOR));
        submission.setTitle(jsonObject.getString(TITLE));
        submission.setScore(jsonObject.getInt(SCORE));
        submission.setNsfw(jsonObject.getBoolean(NSFW));
        submission.setSpoiler(jsonObject.getBoolean(SPOILER));
        submission.setId(jsonObject.getString(ID));
        submission.setCreated(Instant.ofEpochMilli(jsonObject.getLong(CREATED)));

        for(int i = 0 ; i < jsonObject.getJSONArray(COMMENTS).length() ; ++i)
            submission.addComments(JSONComment.of(jsonObject.getJSONArray(COMMENTS).getJSONObject(i)));

        return submission;
    }

    public static JSONObject of(Submission submission){
        JSONObject jsonObject = new JSONObject();

        submission.ifPresentThumbnail(thumbnail -> jsonObject.put(THUMBNAIL, thumbnail));
        submission.ifPresentLinkFlairText(linkFlairText -> jsonObject.put(LINK_FLAIR_TEXT, linkFlairText));
        submission.ifPresentContent(content -> jsonObject.put(CONTENT, content));
        jsonObject.put(AUTHOR, submission.getAuthor());
        jsonObject.put(TITLE, submission.getTitle());
        jsonObject.put(SCORE, submission.getScore());
        jsonObject.put(NSFW, submission.getNsfw());
        jsonObject.put(SPOILER, submission.getSpoiler());
        jsonObject.put(ID, submission.getId());
        jsonObject.put(CREATED, submission.getCreated().toEpochMilli());

        JSONArray comments = new JSONArray();
        for(Comment comment : submission.getComments())
            comments.put(JSONComment.of(comment));
        jsonObject.put(COMMENTS, comments);

        return jsonObject;
    }

    @Override
    public String getPermaLink() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getUrl() {
        throw new UnsupportedOperationException();
    }
}
