package vartas.markdown;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

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

/**
 *
 * @author u/Zavarov
 */
public class MarkdownUtilsTest {
    static final String MARKDOWN = 
            "| A | B |\n" +
            "| --- | --- |\n" +
            "| a | b |\n" +
            "| c | d |\n";
    static final String MISSING = 
            "| A | B |\n" +
            "| --- | --- |\n" +
            "| a |\n" +
            "| c | d |\n";
    static final String FEW_COLUMNS = 
            "| A |  |\n" +
            "| --- | --- |\n" +
            "| a | b |\n" +
            "| c | d |\n";
    static final String MISSING_START = 
            "| A | B |\n" +
            "| --- | --- |\n" +
            "|  | b |\n" +
            "| c | d |\n";
    
    @Test
    public void escapeTest(){
        assertEquals(MarkdownUtils.escape("abc"),"abc");
        assertEquals(MarkdownUtils.escape("#"),"\\#");
    }
    @Test
    public void fromArrayTest(){
        assertEquals(MARKDOWN,MarkdownUtils.fromArray(new Object[][]{{"a","b"},{"c","d"}}, new Object[]{"A","B"}));
    }
    @Test
    public void fromArrayMissingTest(){
        assertEquals(MISSING,MarkdownUtils.fromArray(new Object[][]{{"a"},{"c","d"}}, new Object[]{"A","B"}));
    }
    @Test
    public void fromTableTest(){
        Table<Integer,Integer,String> table = HashBasedTable.create();
        table.put(0, 0, "a");
        table.put(0, 1, "b");
        table.put(1, 0, "c");
        table.put(1, 1, "d");
        List<String> columns = Arrays.asList("A","B");
        assertEquals(MARKDOWN,MarkdownUtils.fromTable(table,columns));
    }
    @Test
    public void fromTableMissingTest(){
        Table<Integer,Integer,String> table = HashBasedTable.create();
        table.put(0, 0, "a");
        table.put(1, 0, "c");
        table.put(1, 1, "d");
        List<String> columns = Arrays.asList("A","B");
        assertEquals(MISSING,MarkdownUtils.fromTable(table,columns));
    }
    @Test
    public void fromTableMissingColumnsTest(){
        Table<Integer,Integer,String> table = HashBasedTable.create();
        table.put(0, 0, "a");
        table.put(0, 1, "b");
        table.put(1, 0, "c");
        table.put(1, 1, "d");
        List<String> columns = Arrays.asList("A");
        assertEquals(FEW_COLUMNS,MarkdownUtils.fromTable(table,columns));
    }
    @Test
    public void fromTableMissingStartTest(){
        Table<Integer,Integer,String> table = HashBasedTable.create();
        table.put(0, 1, "b");
        table.put(1, 0, "c");
        table.put(1, 1, "d");
        List<String> columns = Arrays.asList("A","B");
        assertEquals(MISSING_START,MarkdownUtils.fromTable(table,columns));
    }
}
