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

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * This class implements core functions that allow to transform data into a table.
 * @author u/Zavarov
 */
public final class MarkdownUtils {
    /**
     * A regular expression that matches (hopefully) all reserved keywords in
     * markdown.
     * I.e 
     * \ . [] {} () * + - | ` _ # ! , ~
     */
    protected final static String PATTERN = "(\\\\|\\.|\\[|\\]|\\{|\\}|\\(|\\)|\\*|\\+|\\-|\\||`|_|#|!|,|~)";
    /**
     * This expression will escape everything that is matched by the regex
     */
    protected final static String ESCAPE ="\\\\$1";
    /**
     * Prevents the creation of an instance of this class.
     */
    private MarkdownUtils(){}
    /**
     * A function that escapes all undesired markdown keywords.
     * @param source the original string.
     * @return the escaped string.
     */
    public static String escape(Object source){
        return source.toString().replaceAll(PATTERN, ESCAPE);
    }
    /**
     * @param data the table as a two-dimensional array.
     * @param columns the labels for the columns.
     * @return a Markdown representation of the data. 
     */
    public static String fromArray(Object[][] data, Object[] columns){
        Table<Integer,Integer,Object> table = HashBasedTable.create();
        for(int i = 0 ; i < data.length ; ++i){
            for(int j = 0 ; j < data[i].length ; ++j){
                table.put(i,j,data[i][j]);
            }
        }
        return fromTable(table,Arrays.asList(columns));
    }
    /**
     * @param data the underlying table.
     * @param columns the labels for the columns.
     * @return a Markdown representation of the data. 
     */
    public static String fromTable(Table<Integer,Integer,?> data, List<?> columns){
        return toMarkdown(columns, data);
    }
    /**
     * Creates a Markdown representation of the given table.
     * @param data the underlying table.
     * @param columns the labels for the columns.
     * @return a Markdown representation of the data. 
     */
    private static String toMarkdown(List<?> columns, Table<Integer,Integer,?> data){
        StringBuilder builder = new StringBuilder();
        toMarkdown(builder, i -> {
            if(columns.size() > i)
                return columns.get(i).toString();
            return "";
            }, 
            data.columnKeySet().size()
        );
        toMarkdown(builder, i -> "---", data.columnKeySet().size());
        
        for(int k = 0 ; k < data.rowKeySet().size() ; ++k){
            int i = k;
            toMarkdown(builder, j -> {
                if(data.contains(i, j))
                    return data.get(i,j).toString();
                return "";
            //The "size" is the highest index plus the 0
            }, data.row(i).keySet().stream().mapToInt(Integer::valueOf).max().getAsInt()+1);
        }
        
        return builder.toString();
    }
    /**
     * Creates a single line of the Markdown table.
     * @param builder the builder the line is added to.
     * @param columns a function that returns the element of the respective column.
     * @param size the number of columns in this row.
     */
    private static void toMarkdown(StringBuilder builder, Function<Integer,String> columns, int size){
        builder.append("| ");
        for(int i = 0 ; i < size ; ++i){
            builder.append(columns.apply(i));
            if(i < size - 1){
                builder.append(" | ");
            }
        }
        builder.append(" |");
        builder.append(System.lineSeparator());
    }
}