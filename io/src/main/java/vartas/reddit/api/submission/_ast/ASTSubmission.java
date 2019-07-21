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

package vartas.reddit.api.submission._ast;

import vartas.reddit.SubmissionInterface;
import vartas.reddit.api.submission._symboltable.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.text.StringEscapeUtils.unescapeHtml4;

public class ASTSubmission extends ASTSubmissionTOP implements SubmissionInterface {
    protected  ASTSubmission (){

    }
    protected  ASTSubmission (List<ASTEntry> entryList){
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

    public Optional<String> getLinkFlairText() {
        Optional<LinkFlairTextLiteralSymbol> symbol = getEnclosingScope().resolve("linkFlairText", LinkFlairTextLiteralSymbol.KIND);

        if(symbol.isPresent())
            return Optional.of(unescapeHtml4(symbol.get().getLinkFlairTextLiteralNode().get().getStringLiteral().getValue()));
        else
            return Optional.empty();
    }

    public String getSubreddit() {
        Optional<SubredditLiteralSymbol> symbol = getEnclosingScope().resolve("subreddit", SubredditLiteralSymbol.KIND);

        return symbol.get().getSubredditLiteralNode().get().getStringLiteral().getValue();
    }

    public boolean isNsfw() {
        Optional<NsfwLiteralSymbol> symbol = getEnclosingScope().resolve("nsfw", NsfwLiteralSymbol.KIND);

        return symbol.get().getNsfwLiteralNode().get().getBooleanLiteral().getValue();
    }

    public boolean isSpoiler() {
        Optional<SpoilerLiteralSymbol> symbol = getEnclosingScope().resolve("spoiler", SpoilerLiteralSymbol.KIND);

        return symbol.get().getSpoilerLiteralNode().get().getBooleanLiteral().getValue();
    }

    public int getScore() {
        Optional<ScoreLiteralSymbol> symbol = getEnclosingScope().resolve("score", ScoreLiteralSymbol.KIND);

        return symbol.get().getScoreLiteralNode().get().getNatLiteral().getValue();
    }

    public String getTitle() {
        Optional<TitleLiteralSymbol> symbol = getEnclosingScope().resolve("title", TitleLiteralSymbol.KIND);

        return unescapeHtml4(symbol.get().getTitleLiteralNode().get().getStringLiteral().getValue());
    }

    public Date getCreated() {
        Optional<CreatedLiteralSymbol> symbol = getEnclosingScope().resolve("created", CreatedLiteralSymbol.KIND);

        return new Date(symbol.get().getCreatedLiteralNode().get().getBasicLongLiteral().getValue());
    }

    public Optional<String> getSelfText() {
        Optional<SelfTextLiteralSymbol> symbol = getEnclosingScope().resolve("selfText", SelfTextLiteralSymbol.KIND);

        if(symbol.isPresent())
            return Optional.of(unescapeHtml4(symbol.get().getSelfTextLiteralNode().get().getStringLiteral().getValue()));
        else
            return Optional.empty();
    }

    public Optional<String> getThumbnail() {
        Optional<ThumbnailLiteralSymbol> symbol = getEnclosingScope().resolve("thumbnail", ThumbnailLiteralSymbol.KIND);

        if(symbol.isPresent())
            return Optional.of(unescapeHtml4(symbol.get().getThumbnailLiteralNode().get().getStringLiteral().getValue()));
        else
            return Optional.empty();
    }

    public String getUrl() {
        Optional<UrlLiteralSymbol> symbol = getEnclosingScope().resolve("url", UrlLiteralSymbol.KIND);

        return unescapeHtml4(symbol.get().getUrlLiteralNode().get().getStringLiteral().getValue());
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
