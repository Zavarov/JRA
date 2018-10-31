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
import vartas.reddit.PushshiftWrapper.CompactComment;

/**
 * This class computes the scores for each commenter and sorts them by the total
 * sum of scores over all their comments.
 * @author u/Zavarov
 */
public class TopCommenter implements BiFunction<Collection<CompactComment>,Integer,List<Entry<String,List<Long>>>>{
    /**
     * The comparater compares two comments by there score in ascending order.
     */
    private static final Comparator<Entry<String,List<Long>>>
        SCORE_COMPARATOR = (u,v) -> {
            Long x = u.getValue().stream().reduce((a,b) -> a+b).get();
            Long y = v.getValue().stream().reduce((a,b) -> a+b).get();
            return Long.compare(y,x);
        };
    /**
     * Creates a list where each author is mapped to all of their scores.
     * @param comments all comments.
     * @param size the amount of top comments that are returned.
     * @return a list containing all comment scores for each user.
     */
    @Override
    public List<Entry<String, List<Long>>> apply(Collection<CompactComment> comments, Integer size) {
        List<Entry<String,List<Long>>> authors = comments.stream()
                .filter(e -> !e.getAuthor().equals("[deleted]"))
                .collect(Collectors.groupingBy(
                        CompactComment::getAuthor,
                        Collectors.mapping(CompactComment::getScore, Collectors.toList())))
                .entrySet()
                .stream()
                .sorted(SCORE_COMPARATOR)
                .collect(Collectors.toList());
        
        return authors.subList(0,Math.min(authors.size(), size));
    }
}