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

import org.apache.commons.text.StringEscapeUtils;
import vartas.reddit.Submission;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

/**
 * This class implements the submissions backed by their respective JRAW submissions.
 */
public class JrawSubmission implements Submission {
    /**
     * The URL for to the submission, given its name.
     */
    String PERMALINK_URL = "https://www.reddit.com%s";
    /**
     * The underlying submission instance.
     */
    protected net.dean.jraw.models.Submission referee;
    /**
     * Creates a new instance of a submission.
     * @param referee The underlying JRAW submission .
     */
    public JrawSubmission(net.dean.jraw.models.Submission referee){
        this.referee = referee;
    }
    /**
     * @return {@link net.dean.jraw.models.Submission#getAuthor()}.
     */
    @Override
    public String getAuthor(){
        return referee.getAuthor();
    }
    /**
     * @return {@link net.dean.jraw.models.Submission#getId()}.
     */
    @Override
    public String getId(){
        return referee.getId();
    }
    /**
     * The link flair text might be null, so we wrap it around an {@link Optional}
     * @return {@link net.dean.jraw.models.Submission#getLinkFlairText()}.
     */
    @Override
    public Optional<String> getLinkFlairText(){
        return Optional.ofNullable(referee.getLinkFlairText());
    }
    /**
     * @return {@link net.dean.jraw.models.Submission#getSubreddit()}.
     */
    @Override
    public String getSubreddit(){
        return referee.getSubreddit();
    }
    /**
     * @return {@link net.dean.jraw.models.Submission#isNsfw()}.
     */
    @Override
    public boolean isNsfw(){
        return referee.isNsfw();
    }
    /**
     * @return {@link net.dean.jraw.models.Submission#isSpoiler()}.
     */
    @Override
    public boolean isSpoiler(){
        return referee.isSpoiler();
    }
    /**
     * @return {@link net.dean.jraw.models.Submission#getScore()}.
     */
    @Override
    public int getScore(){
        return referee.getScore();
    }
    /**
     * @return {@link net.dean.jraw.models.Submission#getTitle()}.
     */
    @Override
    public String getTitle(){
        return referee.getTitle();
    }
    /**
     * Transforms the date into a {@link LocalDateTime} with the GMT zone.
     * @return {@link net.dean.jraw.models.Submission#getCreated}.
     */
    @Override
    public LocalDateTime getCreated(){
        return LocalDateTime.ofInstant(referee.getCreated().toInstant(), ZoneId.of("UTC"));
    }
    /**
     * The self text might be null, so we wrap it around an {@link Optional}.
     * @return {@link net.dean.jraw.models.Submission#getSelfText()}.
     */
    @Override
    public Optional<String> getSelfText(){
        if(referee.getSelfText() == null)
            return Optional.empty();

        return Optional.of(StringEscapeUtils.unescapeHtml4(referee.getSelfText()).trim());
    }
    /**
     * The thumbnail might be null, so we wrap it around an {@link Optional}.
     * @return {@link net.dean.jraw.models.Submission#getThumbnail()}.
     */
    @Override
    public Optional<String> getThumbnail(){
        return referee.hasThumbnail() ? Optional.ofNullable(referee.getThumbnail()) : Optional.empty();
    }
    /**
     * @return {@link net.dean.jraw.models.Submission#getUrl}
     */
    @Override
    public String getUrl(){
        return referee.getUrl();
    }
    /**
     * @return The permalink to the submission.
     */
    @Override
    public String getPermalink(){
        return String.format(PERMALINK_URL, referee.getPermalink());
    }
    /**
     * @return a hash code based on {@link #getId()}.
     */
    @Override
    public int hashCode(){
        return getId().hashCode();
    }
    /**
     * Two submissions are equal if they have the same id.
     * @param o An object this submission is compared to.
     * @return True if the object is a submission with the same id.
     */
    @Override
    public boolean equals(Object o){
        if(o instanceof Submission){
            Submission submission = (Submission)o;
            return submission.getId().equals(this.getId());
        }else{
            return false;
        }
    }
}
