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

import net.dean.jraw.tree.CommentNode;
import org.apache.commons.text.StringEscapeUtils;
import vartas.reddit.factory.CommentFactory;

/**
 * This class implements the comments backed by their respective JRAW comments.
 */
public class JrawComment extends Comment {
    public static Comment create(CommentNode<net.dean.jraw.models.Comment> jrawNode){
        net.dean.jraw.models.Comment jrawComment = jrawNode.getSubject();

        Comment comment = CommentFactory.create(
                JrawComment::new,
                jrawComment.getAuthor(),
                StringEscapeUtils.escapeHtml4(jrawComment.getBody()),
                jrawComment.getScore(),
                jrawComment.getUniqueId(),
                jrawComment.getCreated().toInstant()
        );

        for(CommentNode<net.dean.jraw.models.Comment> jrawChild : jrawNode.getReplies())
            comment.addChildren(create(jrawChild));

        return comment;
    }
}
