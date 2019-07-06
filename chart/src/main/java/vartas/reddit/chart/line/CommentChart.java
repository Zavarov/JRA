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
package vartas.reddit.chart.line;

import vartas.reddit.CommentInterface;

import java.util.Collection;

/**
 * This class creates a plot over the total amount of comments in the given
 * time frame.
 */
public class CommentChart extends AbstractChart<CommentInterface> {
    /**
     * @param data a collection of all comments in the interval.
     * @return the total number of comments. 
     */
    @Override
    protected long count(Collection<? extends CommentInterface> data) {
        return data.size();
    }
    /**
     * @return the title of the vartas.reddit.chart.
     */
    @Override
    protected String getTitle() {
        return "Number of comments";
    }
    /**
     * @return the type of values on the y axis. 
     */
    @Override
    protected String getYLabel() {
        return "#Comments";
    }
}
