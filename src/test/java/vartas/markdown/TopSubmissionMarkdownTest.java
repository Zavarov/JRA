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

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import vartas.reddit.stats.CompactData;

/**
 *
 * @author u/Zavarov
 */
public class TopSubmissionMarkdownTest {
    static final String MARKDOWN =
            "| Score | Author | Submission |\n" +
            "| --- | --- | --- |\n" +
            "| 5 | [author5](https://www.reddit.com/user/author5) | [title5](https://redd.it/id5) |\n" +
            "| 4 | [author4](https://www.reddit.com/user/author4) | [title4](https://redd.it/id4) |\n" +
            "| 3 | [author3](https://www.reddit.com/user/author3) | [title3](https://redd.it/id3) |\n" +
            "| 2 | [author2](https://www.reddit.com/user/author2) | [title2](https://redd.it/id2) |\n" +
            "| 1 | [author1](https://www.reddit.com/user/author1) | [title1](https://redd.it/id1) |\n";
    CompactData data;
    TopSubmissionMarkdown top;
    @Before
    public void setUp(){
        data = new CompactData();
        top = new TopSubmissionMarkdown();
    }
    @Test
    public void applyTest(){
        assertEquals(MARKDOWN,top.apply(data.submissions, 10));
    }
}