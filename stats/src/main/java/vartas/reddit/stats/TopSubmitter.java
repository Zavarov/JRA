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
package vartas.reddit.stats;

import org.apache.commons.lang3.tuple.Triple;
import vartas.reddit.SubmissionInterface;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class computes the top submission from every redditor, sorted by their score.
 */
public class TopSubmitter{
    /**
     * The comparator compares two submitters by their total comment score in ascending order.
     */
    private static final Comparator<Triple<String, Integer, List<SubmissionInterface>>>
            TOTAL_SCORE_COMPARATOR = (u,v) -> Integer.compare(v.getMiddle(), u.getMiddle());
    /**
     * The comparator compares two submissions by there score in ascending order.
     */
    private static final Comparator<SubmissionInterface>
            SCORE_COMPARATOR = (u,v) -> Integer.compare(v.getScore(), u.getScore());
    /**
     * Creates list of triples, containing the top submitters in ascending order.
     * The left entry contains the name of the submitter, the middle entry the total score and the right entry
     * the list of all submissions, sorted by their score.
     * @param submissions all submissions.
     * @param size the maximum amount of top submitters that are returned.
     * @return a list containing the first few top submitters.
     */
    public List<Triple<String, Integer, List<SubmissionInterface>>> compute(Collection<? extends SubmissionInterface> submissions, int size) {
        return submissions.parallelStream()
                .filter(e -> !e.getAuthor().equals("[deleted]"))
                .collect(Collectors.groupingBy(SubmissionInterface::getAuthor))
                .entrySet()
                .parallelStream()
                .map(entry -> Triple.of(entry.getKey(), computeScore(entry.getValue()), computeSortedComments(entry.getValue())))
                .sorted(TOTAL_SCORE_COMPARATOR)
                .limit(size)
                .collect(Collectors.toList());
    }

    /**
     * @param submissions the submissions of a specific submitter.
     * @return a sorted list of submissions.
     */
    private List<SubmissionInterface> computeSortedComments(Collection<? extends SubmissionInterface> submissions){
        return submissions.stream().sorted(SCORE_COMPARATOR).collect(Collectors.toList());
    }

    /**
     * @param submissions the submissions of a specific commenter.
     * @return the sum of all submission scores.
     */
    private int computeScore(Collection<? extends SubmissionInterface> submissions){
        return submissions.stream().mapToInt(SubmissionInterface::getScore).sum();
    }
}