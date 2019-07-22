/*
 * Copyright (c) 2019 Zavarov
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

package vartas.reddit;

import org.apache.commons.text.translate.AggregateTranslator;
import org.apache.commons.text.translate.CharSequenceTranslator;
import org.apache.commons.text.translate.LookupTranslator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.text.StringEscapeUtils.ESCAPE_HTML4;
import static org.apache.commons.text.StringEscapeUtils.UNESCAPE_HTML4;

public abstract class MonticoreEscapeUtils {
    protected static final CharSequenceTranslator ESCAPE_MONTICORE;
    static {
        final Map<CharSequence, CharSequence> escapeMonticoreMap = new HashMap<>();
        escapeMonticoreMap.put("\"", "\\\"");
        escapeMonticoreMap.put("\\", "\\\\");
        ESCAPE_MONTICORE = new AggregateTranslator(
                ESCAPE_HTML4,
                new LookupTranslator(Collections.unmodifiableMap(escapeMonticoreMap))
        );
    }

    protected static final CharSequenceTranslator MONTICORE_UNESCAPE;
    static {
        final Map<CharSequence, CharSequence> unescapeMonticoreMap = new HashMap<>();
        unescapeMonticoreMap.put("\\\"", "\"");
        unescapeMonticoreMap.put("\\\\", "\\");
        MONTICORE_UNESCAPE = new AggregateTranslator(
                UNESCAPE_HTML4,
                new LookupTranslator(Collections.unmodifiableMap(unescapeMonticoreMap))
        );
    }


    public static String escapeMonticore(final String input){
        return ESCAPE_MONTICORE.translate(input);
    }

    public static String unescapeMonticore(final String input){
        return MONTICORE_UNESCAPE.translate(input);
    }
}
