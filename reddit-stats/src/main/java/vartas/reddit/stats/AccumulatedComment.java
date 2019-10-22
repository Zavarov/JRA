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

import vartas.reddit.CommentInterface;

import java.util.Collection;

/**
 * This class computes general information over all comments.
 */
public class AccumulatedComment{
    /**
     * The total number of comments.
     */
    protected int total;
    /**
     * The combined score of all comments.
     */
    protected int combinedScore;
    /**
     * The number of unique commenters
     */
    protected long uniqueCommenters;
    /**
     * The number of comments per day.
     */
    protected double rate;
    /**
     * @param comments all comments
     * @param days the number of days over which the comments where gathered.
     */
    public AccumulatedComment(Collection<CommentInterface> comments, long days){
        total = comments.size();
        combinedScore = comments.stream().mapToInt(CommentInterface::getScore).sum();
        uniqueCommenters = comments.stream().map(CommentInterface::getAuthor).distinct().count();
        rate = comments.size() / days;
    }
    /**
     * @return the total amount of comments. 
     */
    public int getTotal(){
        return total;
    }
    /**
     * Rounds to two digits after the comma.
     * @return the amount of comments per day. 
     */
    public double getRate(){
        return Math.round(rate * 100.0)/100.0;
    }
    /**
     * @return the amount of distinct commenters. 
     */
    public long getUniqueCommenters(){
        return uniqueCommenters;
    }
    /**
     * @return the sum over all scores of all comments. 
     */
    public int getCombinedScore(){
        return combinedScore;
    }
}