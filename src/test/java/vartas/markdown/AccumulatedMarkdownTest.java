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
import vartas.reddit.stats.AccumulatedComment;
import vartas.reddit.stats.AccumulatedSubmission;
import vartas.reddit.stats.CompactData;

/**
 *
 * @author u/Zavarov
 */
public class AccumulatedMarkdownTest {
    static final String MARKDOWN =
            "|  | Submissions | Comments |\n" +
            "| --- | --- | --- |\n" +
            "| Total | 5 | 2 |\n" +
            "| Rate (per day) | 5.0 | 2.0 |\n" +
            "| Unique Redditors | 5 | 2 |\n" +
            "| Combined Score | 15 | 3 |\n";
    CompactData data;
    AccumulatedMarkdown accumulated;
    AccumulatedComment comment;
    AccumulatedSubmission submission;
    @Before
    public void setUp(){
        data = new CompactData();
        comment = new AccumulatedComment(data.comments,1);
        submission = new AccumulatedSubmission(data.submissions,1);
        accumulated = new AccumulatedMarkdown();
    }
    @Test
    public void applyTest(){
        assertEquals(MARKDOWN,accumulated.apply(comment,submission));
    }
}