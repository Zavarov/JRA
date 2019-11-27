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
import vartas.reddit.Comment;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * This class implements the comments backed by their respective JRAW comments.
 */
public class JrawComment implements Comment {
    /**
     * The underlying comment instance.
     */
    protected net.dean.jraw.models.Comment referee;
    /**
     * The underlying submission the comment is in.
     */
    protected Submission submission;
    /**
     * Creates a new instance of a comment.
     * @param referee The underlying JRAW comment.
     * @param submission The underlying JRAW submission the comment is a part of.
     */
    public JrawComment(net.dean.jraw.models.Comment referee, Submission submission){
        this.referee = referee;
        this.submission = submission;
    }
    /**
     * Transforms the date into a {@link LocalDateTime} with the GMT zone.
     * @return {@link net.dean.jraw.models.Comment#getCreated()}.
     */
    @Override
    public LocalDateTime getCreated() {
        return LocalDateTime.ofInstant(referee.getCreated().toInstant(), ZoneId.of("UTC"));
    }
    /**
     * @return {@link net.dean.jraw.models.Comment#getAuthor()}.
     */
    @Override
    public String getAuthor(){
        return referee.getAuthor();
    }
    /**
     * @return {@link net.dean.jraw.models.Comment#getId()}.
     */
    @Override
    public String getId(){
        return referee.getId();
    }
    /**
     * @return {@link net.dean.jraw.models.Comment#getScore()}.
     */
    @Override
    public int getScore(){
        return referee.getScore();
    }
    /**
     * @return {@link net.dean.jraw.models.Comment#getId()}.
     */
    @Override
    public String getSubmission(){
        return submission.getId();
    }
    /**
     * @return {@link net.dean.jraw.models.Comment#getSubreddit()}.
     */
    @Override
    public String getSubreddit(){
        return referee.getSubreddit();
    }
    /**
     * @return {@link net.dean.jraw.models.Comment#getSubmissionTitle()}.
     */
    @Override
    public String getSubmissionTitle(){
        return submission.getTitle();
    }
    /**
     * @return a hash code based on {@link #getId()}.
     */
    @Override
    public int hashCode(){
        return getId().hashCode();
    }
    /**
     * Two comments are equal if they have the same id.
     * @param o An object this comment is compared to.
     * @return True if the object is a comment with the same id.
     */
    @Override
    public boolean equals(Object o){
        if(o instanceof Comment){
            Comment comment = (Comment)o;
            return comment.getId().equals(this.getId());
        }else{
            return false;
        }
    }
}
