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
import vartas.reddit.PushshiftWrapper.CompactComment;

/**
 *
 * @author u/Zavarov
 */
public class TopCommentTest {
    CompactData data;
    TopComment entry;
    @Before
    public void setUp(){
        data = new CompactData();
        entry = new TopComment();
    }
    @Test
    public void applyTest(){
        List<CompactComment> entries = entry.apply(data.comments,10);
        assertEquals(entries.size(),2);
        
        assertEquals(entries.get(0),data.comments.get(1));
        assertEquals(entries.get(1),data.comments.get(0));
    }
    @Test
    public void applyDeletedTest(){
        data.comments.get(0).put("author", "[deleted]");
        List<CompactComment> entries = entry.apply(data.comments,10);
        assertEquals(entries.size(),1);
        
        assertEquals(entries.get(0),data.comments.get(1));
    }
}