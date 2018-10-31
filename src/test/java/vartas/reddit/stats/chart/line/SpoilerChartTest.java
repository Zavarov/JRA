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
package vartas.reddit.stats.chart.line;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import vartas.reddit.stats.CompactData;

/**
 *
 * @author u/Zavarov
 */
public class SpoilerChartTest {
    CompactData data;
    SpoilerChart chart;
    @Before
    public void setUp(){
        data = new CompactData();
        chart = new SpoilerChart();
    }
    @Test
    public void applyTest(){
        assertEquals(chart.count(data.submissions),3);
    }
    @Test
    public void getTitleTest(){
        assertEquals(chart.getTitle(),"Number of spoiler submissions");
    }
    @Test
    public void getYLabelTest(){
        assertEquals(chart.getYLabel(),"#Submissions");
    }
}