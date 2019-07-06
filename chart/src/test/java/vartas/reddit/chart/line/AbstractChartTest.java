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

import org.jfree.chart.JFreeChart;
import org.jfree.data.time.TimeSeriesCollection;
import org.junit.Before;
import org.junit.Test;
import vartas.reddit.SubmissionInterface;
import vartas.reddit.chart.AbstractTest;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class AbstractChartTest extends AbstractTest {
    static final long DAY = 24*60*60*1000 , WEEK = 7 * DAY , YEAR = 365 * DAY;
    AbstractChart<SubmissionInterface> chart;
    Map<Instant,List<SubmissionInterface>> map;
        
    OffsetDateTime t1 = OffsetDateTime.ofInstant(Instant.ofEpochMilli(0), ZoneId.of("UTC"));
    OffsetDateTime t2 = OffsetDateTime.ofInstant(Instant.ofEpochMilli(    DAY), ZoneId.of("UTC"));
    OffsetDateTime t3 = OffsetDateTime.ofInstant(Instant.ofEpochMilli(  3*DAY), ZoneId.of("UTC"));
    OffsetDateTime t4 = OffsetDateTime.ofInstant(Instant.ofEpochMilli( 40*DAY), ZoneId.of("UTC"));
    OffsetDateTime t5 = OffsetDateTime.ofInstant(Instant.ofEpochMilli(500*DAY), ZoneId.of("UTC"));
    @Before
    public void setUp(){
        super.setUp();

        chart = new SubmissionChart();
        
        map = new HashMap<>();
        map.put(t1.toInstant(),Arrays.asList(submissions.get(0)));
        map.put(t2.toInstant(),Arrays.asList(submissions.get(1)));
        map.put(t3.toInstant(),Arrays.asList(submissions.get(2)));
        map.put(t4.toInstant(),Arrays.asList(submissions.get(3)));
        map.put(t5.toInstant(),Arrays.asList(submissions.get(4)));
    }
    @Test
    public void createDayTest(){
        map.remove(t4.toInstant());
        map.remove(t5.toInstant());
        JFreeChart plot = chart.create(map, AbstractChart.Interval.DAY);
        TimeSeriesCollection collection = (TimeSeriesCollection)plot.getXYPlot().getDataset();
        
        assertEquals(collection.getSeries(0).getDataItem(0).getValue(),1.0);
        assertEquals(collection.getSeries(0).getDataItem(0).getPeriod().getStart().getTime(),0);
        assertEquals(collection.getSeries(0).getDataItem(1).getValue(),1.0);
        assertEquals(collection.getSeries(0).getDataItem(1).getPeriod().getStart().getTime(),DAY);
        assertEquals(collection.getSeries(0).getDataItem(2).getValue(),0.0);
        assertEquals(collection.getSeries(0).getDataItem(2).getPeriod().getStart().getTime(),2*DAY);
        assertEquals(collection.getSeries(0).getDataItem(3).getValue(),1.0);
        assertEquals(collection.getSeries(0).getDataItem(3).getPeriod().getStart().getTime(),3*DAY);
        assertEquals(collection.getSeries(0).getItemCount(),4);
    }
    @Test
    public void createWeekTest(){
        map.remove(t5.toInstant());
        JFreeChart plot = chart.create(map, AbstractChart.Interval.WEEK);
        TimeSeriesCollection collection = (TimeSeriesCollection)plot.getXYPlot().getDataset();
        
        assertEquals(collection.getSeries(0).getDataItem(0).getValue(),3.0);
        assertEquals(collection.getSeries(0).getDataItem(0).getPeriod().getStart().getTime(),-4*DAY);
        assertEquals(collection.getSeries(0).getDataItem(1).getValue(),0.0);
        assertEquals(collection.getSeries(0).getDataItem(1).getPeriod().getStart().getTime(),3*DAY);
        assertEquals(collection.getSeries(0).getDataItem(2).getValue(),0.0);
        assertEquals(collection.getSeries(0).getDataItem(2).getPeriod().getStart().getTime(),10*DAY);
        assertEquals(collection.getSeries(0).getDataItem(3).getValue(),0.0);
        assertEquals(collection.getSeries(0).getDataItem(3).getPeriod().getStart().getTime(),17*DAY);
        assertEquals(collection.getSeries(0).getDataItem(4).getValue(),0.0);
        assertEquals(collection.getSeries(0).getDataItem(4).getPeriod().getStart().getTime(),24*DAY);
        assertEquals(collection.getSeries(0).getDataItem(5).getValue(),1.0);
        assertEquals(collection.getSeries(0).getDataItem(5).getPeriod().getStart().getTime(),31*DAY);
        assertEquals(collection.getSeries(0).getItemCount(),6);
    }
    @Test
    public void createMonthTest(){
        JFreeChart plot = chart.create(map, AbstractChart.Interval.MONTH);
        TimeSeriesCollection collection = (TimeSeriesCollection)plot.getXYPlot().getDataset();
        
        assertEquals(collection.getSeries(0).getDataItem(0).getValue(),3.0);
        assertEquals(collection.getSeries(0).getDataItem(0).getPeriod().getStart().getTime(),0);
        assertEquals(collection.getSeries(0).getDataItem(1).getValue(),1.0);
        assertEquals(collection.getSeries(0).getDataItem(1).getPeriod().getStart().getTime(),31 * DAY);
        assertEquals(collection.getSeries(0).getDataItem(2).getValue(),0.0);
        assertEquals(collection.getSeries(0).getDataItem(2).getPeriod().getStart().getTime(),59 * DAY);
        assertEquals(collection.getSeries(0).getDataItem(3).getValue(),0.0);
        assertEquals(collection.getSeries(0).getDataItem(3).getPeriod().getStart().getTime(),90 * DAY);
        assertEquals(collection.getSeries(0).getDataItem(4).getValue(),0.0);
        assertEquals(collection.getSeries(0).getDataItem(4).getPeriod().getStart().getTime(),120 * DAY);
        assertEquals(collection.getSeries(0).getDataItem(5).getValue(),0.0);
        assertEquals(collection.getSeries(0).getDataItem(5).getPeriod().getStart().getTime(),151 * DAY);
        assertEquals(collection.getSeries(0).getDataItem(6).getValue(),0.0);
        assertEquals(collection.getSeries(0).getDataItem(6).getPeriod().getStart().getTime(),181 * DAY);
        assertEquals(collection.getSeries(0).getDataItem(7).getValue(),0.0);
        assertEquals(collection.getSeries(0).getDataItem(7).getPeriod().getStart().getTime(),212 * DAY);
        assertEquals(collection.getSeries(0).getDataItem(8).getValue(),0.0);
        assertEquals(collection.getSeries(0).getDataItem(8).getPeriod().getStart().getTime(),243 * DAY);
        assertEquals(collection.getSeries(0).getDataItem(9).getValue(),0.0);
        assertEquals(collection.getSeries(0).getDataItem(9).getPeriod().getStart().getTime(),273 * DAY);
        assertEquals(collection.getSeries(0).getDataItem(10).getValue(),0.0);
        assertEquals(collection.getSeries(0).getDataItem(10).getPeriod().getStart().getTime(),304 * DAY);
        assertEquals(collection.getSeries(0).getDataItem(11).getValue(),0.0);
        assertEquals(collection.getSeries(0).getDataItem(11).getPeriod().getStart().getTime(),334 * DAY);
        assertEquals(collection.getSeries(0).getDataItem(12).getValue(),0.0);
        assertEquals(collection.getSeries(0).getDataItem(12).getPeriod().getStart().getTime(),365 * DAY);
        assertEquals(collection.getSeries(0).getDataItem(13).getValue(),0.0);
        assertEquals(collection.getSeries(0).getDataItem(13).getPeriod().getStart().getTime(),396 * DAY);
        assertEquals(collection.getSeries(0).getDataItem(14).getValue(),0.0);
        assertEquals(collection.getSeries(0).getDataItem(14).getPeriod().getStart().getTime(),424 * DAY);
        assertEquals(collection.getSeries(0).getDataItem(15).getValue(),0.0);
        assertEquals(collection.getSeries(0).getDataItem(15).getPeriod().getStart().getTime(),455 * DAY);
        assertEquals(collection.getSeries(0).getDataItem(16).getValue(),1.0);
        assertEquals(collection.getSeries(0).getDataItem(16).getPeriod().getStart().getTime(),485 * DAY);
        assertEquals(collection.getSeries(0).getItemCount(),17);
        
    }
    @Test
    public void createYearTest(){
        JFreeChart plot = chart.create(map, AbstractChart.Interval.YEAR);
        TimeSeriesCollection collection = (TimeSeriesCollection)plot.getXYPlot().getDataset();
        
        assertEquals(collection.getSeries(0).getDataItem(0).getValue(),4.0);
        assertEquals(collection.getSeries(0).getDataItem(0).getPeriod().getStart().getTime(),0);
        assertEquals(collection.getSeries(0).getDataItem(1).getValue(),1.0);
        assertEquals(collection.getSeries(0).getDataItem(1).getPeriod().getStart().getTime(),YEAR);
        assertEquals(collection.getSeries(0).getItemCount(),2);
    }
    @Test
    public void createEmptyTest(){
        map.clear();
        JFreeChart plot = chart.create(map, AbstractChart.Interval.YEAR);
        TimeSeriesCollection collection = (TimeSeriesCollection)plot.getXYPlot().getDataset();
        
        assertEquals(collection.getSeries(0).getDataItem(0).getValue(),0.0);
        assertEquals(collection.getSeries(0).getDataItem(0).getPeriod().getStart().getTime(),0);
    }
    @Test
    public void collectTest(){
        assertEquals(2,chart.collect(map, t3, t1));
    }
    @Test
    public void collectJumpTest(){
        assertEquals(2,chart.collect(map, t4, t2));
    }
    @Test
    public void getYLabelTest(){
        assertEquals(chart.getXLabel(),"Time");
    }
}