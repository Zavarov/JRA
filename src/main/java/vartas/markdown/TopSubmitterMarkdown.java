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
import java.util.List;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import vartas.reddit.PushshiftWrapper.CompactSubmission;
import vartas.reddit.stats.TopSubmitter;

/**
 * This class creates a Markdown representation of the top submitters.
 * @author u/Zavarov
 */
public class TopSubmitterMarkdown implements BiFunction<Collection<CompactSubmission>, Integer, List<Entry<String,String>>>{
    /**
     * The function that computes the top submitter.
     */
    protected final TopSubmitter submitter = new TopSubmitter();
    /**
     * @param title the title of the submission.
     * @param link a permalink to the comment in the submission.
     * @return a hidden link to the comment.
     */
    private String getLink(String title, String link){
        return String.format("[%s](%s)",MarkdownUtils.escape(title),link);
    }
    /**
     * @param s all comments in the given interval.
     * @return a two-dimension array containing the key informations.
     */
    private Object[][] getData(Collection<CompactSubmission> s, int size){
        return s.stream()
                .limit(size)
                .map(t -> new Object[]{t.getScore(),getLink(t.getTitle(),t.getPermalink())})
                .collect(Collectors.toList()).toArray(new Object[Math.min(s.size(), size)][2]);
    }
    /**
     * @return the column labels of the table. 
     */
    private Object[] getColumns() {
        return new Object[]{"Score","Submission"};
    }
    /**
     * @param s all comments in the given interval.
     * @param size the top number of commenters.
     * @return a list of Markdown representations for each of the top submitters.
     */
    @Override
    public List<Entry<String,String>> apply(Collection<CompactSubmission> s, Integer size) {
        return submitter.apply(s, size)
                .stream()
                .map(e -> Pair.of(e.getKey(), MarkdownUtils.fromArray(getData(e.getValue(),size), getColumns())))
                .collect(Collectors.toList());
    }
}