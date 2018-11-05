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
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import vartas.reddit.PushshiftWrapper.CompactComment;

/**
 * This class goes through all comments and picks the ones with the highest score.
 * @author u/Zavarov
 */
public class TopComment implements BiFunction<Collection<CompactComment>,Integer,List<CompactComment>>{
    /**
     * The comparater compares two comments by there score in ascending order.
     */
    private static final Comparator<CompactComment>
            SCORE_COMPARATOR = (u,v) -> {
                    long x = u.getScore();
                    long y = v.getScore();
                    return Long.compare(y,x);
                };
    
    /**
     * Sorts the comments by their score and picks the highest ones.
     * @param comments all comments.
     * @param size the amount of top comments that are returned.
     * @return a list of the top comments in ascending order.
     */
    @Override
    public List<CompactComment> apply(Collection<CompactComment> comments, Integer size) {
        return comments.stream()
                .filter(e -> !e.getAuthor().equals("[deleted]"))
                .sorted(SCORE_COMPARATOR)
                .limit(size)
                .collect(Collectors.toList());
    }
}