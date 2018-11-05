/*
 * Copyright (C) 2018 u/Zavarov
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
package vartas.markdown;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import vartas.reddit.PushshiftWrapper.CompactComment;
import vartas.reddit.stats.TopComment;

/**
 * This class creates a Markdown representation of the top comments.
 * @author u/Zavarov
 */
public class TopCommentMarkdown implements BiFunction<Collection<CompactComment>, Integer, String>{
    /**
     * The function that computes the top comments.
     */
    protected final TopComment comment = new TopComment();
    /**
     * @param name the name of the user.
     * @return a hidden link to the users Reddit profile.
     */
    private String getUser(String name){
        return String.format("[%s](https://www.reddit.com/user/%s)",name,name);
    }
    /**
     * @param title the title of the submission.
     * @param link a permalink to the comment in the submission.
     * @return a hidden link to the comment.
     */
    private String getLink(String title, String link){
        return String.format("[%s](%s)",MarkdownUtils.escape(title),link);
    }
    /**
     * @param f the data over the accumulated comments.
     * @return a two-dimension array containing the key informations.
     */
    private Object[][] getData(Collection<CompactComment> f, int size){
        return comment.apply(f, size)
                .stream()
                .map(c -> new Object[]{c.getScore(),getUser(c.getAuthor()),getLink(c.getSubmission(),c.getPermalink())})
                .collect(Collectors.toList()).toArray(new Object[Math.min(f.size(), size)][3]);
    }
    /**
     * @return the column labels of the table. 
     */
    private Object[] getColumns() {
        return new Object[]{"Score","Author","Submission"};
    }
    /**
     * @param f all comments in the given interval.
     * @param size the top number of comments.
     * @return a Markdown representation of the top comments.
     */
    @Override
    public String apply(Collection<CompactComment> f, Integer size) {
        return MarkdownUtils.fromArray(getData(f,size), getColumns());
    }
}