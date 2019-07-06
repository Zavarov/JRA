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

import vartas.reddit.SubmissionInterface;

import java.util.Collection;

/**
 * This class creates a plot over all unique redditors that made submissions
 * in the given time frame.
 */
public class SubmitterChart extends AbstractChart<SubmissionInterface> {
    /**
     * @param data a collection of all submissions in the interval.
     * @return the number of distinct authors. 
     */
    @Override
    protected long count(Collection<? extends SubmissionInterface> data) {
        return data.stream().map(SubmissionInterface::getAuthor).distinct().count();
    }
    /**
     * @return the title of the vartas.reddit.chart.
     */
    @Override
    protected String getTitle() {
        return "Number of unique submitters";
    }
    /**
     * @return the type of values on the y axis. 
     */
    @Override
    protected String getYLabel() {
        return "#Submitters";
    }
}
