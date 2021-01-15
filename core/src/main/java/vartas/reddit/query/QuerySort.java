package vartas.reddit.query;

import vartas.reddit.Client;
import vartas.reddit.Endpoint;
import vartas.reddit.Subreddit;
import vartas.reddit.types.Thing;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.function.Function;

public abstract class QuerySort<T, Q extends QuerySort<T,Q>> extends QueryThing<T,Q> {
    /**
     * The time period limiting the age of the things. The age is relative to the time the query is made.
     * @see Subreddit.Query.TimePeriod
     */
    protected static final String TIME_PERIOD = "t";

    public QuerySort(Function<Thing, T> transformer, Client client, Endpoint endpoint, Object... args) {
        super(transformer, client, endpoint, args);
    }

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
