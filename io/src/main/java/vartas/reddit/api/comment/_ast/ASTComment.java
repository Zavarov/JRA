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

package vartas.reddit.api.comment._ast;

import vartas.reddit.CommentInterface;
import vartas.reddit.api.comment._symboltable.*;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.text.StringEscapeUtils.unescapeHtml4;

public class ASTComment extends ASTCommentTOP implements CommentInterface {
    protected  ASTComment (){

    }
    protected  ASTComment (List<ASTEntry> entryList){
        super(entryList);
    }

    public String getAuthor() {
        Optional<AuthorLiteralSymbol> symbol = getEnclosingScope().resolve("author", AuthorLiteralSymbol.KIND);

        return unescapeHtml4(symbol.get().getAuthorLiteralNode().get().getStringLiteral().getValue());
    }

    public String getId() {
        Optional<IdLiteralSymbol> symbol = getEnclosingScope().resolve("id", IdLiteralSymbol.KIND);

        return unescapeHtml4(symbol.get().getIdLiteralNode().get().getStringLiteral().getValue());
    }

    public String getSubreddit() {
        Optional<SubredditLiteralSymbol> symbol = getEnclosingScope().resolve("subreddit", SubredditLiteralSymbol.KIND);

        return unescapeHtml4(symbol.get().getSubredditLiteralNode().get().getStringLiteral().getValue());
    }

    public int getScore() {
        Optional<ScoreLiteralSymbol> symbol = getEnclosingScope().resolve("score", ScoreLiteralSymbol.KIND);

        return symbol.get().getScoreLiteralNode().get().getNatLiteral().getValue();
    }

    public String getSubmissionTitle() {
        Optional<SubmissionTitleLiteralSymbol> symbol = getEnclosingScope().resolve("submissionTitle", SubmissionTitleLiteralSymbol.KIND);

        return unescapeHtml4(symbol.get().getSubmissionTitleLiteralNode().get().getStringLiteral().getValue());
    }

    public String getSubmission() {
        Optional<SubmissionLiteralSymbol> symbol = getEnclosingScope().resolve("submission", SubmissionLiteralSymbol.KIND);

        return unescapeHtml4(symbol.get().getSubmissionLiteralNode().get().getStringLiteral().getValue());
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
