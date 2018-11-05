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

import java.util.function.BiFunction;
import vartas.reddit.stats.AccumulatedComment;
import vartas.reddit.stats.AccumulatedSubmission;

/**
 * This class transforms the information contained in the accumulated comments
 * and submissions into a readable Markdown table.
 * @author u/Zavarov
 */
public class AccumulatedMarkdown implements BiFunction<AccumulatedComment, AccumulatedSubmission, String>{
    /**
     * @param c the data over the accumulated comments.
     * @param s the data over the accumulated submissions.
     * @return a two-dimension array containing the key informations.
     */
    private Object[][] getData(AccumulatedComment c, AccumulatedSubmission s){
        return new Object[][]{
            new Object[]{
                "Total",
                s.getTotal(),
                c.getTotal()},
            new Object[]{
                "Rate (per day)",
                s.getRate(),
                c.getRate()},
            new Object[]{
                "Unique Redditors",
                s.getUniqueRedditors(),
                c.getUniqueRedditors()},
            new Object[]{
                "Combined Score",
                s.getCombinedScore(),
                c.getCombinedScore()}
        };
        
    }
    /**
     * @return the column labels of the table. 
     */
    private Object[] getColumns() {
        return new Object[]{"","Submissions","Comments"};
    }
    /**
     * @param comment the data over the accumulated comments.
     * @param submission the data over the accumulated sumbissions.
     * @return a Markdown table containing the key information of the data. 
     */
    @Override
    public String apply(AccumulatedComment comment, AccumulatedSubmission submission) {
        return MarkdownUtils.fromArray(getData(comment,submission), getColumns());
    }
}
