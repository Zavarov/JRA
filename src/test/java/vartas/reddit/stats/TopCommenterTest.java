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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import vartas.reddit.PushshiftWrapper;
import vartas.reddit.PushshiftWrapper.CompactComment;

/**
 *
 * @author u/Zavarov
 */
public class TopCommenterTest {
    CompactData data;
    TopCommenter entry;
    @Before
    public void setUp(){
        data = new CompactData();
        entry = new TopCommenter();
    }
    @Test
    public void applyTest(){
        List<Entry<String,List<Long>>> entries = entry.apply(data.comments,10);
        assertEquals(entries.size(),2);
        
        assertEquals(entries.get(0).getKey(),"author2");
        assertEquals(entries.get(0).getValue(),Arrays.asList(2L));
        assertEquals(entries.get(1).getKey(),"author1");
        assertEquals(entries.get(1).getValue(),Arrays.asList(1L));
    }
    @Test
    public void applyDeletedTest(){
        data.comments.get(0).put("author", "[deleted]");
        List<Entry<String,List<Long>>> entries = entry.apply(data.comments,10);
        assertEquals(entries.size(),1);
        
        assertEquals(entries.get(0).getKey(),"author2");
        assertEquals(entries.get(0).getValue(),Arrays.asList(2L));
    }
    @Test
    public void applyMultipleTest(){
        CompactComment c1 = new PushshiftWrapper.CompactComment();
        c1.put("author","author2");
        c1.put("id","id3");
        c1.put("score","1");
        c1.put("submission","submission3");
        c1.put("subreddit","subreddit3");
        c1.put("title","title3");
        CompactComment c2 = new PushshiftWrapper.CompactComment();
        c2.put("author","author1");
        c2.put("id","id4");
        c2.put("score","1");
        c2.put("submission","submission4");
        c2.put("subreddit","subreddit4");
        c2.put("title","title4");
        
        List<CompactComment> comments = new ArrayList<>(4);
        comments.addAll(data.comments);
        comments.add(c1);
        comments.add(c2);
        
        List<Entry<String,List<Long>>> entries = entry.apply(comments,10);
        assertEquals(entries.size(),2);
        
        assertEquals(entries.get(0).getKey(),"author2");
        assertEquals(entries.get(0).getValue(),Arrays.asList(2L,1L));
        assertEquals(entries.get(1).getKey(),"author1");
        assertEquals(entries.get(1).getValue(),Arrays.asList(1L,1L));
    }
}