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

import org.junit.Before;
import org.junit.Test;
import vartas.reddit.chart.AbstractTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CommentChartTest extends AbstractTest {
    CommentChart chart;
    @Before
    public void setUp(){
        super.setUp();
        chart = new CommentChart();
    }
    @Test
    public void applyTest(){
        assertThat(chart.count(comments)).isEqualTo(2);
    }
    @Test
    public void getTitleTest(){
        assertThat(chart.getTitle()).isEqualTo("Number of comments");
    }
    @Test
    public void getYLabelTest(){
        assertThat(chart.getYLabel()).isEqualTo("#Comments");
    }
}