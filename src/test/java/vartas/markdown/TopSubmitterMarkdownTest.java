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
package vartas.markdown;

import java.util.List;
import java.util.Map.Entry;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import vartas.reddit.stats.CompactData;

/**
 *
 * @author u/Zavarov
 */
public class TopSubmitterMarkdownTest {
    static final String MARKDOWN1 =
            "| Score | Submission |\n" +
            "| --- | --- |\n" +
            "| 1 | [title1](https://redd.it/id1) |\n";
    static final String MARKDOWN2 =
            "| Score | Submission |\n" +
            "| --- | --- |\n" +
            "| 2 | [title2](https://redd.it/id2) |\n";
    static final String MARKDOWN3 =
            "| Score | Submission |\n" +
            "| --- | --- |\n" +
            "| 3 | [title3](https://redd.it/id3) |\n";
    static final String MARKDOWN4 =
            "| Score | Submission |\n" +
            "| --- | --- |\n" +
            "| 4 | [title4](https://redd.it/id4) |\n";
    static final String MARKDOWN5 =
            "| Score | Submission |\n" +
            "| --- | --- |\n" +
            "| 5 | [title5](https://redd.it/id5) |\n";
    CompactData data;
    TopSubmitterMarkdown top;
    @Before
    public void setUp(){
        data = new CompactData();
        top = new TopSubmitterMarkdown();
    }
    @Test
    public void applyTest(){
        List<Entry<String,String>> list = top.apply(data.submissions, 10);
        assertEquals(list.size(),5);
        
        assertEquals(list.get(0).getKey(),"author5");
        assertEquals(list.get(1).getKey(),"author4");
        assertEquals(list.get(2).getKey(),"author3");
        assertEquals(list.get(3).getKey(),"author2");
        assertEquals(list.get(4).getKey(),"author1");
        
        assertEquals(list.get(0).getValue(),MARKDOWN5);
        assertEquals(list.get(1).getValue(),MARKDOWN4);
        assertEquals(list.get(2).getValue(),MARKDOWN3);
        assertEquals(list.get(3).getValue(),MARKDOWN2);
        assertEquals(list.get(4).getValue(),MARKDOWN1);
    }
}