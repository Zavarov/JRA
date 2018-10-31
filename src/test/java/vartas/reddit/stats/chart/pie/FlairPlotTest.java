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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import vartas.reddit.PushshiftWrapper;
import vartas.reddit.PushshiftWrapper.CompactSubmission;
import vartas.reddit.stats.CompactData;

/**
 *
 * @author u/Zavarov
 */
public class FlairPlotTest {
    CompactData data;
    FlairPlot plot;
    @Before
    public void setUp(){
        data = new CompactData();
        plot = new FlairPlot(0.5);
    }
    
    @Test
    public void applyTest(){
        JFreeChart chart = plot.apply(data.submissions);
        
        assertEquals(4,((PiePlot)chart.getPlot()).getDataset().getValue("flair").intValue());
        assertEquals(1,((PiePlot)chart.getPlot()).getDataset().getValue("unflaired").intValue());
    }
    
    @Test
    public void applyOtherTest(){
        CompactSubmission a = new PushshiftWrapper.CompactSubmission();
        a.put("author","author1");
        a.put("id","id1");
        a.put("score","1");
        a.put("title","title1");
        a.put("link_flair_text","a");
        a.put("subreddit","subreddit");
        a.put("over18", "true");
        a.put("spoiler","true");
        
        CompactSubmission b = new PushshiftWrapper.CompactSubmission();
        b.put("author","author2");
        b.put("id","id2");
        b.put("score","2");
        b.put("title","title2");
        b.put("link_flair_text","b");
        b.put("subreddit","subreddit");
        b.put("over18", "false");
        b.put("spoiler","false");
        
        CompactSubmission c = new PushshiftWrapper.CompactSubmission();
        b.put("author","author3");
        b.put("id","id3");
        b.put("score","3");
        b.put("title","title3");
        b.put("link_flair_text","c");
        b.put("subreddit","subreddit");
        b.put("over18", "false");
        b.put("spoiler","false");
        
        List<CompactSubmission> submissions = new ArrayList<>(102);
        submissions.addAll(Collections.nCopies(100, a));
        submissions.add(b);
        submissions.add(c);
        JFreeChart chart = plot.apply(submissions);
        
        assertEquals(100,((PiePlot)chart.getPlot()).getDataset().getValue("a").intValue());
        assertEquals(2,((PiePlot)chart.getPlot()).getDataset().getValue("other").intValue());
        assertFalse(((PiePlot)chart.getPlot()).getDataset().getKeys().contains("b"));
        assertFalse(((PiePlot)chart.getPlot()).getDataset().getKeys().contains("c"));
    }
}
