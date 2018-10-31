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
package vartas.reddit.stats;

import java.util.Collection;
import java.util.HashMap;
import vartas.reddit.PushshiftWrapper.CompactSubmission;

/**
 * This class computes general informations over all submissions and stores
 * them in the underlying map. The keys for those informations are: <br>
 * total<br>
 * rate<br>
 * unique_redditors<br>
 * combined_scores<br>
 * Which point to the respective entries.
 * @author u/Zavarov
 */
public class AccumulatedSubmission extends HashMap<String,String>{
    private static final long serialVersionUID = 0L;
    /**
     * @param submissions all submissions
     * @param days the number of days over which the submissions where gathered.
     */
    public AccumulatedSubmission(Collection<CompactSubmission> submissions, long days) {
        put("total", Integer.toString(submissions.size()));
        put("combined_score",
                Long.toString(
                        submissions
                                .stream()
                                .mapToLong(CompactSubmission::getScore)
                                .sum()));
        put("unique_redditors",
                Long.toString(
                        submissions
                            .stream()
                            .map(CompactSubmission::getAuthor)
                            .distinct()
                            .count()));
        put("rate", Double.toString(((double)submissions.size()) / days));
    }
    /**
     * @return the total amount of submissions. 
     */
    public long getTotal(){
        return Long.parseLong(get("total"));
    }
    /**
     * Rounds to two digits after the comma.
     * @return the amount of submissions per day. 
     */
    public double getRate(){
        return Math.round(Double.parseDouble(get("rate")) * 100.0)/100.0;
    }
    /**
     * @return the amount of distinct submitters. 
     */
    public long getUniqueRedditors(){
        return Long.parseLong(get("unique_redditors"));
    }
    /**
     * @return the sum over all scores of all submissions. 
     */
    public long getCombinedScore(){
        return Long.parseLong(get("combined_score"));
    }
}