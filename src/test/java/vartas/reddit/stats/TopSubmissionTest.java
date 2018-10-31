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

import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import vartas.reddit.PushshiftWrapper.CompactSubmission;

/**
 *
 * @author u/Zavarov
 */
public class TopSubmissionTest {
    CompactData data;
    TopSubmission entry;
    @Before
    public void setUp(){
        data = new CompactData();
        entry = new TopSubmission();
    }
    @Test
    public void applyTest(){
        List<CompactSubmission> entries = entry.apply(data.submissions,10);
        assertEquals(entries.size(),5);
        
        assertEquals(entries.get(0),data.submissions.get(4));
        assertEquals(entries.get(1),data.submissions.get(3));
        assertEquals(entries.get(2),data.submissions.get(2));
        assertEquals(entries.get(3),data.submissions.get(1));
        assertEquals(entries.get(4),data.submissions.get(0));
    }
    @Test
    public void applyDeletedTest(){
        data.submissions.get(2).put("author", "[deleted]");
        List<CompactSubmission> entries = entry.apply(data.submissions,10);
        assertEquals(entries.size(),4);
        
        assertEquals(entries.get(0),data.submissions.get(4));
        assertEquals(entries.get(1),data.submissions.get(3));
        assertEquals(entries.get(2),data.submissions.get(1));
        assertEquals(entries.get(3),data.submissions.get(0));
    }
}