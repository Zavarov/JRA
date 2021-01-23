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

import net.dean.jraw.tree.CommentNode;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class implements the comments backed by their respective JRAW comments.
 */
public class JrawComment extends Comment {
    public JrawComment(String submission, JSONObject source){
        super(submission, source);
    }

    public static Comment create(Submission submission, CommentNode<net.dean.jraw.models.Comment> jrawNode){
        net.dean.jraw.models.Comment jrawComment = jrawNode.getSubject();
        JSONArray replies = new JSONArray();

        for(CommentNode<net.dean.jraw.models.Comment> jrawChild : jrawNode.getReplies())
            replies.put(create(submission, jrawChild).getSource());

        JSONObject source = new JSONObject();

        source.put(AUTHOR, jrawComment.getAuthor());
        source.put(BODY, jrawComment.getBody());
        source.put(SCORE, jrawComment.getScore());
        source.put(ID, jrawComment.getId());
        source.put(CREATED, jrawComment.getCreated().getTime() / 1000);
        source.put(REPLIES, replies);

        return new JrawComment(submission.getId(), source);
    }
}
