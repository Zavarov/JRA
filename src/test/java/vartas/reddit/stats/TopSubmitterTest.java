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

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import vartas.reddit.PushshiftWrapper.CompactSubmission;

/**
 *
 * @author u/Zavarov
 */
public class TopSubmitterTest {
    CompactData data;
    TopSubmitter entry;
    @Before
    public void setUp(){
        data = new CompactData();
        entry = new TopSubmitter();
    }
    @Test
    public void applyTest(){
        List<Entry<String,List<CompactSubmission>>> entries = entry.apply(data.submissions,10);
        assertEquals(entries.size(),5);
        
        assertEquals(entries.get(0).getKey(),"author5");
        assertEquals(entries.get(0).getValue(),Arrays.asList(data.submissions.get(4)));
        assertEquals(entries.get(1).getKey(),"author4");
        assertEquals(entries.get(1).getValue(),Arrays.asList(data.submissions.get(3)));
        assertEquals(entries.get(2).getKey(),"author3");
        assertEquals(entries.get(2).getValue(),Arrays.asList(data.submissions.get(2)));
        assertEquals(entries.get(3).getKey(),"author2");
        assertEquals(entries.get(3).getValue(),Arrays.asList(data.submissions.get(1)));
        assertEquals(entries.get(4).getKey(),"author1");
        assertEquals(entries.get(4).getValue(),Arrays.asList(data.submissions.get(0)));
    }
    @Test
    public void applyDeletedTest(){
        data.submissions.get(2).put("author", "[deleted]");
        List<Entry<String,List<CompactSubmission>>> entries = entry.apply(data.submissions,10);
        assertEquals(entries.size(),4);
        
        assertEquals(entries.get(0).getKey(),"author5");
        assertEquals(entries.get(0).getValue(),Arrays.asList(data.submissions.get(4)));
        assertEquals(entries.get(1).getKey(),"author4");
        assertEquals(entries.get(1).getValue(),Arrays.asList(data.submissions.get(3)));
        assertEquals(entries.get(2).getKey(),"author2");
        assertEquals(entries.get(2).getValue(),Arrays.asList(data.submissions.get(1)));
        assertEquals(entries.get(3).getKey(),"author1");
        assertEquals(entries.get(3).getValue(),Arrays.asList(data.submissions.get(0)));
    }
    @Test
    public void applyMultipleTest(){
        data.submissions.get(2).put("author", "author5");
        List<Entry<String,List<CompactSubmission>>> entries = entry.apply(data.submissions,10);
        assertEquals(entries.size(),4);
        
        assertEquals(entries.get(0).getKey(),"author5");
        assertEquals(entries.get(0).getValue(),Arrays.asList(data.submissions.get(4),data.submissions.get(2)));
        assertEquals(entries.get(1).getKey(),"author4");
        assertEquals(entries.get(1).getValue(),Arrays.asList(data.submissions.get(3)));
        assertEquals(entries.get(2).getKey(),"author2");
        assertEquals(entries.get(2).getValue(),Arrays.asList(data.submissions.get(1)));
        assertEquals(entries.get(3).getKey(),"author1");
        assertEquals(entries.get(3).getValue(),Arrays.asList(data.submissions.get(0)));
    }
}