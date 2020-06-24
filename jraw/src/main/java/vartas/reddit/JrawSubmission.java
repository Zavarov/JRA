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

import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.validator.routines.UrlValidator;
import vartas.reddit.factory.SubmissionFactory;

import javax.annotation.Nonnull;
import java.util.Optional;

@Nonnull
public class JrawSubmission extends Submission {
    @Nonnull
    private static final String REDDIT_ROOT_NODE = "https://www.reddit.com";
    @Nonnull
    private final net.dean.jraw.models.Submission jrawSubmission;

    public JrawSubmission(@Nonnull net.dean.jraw.models.Submission jrawSubmission){
        this.jrawSubmission = jrawSubmission;
    }

    @Nonnull
    public static Submission create(@Nonnull net.dean.jraw.models.Submission jrawSubmission){
        Submission submission = SubmissionFactory.create(
                () -> new JrawSubmission(jrawSubmission),
                jrawSubmission.getAuthor(),
                jrawSubmission.getTitle(),
                jrawSubmission.getScore(),
                jrawSubmission.isNsfw(),
                jrawSubmission.isSpoiler(),
                jrawSubmission.getUniqueId(),
                jrawSubmission.getCreated().toInstant()
        );

        //Reddit likes to put stuff like "self" into the thumbnail
        if(jrawSubmission.hasThumbnail() && UrlValidator.getInstance().isValid(jrawSubmission.getThumbnail()))
            submission.setThumbnail(jrawSubmission.getThumbnail());

        submission.setLinkFlairText(Optional.ofNullable(jrawSubmission.getLinkFlairText()));
        submission.setContent(Optional.ofNullable(jrawSubmission.getSelfText()).map(StringEscapeUtils::escapeHtml4));

        return submission;
    }

    @Nonnull
    @Override
    public String getPermaLink(){
        return REDDIT_ROOT_NODE + jrawSubmission.getPermalink();
    }

    @Nonnull
    @Override
    public String getUrl(){
        return jrawSubmission.getUrl();
    }
}
