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
package vartas.reddit.chart.pie;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.PieDataset;
import org.junit.Before;
import org.junit.Test;
import vartas.reddit.api.submission._ast.ASTSubmission;
import vartas.reddit.chart.AbstractTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class FlairPlotTest extends AbstractTest {
    FlairPlot plot;
    @Before
    public void setUp(){
        super.setUp();
        plot = new FlairPlot(0.5);
    }
    
    @Test
    public void applyTest(){
        JFreeChart chart = plot.apply(submissions);
        
        assertEquals(4,((PiePlot)chart.getPlot()).getDataset().getValue("flair").intValue());
        assertEquals(1,((PiePlot)chart.getPlot()).getDataset().getValue("unflaired").intValue());
    }
    
    @Test
    public void applyOtherTest(){
        
        List<ASTSubmission> submissions = new ArrayList<>(1002);
        submissions.addAll(Collections.nCopies(100, this.submissions.get(0)));
        submissions.add(this.submissions.get(1));
        JFreeChart chart = plot.apply(submissions);

        PieDataset dataset = ((PiePlot)chart.getPlot()).getDataset();
        assertThat(dataset.getValue("unflaired")).isEqualTo(100L);
        assertThat(dataset.getValue("other")).isEqualTo(1L);
    }
}
