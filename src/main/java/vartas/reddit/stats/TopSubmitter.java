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
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import vartas.reddit.PushshiftWrapper.CompactSubmission;

/**
 * This class computes the top submission from every redditor, sorted by their score.
 * @author u/Zavarov
 */
public class TopSubmitter implements BiFunction<Collection<CompactSubmission>,Integer,List<Entry<String,List<CompactSubmission>>>>{
    /**
     * The comparater compares two submissions by there score in ascending order.
     */
    private static final Comparator<CompactSubmission>
            SCORE_COMPARATOR = (u,v) -> {
                    long x = u.getScore();
                    long y = v.getScore();
                    return Long.compare(y,x);
                };
    /**
     * The comparater compares two lists of submissions by their total sum of scores.
     */
    private static final Comparator<Entry<String,List<CompactSubmission>>>
            TOTAL_SCORE_COMPARATOR = (u,v) -> {
                    long x = u.getValue()
                            .stream()
                            .mapToLong(CompactSubmission::getScore)
                            .sum();
                    long y = v.getValue()
                            .stream()
                            .mapToLong(CompactSubmission::getScore)
                            .sum();
                    return Long.compare(y,x);
                };
    
    /**
     * Searchs for the top submission of each redditor and sorts them by their score.
     * It then returns a list of all (author,submissions) pairs where the
     * submissions are sorted by their score.
     * @param submissions all submissions.
     * @param size the amount of top submitters that are returned.
     * @return a list of the top submission from every redditor in ascending order.
     */
    @Override
    public List<Entry<String,List<CompactSubmission>>> apply(Collection<CompactSubmission> submissions, Integer size) {
        return submissions.stream()
                .filter(e -> !e.getAuthor().equals("[deleted]"))
                .collect(Collectors.groupingBy(
                        CompactSubmission::getAuthor,
                        Collectors.collectingAndThen(Collectors.toList(), 
                                l -> {
                                    l.sort(SCORE_COMPARATOR);
                                    return l;
                                })))
                .entrySet()
                .stream()
                .sorted(TOTAL_SCORE_COMPARATOR)
                .limit(size)
                .collect(Collectors.toList());
    }
}