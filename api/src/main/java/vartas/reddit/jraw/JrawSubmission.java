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

package vartas.reddit.jraw;

import net.dean.jraw.models.Submission;
import org.apache.commons.text.StringEscapeUtils;
import vartas.reddit.SubmissionInterface;

import java.util.Date;
import java.util.Optional;

/**
 * This class implements the submissions backed by their respective JRAW submissions.
 */
public class JrawSubmission implements SubmissionInterface {
    /**
     * The underlying submission instance.
     */
    protected Submission referee;
    /**
     * Creates a new instance of a submission.
     * @param referee the underlying JRAW submission .
     */
    public JrawSubmission(Submission referee){
        this.referee = referee;
    }
    /**
     * @return the author of the submission.
     */
    @Override
    public String getAuthor(){
        return referee.getAuthor();
    }
    /**
     * @return the id of the submission.
     */
    @Override
    public String getId(){
        return referee.getId();
    }
    /**
     * @return the flair text, if one exists, or null otherwise.
     */
    @Override
    public Optional<String> getLinkFlairText(){
        return Optional.ofNullable(referee.getLinkFlairText());
    }
    /**
     * @return the subreddit the submission was posted in.
     */
    @Override
    public String getSubreddit(){
        return referee.getSubreddit();
    }
    /**
     * @return true, if the submission is marked as NSFW.
     */
    @Override
    public boolean isNsfw(){
        return referee.isNsfw();
    }
    /**
     * @return true, if the submission is marked as spoiler.
     */
    @Override
    public boolean isSpoiler(){
        return referee.isSpoiler();
    }
    /**
     * @return the upvotes minus the downvotes.
     */
    @Override
    public int getScore(){
        return referee.getScore();
    }
    /**
     * @return the title of the submission.
     */
    @Override
    public String getTitle(){
        return referee.getTitle();
    }
    /**
     * @return the timestamp when this submission was created.
     */
    @Override
    public Date getCreated(){
        return referee.getCreated();
    }
    /**
     * @return the selftext of the submission.
     */
    @Override
    public Optional<String> getSelfText(){
        if(referee.getSelfText() == null)
            return Optional.empty();

        return Optional.of(StringEscapeUtils.unescapeHtml4(referee.getSelfText().replaceAll("\"", "\\\"")));
    }
    /**
     * @return the thumbnail of the submission.
     */
    @Override
    public Optional<String> getThumbnail(){
        return referee.hasThumbnail() ? Optional.ofNullable(referee.getThumbnail()) : Optional.empty();
    }
    /**
     * @return an absolute URL to the comments in a self post, otherwise an URL to the submission content.
     */
    @Override
    public String getUrl(){
        return referee.getUrl();
    }
    /**
     * @return a hash code based on the id of the submission.
     */
    @Override
    public int hashCode(){
        return getId().hashCode();
    }
    /**
     * Two submissions are equal if they have the same id.
     * @param o an object this submission is compared to.
     * @return true if the object is a submission with the same id.
     */
    @Override
    public boolean equals(Object o){
        if(o instanceof SubmissionInterface){
            SubmissionInterface submission = (SubmissionInterface)o;
            return submission.getId().equals(this.getId());
        }else{
            return false;
        }
    }
}
