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
package vartas.reddit.stats.chart.pie;

import java.util.Arrays;
import vartas.reddit.stats.CompactData;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author u/Zavarov
 */
public class TagPlotTest {
    CompactData data;
    TagPlot plot;
    @Before
    public void setUp(){
        data = new CompactData();
        plot = new TagPlot(0.5);
    }
    @Test
    public void applyTest(){
        JFreeChart chart = plot.apply(data.submissions);
        
        assertEquals(2.0,((PiePlot)chart.getPlot()).getDataset().getValue("both"));
        assertEquals(1.0,((PiePlot)chart.getPlot()).getDataset().getValue("nsfw"));
        assertEquals(1.0,((PiePlot)chart.getPlot()).getDataset().getValue("spoiler"));
        assertEquals(1.0,((PiePlot)chart.getPlot()).getDataset().getValue("untagged"));
    }
    @Test
    public void applyEmptyTest(){
        JFreeChart chart = plot.apply(Arrays.asList());
        
        assertEquals(0.0,((PiePlot)chart.getPlot()).getDataset().getValue("both"));
        assertEquals(0.0,((PiePlot)chart.getPlot()).getDataset().getValue("nsfw"));
        assertEquals(0.0,((PiePlot)chart.getPlot()).getDataset().getValue("spoiler"));
        assertEquals(0.0,((PiePlot)chart.getPlot()).getDataset().getValue("untagged"));
    }
}
