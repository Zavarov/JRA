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

import net.dean.jraw.RedditClient;
import net.dean.jraw.references.SubmissionReference;
import net.dean.jraw.tree.CommentNode;
import net.dean.jraw.tree.RootCommentNode;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.LoggerFactory;
import vartas.reddit.$factory.SubmissionFactory;

import javax.annotation.Nonnull;
import java.util.*;

@Nonnull
public class JrawSubmission extends Submission {
    @Nonnull
    private final net.dean.jraw.models.Submission jrawSubmission;

    public JrawSubmission(@Nonnull net.dean.jraw.models.Submission jrawSubmission){
        this.jrawSubmission = jrawSubmission;
    }

    @Nonnull
    public static Submission create(@Nonnull net.dean.jraw.models.Submission jrawSubmission, RedditClient jrawClient){
        Submission submission = SubmissionFactory.create(
                () -> new JrawSubmission(jrawSubmission),
                jrawSubmission.getAuthor(),
                jrawSubmission.getTitle(),
                jrawSubmission.getUrl(),
                jrawSubmission.getScore(),
                jrawSubmission.isNsfw(),
                jrawSubmission.isSpoiler(),
                jrawSubmission.getId(),
                jrawSubmission.getCreated().toInstant()
        );

        //Reddit likes to put stuff like "self" into the thumbnail
        if(jrawSubmission.hasThumbnail() && UrlValidator.getInstance().isValid(jrawSubmission.getThumbnail()))
            submission.setThumbnail(jrawSubmission.getThumbnail());

        submission.setLinkFlairText(Optional.ofNullable(jrawSubmission.getLinkFlairText()));
        submission.setContent(Optional.ofNullable(jrawSubmission.getSelfText()).map(StringEscapeUtils::unescapeHtml4));
        submission.addAllComments(requestComments(submission, jrawSubmission, jrawClient));

        return submission;
    }

    private static List<Comment> requestComments(Submission submission, net.dean.jraw.models.Submission jrawSubmission, RedditClient jrawClient){
        List<Comment> comments = new ArrayList<>();

        RootCommentNode root;
        SubmissionReference submissionReference = jrawSubmission.toReference(jrawClient);

        try {
            root = submissionReference.comments();

            //Acquire all the comments
            root.loadFully(jrawClient);

            //Add all comments
            Deque<CommentNode<net.dean.jraw.models.Comment>> unvisited = new LinkedList<>(root.getReplies());
            while(!unvisited.isEmpty()){
                CommentNode<net.dean.jraw.models.Comment> comment = unvisited.removeFirst();

                comments.add(JrawComment.create(submission, comment));

                unvisited.addAll(comment.getReplies());
            }
        }catch(NullPointerException e){
            //null if the submission doesn't exist -> Not a communication error
            LoggerFactory.getLogger(JrawSubmission.class.getSimpleName()).warn(e.getMessage(), e);
        }

        return comments;
    }
}
