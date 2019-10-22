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

import net.dean.jraw.models.Comment;
import net.dean.jraw.models.Submission;
import vartas.reddit.CommentInterface;

/**
 * This class implements the comments backed by their respective JRAW comments.
 */
public class JrawComment implements CommentInterface{
    /**
     * The underlying comment instance.
     */
    protected Comment referee;
    /**
     * The underlying submission the comment is in.
     */
    protected Submission submission;
    /**
     * Creates a new instance of a comment.
     * @param referee the underlying JRAW comment.
     * @param submission the underlying JRAW submission the comment is a part of.
     */
    public JrawComment(Comment referee, Submission submission){
        this.referee = referee;
        this.submission = submission;
    }
    /**
     * @return the author of the comment.
     */
    public String getAuthor(){
        return referee.getAuthor();
    }
    /**
     * @return the id of the comment.
     */
    public String getId(){
        return referee.getId();
    }
    /**
     * @return the upvotes minus the downvotes.
     */
    public int getScore(){
        return referee.getScore();
    }
    /**
     * @return the  name of the submission, the comment is in.
     */
    public String getSubmission(){
        return submission.getId();
    }
    /**
     * @return the name of the subreddit, the comment is in.
     */
    public String getSubreddit(){
        return referee.getSubreddit();
    }
    /**
     * @return the title of the submission, the comment is in.
     */
    public String getSubmissionTitle(){
        return submission.getTitle();
    }
    /**
     * @return a hash code based on the id of the comment.
     */
    @Override
    public int hashCode(){
        return getId().hashCode();
    }
    /**
     * Two comments are equal if they have the same id.
     * @param o an object this comment is compared to.
     * @return true if the object is a comment with the same id.
     */
    @Override
    public boolean equals(Object o){
        if(o instanceof CommentInterface){
            CommentInterface comment = (CommentInterface)o;
            return comment.getId().equals(this.getId());
        }else{
            return false;
        }
    }
}
