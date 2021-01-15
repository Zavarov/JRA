package vartas.reddit.query;

import vartas.reddit.Subreddit;

import javax.annotation.Nonnull;
import java.util.Locale;

public abstract class QuerySort<Q extends QuerySort<Q>> extends QueryLink<Q> {
    /**
     * The time period limiting the age of the things. The age is relative to the time the query is made.
     * @see Subreddit.Query.TimePeriod
     */
    protected static final String TIME_PERIOD = "t";

    public Q setTimePeriod(@Nonnull TimePeriod timePeriod){
        args.put(TIME_PERIOD, timePeriod);
        return getRealThis();
    }

    public enum TimePeriod{
        HOUR,
        DAY,
        WEEK,
        MONTH,
        YEAR,
        ALL;

        @Override
        public String toString(){
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}
