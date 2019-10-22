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
import vartas.reddit.CommentInterface;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class computes the scores for each commenter and sorts them by the total
 * sum of scores over all their comments.
 */
public class TopCommenter{
    /**
     * The comparator compares two commenters by their total comment score in ascending order.
     */
    private static final Comparator<Triple<String, Integer, List<CommentInterface>>>
        TOTAL_SCORE_COMPARATOR = (u,v) -> Integer.compare(v.getMiddle(), u.getMiddle());
    /**
     * The comparator compares two comments by there score in ascending order.
     */
    private static final Comparator<CommentInterface>
        SCORE_COMPARATOR = (u,v) -> Integer.compare(v.getScore(), u.getScore());
    /**
     * Creates list of triples, containing the top commenters in ascending order.
     * The left entry contains the name of the commenter, the middle entry the total score and the right entry
     * the list of all comments, sorted by their score.
     * @param comments all comments.
     * @param size the maximum amount of top comments that are returned.
     * @return a list containing the first few top commenters.
     */
    public List<Triple<String, Integer, List<CommentInterface>>> compute(Collection<? extends CommentInterface> comments, int size) {
        return comments.parallelStream()
                .filter(e -> !e.getAuthor().equals("[deleted]"))
                .collect(Collectors.groupingBy(CommentInterface::getAuthor))
                .entrySet()
                .parallelStream()
                .map(entry -> Triple.of(entry.getKey(), computeScore(entry.getValue()), computeSortedComments(entry.getValue())))
                .sorted(TOTAL_SCORE_COMPARATOR)
                .limit(size)
                .collect(Collectors.toList());
    }

    /**
     * @param comments the comments of a specific commenter.
     * @return a sorted list of comments.
     */
    private List<CommentInterface> computeSortedComments(Collection<? extends CommentInterface> comments){
        return comments.stream().sorted(SCORE_COMPARATOR).collect(Collectors.toList());
    }

    /**
     * @param comments the comments of a specific commenter.
     * @return the sum of all comment scores.
     */
    private int computeScore(Collection<? extends CommentInterface> comments){
        return comments.stream().mapToInt(CommentInterface::getScore).sum();
    }
}