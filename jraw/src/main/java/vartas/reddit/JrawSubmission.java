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
import vartas.reddit.factory.SubmissionFactory;

public class JrawSubmission extends Submission {
    public static Submission create(net.dean.jraw.models.Submission jrawSubmission){
        Submission submission = SubmissionFactory.create(
                JrawSubmission::new,
                jrawSubmission.getAuthor(),
                jrawSubmission.getTitle(),
                jrawSubmission.getScore(),
                jrawSubmission.isNsfw(),
                jrawSubmission.isSpoiler(),
                jrawSubmission.getUniqueId(),
                jrawSubmission.getCreated().toInstant()
        );

        if(jrawSubmission.getSelfText() != null)
            submission.setContent(StringEscapeUtils.unescapeHtml4(jrawSubmission.getThumbnail()));

        if(jrawSubmission.hasThumbnail())
            submission.setThumbnail(jrawSubmission.getThumbnail());

        return submission;
    }
}